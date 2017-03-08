package com.github.kvnxiao.kommandant.impl

import com.github.kvnxiao.kommandant.ICommandBank
import com.github.kvnxiao.kommandant.Kommandant.Companion.LOGGER
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.utility.CommandMap
import com.github.kvnxiao.kommandant.utility.ImmutableCommandMap
import com.github.kvnxiao.kommandant.utility.PrefixMap
import java.util.*

/**
 * Created by kvnxiao on 3/3/17.
 */
open class CommandBank : ICommandBank {

    @JvmField
    protected val prefixSet: MutableSet<String> = sortedSetOf(Collections.reverseOrder())
    @JvmField
    protected val prefixMap: PrefixMap = mutableMapOf()
    @JvmField
    protected val commandMap: CommandMap = TreeMap()
    @JvmField
    protected val commandUniques: CommandMap = mutableMapOf()

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
        prefixSet.add(command.props.prefix)

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
        return true
    }

    override fun deleteCommand(command: ICommand<*>): Boolean {
        // Remove command info from bank
        if (removeCommand(command)) {

            // Unlink all subcommand references
            command.deleteSubcommands()

            LOGGER.debug("Deleted command '${command.props.uniqueName}' with prefix '${command.props.prefix}'")
            return true
        }
        return false
    }

    override fun getCommand(singleString: String): ICommand<*>? = commandMap[singleString]

    fun getCommand(prefix: String, alias: String): ICommand<*>? = this.getCommand(prefix + alias)

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
        }
        return false
    }

    override fun getPrefixes(): Set<String> = prefixSet.toSet()

    override fun getCommandsForPrefix(prefix: String): ImmutableCommandMap? = prefixMap[prefix]?.toMap()

    override fun getCommands(): ImmutableCommandMap = commandMap.toMap()

    fun isAliasClash(prefix: String, aliases: List<String>): Boolean = aliases.any { commandMap.containsKey(prefix + it) }

}