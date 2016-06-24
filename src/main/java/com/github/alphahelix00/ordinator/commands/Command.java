package com.github.alphahelix00.ordinator.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Command implements CommandExecutor {

    @Getter
    @NonNull
    protected String prefix;
    @Getter
    @NonNull
    protected String name;
    @Getter
    @NonNull
    protected String description;
    @Getter
    @NonNull
    protected List<String> aliases;
    @Getter
    protected boolean isMain;
    @Getter
    protected boolean isEnabled;
    @Getter
    protected boolean isEssential;
    @Getter
    protected Map<String, Command> subCommandMap;
    protected Map<String, String> subCommandNames;

    public void addSubCommand(Command subCommand) {
        subCommandMap.put(subCommand.getName(), subCommand);
    }

    public boolean hasSubCommand() {
        return !subCommandMap.isEmpty() || !subCommandNames.isEmpty();
    }

    public boolean isRepeating() {
        return subCommandMap.containsKey(name);
    }

    public static CommandBuilder builder(String name, String description) {
        return new CommandBuilder(name, description);
    }

    public Set<String> getSubCommandNames() {
        return subCommandNames.keySet();
    }

    public boolean subCommandExists(String name) {
        return subCommandMap.containsKey(name);
    }

    /**
     * Builder class for building Commands
     */
    public static class CommandBuilder {

        private boolean isMain = CommandDefaults.ENABLED;
        private boolean isEssential = CommandDefaults.ESSENTIAL;
        private boolean isEnabled = CommandDefaults.ENABLED;
        private String prefix = CommandDefaults.PREFIX;
        private final String name, description;
        private List<String> aliases;
        private Map<String, Command> subCommandMap = new HashMap<>();
        private Map<String, String> subCommandNames = new HashMap<>();

        private CommandBuilder(final String name, final String description) {
            this.name = name;
            this.description = description;
            this.aliases = Collections.singletonList(name);
        }

        public CommandBuilder addSubCommand(Command subCommand) {
            String name = subCommand.getName();
            this.subCommandMap.put(name, subCommand);
            this.subCommandNames.put(name, name);
            return this;
        }

        public CommandBuilder subCommandNames(String... names) {
            for (String name : names) {
                subCommandNames.put(name, name);
            }
            return this;
        }

        public CommandBuilder alias(String... aliases) {
            if (aliases.length > 0) {
                this.aliases = Arrays.asList(aliases);
            } else {
                this.aliases = Collections.singletonList(name.split("\\s+")[0]);
            }
            return this;
        }

        public CommandBuilder essential(boolean isEssential) {
            this.isEssential = isEssential;
            return this;
        }

        public CommandBuilder isMain(boolean isMain) {
            this.isMain = isMain;
            return this;
        }

        public CommandBuilder enabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }

        public CommandBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Command build(CommandExecutor executor) {
            return new Command(prefix, name, description, aliases, isMain, isEnabled, isEssential, subCommandMap, subCommandNames) {
                @Override
                public Optional<Object> execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
                    return executor.execute(args);
                }
            };
        }

        public Command build(Object obj, Method method) {
            return new Command(prefix, name, description, aliases, isMain, isEnabled, isEssential, subCommandMap, subCommandNames) {
                @Override
                public Optional<Object> execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
                    return Optional.ofNullable(method.invoke(obj, args));
                }
            };
        }

    }

    @Override
    public String toString() {
        return "(" + prefix + ")" + aliases + " <" + name + " --- " + description + ">";
    }
}
