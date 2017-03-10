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
 * Created by kxiao on 3/7/17.
 */
object CommandConfig {

    @JvmField
    var CONFIG_FOLDER = "kommandant"

    @JvmField
    val OBJECT_MAPPER: ObjectMapper = ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .registerModule(AfterburnerModule())
            .registerModule(KotlinModule())

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
