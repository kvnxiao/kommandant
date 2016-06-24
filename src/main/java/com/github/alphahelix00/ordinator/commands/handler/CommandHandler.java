package com.github.alphahelix00.ordinator.commands.handler;

import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class CommandHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger("CommandHandler");
    protected final CommandRegistry commandRegistry;

    public CommandHandler(final CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    public boolean validateParse(String message) {
        Tuple3<Boolean, Optional<String>, Optional<List<String>>> validMessage = validateMessage(message);
        if (validMessage.v1 && validMessage.v2.isPresent() && validMessage.v3.isPresent()) {
            parseForCommands(validMessage.v2.get(), validMessage.v3.get());
        }
        return validMessage.v1;
    }

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

    public void parseForCommands(String prefix, List<String> messageArgs, Object... extraArgs) {
        // Get and remove first argument from message
        String argFirst = messageArgs.get(0);
        messageArgs.remove(0);
        // Use and prefix and first argument String as command alias to try and retrieve a command from registry
        Optional<Command> optCommand = commandRegistry.getCommandByAlias(prefix, argFirst);
        if (optCommand.isPresent()) {
            executeCommands(optCommand.get(), messageArgs, extraArgs);
        } else {
            LOGGER.info("Attempted to execute a command that doesn't exist: " + prefix + argFirst);
        }
    }

    public void executeCommand(Command command, List<String> args) throws IllegalAccessException, InvocationTargetException {
        command.execute(args);
    }

    public void executeCommand(Command command, List<String> args, Object... extraArgs) throws IllegalAccessException, InvocationTargetException {
        LOGGER.info("Executing command: \"" + command.getName() + "\"");
        executeCommand(command, args);
    }

    public void executeCommands(Command command, List<String> messageArgs, Object... extraArgs) {
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
