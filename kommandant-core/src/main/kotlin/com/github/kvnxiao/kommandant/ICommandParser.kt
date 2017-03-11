package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.ICommand
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * An interface that defines the methods a command parser needs.
 */
interface ICommandParser {

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun parseAnnotations(instance: Any): List<ICommand<*>>

    fun createCommand(instance: Any, method: Method, annotation: CommandAnn): ICommand<Any?>

}