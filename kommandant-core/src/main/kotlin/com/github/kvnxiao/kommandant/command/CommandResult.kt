package com.github.kvnxiao.kommandant.command

/**
 * A data class containing the results after command execution.
 */
data class CommandResult<out T>(
        /**
         * Whether the command execution was successful or not.
         */
        val success: Boolean,
        /**
         * The result of the command execution. Can be null if the command failed to execute.
         */
        val result: T? = null
)