/*
 *   Copyright (C) 2017-2018 Ze Hao Xiao
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing commandSettings and
 *   limitations under the License.
 */
package com.github.kvnxiao.kommandant.registry

import com.github.kvnxiao.kommandant.command.Context
import com.github.kvnxiao.kommandant.command.CommandExecutable
import com.github.kvnxiao.kommandant.command.CommandPackage
import com.github.kvnxiao.kommandant.command.CommandProperties
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TestCommandRegistry {

    @Test
    fun `test root-command in registry`() {
        val registry: CommandRegistry = CommandRegistryImpl()
        val command = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test root-command in registry",
                setOf("first", "second", "third"),
                "-"
            )
        )
        assertTrue(registry.addCommand(command))
        assertEquals(command, registry.getCommandById("test root-command in registry"))
        assertEquals(command, registry.getCommandByAlias("-first"))
        assertEquals(command, registry.getCommandByAlias("-second"))
        assertEquals(command, registry.getCommandByAlias("-third"))
    }

    @Test
    fun `test sub-commands in registry`() {
        val registry: CommandRegistry = CommandRegistryImpl()
        val command = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test sub-commands in registry",
                setOf("parent", "p"),
                "-"
            )
        )
        val subCommandA = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test sub-commands in registry.subCommandA",
                setOf("subA", "a")
            )
        )
        val subCommandB = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test sub-commands in registry.subCommandB",
                setOf("subB", "b")
            )
        )
        val subCommandC = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test sub-commands in registry.subCommandC",
                setOf("subC", "c")
            )
        )
        val subCommandDofC = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test sub-commands in registry.subCommandC.subCommandD",
                setOf("subD", "d")
            )
        )
        val subCommandEofC = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test sub-commands in registry.subCommandC.subCommandE",
                setOf("subE", "e")
            )
        )
        assertTrue(registry.addCommand(command))
        assertTrue(registry.addSubCommand(subCommandA, command.properties.id))
        assertTrue(registry.addSubCommand(subCommandB, command.properties.id))
        assertTrue(registry.addSubCommand(subCommandC, command.properties.id))
        assertTrue(registry.addSubCommand(subCommandDofC, subCommandC.properties.id))
        assertTrue(registry.addSubCommand(subCommandEofC, subCommandC.properties.id))

        // Check on root-level command
        assertEquals(command, registry.getCommandById(command.properties.id))
        assertEquals(command, registry.getCommandByAlias("-parent"))
        assertEquals(command, registry.getCommandByAlias("-p"))

        // Check on whether command has sub-commands or not
        assertTrue(registry.hasSubCommands(command.properties.id))
        assertNotNull(registry.getSubCommandRegistry(command.properties.id))
        assertFalse(registry.hasSubCommands(subCommandA.properties.id))
        assertNull(registry.getSubCommandRegistry(subCommandA.properties.id))
        assertFalse(registry.hasSubCommands(subCommandB.properties.id))
        assertNull(registry.getSubCommandRegistry(subCommandB.properties.id))
        assertTrue(registry.hasSubCommands(subCommandC.properties.id))
        assertNotNull(registry.getSubCommandRegistry(subCommandC.properties.id))
        assertFalse(registry.hasSubCommands(subCommandDofC.properties.id))
        assertNull(registry.getSubCommandRegistry(subCommandDofC.properties.id))
        assertFalse(registry.hasSubCommands(subCommandEofC.properties.id))
        assertNull(registry.getSubCommandRegistry(subCommandEofC.properties.id))

        // Check sizes of sub-command registries
        assertEquals(3, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandIds().size)
        assertEquals(6, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandAliases().size)
        assertEquals(2, registry.getSubCommandRegistry(subCommandC.properties.id)!!.getAllSubCommandIds().size)
        assertEquals(4, registry.getSubCommandRegistry(subCommandC.properties.id)!!.getAllSubCommandAliases().size)

        // Check getting sub-commands of root-level command by aliases
        assertEquals(subCommandA, registry.getSubCommandByAlias("subA", command.properties.id))
        assertEquals(subCommandA, registry.getSubCommandByAlias("a", command.properties.id))
        assertEquals(subCommandB, registry.getSubCommandByAlias("subB", command.properties.id))
        assertEquals(subCommandB, registry.getSubCommandByAlias("b", command.properties.id))
        assertEquals(subCommandC, registry.getSubCommandByAlias("subC", command.properties.id))
        assertEquals(subCommandC, registry.getSubCommandByAlias("c", command.properties.id))
        // Check getting sub-commands of sub-command by aliases
        assertEquals(subCommandDofC, registry.getSubCommandByAlias("subD", subCommandC.properties.id))
        assertEquals(subCommandDofC, registry.getSubCommandByAlias("d", subCommandC.properties.id))
        assertEquals(subCommandEofC, registry.getSubCommandByAlias("subE", subCommandC.properties.id))
        assertEquals(subCommandEofC, registry.getSubCommandByAlias("e", subCommandC.properties.id))
    }

    @Test
    fun `test empty prefix on root-level command`() {
        val registry: CommandRegistry = CommandRegistryImpl()
        val command = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test empty prefix",
                setOf("first", "second", "third")
            )
        )
        assertTrue(registry.addCommand(command))
        assertEquals(command, registry.getCommandById("test empty prefix"))
        assertEquals(command, registry.getCommandByAlias("first"))
        assertEquals(command, registry.getCommandByAlias("second"))
        assertEquals(command, registry.getCommandByAlias("third"))
    }

    @Test
    fun `test unique id clash on root-level command`() {
        val registry: CommandRegistry = CommandRegistryImpl()
        val command = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test unique id clash",
                setOf("alpha")
            )
        )
        assertTrue(registry.addCommand(command))
        assertEquals(command, registry.getCommandById("test unique id clash"))
        assertEquals(command, registry.getCommandByAlias("alpha"))

        // When inserting a new command with the same unique id, expect a clash
        val newCommand = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test unique id clash",
                setOf("beta")
            )
        )
        assertFalse(registry.addCommand(newCommand))

        // When inserting the same command with the same unique id, expect a clash
        assertFalse(registry.addCommand(command))
    }

    @Test
    fun `test alias clash on root-level command`() {
        val registry: CommandRegistry = CommandRegistryImpl()
        val command = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test alias clash",
                setOf("alpha", "beta", "charlie"),
                "-"
            )
        )
        assertTrue(registry.addCommand(command))
        assertEquals(command, registry.getCommandById(command.properties.id))
        assertEquals(command, registry.getCommandByAlias("-alpha"))
        assertEquals(command, registry.getCommandByAlias("-beta"))
        assertEquals(command, registry.getCommandByAlias("-charlie"))

        // When inserting a new command with the same alias, but different unique id, expect a clash
        val newCommand = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test alias clash new",
                setOf("delta", "echo", "charlie"),
                "-"
            )
        )
        assertFalse(registry.addCommand(newCommand))
    }

    @Test
    fun `test no alias clash with different prefixes on root-level commands`() {
        val registry: CommandRegistry = CommandRegistryImpl()
        val command = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test no alias clash",
                setOf("alpha", "beta", "charlie"),
                "-"
            )
        )
        assertTrue(registry.addCommand(command))
        assertEquals(command, registry.getCommandById(command.properties.id))
        assertEquals(command, registry.getCommandByAlias("-alpha"))
        assertEquals(command, registry.getCommandByAlias("-beta"))
        assertEquals(command, registry.getCommandByAlias("-charlie"))

        // When inserting a new command with the same alias, but different unique id and prefixes, expect no errors
        val newCommand = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test no alias clash new",
                setOf("alpha", "beta", "charlie"),
                "/"
            )
        )
        assertTrue(registry.addCommand(newCommand))
        assertEquals(newCommand, registry.getCommandById(newCommand.properties.id))
        assertEquals(newCommand, registry.getCommandByAlias("/alpha"))
        assertEquals(newCommand, registry.getCommandByAlias("/beta"))
        assertEquals(newCommand, registry.getCommandByAlias("/charlie"))
    }

    @Test
    fun `test unique id clash on sub-command level`() {
        val registry: CommandRegistry = CommandRegistryImpl()
        val command = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test unique id clash on sub-command level",
                setOf("parent", "p"),
                "-"
            )
        )
        assertTrue(registry.addCommand(command))
        assertEquals(command, registry.getCommandById(command.properties.id))
        assertEquals(command, registry.getCommandByAlias("-parent"))
        assertEquals(command, registry.getCommandByAlias("-p"))

        val subCommandA = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test unique id clash on sub-command level.child",
                setOf("child", "c")
            )
        )
        assertTrue(registry.addSubCommand(subCommandA, command.properties.id))
        assertNotNull(registry.getSubCommandRegistry(command.properties.id))
        assertTrue(registry.hasSubCommands(command.properties.id))
        assertEquals(1, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandIds().size)
        assertEquals(2, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandAliases().size)
        assertEquals(subCommandA, registry.getSubCommandByAlias("child", command.properties.id))
        assertEquals(subCommandA, registry.getSubCommandByAlias("c", command.properties.id))

        val subCommandB = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test unique id clash on sub-command level.child",
                setOf("child2", "c2")
            )
        )
        assertFalse(registry.addSubCommand(subCommandB, command.properties.id))
        assertEquals(1, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandIds().size)
        assertEquals(2, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandAliases().size)
    }

    @Test
    fun `test alias clash on sub-command level`() {
        val registry: CommandRegistry = CommandRegistryImpl()
        val command = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test alias clash on sub-command level",
                setOf("parent", "p"),
                "-"
            )
        )
        assertTrue(registry.addCommand(command))
        assertEquals(command, registry.getCommandById(command.properties.id))
        assertEquals(command, registry.getCommandByAlias("-parent"))
        assertEquals(command, registry.getCommandByAlias("-p"))

        val subCommandA = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test alias clash on sub-command level.child",
                setOf("child", "c")
            )
        )
        assertTrue(registry.addSubCommand(subCommandA, command.properties.id))
        assertNotNull(registry.getSubCommandRegistry(command.properties.id))
        assertTrue(registry.hasSubCommands(command.properties.id))
        assertEquals(1, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandIds().size)
        assertEquals(2, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandAliases().size)
        assertEquals(subCommandA, registry.getSubCommandByAlias("child", command.properties.id))
        assertEquals(subCommandA, registry.getSubCommandByAlias("c", command.properties.id))

        val subCommandB = CommandPackage(
            emptyCommandExecutable(),
            CommandProperties(
                "test alias clash on sub-command level.child2",
                setOf("child2", "c")
            )
        )
        assertFalse(registry.addSubCommand(subCommandB, command.properties.id))
        assertEquals(1, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandIds().size)
        assertEquals(2, registry.getSubCommandRegistry(command.properties.id)!!.getAllSubCommandAliases().size)
    }

    private fun emptyCommandExecutable(): CommandExecutable<Unit> = object : CommandExecutable<Unit> {
        override fun execute(context: Context, opt: Array<Any>?) {}
    }
}
