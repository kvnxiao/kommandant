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
package com.github.kvnxiao.kommandant.command

/**
 * Represents the context during runtime when a command is processed from a string.
 * This includes the command alias, (nullable) arguments, and the properties of the command.
 */
open class Context(
    /**
     * The alias used to call the command (this includes the command's prefix + the individual alias concatenated into a single string).
     */
    val alias: String,
    /**
     * The nullable string argument supplied to the command for execution. Will be null if the processed input string was only the prefixed alias of the command.
     */
    val args: String?,
    /**
     * The properties describing the command being executed.
     */
    val properties: CommandProperties
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Context) return false

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
        return "Context(alias='$alias', args=$args, properties=$properties)"
    }
}
