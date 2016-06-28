package com.github.alphahelix00.ordinator.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * Single functional interface that all commands must implement
 *
 * <p>Created on:   6/23/2016</p>
 * <p>Author:       Kevin Xiao (github.com/alphahelix00)</p>
 */
@FunctionalInterface
public interface CommandExecutor {

    /**
     * Required method to be overrided when declaring commands to declare what
     * actions the command will perform
     *
     * @param args a list of string as arguments for the command
     * @return Optional object that may or may not wrap another object
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    Optional execute(List<String> args) throws IllegalAccessException, InvocationTargetException;
}

