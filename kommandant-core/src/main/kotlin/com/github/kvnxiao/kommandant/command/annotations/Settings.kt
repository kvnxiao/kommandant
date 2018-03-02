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
package com.github.kvnxiao.kommandant.command.annotations

import com.github.kvnxiao.kommandant.command.CommandDefaults

/**
 * Runtime annotation for methods to specify the command settings of an annotated command.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Settings(
    /**
     * The setting for whether the annotated command should be executed along with its sub-commmands
     * in a fire-and-forget manner. Only applies when one of the sub-commands are called.
     */
    val execWithSubCommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
    /**
     * The setting for whether the annotated command is enabled or disabled.
     */
    val isDisabled: Boolean = CommandDefaults.IS_DISABLED
)
