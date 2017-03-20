package com.github.kvnxiao.kommandant.command

/**
 * An annotation class that should be used by classes implementing [ICommandParser][com.github.kvnxiao.kommandant.ICommandParser]
 * (e.g. [CommandParser][com.github.kvnxiao.kommandant.impl.CommandParser]), which will process classes for methods
 * annotated with this annotation and create new commands from them.
 *
 * The annotation should target methods with the same signature as [ICommandExecutable] to define commands and their properties.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CommandAnn(
    /**
     * Defines the prefix of the command. Defaults to [CommandDefaults.PREFIX].
     */
    val prefix: String = CommandDefaults.PREFIX,
    /**
     * Defines the unique name identifier of the command. This field is always required.
     */
    val uniqueName: String,
    /**
     * Defines the aliases for the command represented in an array of strings. This field is always required.
     */
    val aliases: Array<String>,
    /**
     * Defines the description of the command. Defaults to [CommandDefaults.NO_DESCRIPTION].
     */
    val description: String = CommandDefaults.NO_DESCRIPTION,
    /**
     * Defines the usage of the command. Defaults to [CommandDefaults.NO_USAGE].
     */
    val usage: String = CommandDefaults.NO_USAGE,
    /**
     * Defines the unique name of the parent command which implies that this command will be a subcommand of that parent.
     * This is used to link subcommands to their parent commands. Defaults to [CommandDefaults.PARENT] as an empty string representing no parent command.
     */
    val parentName: String = CommandDefaults.PARENT,
    /**
     * Defines whether the command should execute alongside its subcommand or be skipped when subcommands are processed.
     * Defaults to [CommandDefaults.EXEC_WITH_SUBCOMMANDS].
     */
    val execWithSubcommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
    /**
     * Defines whether the command is disabled. Defaults to [CommandDefaults.IS_DISABLED].
     */
    val isDisabled: Boolean = CommandDefaults.IS_DISABLED
)
