package com.github.alphahelix00.ordinator.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Command implements CommandExecutor {

    @Getter
    @NonNull
    private String prefix;
    @Getter
    @NonNull
    private String name;
    @Getter
    @NonNull
    private String description;
    @Getter
    @NonNull
    private List<String> aliases;
    @Getter
    private boolean isEnabled;
    @Getter
    private boolean isEssential = false;
    @Getter
    private Map<String, Command> subCommandMap = new HashMap<>();

    public boolean hasSubCommand() {
        return !subCommandMap.isEmpty();
    }

    public boolean isRepeating() {
        return subCommandMap.containsKey(name);
    }

    public static CommandBuilder builder(String name, String description, String... alias) {
        return new CommandBuilder(name, description, alias);
    }

    public static class CommandBuilder {

        private boolean isEssential = CommandDefaults.ESSENTIAL;
        private boolean isEnabled = CommandDefaults.ENABLED;
        private String prefix = CommandDefaults.PREFIX;
        private final String name, description;
        private final List<String> aliases;
        private Map<String, Command> subCommandMap = new HashMap<>();

        private CommandBuilder(final String name, final String description, final String... aliases) {
            this.name = name;
            this.description = description;
            if (aliases.length > 0) {
                this.aliases = Arrays.asList(aliases);
            } else {
                this.aliases = Collections.singletonList(name.split("\\s+")[0]);
            }
        }

        public CommandBuilder addSubCommand(Command subCommand) {
            this.subCommandMap.put(subCommand.getName(), subCommand);
            return this;
        }

        public CommandBuilder essential(boolean isEssential) {
            this.isEssential = isEssential;
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
            return new Command(prefix, name, description, aliases, isEnabled, isEssential, subCommandMap) {
                @Override
                public void execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
                    executor.execute(args);
                }
            };
        }

    }

    @Override
    public String toString() {
        return "(" + prefix + ")" + aliases + " <" + name + " --- " + description + ">";
    }
}
