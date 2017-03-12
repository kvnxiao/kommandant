package com.github.kvnxiao.kommandant.command

/**
 * A data class containing the results after command execution.
 *
 * @property[success] Whether the command execution was successful or not.
 * @property[result] The result of the command execution. Can be null if the command failed to execute.
 */
data class CommandResult<out T>(val success: Boolean, val result: T? = null)