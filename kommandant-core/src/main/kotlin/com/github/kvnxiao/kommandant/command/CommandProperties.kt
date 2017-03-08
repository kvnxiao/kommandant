package com.github.kvnxiao.kommandant.command

/**
 * Created on:   2017-03-04
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
data class CommandProperties(
        var prefix: String = CommandDefaults.PREFIX,
        val uniqueName: String,
        var description: String = CommandDefaults.NO_DESCRIPTION,
        var usage: String = CommandDefaults.NO_USAGE,
        var execWithSubcommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
        var aliases: List<String> = listOf(uniqueName)) {

    override fun toString(): String {
        return this.uniqueName
    }

}