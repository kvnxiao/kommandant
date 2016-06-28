package com.github.alphahelix00.ordinator.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation for sub commands
 *
 * Created on:   6/24/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    /**
     * Gets a boolean value denoting whether or not the command is an essential command
     *
     * @return is command essential? (default = false)
     */
    boolean essential() default CommandDefaults.ESSENTIAL;

    /**
     * Gets the prefix string of the sub command
     *
     * @return prefix of the command (default = "" -> no prefix)
     */
    String prefix() default CommandDefaults.NO_PREFIX;

    /**
     * Gets the unique name identifier of the command
     *
     * @return name of the command (REQUIRED FIELD)
     */
    String name();

    /**
     * Gets the aliases assigned to the command
     *
     * @return simple String array of aliases assigned to the command (REQUIRED FIELD)
     */
    String[] alias();

    /**
     * Gets the description of the command
     *
     * @return description of the command (REQUIRED FIELD)
     */
    String description();

    /**
     * Gets the names of all sub commands assigned to this command
     *
     * @return simple String array of sub command name identifiers (not aliases). default to none
     */
    String[] subCommands() default {};
}
