package com.github.kvnxiao.kommandant.command

/**
 * Created by kvnxiao on 3/3/17.
 */
data class CommandResult<out T>(val success: Boolean, val result: T? = null)