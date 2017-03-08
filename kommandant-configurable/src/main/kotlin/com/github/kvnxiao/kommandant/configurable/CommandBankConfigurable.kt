package com.github.kvnxiao.kommandant.configurable

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.configurable.CommandConfig.Companion.getConfigs
import com.github.kvnxiao.kommandant.configurable.CommandConfig.Companion.loadConfigs
import com.github.kvnxiao.kommandant.configurable.CommandConfig.Companion.writeConfigs
import com.github.kvnxiao.kommandant.impl.CommandBank

/**
 * Created by kxiao on 3/6/17.
 */
open class CommandBankConfigurable : CommandBank() {

    companion object {
        @JvmField
        val OBJECT_MAPPER: ObjectMapper = ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(JsonParser.Feature.ALLOW_COMMENTS)
                .registerModule(AfterburnerModule())
                .registerModule(KotlinModule())
        const val CONFIG_FOLDER = "config"
    }

    override fun addCommand(command: ICommand<*>): Boolean {
        val configMap = getConfigs(command, CONFIG_FOLDER)

        // Not all configs were loaded properly, will create missing configs with default values
        if (configMap.containsValue(null)) {
            writeConfigs(command, CONFIG_FOLDER)
        }

        loadConfigs(command, CONFIG_FOLDER, configMap)

        return super.addCommand(command)
    }

}