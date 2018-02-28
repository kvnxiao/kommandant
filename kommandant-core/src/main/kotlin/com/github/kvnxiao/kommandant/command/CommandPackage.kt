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

/**
 * A container class that represents a "fully functional command". Holds the command executable, command properties,
 * and error handler for the command.
 *
 * @see CommandPackage
 * @see CommandProperties
 * @see ExecutionErrorHandler
 */
open class CommandPackage<out T>(
    /**
     * The executable action for the command.
     */
    val executable: ExecutableAction<T>,
    /**
     * The properties of the command
     */
    val properties: CommandProperties,
    /**
     * The error handler for the command.
     */
    val errorHandler: ExecutionErrorHandler = DefaultErrorHandler()
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
