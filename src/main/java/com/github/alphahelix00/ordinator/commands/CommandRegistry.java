package com.github.alphahelix00.ordinator.commands;

import com.github.alphahelix00.ordinator.commands.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class CommandRegistry {

    protected final Logger LOGGER = LoggerFactory.getLogger("CommandRegistry");

    private final Map<String, Map<String, Command>> prefixMap = new HashMap<>();

    private final List<Command> mainCommandList = new ArrayList<>();
    private AbstractCommandHandler commandHandler;

    public boolean prefixExists(String prefix) {
        return prefixMap.containsKey(prefix);
    }

    public boolean addCommand(Command command) {
        String prefix = command.getPrefix();
        String name = command.getName();

        // If prefix doesn't exist, add a new prefix command map to it
        if (!prefixExists(prefix)) {
            prefixMap.put(prefix, new HashMap<>());
        }
        // Get command map for specified prefix, which could be null
        Map<String, Command> commandMap = prefixMap.get(prefix);
        return addCommand(commandMap, name, command);
    }

    private boolean addCommand(Map<String, Command> commandMap, String commandName, Command command) {
        if (!commandMap.containsKey(commandName)) {
            LOGGER.info("Registered command.");
            commandMap.put(commandName, command);
            mainCommandList.add(command);
            return true;
        } else {
            LOGGER.info("A command with that name already exists!");
            return false;
        }
    }

    public boolean commandExists(String prefix, String name) {
        return getCommandByName(prefix, name).isPresent();
    }

    public Optional<Map<String, Command>> getCommandMap(String prefix) {
        return Optional.ofNullable(prefixMap.get(prefix));
    }

    public Map<String, Map<String, Command>> getPrefixMap() {
        return Collections.unmodifiableMap(prefixMap);
    }

    public List<Command> getCommandList() {
        return mainCommandList;
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

    public AbstractCommandHandler setCommandHandler(AbstractCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        return this.commandHandler;
    }

    public AbstractCommandHandler getCommandHandler() {
        return commandHandler;
    }
}
