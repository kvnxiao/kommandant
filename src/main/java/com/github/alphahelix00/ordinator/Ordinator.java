package com.github.alphahelix00.ordinator;

import com.github.alphahelix00.ordinator.commands.CommandRegistry;

/**
 * A simple class that holds a single static instance to a command registry.
 * This class can be used if only a single command registry is required.
 *
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class Ordinator {

    private static final CommandRegistry commandRegistry = new CommandRegistry();

    /**
     * Returns a singleton instance of the command registry that can be easily referenced elsewhere
     * due to its static call
     *
     * @return singleton instance of command registry
     */
    public static CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

}
