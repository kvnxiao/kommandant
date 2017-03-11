package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.configurable.CommandConfig
import com.github.kvnxiao.kommandant.impl.CommandBank
import com.github.kvnxiao.kommandant.impl.CommandExecutor
import com.github.kvnxiao.kommandant.impl.CommandParser

/**
 * Created by kxiao on 3/8/17.
 */
open class KommandantConfigurable(var enableConfigReadWrite: Boolean = false,
                                  cmdBank: ICommandBank = CommandBank(),
                                  cmdExecutor: ICommandExecutor = CommandExecutor(),
                                  cmdParser: ICommandParser = CommandParser()) : Kommandant(cmdBank, cmdExecutor, cmdParser) {

    open fun enableCommand(command: ICommand<*>?) {
        if (command !== null) command.props.isDisabled = false
    }

    open fun disableCommand(command: ICommand<*>?) {
        if (command !== null) command.props.isDisabled = true
    }

    override fun addCommand(command: ICommand<*>): Boolean {
        if (enableConfigReadWrite) {
            val configMap = CommandConfig.getConfigs(command, CommandConfig.CONFIG_FOLDER)

            // Not all configs were loaded properly, will create missing configs with default values
            if (configMap.containsValue(null)) {
                CommandConfig.writeConfigs(command, CommandConfig.CONFIG_FOLDER)
            }

            CommandConfig.loadConfigs(command, CommandConfig.CONFIG_FOLDER, configMap)
        }

        return super.addCommand(command)
    }

    open fun loadConfig(command: ICommand<*>): Boolean {
        val configMap = CommandConfig.getConfigs(command, CommandConfig.CONFIG_FOLDER)

        if (!configMap.containsValue(null)) {
            CommandConfig.loadConfigs(command, CommandConfig.CONFIG_FOLDER, configMap)
            return true
        }
        return false
    }

    open fun saveConfig(command: ICommand<*>) = CommandConfig.writeConfigs(command, CommandConfig.CONFIG_FOLDER)

}