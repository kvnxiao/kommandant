package com.github.alphahelix00.ordinator.commands.essential;

import com.github.alphahelix00.ordinator.commands.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created on:   6/24/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class EssentialCommands {

    public static class Enable extends Command {

        public Enable(String prefix, String name, String description, List<String> aliases, boolean isMain, boolean isEnabled, boolean isEssential, Map<String, Command> subCommandMap, Map<String, String> subCommandNames) {
            super(prefix, name, description, aliases, isMain, isEnabled, isEssential, subCommandMap, subCommandNames);
        }

        @Override
        public Optional<Object> execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
            return null;
        }

    }
}
