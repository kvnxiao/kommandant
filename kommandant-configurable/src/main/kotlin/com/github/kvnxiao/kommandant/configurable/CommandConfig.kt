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
package com.github.kvnxiao.kommandant.configurable

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kvnxiao.kommandant.Kommandant.Companion.LOGGER
import com.github.kvnxiao.kommandant.command.CommandProperties
import com.github.kvnxiao.kommandant.command.ICommand
import java.io.IOException
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths

/**
 * An object class containing default constants and methods for getting, loading, and saving command configs.
 */
object CommandConfig {

    /**
     * The default folder location where all command configuration files will be found. Defaults to "kommandant"
     */
    @JvmField
    var CONFIG_FOLDER = "kommandant"

    /**
     * Default Jackson object mapper instance for serializing and deserializing JSON
     */
    @JvmField
    val OBJECT_MAPPER: ObjectMapper = ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .registerModule(AfterburnerModule())
            .registerModule(KotlinModule())

    /**
     * Default method to get configuration files for a command and all of its subcommands.
     */
    @JvmStatic
    fun getConfigs(command: ICommand<*>, currentPath: String, configMap: MutableMap<String, ByteArray?> = mutableMapOf()): Map<String, ByteArray?> {
        val path = Paths.get("$currentPath/${command.props.uniqueName}.json")

        // Read json file as byte array
        try {
            val file = Files.readAllBytes(path)
            configMap.put("$currentPath/${command.props.uniqueName}", file)
        } catch (e: NoSuchFileException) {
            configMap.put("$currentPath/${command.props.uniqueName}", null)
        }

        if (command.hasSubcommands()) {
            command.subCommands.forEach {
                getConfigs(it, "$currentPath/${command.props.uniqueName}", configMap)
            }
        }
        return configMap.toMap()
    }

    /**
     * Default method to load configuration files for a command and all of its subcommands.
     */
    @JvmStatic
    fun loadConfigs(command: ICommand<*>, currentPath: String, configMap: Map<String, ByteArray?>) {
        val path = "$currentPath/${command.props.uniqueName}"
        val configByteArr = configMap[path]

        if (configByteArr !== null) {
            // Read the byte array and set new properties for command
            val loadedProps: CommandProperties = OBJECT_MAPPER.readValue(configByteArr)
            command.props = loadedProps
        }

        if (command.hasSubcommands()) {
            command.subCommands.forEach {
                loadConfigs(it, "$currentPath/${command.props.uniqueName}", configMap)
            }
        }
    }

    /**
     * Default method to write configuration file sfor a command and all of its subcommands.
     */
    @JvmStatic
    fun writeConfigs(command: ICommand<*>, currentPath: String) {
        val path = Paths.get("$currentPath/${command.props.uniqueName}.json")
        val configJson = OBJECT_MAPPER.writeValueAsBytes(command.props)

        // Create file and directories if they do not exist
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path.parent)
                Files.createFile(path)
                Files.write(path, configJson)
            }
        } catch (e: IOException) {
            LOGGER.error("An IO error occurred in writing configuration files for $path")
        }

        if (command.hasSubcommands()) {
            command.subCommands.forEach {
                writeConfigs(it, "$currentPath/${command.props.uniqueName}")
            }
        }
    }

}
