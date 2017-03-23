/*
 * Copyright 2017 Ze Hao Xiao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kvnxiao.kommandant.command

/**
 * A data class containing the properties of the command.
 *
 * @property[prefix] The command's prefix. Defaults to [CommandDefaults.PREFIX].
 * @property[uniqueName] The command's unique name / identifier.
 * @property[description] The command's description. Defaults to [CommandDefaults.NO_DESCRIPTION].
 * @property[usage] The command's usage information. Defaults to [CommandDefaults.NO_USAGE].
 * @property[execWithSubcommands] Specifies whether to execute the command alongside its subcommands. Defaults to [CommandDefaults.EXEC_WITH_SUBCOMMANDS].
 * @property[disabled] Specifies whether the command is disabled or not. Defaults to [CommandDefaults.IS_DISABLED].
 * @property[aliases] The command's aliases. Defaults to a singleton list containing the [uniqueName].
 */
data class CommandProperties(
    val prefix: String = CommandDefaults.PREFIX,
    val uniqueName: String,
    val description: String = CommandDefaults.NO_DESCRIPTION,
    val usage: String = CommandDefaults.NO_USAGE,
    val execWithSubcommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
    val disabled: Boolean = CommandDefaults.IS_DISABLED,
    val aliases: List<String> = listOf(uniqueName)) {

    /**
     * Overrided toString method returns the [uniqueName].
     *
     * @return[uniqueName] The command's unique name / identifier.
     */
    override fun toString(): String {
        return this.uniqueName
    }

}