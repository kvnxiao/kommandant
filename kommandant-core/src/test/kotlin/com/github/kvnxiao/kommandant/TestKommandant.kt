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
import com.github.kvnxiao.kommandant.command.CommandExecutable
import com.github.kvnxiao.kommandant.command.CommandPackage
import com.github.kvnxiao.kommandant.command.CommandProperties
import com.github.kvnxiao.kommandant.command.Context
import com.github.kvnxiao.kommandant.command.errors.CommandNotFoundException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class TestKommandant {

    @Test
    fun `test executing root level command`() {
        val kommandant: CommandManager = Kommandant()
        kommandant.addCommand(CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 100
        }, CommandProperties("root_level_command", setOf("root"), "-")))

        assertTrue(kommandant.process<Int>("something").isLeft())
        kommandant.process<Int>("something").getOrHandle {
            assertEquals(CommandNotFoundException::class.java, it::class.java)
        }
        val result = kommandant.process<Int>("-root")
        assertTrue(result.isRight())
        assertEquals(100, result.getOrElse { fail() })
    }

    @Test
    fun `test executing sub level command`() {
        val kommandant: CommandManager = Kommandant()

        assertTrue(kommandant.addCommand(CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 200
        }, CommandProperties("parent", setOf("parent", "p"), "-"))))
        assertEquals(200, kommandant.process<Int>("-parent").getOrElse { fail() })
        assertEquals(200, kommandant.process<Int>("-p").getOrElse { fail() })

        assertTrue(kommandant.addSubCommand(CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 400
        }, CommandProperties("parent.child", setOf("child", "c"), "-")), "parent"))
        assertEquals(400, kommandant.process<Int>("-parent child").getOrElse { fail() })
        assertEquals(400, kommandant.process<Int>("-parent c").getOrElse { fail() })
        assertEquals(400, kommandant.process<Int>("-p child").getOrElse { fail() })
        assertEquals(400, kommandant.process<Int>("-p c").getOrElse { fail() })
    }

    @Test
    fun `test exception from command`() {
        val kommandant: CommandManager = Kommandant()

        assertTrue(kommandant.addCommand(CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int {
                throw UnsupportedOperationException("UnsupportedOperationException")
            }
        }, CommandProperties("exception", setOf("e"), "-"))))
        val result = kommandant.process<Int>("-e")
        assertTrue(result.isLeft())
        result.getOrHandle {
            assertEquals(UnsupportedOperationException::class.java, it::class.java)
        }
    }

    @Test
    fun `test async command`() {
        val kommandant: CommandManager = Kommandant()
        kommandant.addCommand(CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 100
        }, CommandProperties("root_level_command", setOf("root"), "-")))
        val future = kommandant.processAsync<Int>("-root")
        val result = future.get()
        assertTrue(result.isRight())
        assertEquals(100, result.getOrElse { fail() })
    }

    @Test
    fun `test loop fold of integer-return command`() {
        val kommandant: CommandManager = Kommandant()
        kommandant.addCommand(CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 1
        }, CommandProperties("root_level_command", setOf("root"), "-")))

        assertEquals(1000, (1..1000).map {
            kommandant.process<Int>("-root").get()
        }.foldRight(0, { total, next -> total + next }))
    }

    @Test
    fun `test adding two of the same commands`() {
        val kommandant: CommandManager = Kommandant()
        val commandA = CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 1
        }, CommandProperties("same_command", setOf("same"), "-"))
        val commandB = CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 1
        }, CommandProperties("same_command", setOf("same"), "-"))
        assertEquals(commandA, commandB)
        assertEquals(commandA.hashCode(), commandB.hashCode())
        assertTrue(kommandant.addCommand(commandA))
        assertFalse(kommandant.addCommand(commandA))
        assertFalse(kommandant.addCommand(commandB))
        assertEquals(1, kommandant.process<Int>("-same").getOrElse { fail() })
    }

    @Test
    fun `test adding two clashing commands`() {
        val kommandant: CommandManager = Kommandant()
        val commandA = CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 1
        }, CommandProperties("same_command", setOf("same_alias"), "-"))
        val commandB = CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 1
        }, CommandProperties("not_same_command", setOf("same_alias"), "-"))
        assertNotEquals(commandA, commandB)
        assertTrue(kommandant.addCommand(commandA))
        assertFalse(kommandant.addCommand(commandA))
        assertFalse(kommandant.addCommand(commandB))
        assertEquals(1, kommandant.process<Int>("-same_alias").getOrElse { fail() })
    }

    @Test
    fun `test adding two different commands`() {
        val kommandant: CommandManager = Kommandant()
        val commandA = CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 1
        }, CommandProperties("same_command", setOf("same"), "-"))
        val commandB = CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 2
        }, CommandProperties("different_command", setOf("different"), "-"))
        assertNotEquals(commandA, commandB)
        assertNotEquals(commandA, kommandant)
        assertTrue(kommandant.addCommand(commandA))
        assertFalse(kommandant.addCommand(commandA))
        assertTrue(kommandant.addCommand(commandB))
        assertFalse(kommandant.addCommand(commandB))
        assertEquals(1, kommandant.process<Int>("-same").getOrElse { fail() })
        assertEquals(2, kommandant.process<Int>("-different").getOrElse { fail() })
    }

    @Test
    fun `test exec with sub-commands`() {
        val kommandant: CommandManager = Kommandant()

        assertTrue(kommandant.addCommand(CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 200
        }, CommandProperties("parent", setOf("parent", "p"), "-", execWithSubCommands = true))))
        assertEquals(200, kommandant.process<Int>("-parent").getOrElse { fail() })
        assertEquals(200, kommandant.process<Int>("-p").getOrElse { fail() })

        assertTrue(kommandant.addSubCommand(CommandPackage(object : CommandExecutable<Int> {
            override fun execute(context: Context, opt: Array<Any>?): Int = 400
        }, CommandProperties("parent.child", setOf("child", "c"), "-")), "parent"))
        assertEquals(400, kommandant.process<Int>("-parent child").getOrElse { fail() })
        assertEquals(400, kommandant.process<Int>("-parent c").getOrElse { fail() })
        assertEquals(400, kommandant.process<Int>("-p child").getOrElse { fail() })
        assertEquals(400, kommandant.process<Int>("-p c").getOrElse { fail() })
        assertEquals(200, kommandant.process<Int>("-p nothing").getOrElse { fail() })
    }

    @Test
    fun `test kommandant constructor`() {
        Kommandant()
        val kommandant = KommandantSubclass()
        kommandant.test()
    }

    class KommandantSubclass : Kommandant() {
        fun test() {
            this.registry
            this.scheduler
            this.executor
            this.registry
            this.annotationParser
        }
    }
}
