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
package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.ICommand
import java.lang.reflect.Method

/**
 * An interface that defines the methods a command parser needs.
 */
interface ICommandParser {

    /**
     * Parse [CommandAnn] annotations in a class and returns a list of commands created from those annotations.
     *
     * @param[instance] The instance object for which its class is to be parsed.
     * @return[List] The list of commands created after parsing.
     */
    fun parseAnnotations(instance: Any): List<ICommand<*>>

    /**
     * Creates a command by parsing a single [CommandAnn] annotation, with its execution method set as the method
     * targeted by the annotation.
     *
     * @param[instance] The instance object for which its class is to be parsed.
     * @param[method] The method to invoke for command execution.
     * @param[annotation] The annotation to parse.
     * @return[ICommand] A newly created command with properties taken from the annotation.
     */
    fun createCommand(instance: Any, method: Method, annotation: CommandAnn): ICommand<Any?>

}