package com.github.kvnxiao.kommandant.command

import java.lang.reflect.InvocationTargetException

/**
 * Created by kvnxiao on 3/3/17.
 */
@FunctionalInterface
interface ICommandExecutable<out T> {

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun execute(context: CommandContext, vararg opt: Any): T

}