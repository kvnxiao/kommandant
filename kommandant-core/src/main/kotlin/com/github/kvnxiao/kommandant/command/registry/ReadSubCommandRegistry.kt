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
package com.github.kvnxiao.kommandant.command.registry

import com.github.kvnxiao.kommandant.command.CommandPackage

/**
 * Interface which defines the ready-only methods required by a command registry, for sub-commands only.
 */
interface ReadSubCommandRegistry {
    /**
     * Gets a sub-command from the registry by its unique id.
     * Returns null if the sub-command with the supplied id does not exist.
     */
    fun getSubCommandById(id: String): CommandPackage<*>?

    /**
     * Gets a sub-command from the registry by its alias.
     * Returns null if the sub-command with the supplied alias does not exist.
     */
    fun getSubCommandIdByAlias(alias: String): String?

    /**
     * Gets the list of all sub-command ids from the registry.
     */
    fun getAllSubCommandIds(sortById: Boolean = true): List<String>

    /**
     * Gets the list of all sub-command aliases from the registry.
     */
    fun getAllSubCommandAliases(sorted: Boolean = true): List<String>

    /**
     * Check if this sub-command registry is not empty (i.e., that there are sub-commands defined)
     */
    fun containsCommands(): Boolean
}
