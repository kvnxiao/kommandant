package com.github.kvnxiao.kommandant.command

/**
 * Created by kvnxiao on 3/3/17.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CommandAnn(
        val prefix: String = CommandDefaults.PREFIX,
        val uniqueName: String,
        val aliases: Array<String>,
        val description: String = CommandDefaults.NO_DESCRIPTION,
        val usage: String = CommandDefaults.NO_USAGE,
        val parentName: String = CommandDefaults.PARENT,
        val execWithSubcommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
        val isDisabled: Boolean = CommandDefaults.IS_DISABLED
)
