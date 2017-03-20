package com.github.kvnxiao.kommandant.command

import com.github.kvnxiao.kommandant.Kommandant.Companion.LOGGER
import com.github.kvnxiao.kommandant.utility.CommandMap
import com.github.kvnxiao.kommandant.utility.CommandSet
import java.util.TreeMap

/**
 * An abstract class which represents a command. All commands implement [ICommandExecutable] as their main
 * executing method or function.
 *
 * @property[props] The command properties.
 * @constructor Constructor requiring a [CommandProperties] instance.
 */
abstract class ICommand<T>(var props: CommandProperties) : ICommandExecutable<T> {

    /**
     * A sorted map of [String] to [ICommand] which maps all subcommand aliases to the respective commands.
     */
    val subCommandMap: CommandMap = TreeMap()
    /**
     * A set containing all subcommands registered to this command.
     */
    val subCommands: CommandSet = mutableSetOf()

    /**
     * Constructor which takes in values for [CommandProperties] directly.
     */
    constructor(prefix: String, uniqueName: String, description: String, usage: String, execWithSubcommands: Boolean, isDisabled: Boolean, vararg aliases: String) : this(CommandProperties(prefix, uniqueName, description, usage, execWithSubcommands, isDisabled, aliases.asList()))

    /**
     * Constructor which takes in values for [CommandProperties] directly.
     */
    constructor(prefix: String, uniqueName: String, description: String, usage: String, execWithSubcommands: Boolean, isDisabled: Boolean, aliases: List<String>) : this(CommandProperties(prefix, uniqueName, description, usage, execWithSubcommands, isDisabled, aliases))

    /**
     * Adds a provided command as a subcommand to this command.
     *
     * @param[subCommand] The subcommand to add.
     * @return[ICommand] The current command (NOT the subcommand!).
     */
    open fun addSubcommand(subCommand: ICommand<*>): ICommand<T> {
        for (alias in subCommand.props.aliases) {
            if (subCommandMap.containsKey(alias)) {
                LOGGER.error("Failed to link sub-command '${subCommand.props.uniqueName}' to '${this.props.uniqueName}'; the alias '$alias' is already taken!")
                return this
            } else {
                subCommandMap.put(alias, subCommand)
            }
        }
        subCommands.add(subCommand)
        return this
    }

    /**
     * Checks whether this command has subcommands.
     *
     * @return[Boolean] Whether the subcommand set is empty or not.
     */
    open fun hasSubcommands(): Boolean = subCommands.isNotEmpty()

    /**
     * Removes all subcommands associated to this command. This clears the [subCommandMap] map and [subCommands] set.
     */
    open fun deleteSubcommands() {
        if (this.hasSubcommands()) {
            subCommandMap.clear()
            subCommands.forEach(ICommand<*>::deleteSubcommands)
            subCommands.clear()
        }
    }

    /**
     * Overrided toString method returns the [CommandProperties.uniqueName].
     *
     * @see[CommandProperties]
     */
    override fun toString(): String {
        return this.props.toString()
    }

    /**
     * Equals comparator takes into account the [props] and [subCommands] set.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ICommand<*>) return false

        if (props != other.props) return false
        if (subCommands != other.subCommands) return false

        return true
    }

    /**
     * Hashcode takes into account the [props] and [subCommands] set.
     */
    override fun hashCode(): Int {
        var result = props.hashCode()
        result = 31 * result + subCommands.hashCode()
        return result
    }

}