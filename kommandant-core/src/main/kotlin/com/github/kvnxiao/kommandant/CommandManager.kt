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
package com.github.kvnxiao.kommandant

import arrow.core.Either
import com.github.kvnxiao.kommandant.command.Context
import com.github.kvnxiao.kommandant.command.CommandPackage
import java.util.concurrent.Future

interface CommandManager {
    fun <T> processAsync(input: String, opt: Array<Any>? = null): Future<Either<Exception, T>>
    fun <T> process(input: String, opt: Array<Any>? = null): Either<Exception, T> = processAsync<T>(input, opt).get()
    fun <T> execute(command: CommandPackage<*>, context: Context, opt: Array<Any>? = null): Future<Either<Exception, T>>
    fun addAnnotatedCommands(vararg instances: Any): Boolean
    fun addCommand(command: CommandPackage<*>): Boolean
    fun addSubCommand(subCommand: CommandPackage<*>, parentId: String): Boolean
}
