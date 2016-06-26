package com.github.alphahelix00.ordinator.commands.essential;

import com.github.alphahelix00.ordinator.Ordinator;
import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.handler.AbstractCommandHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created on:   6/24/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class EssentialCommands {

    public static class Enable extends Command {

        private static final String NAME = "Enable Command";
        private static final String DESCRIPTION = "enables the specified command";
        private static final List<String> ALIAS = Collections.singletonList("enable");
        private static final String PREFIX = "!";

        public Enable() {
            super(PREFIX, NAME, DESCRIPTION, ALIAS, true, true, true, new HashMap<>(), new HashMap<>());
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

    public static class Disable extends Command {

        private static final String NAME = "Disable Command";
        private static final String DESCRIPTION = "disables the specified command";
        private static final List<String> ALIAS = Collections.singletonList("disable");
        private static final String PREFIX = "!";

        public Disable() {
            super(PREFIX, NAME, DESCRIPTION, ALIAS, true, true, true, new HashMap<>(), new HashMap<>());
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

    public static class Help extends Command {

        private static final String NAME = "Help Command";
        private static final String DESCRIPTION = "lists all commands, or info about a specific command";
        private static final List<String> ALIAS = Collections.singletonList("help");
        private static final String PREFIX = "!";

        public Help() {
            super(PREFIX, NAME, DESCRIPTION, ALIAS, true, true, true, new HashMap<>(), new HashMap<>());
        }

        @Override
        public Optional execute(List<String> args) throws IllegalAccessException, InvocationTargetException {
            if (args.isEmpty()) {
                String commandList = getCommandListQuote();
//                System.out.println(commandList);
                return Optional.of(commandList);
            } else {
                AbstractCommandHandler commandHandler = Ordinator.getCommandRegistry().getCommandHandler();
                Optional<Command> command = commandHandler.gotoCommand(args);
                if (command.isPresent()) {
                    String commandInfo = getCommandInfoQuote(command.get());
//                    System.out.println(commandInfo);
                    return Optional.of(commandInfo);
                }
            }
            return Optional.empty();
        }

        protected String getCommandInfoQuote(Command command) {
            List<String> text = new ArrayList<>();
            text.add(String.format("%1$-12s: %2$s", "Command Name", command.getName()));
            text.add(String.format("%1$-12s: %2$s", "Description", command.getDescription()));
            text.add(String.format("%1$-12s: %2$s", "Sub-Commands", (!command.getSubCommandNames().isEmpty()) ? command.getSubCommandNames().toString() : "N/A"));
            return String.join("\r\n", text);
        }

        protected String getCommandListQuote() {
            List<Command> mainCommands = Ordinator.getCommandRegistry().getCommandList();
            List<String> text = new ArrayList<>();
            text.add("[COMMAND LIST]");
            Collections.sort(mainCommands, COMMAND_COMPARATOR);
            mainCommands.forEach(command -> text.add(getFormattedString(command)));
            return String.join("\r\n", text);
        }

        protected String getFormattedString(Command command) {
            String prefix = command.getPrefix();
            List<String> aliasList = command.getAliases();
            Collections.sort(aliasList);
            String aliases = prefix + String.join(", " + prefix, aliasList);
            String description = command.getDescription();
            return String.format("%1$-16s - %2$s", aliases, description);
        }

        protected static final Comparator<Command> COMMAND_COMPARATOR = (o1, o2) -> {
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
