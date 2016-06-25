package com.github.alphahelix00.ordinator.commands.handler;

import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import com.github.alphahelix00.ordinator.commands.MainCommand;
import com.github.alphahelix00.ordinator.commands.SubCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created on:   6/24/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class CommandHandler extends AbstractCommandHandler{

    public CommandHandler(CommandRegistry commandRegistry) {
        super(commandRegistry);
    }

    public void executeCommand(Command command, List<String> args, Object... extraArgs) throws IllegalAccessException, InvocationTargetException {
        LOGGER.info("Executing" + ((command.isMain()) ? " main " : " sub ") + "command: \"" + command.toString() + ", with arguments: " + args + "\"");
        executeCommand(command, args);
    }

    @Override
    protected Command createMainCommand(MainCommand annotation, Object obj, Method method, boolean isMainCommand) {
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

    @Override
    protected Command createSubCommand(SubCommand annotation, Object obj, Method method, boolean isMainCommand) {
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
