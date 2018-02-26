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
package com.github.kvnxiao.kommandant

import arrow.core.getOrElse
import arrow.core.getOrHandle
import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.Context
import com.github.kvnxiao.kommandant.command.annotations.Command
import com.github.kvnxiao.kommandant.command.annotations.CommandGroup
import com.github.kvnxiao.kommandant.command.annotations.CommandInfo
import com.github.kvnxiao.kommandant.command.annotations.CommandSettings
import com.github.kvnxiao.kommandant.command.annotations.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class TestKommandantAnnotations {

    @Test
    fun `test parsing and executing annotated commands`() {
        val kommandant: CommandManager = Kommandant()
        assertTrue(kommandant.addAnnotatedCommands(
            AnnotatedCommands()
        ))

        // Test evaluating root-level commands
        assertEquals(1, kommandant.process<Int>("--int").getOrElse { fail() })
        assertEquals(1, kommandant.process<Int>("--i").getOrElse { fail() })
        assertEquals("string", kommandant.process<String>("--string").getOrElse { fail() })
        assertEquals("string", kommandant.process<String>("--s").getOrElse { fail() })
        // Test evaluating sub-commands
        assertEquals(2, kommandant.process<Int>("--int child").getOrElse { fail() })
        assertEquals(2, kommandant.process<Int>("--int c").getOrElse { fail() })
        assertEquals(2, kommandant.process<Int>("--i child").getOrElse { fail() })
        assertEquals(2, kommandant.process<Int>("--i c").getOrElse { fail() })
        // Test evaluating next level of sub-commands
        assertEquals(3, kommandant.process<Int>("--int child nextChild").getOrElse { fail() })
        assertEquals(3, kommandant.process<Int>("--int c nextChild").getOrElse { fail() })
        assertEquals(3, kommandant.process<Int>("--i child nextChild").getOrElse { fail() })
        assertEquals(3, kommandant.process<Int>("--i c nextChild").getOrElse { fail() })
        assertEquals(3, kommandant.process<Int>("--int child nc").getOrElse { fail() })
        assertEquals(3, kommandant.process<Int>("--int c nc").getOrElse { fail() })
        assertEquals(3, kommandant.process<Int>("--i child nc").getOrElse { fail() })
        assertEquals(3, kommandant.process<Int>("--i c nc").getOrElse { fail() })
    }

    @Test
    fun `test adding annotated commands with duplicate ids`() {
        val kommandant: CommandManager = Kommandant()
        assertFalse(kommandant.addAnnotatedCommands(
            AnnotatedCommands(), AnnotatedCommandsDuplicateId()
        ))
    }

    @Test
    fun `test adding annotated commands with duplicate aliases`() {
        val kommandant: CommandManager = Kommandant()
        assertFalse(kommandant.addAnnotatedCommands(
            AnnotatedCommands(), AnnotatedCommandsDuplicateAlias()
        ))
    }

    @Test
    fun `test adding annotated commands conflicting with registry`() {
        val kommandant: CommandManager = Kommandant()
        assertTrue(kommandant.addAnnotatedCommands(
            AnnotatedCommands()
        ))
        assertFalse(kommandant.addAnnotatedCommands(
            AnnotatedCommands()
        ))
        assertFalse(kommandant.addAnnotatedCommands(
            AnnotatedCommandsDuplicateId()
        ))
        assertFalse(kommandant.addAnnotatedCommands(
            AnnotatedCommandsDuplicateAlias()
        ))
    }

    @Test
    fun `test command properties and context`() {
        val kommandant: CommandManager = Kommandant()
        assertTrue(kommandant.addAnnotatedCommands(CustomAnnotatedCommand()))
        assertTrue(kommandant.process<Boolean>("-test value").getOrElse { false })
    }

    @Test
    fun `test invalid object to parse`() {
        val kommandant: CommandManager = Kommandant()
        assertFalse(kommandant.addAnnotatedCommands(Object()))
    }
}

@CommandGroup("test")
@Prefix("--")
class AnnotatedCommands {
    @Command(
        id = "returns_int",
        aliases = ["int", "i"]
    )
    fun commandReturnsInt(context: Context, opt: Array<Any>?): Int {
        return 1
    }

    @Command(
        id = "child",
        aliases = ["child", "c"],
        parentId = "returns_int"
    )
    fun commandReturnsIntChild(context: Context, opt: Array<Any>?): Int {
        return 2
    }

    @Command(
        id = "nextChild",
        aliases = ["nextChild", "nc"],
        parentId = "child"
    )
    fun commandReturnsIntGrandChild(context: Context, opt: Array<Any>?): Int {
        return 3
    }

    @Command(
        id = "returns_string",
        aliases = ["string", "s"]
    )
    fun commandReturnsString(context: Context, opt: Array<Any>?): String {
        return "string"
    }
}

@CommandGroup("test")
@Prefix("--")
class AnnotatedCommandsDuplicateId {
    @Command(
        id = "returns_int",
        aliases = ["int", "i"]
    )
    fun commandReturnsInt(context: Context, opt: Array<Any>?): Int {
        return 1
    }
}

@CommandGroup("test")
@Prefix("--")
class AnnotatedCommandsDuplicateAlias {
    @Command(
        id = "returns_int_dupe",
        aliases = ["int", "i"]
    )
    fun commandReturnsInt(context: Context, opt: Array<Any>?): Int {
        return 1
    }
}

@CommandGroup("test")
class CustomAnnotatedCommand {
    @Command(
        id = "returns_true",
        aliases = ["test"]
    )
    @Prefix("-")
    @CommandInfo(description = "some description goes here.", usage = "some usage information goes here.")
    @CommandSettings(execWithSubCommands = true, isDisabled = true)
    fun commandReturnsTrue(context: Context, opt: Array<Any>?): Boolean {
        // expect test case to pass in "value" for args
        assertEquals("-test", context.alias)
        assertNotNull(context.args)
        assertEquals("value", context.args)
        assertEquals("test.returns_true", context.properties.id)
        assertEquals(1, context.properties.aliases.size)
        assertTrue(context.properties.aliases.contains("test"))
        assertEquals(CommandDefaults.PARENT_ID, context.properties.parentId)
        assertEquals("-", context.properties.prefix)
        assertEquals("some description goes here.", context.properties.description)
        assertEquals("some usage information goes here.", context.properties.usage)
        assertTrue(context.properties.execWithSubCommands)
        assertTrue(context.properties.isDisabled)
        assertNull(opt)
        return true
    }
}