package com.github.kvnxiao.kommandant.utility

import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.impl.CommandParser
import java.util.*

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