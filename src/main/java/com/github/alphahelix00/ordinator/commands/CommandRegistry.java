package com.github.alphahelix00.ordinator.commands;

import com.github.alphahelix00.ordinator.commands.handler.CommandHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
@Slf4j(topic = "CommandRegistry")
public class CommandRegistry {

    private final Map<String, Map<String, Command>> prefixMap = new HashMap<>();
    private final List<Command> mainCommandList = new ArrayList<>();
    private final CommandHandler commandHandler = new CommandHandler(this);

    public boolean prefixExists(String prefix) {
        return prefixMap.containsKey(prefix);
    }

    public void addCommand(Command command) {
        String prefix = command.getPrefix();
        String name = command.getName();

        // If prefix doesn't exist, add a new prefix command map to it
        if (!prefixExists(prefix)) {
            prefixMap.put(prefix, new HashMap<>());
        }
        // Get command map for specified prefix, which could be null
        Map<String, Command> commandMap = prefixMap.get(prefix);
        addCommand(commandMap, name, command);
    }

    private void addCommand(Map<String, Command> commandMap, String commandName, Command command) {
        if (!commandMap.containsKey(commandName)) {
            log.info("Registering command " + command.getName());
            commandMap.put(commandName, command);
        } else {
            log.info("A command with name \"" + commandName + "\" already exists!");
        }
    }

    public Optional<Map<String, Command>> getCommandMap(String prefix) {
        return Optional.ofNullable(prefixMap.get(prefix));
    }

    public Map<String, Map<String, Command>> getPrefixMap() {
        return Collections.unmodifiableMap(prefixMap);
    }

    public Optional<Command> getCommandByAlias(String prefix, String alias) {
        Optional<Map<String, Command>> commandMap = getCommandMap(prefix);
        if (commandMap.isPresent()) {
            for (Command command : commandMap.get().values()) {
                if (command.getAliases().contains(alias)) {
                    return Optional.of(command);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Command> getCommandByName(String prefix, String name) {
        return getCommandMap(prefix)
                .filter(stringCommandMap -> stringCommandMap.containsKey(name))
                .map(stringCommandMap -> stringCommandMap.get(name));
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
