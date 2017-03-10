package com.github.kvnxiao.kommandant.command

import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
open class CommandBuilder<T>(private val uniqueName: String) {

    private var aliases: List<String>? = null

    private var description: String = CommandDefaults.NO_DESCRIPTION
    private var usage: String = CommandDefaults.NO_USAGE
    private var prefix: String = CommandDefaults.PREFIX
    private var execWithSubcommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS
    private var isDisabled: Boolean = CommandDefaults.IS_DISABLED

    constructor(prefix: String, uniqueName: String) : this(uniqueName) {
        this.prefix = prefix
    }

    fun withPrefix(prefix: String): CommandBuilder<T> {
        this.prefix = prefix
        return this
    }

    fun withDescription(description: String): CommandBuilder<T> {
        this.description = description
        return this
    }

    fun withUsage(usage: String): CommandBuilder<T> {
        this.usage = usage
        return this
    }

    fun setExecWithSubcommands(execWithSubcommands: Boolean): CommandBuilder<T> {
        this.execWithSubcommands = execWithSubcommands
        return this
    }

    fun withAliases(vararg aliases: String): CommandBuilder<T> {
        this.aliases = aliases.asList()
        return this
    }

    fun withAliases(aliases: List<String>): CommandBuilder<T> {
        this.aliases = aliases
        return this
    }

    fun setDisabled(disabled: Boolean): CommandBuilder<T> {
        this.isDisabled = disabled
        return this
    }

    fun build(executor: ICommandExecutable<T>): ICommand<T> {
        if (aliases === null) aliases = Collections.singletonList(uniqueName)
        return object : ICommand<T>(prefix, uniqueName, description, usage, execWithSubcommands, isDisabled, aliases!!) {
            @Throws(InvocationTargetException::class, IllegalAccessException::class)
            override fun execute(context: CommandContext, vararg opt: Any?): T {
                return executor.execute(context, *opt)
            }
        }
    }

    companion object {
        @JvmStatic
        fun <T> execute(handler: (CommandContext, Array<out Any?>) -> T): ICommandExecutable<T> {
            return object : ICommandExecutable<T> {
                override fun execute(context: CommandContext, vararg opt: Any?): T = handler(context, opt)
            }
        }
    }

}
