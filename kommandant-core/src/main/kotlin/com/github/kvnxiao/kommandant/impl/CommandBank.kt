package com.github.kvnxiao.kommandant.impl

import com.github.kvnxiao.kommandant.ICommandBank
import com.github.kvnxiao.kommandant.Kommandant.Companion.LOGGER
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.utility.CommandMap
import com.github.kvnxiao.kommandant.utility.ImmutableCommandMap
import com.github.kvnxiao.kommandant.utility.PrefixMap
import java.util.*

/**
 * The default implementation of [ICommandBank]. Capable of adding, removing, finding, and deleting [commands][ICommand].
 */
open class CommandBank : ICommandBank {

    /**
     * The set of all prefixes registered in the command bank, for all commands.
     */
    @JvmField
    protected val prefixSet: MutableSet<String> = sortedSetOf(Collections.reverseOrder())
    /**
     * The set of all custom prefixes registered in the command bank.
     */
    @JvmField
    protected val customPrefixSet: MutableSet<String> = sortedSetOf(Collections.reverseOrder())
    /**
     * The mutable map which maps prefixes to the respective [CommandMap].
     */
    @JvmField
    protected val prefixMap: PrefixMap = mutableMapOf()
    /**
     * The mutable map which maps "prefix + aliases" strings to the respective command, for all commands.
     */
    @JvmField
    protected val commandMap: CommandMap = TreeMap()
    /**
     * The mutable map which maps unique name identifier strings to the respective command, for all commands.
     */
    @JvmField
    protected val commandUniques: CommandMap = mutableMapOf()

    companion object {
        /**
         * An immutable, empty command map.
         *
         * @see[getCommandsForPrefix]
         */
        @JvmField
        val emptyCommandMap: ImmutableCommandMap = kotlin.collections.emptyMap()
    }

    /**
     * Adds a [command] to the registry. Failure to add commands occur if an alias clashes with another command's alias
     * for the provided prefix, or if the command's identifier / name is not unique.
     *
     * @param[command] The command to add.
     * @return[Boolean] Whether the command was added successfully to the registry.
     */
    override fun addCommand(command: ICommand<*>): Boolean {
        // Cannot add command if there is already an existing alias
        if (isAliasClash(command.props.prefix, command.props.aliases)) {
            LOGGER.warn("Could not register command '${command.props.uniqueName}' with prefix '${command.props.prefix}' due to clashing prefix + aliases")
            return false
        } else if (commandUniques.contains(command.props.uniqueName)) {
            LOGGER.warn("Could not register command '${command.props.uniqueName}' with prefix '${command.props.prefix}' due to clashing unique name")
            return false
        }

        // Add prefix to bank
        if (!addPrefix(command.props.prefix)) {
            LOGGER.warn("Could not register '$command' with prefix '${command.props.prefix}' as the prefix could not be added to the registry.")
        }

        // Create prefixMap if non-existent
        if (!prefixMap.containsKey(command.props.prefix))
            prefixMap.put(command.props.prefix, mutableMapOf())

        // Insert command into prefixMap and commandMap
        command.props.aliases.forEach {
            prefixMap[command.props.prefix]!!.put(it, command)
            commandMap.put(command.props.prefix + it, command)
        }

        // Add command to unique map
        commandUniques.put(command.props.uniqueName, command)

        LOGGER.info("Registered command '${command.props.uniqueName}' with prefix '${command.props.prefix}'")
        return true
    }

    /**
     * Removes the command from the registry, but does not destroy subcommand references for garbage collection.
     *
     * @param[command] The command to remove.
     * @return[Boolean] Whether the removal was successful.
     * @see[deleteCommand]
     */
    override fun removeCommand(command: ICommand<*>): Boolean {
        // Cannot delete command if it doesn't exist in the bank
        if (!isAliasClash(command.props.prefix, command.props.aliases) && !commandUniques.contains(command.props.uniqueName)) return false

        // Remove all occurrences of command from prefixMap and commandMap
        command.props.aliases.forEach {
            prefixMap[command.props.prefix]!!.remove(it)
            commandMap.remove(command.props.prefix + it)
        }

        // Delete command map from prefix map if this was the last command in the prefixMap
        if (prefixMap[command.props.prefix]!!.isEmpty()) {
            prefixMap.remove(command.props.prefix)
            prefixSet.remove(command.props.prefix)
        }

        // Remove command from unique name set
        commandUniques.remove(command.props.uniqueName)
        LOGGER.debug("Removed command '${command.props.uniqueName}' with prefix '${command.props.prefix}'")
        return true
    }

    /**
     * Deletes the command from the registry by removing it and unlinking all subcommand references.
     *
     * @param[command] The command to delete.
     * @return[Boolean] Whether the deletion was successful.
     * @see[removeCommand]
     */
    override fun deleteCommand(command: ICommand<*>): Boolean {
        // Remove command info from bank
        if (removeCommand(command)) {

            // Unlink all subcommand references
            command.deleteSubcommands()

            LOGGER.debug("Deleted and removed subcommand references for the command '${command.props.uniqueName}' with prefix '${command.props.prefix}'")
            return true
        }
        return false
    }

    /**
     * Gets a command that exists in the [commandMap] by providing a single string that represents the command's
     * "prefix + alias" used in command processing / execution.
     *
     * @return[ICommand] The (nullable) command with the provided prefix and alias combination.
     */
    override fun getCommand(singleString: String): ICommand<*>? = commandMap[singleString]

    /**
     * Adds a prefix to the bank to keep track of all prefixes used by commands.
     *
     * @return[Boolean] Whether the prefix was added successfully. Returns false if it already exists.
     */
    override fun addPrefix(prefix: String): Boolean {
        if (prefixSet.contains(prefix)) return true
        return prefixSet.add(prefix)
    }

    /**
     * Changes the prefix of a command in the registry.
     *
     * @return[Boolean] Whether the prefix change was successful.
     */
    override fun changePrefix(command: ICommand<*>, newPrefix: String): Boolean {
        // Cannot change prefix if command doesn't exist in bank
        if (!isAliasClash(command.props.prefix, command.props.aliases) && !commandUniques.contains(command.props.uniqueName)) return false

        val oldPrefix = command.props.prefix

        // Remove old command info from bank
        removeCommand(command)

        // Set new prefix for command and re-add the command to bank
        command.props.prefix = newPrefix
        if (addCommand(command)) {
            LOGGER.debug("Renamed prefix for command '${command.props.uniqueName}' from '$oldPrefix' to '${command.props.prefix}'")
        } else {
            // Re-add command to the bank with old prefix
            command.props.prefix = oldPrefix
            addCommand(command)
        }
        return false
    }

    /**
     * Gets an immutable [prefixSet].
     *
     * @return[Set] The set of all prefixes.
     */
    override fun getPrefixes(): Set<String> = prefixSet.toSet()

    /**
     * Gets an immutable command map of aliases to commands for the provided prefix. The command map may be empty
     * if the alias does not exist in the [prefixSet].
     *
     * @return[ImmutableCommandMap] The immutable command map of aliases to commands. Can be empty if prefix is invalid.
     */
    override fun getCommandsForPrefix(prefix: String): ImmutableCommandMap = prefixMap[prefix]?.toMap() ?: emptyCommandMap

    /**
     * Gets an immutable command map of all "prefix + aliases" to commands.
     *
     * @return[ImmutableCommandMap] The immutable command map of all "prefix + aliases" to commands.
     */
    override fun getCommands(): ImmutableCommandMap = commandMap.toMap()

    /**
     * Gets an immutable list of all registered commands in the registry.
     *
     * @return[List] The immutable list of all registered commands.
     */
    override fun getCommandsUnique(): List<ICommand<*>> = commandUniques.values.toList()

    /**
     * Deletes all commands from the registry and clears the registry's maps and sets.
     */
    override fun clearAll() {
        LOGGER.debug("Clearing all commands from the bank.")
        commandUniques.values.forEach(ICommand<*>::deleteSubcommands)
        prefixSet.clear()
        prefixMap.clear()
        commandMap.clear()
        commandUniques.clear()
    }

    /**
     * Checks whether the provided prefix and aliases clash with the prefix and aliases of existing commands in the registry.
     *
     * @param[prefix] The prefix to check.
     * @param[aliases] The list of aliases for the specified prefix.
     * @return[Boolean] Whether the provided prefix and aliases clash with any of the existing commands in the registry.
     */
    open fun isAliasClash(prefix: String, aliases: List<String>): Boolean = aliases.any { commandMap.containsKey(prefix + it) }

}