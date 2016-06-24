package com.github.alphahelix00.ordinator;

import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import com.github.alphahelix00.ordinator.commands.handler.CommandHandler;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class Ordinator {

    private static final CommandRegistry commandRegistry = new CommandRegistry();
    private static final CommandHandler commandHandler = commandRegistry.getCommandHandler();

    public static CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

}
