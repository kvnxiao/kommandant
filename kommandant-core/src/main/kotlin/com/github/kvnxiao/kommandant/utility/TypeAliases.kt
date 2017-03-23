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
package com.github.kvnxiao.kommandant.utility

import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.impl.CommandParser
import java.util.Stack

/**
 * A utility file containing type aliases used in the code for better readability.
 */

/**
 * Represents a mutable map of [String] to [ICommand].
 */
typealias CommandMap = MutableMap<String, ICommand<*>>
/**
 * Represents an immutable map of [String] to [ICommand].
 */
typealias ImmutableCommandMap = Map<String, ICommand<*>>
/**
 * Represents a mutable map of [ICommand] to [String], used in subcommmand parsing. The opposite of [CommandMap].
 *
 * @see[CommandParser].
 */
typealias CommandStringMap = MutableMap<ICommand<*>, String>
/**
 * Represents a mutable map of [String] to [CommandMap].
 */
typealias PrefixMap = MutableMap<String, CommandMap>
/**
 * Represents a mutable set of [ICommand].
 */
typealias CommandSet = MutableSet<ICommand<*>>
/**
 * Represents a mutable stack of [ICommand].
 */
typealias CommandStack = Stack<ICommand<*>>