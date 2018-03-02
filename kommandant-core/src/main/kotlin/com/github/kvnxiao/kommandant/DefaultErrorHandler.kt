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
package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandPackage
import com.github.kvnxiao.kommandant.command.ExecutionErrorHandler
import mu.KotlinLogging

private val LOGGER = KotlinLogging.logger { }

/**
 * The default error handler for commands, which simply logs that an error has occurred for the command
 * to the underlying SLF4J logger.
 *
 * @see ExecutionErrorHandler
 */
class DefaultErrorHandler : ExecutionErrorHandler {
    /**
     * Method called when the execution of a command encounters an exception, which simply logs an error message to the
     * underlying SLF4J logger.
     */
    override fun onError(command: CommandPackage<*>, ex: Exception) {
        LOGGER.error(ex) { "An error has occurred for $command." }
    }
}
