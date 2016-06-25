package com.github.alphahelix00.ordinator.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public abstract class Command implements CommandExecutor {

    protected String prefix;
    protected String name;
    protected String description;
    protected List<String> aliases;
    protected boolean isMain;
    protected boolean isEnabled;
    protected boolean isEssential;
    protected Map<String, Command> subCommandMap;
    protected Map<String, String> subCommandNames;

    public Command(String prefix, String name, String description, List<String> aliases, boolean isMain, boolean isEnabled, boolean isEssential, Map<String, Command> subCommandMap, Map<String, String> subCommandNames) {
        this.prefix = prefix;
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.isMain = isMain;
        this.isEnabled = isEnabled;
        this.isEssential = isEssential;
        this.subCommandMap = subCommandMap;
        this.subCommandNames = subCommandNames;
    }

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

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean isMain() {
        return isMain;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isEssential() {
        return isEssential;
    }

    public Map<String, Command> getSubCommandMap() {
        return subCommandMap;
    }

    @Override
    public String toString() {
        return "(" + prefix + ")" + aliases + " <" + name + " --- " + description + ">";
    }
}
