package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandResult
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

/**
 * Created by kxiao on 3/9/17.
 */
open class KommandantAsync : Kommandant() {

    open fun <T> processAsync(input: String, vararg opt: Any?): Deferred<CommandResult<T>> = async(CommonPool) { process<T>(input, *opt) }

}