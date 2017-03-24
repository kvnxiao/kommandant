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
 * A data class for holding the context of the command. It parses a string and splits it in two based on the first
 * available space. The first string represents the alias, and the second string represents the arguments leftover.
 *
 * @property[alias] The alias or prefixed-alias used to call the command.
 * @property[args] The (nullable) arguments provided for the command.
 * @property[properties] The properties of the command being called.
 */
open class CommandContext(
    val alias: String,
    val args: String?,
    val properties: CommandProperties) {

    constructor(alias: String, args: String?, command: ICommand<*>): this(alias, args, command.props)

    /**
     * Whether the command context contains arguments for the command.
     *
     * @return[Boolean] Whether the args property in the command context is not null.
     */
    fun hasArgs(): Boolean = args !== null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandContext) return false

        if (alias != other.alias) return false
        if (args != other.args) return false
        if (properties != other.properties) return false

        return true
    }

    override fun hashCode(): Int {
        var result = alias.hashCode()
        result = 31 * result + (args?.hashCode() ?: 0)
        result = 31 * result + properties.hashCode()
        return result
    }

    override fun toString(): String {
        return "CommandContext(alias='$alias', args=$args, properties=$properties)"
    }

}