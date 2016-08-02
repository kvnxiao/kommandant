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
 * An abstract Command Handler class that should be completed by filling out
 * the executeCommand, createMainCommand, and createSubCommand methods in order to properly
 * handle the execution of commands and registration of annotated commands
 *
 * <p>Created on:   6/23/2016</p>
 * <p>Author:       Kevin Xiao (github.com/alphahelix00)</p>
 */
public abstract class AbstractCommandHandler {

    /**
     * Logger for any AbstractCommandHandler implementation
     */
    public static final Logger LOGGER = LoggerFactory.getLogger("CommandHandler");
    protected final CommandRegistry commandRegistry;

    /**
     * Constructor that takes a command registry instance to be linked
     *
     * @param commandRegistry command registry instance
     */
    public AbstractCommandHandler(final CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    /**
     * Required implementation in concrete sub-classes. Executes a command with given arguments
     *
     * @param command   command to execute
     * @param args      list of string arguments to pass to the command
     * @param extraArgs an optional Object array of extra arguments to be passed to the command for execution
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected abstract void executeCommand(Command command, List<String> args, Object... extraArgs) throws IllegalAccessException, InvocationTargetException;

    /**
     * Required implementation in concrete sub-classes.
     * Is called by #registerAnnotatedCommands to create main commands from annotations
     *
     * @param annotation MainCommand annotation
     * @param obj        object instance with methods
     * @param method     method containing the MainCommand annotation
     * @return new command with properties defined by annotated method
     */
    protected abstract Command createMainCommand(MainCommand annotation, Object obj, Method method);

    /**
     * Required implementation in concrete sub-classes.
     * Is called by #registerAnnotatedCommands to create sub commands from annotations
     *
     * @param annotation SubCommand annotation
     * @param obj        object instance with methods
     * @param method     method containing the SubCommand annotation
     * @return new command with properties defined by annotated method
     */
    protected abstract Command createSubCommand(SubCommand annotation, Object obj, Method method);

    /**
     * Validates a message for valid command prefixes and further attempts to parse the message for command calls
     *
     * @param message   message to validate and parse for commands
     * @param extraArgs an optional Object array of extra arguments to be passed to the command for execution
     * @return true if message was successfully validated and parsed
     */
    public boolean validateAndParse(String message, Object... extraArgs) {
        Tuple3<Boolean, Optional<String>, Optional<List<String>>> validMessage = validateMessage(message);
        if (validMessage.v1 && validMessage.v2.isPresent() && validMessage.v3.isPresent()) {
            return parseForCommands(validMessage.v2.get(), validMessage.v3.get(), extraArgs);
        }
        return false;
    }

    /**
     * Attempts to go to and retrieve a command through the given list of message arguments
     *
     * @param args list of string arguments denoting which command / sub-command to go to
     * @return Optional object containing command if it exists, empty otherwise
     */
    public Optional<Command> gotoCommand(List<String> args) {
        Tuple3<Boolean, Optional<String>, Optional<List<String>>> validMessage = validateMessage(args.get(0));
        args.remove(0);
        if (validMessage.v1 && validMessage.v2.isPresent() && validMessage.v3.isPresent()) {
            String msgPrefix = validMessage.v2.get();
            List<String> msgArgs = validMessage.v3.get();
            Optional<Command> mainCommand = commandRegistry.getCommandByAlias(msgPrefix, msgArgs.get(0));
            if (mainCommand.isPresent() && !args.isEmpty()) {
                System.out.println(args);
                return gotoSubCommand(mainCommand.get(), args);
            } else if (mainCommand.isPresent() && args.isEmpty()) {
                return mainCommand;
            }
        }
        return Optional.empty();
    }

    /**
     * Attempts to go to and retrieve a sub command through a given parent command and a list of message arguments
     * following the parent command
     *
     * @param parentCommand parent command to start at
     * @param args          list of string arguments denoting which sub-command to go to
     * @return Optional object containing command if it exists, empty otherwise
     */
    public Optional<Command> gotoSubCommand(Command parentCommand, List<String> args) {
        String alias = args.get(0);
        args.remove(0);

        for (Command command : parentCommand.getSubCommandMap().values()) {
            if (command.getAliases().contains(alias)) {
                if (!args.isEmpty()) {
                    return gotoSubCommand(command, args);
                } else {
                    return Optional.of(command);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Returns a Tuple3 containing a boolean for prefix validation, an optional string containing the prefix,
     * and a list of message args split from the original message by white space
     *
     * @param message message to parse for valid prefix and command calls
     * @return Tuple3 containing valid prefix boolean, (optional) prefix string, and (optional) list of message args
     */
    public Tuple3<Boolean, Optional<String>, Optional<List<String>>> validateMessage(String message) {
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

    /**
     * Attempts to parse a command, given a prefix, the list of message args, and any extra arguments to pass to the command
     *
     * @param prefix      command prefix string
     * @param messageArgs list of message arguments to parse for command aliases
     * @param extraArgs   an optional Object array of extra arguments to be passed to the command for execution
     * @return true if successfully parsed command, false if command does not exist
     */
    public boolean parseForCommands(String prefix, List<String> messageArgs, Object... extraArgs) {
        // Get and remove first argument from message
        String argFirst = messageArgs.get(0);
        messageArgs.remove(0);
        // Use and prefix and first argument String as command alias to try and retrieve a command from registry
        Optional<Command> optCommand = commandRegistry.getCommandByAlias(prefix, argFirst);
        if (optCommand.isPresent()) {
            LOGGER.info("Parsing command arguments: " + prefix + argFirst + " " + String.join(" ", messageArgs));
            executeCommands(optCommand.get(), messageArgs, extraArgs);
            return true;
        } else {
            LOGGER.info("Attempted to parse a command that doesn't exist: " + prefix + argFirst);
            return false;
        }
    }

    /**
     * Registers a command into the command registry linked to this command handler
     *
     * @param command command to register
     * @return true if command was successfully registered
     */
    public boolean registerCommand(Command command) {
        LOGGER.info("Attempting to register command: " + command.toString());
        return commandRegistry.addCommand(command);
    }

    /**
     * Parses a class instance for methods annotated with MainCommand and SubCommand to create commands from these
     * annotations and have them added to the registry
     *
     * @param obj object containing methods annotated with MainCommand and SubCommand
     */
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
            if (method.isAnnotationPresent(MainCommand.class)) {
                final MainCommand annotation = method.getAnnotation(MainCommand.class);
                if (!commandRegistry.commandExists(annotation.prefix(), annotation.name())) {
                    Command command = createMainCommand(annotation, obj, method);
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
        return false;
    }

    protected boolean registerSubCommands(Object obj, List<Method> methodsSub, Command parentCommand) {
        // Iterate through all sub command names declared in parent command
        for (String subCommandName : parentCommand.getSubCommandNames()) {
            // Check and match each method annotation's name field with sub command name
            for (Method method : methodsSub) {
                final SubCommand annotation = method.getAnnotation(SubCommand.class);
                if (subCommandName.equals(annotation.name()) && !parentCommand.subCommandExists(annotation.name())) {
                    Command command = createSubCommand(annotation, obj, method);
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

    public void enableCommand(Command command) {
        if (!command.isEssential()) {
            LOGGER.info("ENABLING COMMAND: " + command.toString());
            command.setEnabled(true);
        } else {
            LOGGER.info("Cannot enable an essential command (already enabled)!");
        }
    }

    public void disableCommand(Command command) {
        if (!command.isEssential()) {
            LOGGER.info("Disabling command: " + command.toString());
            command.setEnabled(false);
        } else {
            LOGGER.info("Cannot disable an essential command!");
        }
    }

    public static List<String> splitMessage(String message) {
        return new LinkedList<>(Arrays.asList(message.split("\\s+")));
    }

}
