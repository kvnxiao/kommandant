package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.ICommand
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
interface ICommandParser {

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun parseAnnotations(instance: Any, cmdBank: ICommandBank)

    fun createCommand(instance: Any, method: Method, annotation: CommandAnn): ICommand<Any?>

}