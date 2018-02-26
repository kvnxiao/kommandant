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

import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.CommandProperties
import com.github.kvnxiao.kommandant.command.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TestCommandClasses {

    @Test
    fun `test object declarations`() {
        CommandDefaults
    }

    @Test
    fun `test command properties equals`() {
        val propertiesA = CommandProperties("a", setOf("b"))
        val propertiesB = CommandProperties("a", setOf("b"))
        val propertiesC = CommandProperties("c", setOf("c"))
        assertEquals(propertiesA, propertiesA)
        assertEquals(propertiesA, propertiesB)
        assertEquals(propertiesA.hashCode(), propertiesB.hashCode())
        assertNotEquals(propertiesA, 0)
        assertNotEquals(propertiesA, propertiesC)
        assertNotEquals(propertiesB, propertiesC)
        assertEquals("CommandProperties(id=a, aliases=[b], prefix=, parentId=, description=No description available., usage=No usage information available., execWithSubCommands=false, isDisabled=false)", propertiesA.toString())
    }

    @Test
    fun `test command properties destructuring`() {
        val properties = CommandProperties("a", setOf("b"))
        val (a, b, c, d, e, f, g, h) = properties
        assertEquals("a", a)
        assertEquals(setOf("b"), b)
        assertEquals(CommandDefaults.NO_PREFIX, c)
        assertEquals(CommandDefaults.PARENT_ID, d)
        assertEquals(CommandDefaults.NO_DESCRIPTION, e)
        assertEquals(CommandDefaults.NO_USAGE, f)
        assertEquals(CommandDefaults.EXEC_WITH_SUBCOMMANDS, g)
        assertEquals(CommandDefaults.IS_DISABLED, h)
    }

    @Test
    fun `test command properties copy`() {
        val properties = CommandProperties("a", setOf("b"))
        val propertiesCopy = properties.copy()
        assertEquals(properties, propertiesCopy)
    }

    @Test(expected = IllegalStateException::class)
    fun `test command properties empty`() {
        CommandProperties("a", setOf())
    }

    @Test
    fun `test command context equals`() {
        val contextA = Context("abc", null, CommandProperties("a"))
        val contextB = Context("abc", null, CommandProperties("a"))
        val contextC = Context("abc", null, CommandProperties("b"))
        val contextD = Context("ab", null, CommandProperties("b"))
        val contextE = Context("ab", "a", CommandProperties("b"))
        assertEquals(contextA, contextA)
        assertEquals(contextA, contextB)
        assertEquals(contextA.hashCode(), contextB.hashCode())
        assertNotEquals(contextA, contextC)
        assertNotEquals(contextB, contextC)
        assertNotEquals(contextD, contextC)
        assertNotEquals(contextD, contextA)
        assertNotEquals(contextD, contextE)
        assertNotEquals(contextA, 0)
        assertEquals("Context(alias='abc', args=null, properties=CommandProperties(id=a, aliases=[a], prefix=, parentId=, description=No description available., usage=No usage information available., execWithSubCommands=false, isDisabled=false))", contextA.toString())
    }
}
