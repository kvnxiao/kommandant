package com.github.alphahelix00.ordinator.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MainCommand {

    boolean essential() default CommandDefaults.ESSENTIAL;

    String prefix() default CommandDefaults.PREFIX;

    String name();

    String[] alias();

    String description();

    String[] subCommands() default {};
}
