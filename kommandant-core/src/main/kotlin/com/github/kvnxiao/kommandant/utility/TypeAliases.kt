package com.github.kvnxiao.kommandant.utility

import com.github.kvnxiao.kommandant.command.ICommand
import java.util.*

/**
 * Created by kvnxiao on 3/3/17.
 */
typealias CommandMap = MutableMap<String, ICommand<*>>
typealias ImmutableCommandMap = Map<String, ICommand<*>>
typealias CommandStringMap = MutableMap<ICommand<*>, String>
typealias PrefixMap = MutableMap<String, CommandMap>
typealias CommandSet = MutableSet<ICommand<*>>
typealias CommandStack = Stack<ICommand<*>>