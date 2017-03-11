package com.github.kvnxiao.kommandant.command

/**
 * An object class containing the default command property settings.
 */
object CommandDefaults {

    /**
     * The default parent name, set as an empty string.
     */
    const val PARENT = ""
    /**
     * The default command description, set as no description available.
     */
    const val NO_DESCRIPTION = "No description available."
    /**
     * The default command usage information, set as no usage information available.
     */
    const val NO_USAGE = "No usage information available."
    /**
     * The default command prefix / activator, set as a forward slash '/'.
     */
    const val PREFIX = "/"
    /**
     * The default setting for whether commands should execute alongside their subcommands, set as false.
     */
    const val EXEC_WITH_SUBCOMMANDS = false
    /**
     * The default setting for whether the command is disabled, set as false (commands default to enabled).
     */
    const val IS_DISABLED = false

}