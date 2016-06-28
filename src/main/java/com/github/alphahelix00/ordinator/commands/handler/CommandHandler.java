package com.github.alphahelix00.ordinator.commands.handler;

import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import com.github.alphahelix00.ordinator.commands.MainCommand;
import com.github.alphahelix00.ordinator.commands.SubCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A concrete command handler implementation
 * with completed method bodies to create main and sub commands from annotations,
 * as well as executing commands with no extra arguments
 *
 * Created on:   6/24/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class CommandHandler extends AbstractCommandHandler {

    public CommandHandler(CommandRegistry commandRegistry) {
        super(commandRegistry);
    }

    /**
     * Executes command with no extra arguments
     *
     * @param command   command to execute
     * @param args      list of string arguments to pass to the command
     * @param extraArgs an optional Object array of extra arguments to be passed to the command for execution
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void executeCommand(Command command, List<String> args, Object... extraArgs) throws IllegalAccessException, InvocationTargetException {
        LOGGER.info("Executing" + ((command.isMain()) ? " main " : " sub ") + "command: \"" + command.toString() + ", with arguments: " + args + "\"");
        executeCommand(command, args);
    }

    /**
     * Creates a new command from MainCommand annotations
     *
     * @param annotation MainCommand annotation
     * @param obj        object instance with methods
     * @param method     method containing the MainCommand annotation
     * @return new command with properties defined by annotated method
     */
    @Override
    protected Command createMainCommand(MainCommand annotation, Object obj, Method method) {
        return Command.builder(
                annotation.name(), annotation.description())
                .prefix(annotation.prefix())
                .alias(annotation.alias())
                .enabled(true)
                .essential(annotation.essential())
                .subCommandNames(annotation.subCommands())
                .isMain(true)
                .build(obj, method);
    }

    /**
     * Creates a new command from SubCommand annotations
     *
     * @param annotation SubCommand annotation
     * @param obj        object instance with methods
     * @param method     method containing the SubCommand annotation
     * @return new command with properties defined by annotated method
     */
    @Override
    protected Command createSubCommand(SubCommand annotation, Object obj, Method method) {
        return Command.builder(
                annotation.name(), annotation.description())
                .prefix(annotation.prefix())
                .alias(annotation.alias())
                .enabled(true)
                .essential(annotation.essential())
                .subCommandNames(annotation.subCommands())
                .isMain(false)
                .build(obj, method);
    }
}
