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

import com.github.kvnxiao.kommandant.DefaultErrorHandler

open class CommandPackage<T>(
    val executable: CommandExecutable<T>,
    val properties: CommandProperties,
    val errorHandler: CommandErrorHandler = DefaultErrorHandler()
) {

    override fun toString(): String {
        return "CommandPackage(id=${properties.id}, prefix=${properties.prefix}, aliases=${properties.aliases}, parentId=${properties.parentId})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandPackage<*>) return false

        if (properties != other.properties) return false

        return true
    }

    override fun hashCode(): Int {
        return properties.hashCode()
    }

}
