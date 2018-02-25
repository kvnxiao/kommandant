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

open class Context(
    val alias: String,
    val args: String?,
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
