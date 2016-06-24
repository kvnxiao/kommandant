package com.github.alphahelix00.ordinator.commands.handler;

import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import com.github.alphahelix00.ordinator.commands.MainCommand;
import com.github.alphahelix00.ordinator.commands.SubCommand;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public abstract class AbstractCommandHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger("CommandHandler");
    protected final CommandRegistry commandRegistry;

    public AbstractCommandHandler(final CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    // Required implementations in sub-concrete classes
    public abstract void executeCommand(Command command, List<String> args, Object... extraArgs) throws IllegalAccessException, InvocationTargetException;

    protected abstract Command createMainCommand(MainCommand annotation, Object obj, Method method, boolean isMainCommand);

    protected abstract Command createSubCommand(SubCommand annotation, Object obj, Method method, boolean isMainCommand);

    public boolean validateParse(String message) {
        Tuple3<Boolean, Optional<String>, Optional<List<String>>> validMessage = validatePrefix(message);
        if (validMessage.v1 && validMessage.v2.isPresent() && validMessage.v3.isPresent()) {
            return parseForCommands(validMessage.v2.get(), validMessage.v3.get());
        }
        return false;
    }

    public Tuple3<Boolean, Optional<String>, Optional<List<String>>> validatePrefix(String message) {
        boolean isValid = false;
        Optional<String> optPrefix = Optional.empty();
        Optional<List<String>> optMessageArgs = Optional.empty();

        for (String prefix : commandRegistry.getPrefixMap().keySet()) {
            if (message.startsWith(prefix)) {
                optPrefix = Optional.of(prefix);
                optMessageArgs = Optional.of(splitMessage(message.substring(prefix.length())));
                isValid = true;
                break;
            }
        }
        // Check if message starts with a command prefix
        return Tuple.tuple(isValid, optPrefix, optMessageArgs);
    }

    public boolean parseForCommands(String prefix, List<String> messageArgs, Object... extraArgs) {
        // Get and remove first argument from message
        String argFirst = messageArgs.get(0);
        messageArgs.remove(0);
        // Use and prefix and first argument String as command alias to try and retrieve a command from registry
        Optional<Command> optCommand = commandRegistry.getCommandByAlias(prefix, argFirst);
        if (optCommand.isPresent()) {
            LOGGER.info("Executing command arguments: " + prefix + argFirst + " " + String.join(" ", messageArgs));
            executeCommands(optCommand.get(), messageArgs, extraArgs);
            return true;
        } else {
            LOGGER.info("Attempted to execute a command that doesn't exist: " + prefix + argFirst);
            return false;
        }
    }

    public boolean registerCommand(Command command) {
        LOGGER.info("Attempting to register command: " + command.toString());
        return commandRegistry.addCommand(command);
    }

    public void registerAnnotatedCommands(Object obj) {
        List<Method> methodsMain = new ArrayList<>();
        List<Method> methodsSub = new ArrayList<>();

        // Parse class for methods with command annotations
        for (Method method : obj.getClass().getMethods()) {
            if (method.isAnnotationPresent(MainCommand.class)) {
                methodsMain.add(method);
            } else if (method.isAnnotationPresent(SubCommand.class)) {
                methodsSub.add(method);
            }
        }

        // Create and register commands from the main and sub methods
        if (!methodsMain.isEmpty()) {
            registerMainCommands(obj, methodsMain, methodsSub);
        } else {
            LOGGER.warn("No main methods detected in " + obj.getClass());
        }
    }

    protected boolean registerMainCommands(Object obj, List<Method> methodsMain, List<Method> methodsSub) {
        for (Method method : methodsMain) {
            List<Annotation> annotationList = Arrays.asList(method.getAnnotations());
            for (Annotation ann : annotationList) {
                if (ann instanceof MainCommand) {
                    final MainCommand annotation = (MainCommand) ann;
                    if (!commandRegistry.commandExists(annotation.prefix(), annotation.name())) {
                        Command command = createMainCommand(annotation, obj, method, true);
                        // Check if command is a repeating command or if it has sub commands
                        if (command.isRepeating()) {
                            command.addSubCommand(command);
                        } else if (command.hasSubCommand()) {
                            registerSubCommands(obj, methodsSub, command);
                        }
                        registerCommand(command);
                    }
                }
            }
        }
        return false;
    }

    protected boolean registerSubCommands(Object obj, List<Method> methodsSub, Command parentCommand) {
        // Iterate through all sub command names declared in parent command
        for (String subCommandName : parentCommand.getSubCommandNames()) {
            // Check and match each method annotation's name field with sub command name
            for (Method method : methodsSub) {
                final SubCommand annotation = method.getAnnotation(SubCommand.class);
                if (subCommandName.equals(annotation.name()) && !parentCommand.subCommandExists(annotation.name())) {
                    Command command = createSubCommand(annotation, obj, method, false);
                    LOGGER.info("Registering sub command: \"" + command.getName() + "\" of parent \"" + parentCommand.getName() + "\"");
                    if (command.hasSubCommand()) {
                        registerSubCommands(obj, methodsSub, command);
                    }
                    parentCommand.addSubCommand(command);
                }
            }
        }
        return false;
    }

    protected void executeCommand(Command command, List<String> args) throws IllegalAccessException, InvocationTargetException {
        command.execute(args);
    }

    protected void executeCommands(Command command, List<String> messageArgs, Object... extraArgs) {
        if (command.isEnabled()) {
            try {
                executeCommand(command, messageArgs, extraArgs);
            } catch (IllegalAccessException e) {
                LOGGER.error("IllegalAccessException in attempting to execute command " + command.getName(), e);
            } catch (InvocationTargetException e) {
                LOGGER.error("InvocationTargetException in attempting to execute command " + command.getName(), e);
            } finally {
                // Check if current command has sub command
                if (!messageArgs.isEmpty() && command.hasSubCommand()) {
                    // Get next argument from message
                    String subCommandAlias = messageArgs.get(0);
                    messageArgs.remove(0);
                    // Iterate through all sub commands and check if alias of those commands contain specific argument
                    Collections.unmodifiableCollection(command.getSubCommandMap().values()).forEach((subCommand) -> {
                        if (subCommand.getAliases().contains(subCommandAlias)) {
                            executeCommands(subCommand, messageArgs, extraArgs);
                        }
                    });
                }
            }
        } else {
            LOGGER.warn("Command \"" + command.getName() + "\" is disabled and will not be executed!");
        }
    }

    public static List<String> splitMessage(String message) {
        return new ArrayList<>(Arrays.asList(message.split("\\s+")));
    }


}
