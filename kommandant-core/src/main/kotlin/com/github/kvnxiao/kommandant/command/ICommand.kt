package com.github.kvnxiao.kommandant.command

import com.github.kvnxiao.kommandant.Kommandant.Companion.LOGGER
import com.github.kvnxiao.kommandant.utility.CommandMap
import com.github.kvnxiao.kommandant.utility.CommandSet
import java.util.*

/**
 * Created by kvnxiao on 3/3/17.
 */
abstract class ICommand<T>(var props: CommandProperties) : ICommandExecutable<T> {

    val subCommandMap: CommandMap = TreeMap()
    val subCommands: CommandSet = mutableSetOf()

    constructor(prefix: String, uniqueName: String, description: String, usage: String, execWithSubcommands: Boolean, isDisabled: Boolean, vararg aliases: String) : this(CommandProperties(prefix, uniqueName, description, usage, execWithSubcommands, isDisabled, aliases.asList()))
    constructor(prefix: String, uniqueName: String, description: String, usage: String, execWithSubcommands: Boolean, isDisabled: Boolean, aliases: List<String>) : this(CommandProperties(prefix, uniqueName, description, usage, execWithSubcommands, isDisabled, aliases))

    fun addSubcommand(subCommand: ICommand<*>) : ICommand<T> {
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

    fun hasSubcommands(): Boolean = subCommands.isNotEmpty()

    fun deleteSubcommands() {
        if (this.hasSubcommands()) {
            subCommandMap.clear()
            subCommands.forEach(ICommand<*>::deleteSubcommands)
            subCommands.clear()
        }
    }

    override fun toString(): String {
        return this.props.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ICommand<*>) return false

        if (props != other.props) return false
        if (subCommands != other.subCommands) return false

        return true
    }

    override fun hashCode(): Int {
        var result = props.hashCode()
        result = 31 * result + subCommands.hashCode()
        return result
    }

}