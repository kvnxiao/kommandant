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
 *   See the License for the specific language governing commandSettings and
 *   limitations under the License.
 */
package com.github.kvnxiao.kommandant.command.registry

import com.github.kvnxiao.kommandant.command.CommandPackage

interface ReadCommandRegistry {

    // Get command by prefixed alias
    fun getCommandByAlias(alias: String): CommandPackage<*>?
    fun getCommandById(id: String): CommandPackage<*>?
    fun getSubCommandByAlias(alias: String, parentId: String): CommandPackage<*>?

    // Get list of all top-level commands
    fun getAllCommands(sortById: Boolean = false): List<CommandPackage<*>>
    // Get list of all prefixed aliases
    fun getAllCommandAliases(sorted: Boolean = false): List<String>

    // Sub-command info
    fun hasSubCommands(parentId: String): Boolean
    fun getSubCommandRegistry(parentId: String): SubCommandRegistry?
}
