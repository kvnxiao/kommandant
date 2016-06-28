package com.github.alphahelix00.ordinator.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public abstract class Command implements CommandExecutor {

    private String prefix;
    private String name;
    private String description;
    private List<String> aliases;
    private boolean isMain;
    private boolean isEnabled;
    private boolean isEssential;
    private Map<String, Command> subCommandMap;
    private Map<String, String> subCommandNames;

    /**
     * All field args constructor
     *
     * @param prefix
     * @param name
     * @param description
     * @param aliases
     * @param isMain
     * @param isEnabled
     * @param isEssential
     * @param subCommandMap
     * @param subCommandNames
     */
    protected Command(String prefix, String name, String description, List<String> aliases, boolean isMain, boolean isEnabled, boolean isEssential, Map<String, Command> subCommandMap, Map<String, String> subCommandNames) {
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

    /**
     * Adds a sub command to this command
     *
     * @param subCommand sub command to add to this command
     */
    public void addSubCommand(Command subCommand) {
        subCommandMap.put(subCommand.getName(), subCommand);
    }

    /**
     * Checks if this command has sub commands at all
     *
     * @return true if sub commands are linked to this command, false if it has no sub commands at all
     */
    public boolean hasSubCommand() {
        return !subCommandMap.isEmpty() || !subCommandNames.isEmpty();
    }

    /**
     * Checks if this command is a repeating command
     *
     * @return true if this command has a sub command of itself
     */
    public boolean isRepeating() {
        return subCommandMap.containsKey(name);
    }

    /**
     * Get a new builder instance to create new commands
     *
     * @param name        name of command to create
     * @param description description of command to create
     * @return command builder instance
     */
    public static CommandBuilder builder(String name, String description) {
        return new CommandBuilder(name, description);
    }

    /**
     * Gets a set of all sub command names that have been declared for this command
     *
     * @return set of all sub command names for this command
     */
    public Set<String> getSubCommandNames() {
        return subCommandNames.keySet();
    }

    /**
     * Checks if this command holds a sub command with provided name
     *
     * @param name name identifier of sub command to check for
     * @return true if sub command with matching name identifier exists
     */
    public boolean subCommandExists(String name) {
        return subCommandMap.containsKey(name);
    }

    /**
     * Enables or disables the command with provided boolean value
     *
     * @param isEnabled boolean value to enable to disable command
     */
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
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

        /**
         * Adds a sub command to the current command being built
         *
         * @param subCommand sub command to add to the current command being built
         * @return the current command builder instance
         */
        public CommandBuilder addSubCommand(Command subCommand) {
            String name = subCommand.getName();
            this.subCommandMap.put(name, subCommand);
            this.subCommandNames.put(name, name);
            return this;
        }

        /**
         * Adds name identifiers of sub commands to the current command being built
         * (used for annotated command building)
         *
         * @param names String array of sub command name identifiers
         * @return the current command builder instance
         */
        public CommandBuilder subCommandNames(String... names) {
            for (String name : names) {
                subCommandNames.put(name, name);
            }
            return this;
        }

        /**
         * Sets the aliases for the current command being built
         *
         * @param aliases String array of command aliases
         * @return the current command builder instance
         */
        public CommandBuilder alias(String... aliases) {
            if (aliases.length > 0) {
                this.aliases = Arrays.asList(aliases);
            } else {
                this.aliases = Collections.singletonList(name.split("\\s+")[0]);
            }
            return this;
        }

        /**
         * Sets whether or not the command is an essential command
         *
         * @param isEssential boolean value denoting essentiality
         * @return the current command builder instance
         */
        public CommandBuilder essential(boolean isEssential) {
            this.isEssential = isEssential;
            return this;
        }

        /**
         * Sets whether or not the command is a main command.
         * Set to false if it is sub command to avoid problems with the registry!
         *
         * @param isMain boolean value denoting whether the command is a main or sub command
         * @return the current command builder instance
         */
        public CommandBuilder isMain(boolean isMain) {
            this.isMain = isMain;
            return this;
        }

        /**
         * Sets whether or not the command is enabled upon creation
         *
         * @param isEnabled boolean value denoting whether the command is enabled or not
         * @return the current command builder instance
         */
        public CommandBuilder enabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }

        /**
         * Sets the prefix string for the command
         *
         * @param prefix prefix string identifier
         * @return the current command builder instance
         */
        public CommandBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Builds the command with a supplied CommandExecutor
         *
         * @param executor CommandExecutor object (recommended use of lambdas)
         * @return complete Command that has been built
         */
        public Command build(CommandExecutor executor) {
            return new Command(prefix, name, description, aliases, isMain, isEnabled, isEssential, subCommandMap, subCommandNames) {
                @Override
                public Optional execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
                    return executor.execute(args);
                }
            };
        }

        /**
         * Builds the command with supplied object instance and its method to invoke upon execution.
         * This is mainly for annotated commands.
         *
         * @param obj    object instance containing the method
         * @param method method for the command to invoke
         * @return complete Command that has been built
         */
        public Command build(Object obj, Method method) {
            return new Command(prefix, name, description, aliases, isMain, isEnabled, isEssential, subCommandMap, subCommandNames) {
                @Override
                public Optional execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
                    return Optional.ofNullable(method.invoke(obj, args));
                }
            };
        }

    }

    /**
     * Gets the prefix of the command
     *
     * @return prefix of command
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the name identifier of the command
     *
     * @return name of command
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the command
     *
     * @return description of command
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the aliases assigned to the command
     *
     * @return list of aliases
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Gets boolean value denoting whether or not the command is a main command or sub command
     *
     * @return true if main command, false if sub command
     */
    public boolean isMain() {
        return isMain;
    }

    /**
     * Gets boolean value denoting whether or not the command is enabled
     *
     * @return true if enabled, false if disabled
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Gets boolean value denoting whether or not the command is essential
     *
     * @return true if essential, false if non-essential
     */
    public boolean isEssential() {
        return isEssential;
    }

    /**
     * Gets the sub command map pertaining to this command
     *
     * @return a map of sub commands for this command
     */
    public Map<String, Command> getSubCommandMap() {
        return subCommandMap;
    }

    @Override
    public String toString() {
        return "(" + prefix + ")" + aliases + " <" + name + " --- " + description + ">";
    }
}
