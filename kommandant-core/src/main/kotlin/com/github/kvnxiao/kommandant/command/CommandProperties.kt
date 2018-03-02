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
 * Data class containing properties associated with every command.
 */
data class CommandProperties(
    // Required
    /**
     * Unique id of the command.
     */
    val id: String,
    /**
     * Set of aliases belonging to the command. Will use the value of [id] if not set.
     */
    val aliases: Set<String> = setOf(id),
    /**
     * Prefix used by the command. Defaults to [CommandDefaults.NO_PREFIX].
     */
    val prefix: String = CommandDefaults.NO_PREFIX,
    // Parent command id
    /**
     * Unique id of the parent command. Defaults to [CommandDefaults.PARENT_ID].
     */
    val parentId: String = CommandDefaults.PARENT_ID,
    // Metadata
    /**
     * The description of what the command does. Defaults to [CommandDefaults.NO_DESCRIPTION].
     */
    val description: String = CommandDefaults.NO_DESCRIPTION,
    /**
     * The usage information describing how the command should be used. Defaults to [CommandDefaults.NO_USAGE].
     */
    val usage: String = CommandDefaults.NO_USAGE,
    // Command settings
    /**
     * The command setting for whether this command should be executed along with its sub-commands in a fire-and-forget
     * manner. Only applies to when the sub-commands of this command are executed.
     */
    val execWithSubCommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
    /**
     * The command setting for whether this command is enabled or disabled.
     */
    val isDisabled: Boolean = CommandDefaults.IS_DISABLED
) {
    init {
        check(aliases.isNotEmpty(), { "CommandProperties aliases cannot be empty!" })
    }
}
