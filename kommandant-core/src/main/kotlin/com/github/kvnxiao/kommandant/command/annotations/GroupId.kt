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
package com.github.kvnxiao.kommandant.command.annotations

/**
 * Runtime annotation for classes to specify a group parent id for all annotated commands declared in the class.
 *
 * For example, an annotated command with an id of "command" declared in a class annotated with a command group annotation
 * value of "group", will have an effective id of "group.command" once parsed by the annotation parser.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class GroupId(
    /**
     * The group id / name for the annotated commands declared in this class.
     */
    val groupName: String
)