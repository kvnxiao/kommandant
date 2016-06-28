package com.github.alphahelix00.ordinator.commands;

import com.github.alphahelix00.ordinator.commands.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The registry is the storage containing all the main commands in both a list and for each prefix, a separate command map
 * A single prefix map exists which holds values to the command maps pertaining to a specific prefix key.
 *
 * <p>Created on:   6/23/2016</p>
 * <p>Author:       Kevin Xiao (github.com/alphahelix00)</p>
 */
public class CommandRegistry {

    public static final Logger LOGGER = LoggerFactory.getLogger("CommandRegistry");

    private final Map<String, Map<String, Command>> prefixMap = new HashMap<>();

    private final List<Command> mainCommandList = new ArrayList<>();
    private AbstractCommandHandler commandHandler;

    /**
     * Checks if a command prefix exists within the prefix map
     *
     * @param prefix command prefix string
     * @return true if prefix exists
     */
    public boolean prefixExists(String prefix) {
        return prefixMap.containsKey(prefix);
    }

    /**
     * Adds a command to the registry map
     *
     * @param command command to add to registry
     * @return true if command is added successfully, false if failed (command does not have a unique name)
     */
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
            LOGGER.info("Registered command " + command.getName());
            commandMap.put(commandName, command);
            mainCommandList.add(command);
            return true;
        } else {
            LOGGER.info("A command with that name already exists!");
            return false;
        }
    }

    /**
     * Checks if command exists within the registry map when provided with the command's prefix and name
     *
     * @param prefix command prefix string
     * @param name   name of command
     * @return true if command exists
     */
    public boolean commandExists(String prefix, String name) {
        return getCommandByName(prefix, name).isPresent();
    }

    /**
     * Returns an optional command map that may or may not exist depending on the provided command prefix
     *
     * @param prefix command prefix string
     * @return Optional command map pertaining to the supplied prefix
     */
    public Optional<Map<String, Command>> getCommandMap(String prefix) {
        return Optional.ofNullable(prefixMap.get(prefix));
    }

    /**
     * Gets an unmodifiable copy of the prefix command map
     *
     * @return unmodifiable copy of prefix map containing all command maps
     */
    public Map<String, Map<String, Command>> getPrefixMap() {
        return Collections.unmodifiableMap(prefixMap);
    }

    /**
     * Gets the entire list of all main commands in the registry
     *
     * @return list of all main commands
     */
    public List<Command> getCommandList() {
        return mainCommandList;
    }

    /**
     * Gets command by prefix and (possibly non-unique) alias identifier
     *
     * @param prefix command prefix string
     * @param alias  alias of command
     * @return an Optional containing the command if fount, otherwise empty
     */
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

    /**
     * Gets command by prefix and unique name identifier
     *
     * @param prefix command prefix string
     * @param name   name of command
     * @return an Optional containing the command if found, otherwise empty
     */
    public Optional<Command> getCommandByName(String prefix, String name) {
        return getCommandMap(prefix)
                .filter(stringCommandMap -> stringCommandMap.containsKey(name))
                .map(stringCommandMap -> stringCommandMap.get(name));
    }

    /**
     * Required to establish link from a command handler instance to this command registry instance
     *
     * @param commandHandler an instance of a command handler to link
     * @return returns the supplied command handler
     */
    public AbstractCommandHandler setCommandHandler(AbstractCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        return this.commandHandler;
    }

    /**
     * Returns the linked command handler for this registry
     *
     * @return command handler linked to this registry
     */
    public AbstractCommandHandler getCommandHandler() {
        return commandHandler;
    }
}
