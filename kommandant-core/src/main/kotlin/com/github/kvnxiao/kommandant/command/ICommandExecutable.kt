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
    fun execute(context: CommandContext, vararg opt: Any?): T

}