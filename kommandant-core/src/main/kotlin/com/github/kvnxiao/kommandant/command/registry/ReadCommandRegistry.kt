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

/**
 * Interface which defines the ready-only methods required by a command registry
 */
interface ReadCommandRegistry {

    /**
     * Gets a command from the registry by its prefixed alias (prefix + one of the command's aliases).
     * Returns null if the command with the supplied prefixed alias does not exist.
     */
    fun getCommandByAlias(alias: String): CommandPackage<*>?

    /**
     * Gets a command from the registry by its unique id.
     * Returns null if the command with the supplied id does not exist.
     */
    fun getCommandById(id: String): CommandPackage<*>?

    /**
     * Gets a sub-command from the registry by the sub-command's alias and its parent's unique id.
     * Returns null if either the parent id is invalid, or the command with the supplied alias does not exist.
     */
    fun getSubCommandByAlias(alias: String, parentId: String): CommandPackage<*>?

    /**
     * Gets the list of all top-level commands (commands with no parent) from the registry.
     */
    fun getAllCommands(sortById: Boolean = false): List<CommandPackage<*>>

    /**
     * Gets the list of all prefixed command aliases as a string.
     */
    fun getAllCommandAliases(sorted: Boolean = false): List<String>

    /**
     * Returns whether the command with the specified id has sub-commands or not.
     */
    fun hasSubCommands(id: String): Boolean

    /**
     * Gets the sub-command registry belonging to the command with the specified id.
     */
    fun getSubCommandRegistry(parentCommandId: String): SubCommandRegistry?
}
