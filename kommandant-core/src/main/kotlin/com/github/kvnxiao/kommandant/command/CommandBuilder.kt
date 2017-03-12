package com.github.kvnxiao.kommandant.command

import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * A builder class that helps define a command without needing to specify all the properties for the [ICommand] constructor.
 *
 * @constructor Constructor which defines the [uniqueName] of the command to build.
 * @property[uniqueName] The unique name of the command to build.
 */
open class CommandBuilder<T>(private val uniqueName: String) {

    /**
     * Constructor which defines the [prefix] and [uniqueName] of the command to build.
     */
    constructor(prefix: String, uniqueName: String) : this(uniqueName) {
        this.prefix = prefix
    }

    /**
     * The aliases of the command to build, represented as a (nullable) list of strings. If the list is null, the builder
     * will default to using the command's [uniqueName] as the command's alias.
     */
    private var aliases: List<String>? = null

    /**
     * The description of the command to build. Defaults to [CommandDefaults.NO_DESCRIPTION].
     */
    private var description: String = CommandDefaults.NO_DESCRIPTION

    /**
     * The usage of the command to build. Defaults to [CommandDefaults.NO_USAGE].
     */
    private var usage: String = CommandDefaults.NO_USAGE

    /**
     * The prefix of the command to build. Defaults to [CommandDefaults.PREFIX].
     */
    private var prefix: String = CommandDefaults.PREFIX

    /**
     * Whether the command should execute alongside its subcommand or be skipped when subcommands are processed.
     * Defaults to [CommandDefaults.EXEC_WITH_SUBCOMMANDS].
     */
    private var execWithSubcommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS

    /**
     * Whether the command is disabled. Defaults to [CommandDefaults.IS_DISABLED].
     */
    private var isDisabled: Boolean = CommandDefaults.IS_DISABLED

    /**
     * Sets the prefix of the command to build.
     *
     * @return The current [CommandBuilder] instance.
     */
    fun withPrefix(prefix: String): CommandBuilder<T> {
        this.prefix = prefix
        return this
    }

    /**
     * Sets the description of the command to build.
     *
     * @return The current [CommandBuilder] instance.
     */
    fun withDescription(description: String): CommandBuilder<T> {
        this.description = description
        return this
    }

    /**
     * Sets the usage of the command to build.
     *
     * @return The current [CommandBuilder] instance.
     */
    fun withUsage(usage: String): CommandBuilder<T> {
        this.usage = usage
        return this
    }

    /**
     * Sets whether the command to build should be executed along with its subcommand.
     *
     * @return The current [CommandBuilder] instance.
     */
    fun setExecWithSubcommands(execWithSubcommands: Boolean): CommandBuilder<T> {
        this.execWithSubcommands = execWithSubcommands
        return this
    }

    /**
     * Sets the aliases of the command to build, taking in a vararg amount of strings.
     *
     * @return The current [CommandBuilder] instance.
     */
    fun withAliases(vararg aliases: String): CommandBuilder<T> {
        this.aliases = aliases.asList()
        return this
    }

    /**
     * Sets the aliases of the command to build, taking in a list of strings.
     *
     * @return The current [CommandBuilder] instance.
     */
    fun withAliases(aliases: List<String>): CommandBuilder<T> {
        this.aliases = aliases
        return this
    }

    /**
     * Sets whether the command to build is disabled.
     *
     * @return The current [CommandBuilder] instance.
     */
    fun setDisabled(disabled: Boolean): CommandBuilder<T> {
        this.isDisabled = disabled
        return this
    }

    /**
     * Builds the command with the provided [ICommandExecutable].
     *
     * @return[ICommand] The built command.
     */
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
        /**
         * Kotlin lambda helper to easily define the [executable][ICommandExecutable] command method. It is recommended
         * to not use this in Java as a lambda can be directly used in [build].
         *
         * @return[ICommandExecutable] The executable method for the commmand.
         */
        @JvmStatic
        fun <T> execute(handler: (CommandContext, Array<out Any?>) -> T): ICommandExecutable<T> {
            return object : ICommandExecutable<T> {
                override fun execute(context: CommandContext, vararg opt: Any?): T = handler(context, opt)
            }
        }
    }

}
