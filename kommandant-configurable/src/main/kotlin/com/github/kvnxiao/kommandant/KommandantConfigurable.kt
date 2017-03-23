/*
 * Copyright 2017 Ze Hao Xiao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandProperties
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.configurable.CommandConfig
import com.github.kvnxiao.kommandant.impl.CommandBank
import com.github.kvnxiao.kommandant.impl.CommandExecutor
import com.github.kvnxiao.kommandant.impl.CommandParser

/**
 * An extension of the default Kommandant aggregation which allows one to completely disable or enable a registered command,
 * as well as including a boolean setting to enable or disable command properties being written to and read from JSON files.
 *
 * @property[enableConfigReadWrite] Whether commands should have their properties loaded from and written to JSON.
 * @property[cmdBank] The command registry, an implementation of [ICommandBank].
 * @property[cmdExecutor] The command executor, an implementation of [ICommandExecutor].
 * @property[cmdParser] The command parser, an implementation of [ICommandParser].
 * @constructor Default constructor which uses default implementations of the registry, parser, and executor.
 */
open class KommandantConfigurable(var enableConfigReadWrite: Boolean = false,
                                  cmdBank: ICommandBank = CommandBank(),
                                  cmdExecutor: ICommandExecutor = CommandExecutor(),
                                  cmdParser: ICommandParser = CommandParser()) : Kommandant(cmdBank, cmdExecutor, cmdParser) {

    /**
     * Enables a command for execution.
     *
     * @param[command] The command to enable.
     */
    open fun enableCommand(command: ICommand<*>?) {
        if (command !== null) {
            command.props = CommandProperties(command.props.prefix, command.props.uniqueName, command.props.description, command.props.usage, command.props.execWithSubcommands, false, command.props.aliases)
        }
    }

    /**
     * Disables a command to prevent execution.
     *
     * @param[command] The command to disable.
     */
    open fun disableCommand(command: ICommand<*>?) {
        if (command !== null) {
            command.props = CommandProperties(command.props.prefix, command.props.uniqueName, command.props.description, command.props.usage, command.props.execWithSubcommands, true, command.props.aliases)
        }
    }

    /**
     * Overrided addCommand method which adds a command to the registry and additionally checks for [enableConfigReadWrite]
     * value in attempt to load command properties from JSON if true. If the JSON does not exist, it will be created with
     * the command's default properties.
     *
     * @param[command] The command to add to the registry.
     * @return[Boolean] Whether adding the command was successful or not.
     * @see[loadConfig]
     * @see[saveConfig]
     */
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

    /**
     * Loads a command's JSON configuration for customized command properties.
     *
     * @param[command] The command to load its properties from JSON.
     * @return[Boolean] Whether loading the custom properties was successfully completed.
     * @see[CommandConfig.loadConfigs]
     * @see[CommandConfig.getConfigs]
     */
    open fun loadConfig(command: ICommand<*>): Boolean {
        val configMap = CommandConfig.getConfigs(command, CommandConfig.CONFIG_FOLDER)

        if (!configMap.containsValue(null)) {
            CommandConfig.loadConfigs(command, CommandConfig.CONFIG_FOLDER, configMap)
            return true
        }
        return false
    }

    /**
     * Saves the command's current properties to JSON.
     *
     * @param[command] The command to save its properties to JSON.
     * @see[CommandConfig.writeConfigs]
     */
    open fun saveConfig(command: ICommand<*>) = CommandConfig.writeConfigs(command, CommandConfig.CONFIG_FOLDER)

}