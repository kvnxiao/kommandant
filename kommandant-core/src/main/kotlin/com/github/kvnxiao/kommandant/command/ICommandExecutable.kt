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

import java.lang.reflect.InvocationTargetException

/**
 * A functional interface that represents the executing method or function for a command.
 */
@FunctionalInterface
interface ICommandExecutable<out T> {

    /**
     * The executing method implemented by [commands][ICommand].
     *
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any], which can be useful in specific implementations when a command requires
     * more than just context for execution.
     * @throws[InvocationTargetException] When the method invocation failed.
     * @throws[IllegalAccessException] When the method could not be accessed through reflection.
     * @return[T] Returns the result of the execution.
     */
    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun execute(context: CommandContext, vararg opt: Any?): T?

}