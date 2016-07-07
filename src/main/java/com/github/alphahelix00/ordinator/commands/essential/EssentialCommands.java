package com.github.alphahelix00.ordinator.commands.essential;

import com.github.alphahelix00.ordinator.Ordinator;
import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.handler.AbstractCommandHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Essential command implementations:
 * enable, disable, and help command
 *
 * <p>Created on:   6/24/2016</p>
 * <p>Author:       Kevin Xiao (github.com/alphahelix00)</p>
 */
public class EssentialCommands {

    /**
     * Enable command which helps enable any disabled commands so that they may be called again
     */
    public static class Enable extends Command {

        private static final String NAME = "Enable Command";
        private static final String DESCRIPTION = "enables the specified command";
        private static final List<String> ALIAS = Collections.singletonList("enable");
        private static final String PREFIX = "!";
        private static final String USAGE = PREFIX + ALIAS.get(0) + " <command alias>";

        public Enable() {
            super(PREFIX, NAME, DESCRIPTION, USAGE, ALIAS, true, true, true, new HashMap<>(), new HashMap<>());
        }

        @Override
        public Optional execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
            if (!args.isEmpty()) {
                AbstractCommandHandler commandHandler = Ordinator.getCommandRegistry().getCommandHandler();
                Optional<Command> command = commandHandler.gotoCommand(args);
                command.ifPresent(commandHandler::enableCommand);
                return command;
            }
            return Optional.empty();
        }

    }

    /**
     * Disable command which disables any non-essential commands and prevents them from being called
     */
    public static class Disable extends Command {

        private static final String NAME = "Disable Command";
        private static final String DESCRIPTION = "disables the specified command";
        private static final List<String> ALIAS = Collections.singletonList("disable");
        private static final String PREFIX = "!";
        private static final String USAGE = PREFIX + ALIAS.get(0) + " <command alias>";

        public Disable() {
            super(PREFIX, NAME, DESCRIPTION, USAGE, ALIAS, true, true, true, new HashMap<>(), new HashMap<>());
        }

        @Override
        public Optional execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
            if (!args.isEmpty()) {
                AbstractCommandHandler commandHandler = Ordinator.getCommandRegistry().getCommandHandler();
                Optional<Command> command = commandHandler.gotoCommand(args);
                command.ifPresent(commandHandler::disableCommand);
                return command;
            }
            return Optional.empty();
        }

    }

    /**
     * Help command which can retrieve a formatted list of all main commands, or retrieve more information
     * regarding a specific command
     */
    public static class Help extends Command {

        private static final String NAME = "Help Command";
        private static final String DESCRIPTION = "lists all commands, or info about a specific command";
        private static final List<String> ALIAS = Collections.singletonList("help");
        private static final String PREFIX = "!";
        private static final String USAGE = PREFIX + ALIAS.get(0) + " ||| " + PREFIX + ALIAS.get(0) + " <command alias>";

        public Help() {
            super(PREFIX, NAME, DESCRIPTION, USAGE, ALIAS, true, true, true, new HashMap<>(), new HashMap<>());
        }

        @Override
        public Optional execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
            if (args.isEmpty()) {
                String commandList = getCommandListQuote();
                System.out.println(commandList);
                return Optional.of(commandList);
            } else {
                AbstractCommandHandler commandHandler = Ordinator.getCommandRegistry().getCommandHandler();
                Optional<Command> command = commandHandler.gotoCommand(args);
                if (command.isPresent()) {
                    String commandInfo = getCommandInfoQuote(command.get());
                    System.out.println(commandInfo);
                    return Optional.of(commandInfo);
                }
            }
            return Optional.empty();
        }

        /**
         * Gets more information regarding a specific command
         *
         * @param command command to get information
         * @return a formatted string containing a command's name, description, and any sub commands that it has
         */
        public static String getCommandInfoQuote(Command command) {
            List<String> text = new ArrayList<>();
            text.add(String.format("%1$-12s: %2$s", "Command Name", command.getName()));
            text.add(String.format("%1$-12s: %2$s", "Description", command.getDescription()));
            text.add(String.format("%1$-12s: %2$s", "Usage", command.getUsage()));
            text.add(String.format("%1$-12s: %2$s", "Sub-Commands", (getSubCommandAliases(command))));
            return String.join("\r\n", text);
        }

        /**
         * Gets a string list of sub commands by their alias for a specified command
         *
         * @param command the parent command to get sub command information from
         * @return formatted string of sub command aliases, separated by commas
         */
        public static String getSubCommandAliases(Command command) {
            if (!command.getSubCommandNames().isEmpty()) {
                List<String> aliases = command.getSubCommandMap().values().stream().map(command1 -> command1.getAliases().toString()).collect(Collectors.toList());
                Collections.sort(aliases);
                return String.join(", ", aliases);
            }
            return "N/A";
        }

        /**
         * Gets a formatted list of all main commands currently stored in the registry
         *
         * @return a formatted string containing a list of all main commands and their descriptions
         */
        public static String getCommandListQuote() {
            List<Command> mainCommands = Ordinator.getCommandRegistry().getCommandList();
            List<String> text = new ArrayList<>();
            text.add("[COMMAND LIST]");
            Collections.sort(mainCommands, COMMAND_COMPARATOR);
            mainCommands.forEach(command -> text.add(getFormattedString(command)));
            return String.join("\r\n", text);
        }

        /**
         * Gets the prefix, command aliases, and description of a command in a formatted string
         *
         * @param command command to retrieve information from
         * @return a formatted string of prefix + command aliases, followed by the description for a command
         */
        public static String getFormattedString(Command command) {
            String prefix = command.getPrefix();
            List<String> aliasList = command.getAliases();
            Collections.sort(aliasList);
            String aliases = prefix + String.join(", " + prefix, aliasList);
            String description = command.getDescription();
            return String.format("%1$-16s - %2$s", aliases, description);
        }

        /**
         * Comparator to sort commands, which first sorts the aliases of each command by alphabetical order,
         * and then sorts all commands by the natural occurence of prefixes followed by the first sorted alias
         */
        public static final Comparator<Command> COMMAND_COMPARATOR = (o1, o2) -> {
            List<String> o1alias = o1.getAliases(), o2alias = o2.getAliases();
            Collections.sort(o1alias);
            Collections.sort(o2alias);
            if (o1.getPrefix().equals(o2.getPrefix())) {
                return o1alias.get(0).compareTo(o2alias.get(0));
            } else {
                return o1.getPrefix().compareTo(o2.getPrefix());
            }
        };

    }
}
