/*
 *   Copyright (C) 2017-2018 Ze Hao Xiao
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.kvnxiao.kommandant.command

/**
 * Sealed class to represent a command execution's result. Very similar to an "Either" class, where the left
 * value is an error and the right value is an actual value. Either a value has been returned by the command or
 * an error occurred in executing the command.
 */
sealed class CommandResult<out T> {

    data class Error(val error: Exception) : CommandResult<Nothing>()
    data class Value<out T>(val value: T) : CommandResult<T>()
}

/**
 * Wrapper to construct a CommandResult sealed class within a try-catch clause.
 */
inline fun <T> tryExecute(action: () -> T, cleanup: (ex: Exception) -> Unit = {}): CommandResult<T> =
    try {
        CommandResult.Value(action())
    } catch (ex: Exception) {
        cleanup(ex)
        CommandResult.Error(ex)
    }

/**
 * Checks if the command result is a boolean or not
 */
fun <T> CommandResult<T>.isError(): Boolean = this is CommandResult.Error

/**
 * Attempts to retrieve a value from the command result if it exists, otherwise returns a default value without handling
 * the error.
 */
inline fun <T> CommandResult<T>.getOrElse(default: () -> T): T =
    when (this) {
        is CommandResult.Value -> this.value
        else -> default()
    }

/**
 * Attempts to retrieve a value from the command result if it exists, otherwise handles the error in addition to
 * returning a default value.
 */
inline fun <T> CommandResult<T>.getOrHandle(handleError: (ex: Exception) -> T): T =
    when (this) {
        is CommandResult.Value -> this.value
        is CommandResult.Error -> handleError(this.error)
    }
