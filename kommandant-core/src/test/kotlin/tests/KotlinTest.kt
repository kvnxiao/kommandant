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
package tests

import com.github.kvnxiao.kommandant.Kommandant
import com.github.kvnxiao.kommandant.command.CommandBuilder
import com.github.kvnxiao.kommandant.command.CommandBuilder.Companion.execute
import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.ICommand
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import testcommands.KotlinCommand
import testcommands.VarargCommand

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
class KotlinTest {

    companion object {
        private val kommandant: Kommandant = Kommandant()
    }

    @After
    fun after() {
        kommandant.clearAll()
    }

    @Test
    fun testBasic() {
        kommandant.addAnnotatedCommands(KotlinCommand::class)
        kommandant.addCommand(CommandBuilder<Unit>("kotlintest").withPrefix("-").withAliases("ktest").build(execute {
            context, opt ->
            println("HELLO WORLD!")
            opt.forEach(::println)
        }))

        // Test command added with kotlin lambda
        val fail = kommandant.process<Unit>("/ktest")
        assertFalse(fail.success)
        val success = kommandant.process<Unit>("-ktest", "hello", "world")
        assertTrue(success.success)

        // Test annotated command
        val annotated = kommandant.process<String>("/kotlin")
        assertTrue(annotated.success)
        assertEquals("this is a kotlin based command!", annotated.result)
    }

    @Test
    fun testDeleteCommand() {
        kommandant.addAnnotatedCommands(KotlinCommand::class)
        assertEquals(1, kommandant.getPrefixes().size)
        assertTrue(kommandant.getPrefixes().contains("/"))
        assertEquals(1, kommandant.getCommandsUnique().size)
        assertTrue(kommandant.process<String>("/kotlin").success)

        kommandant.deleteCommand(kommandant.getCommand("/kotlin"))
        assertFalse(kommandant.process<String>("/kotlin").success)
        assertTrue(kommandant.getPrefixes().isEmpty())
        assertTrue(kommandant.getCommandsUnique().isEmpty())
        assertTrue(kommandant.getAllCommands().isEmpty())
    }

    @Test
    fun testVarargs() {
        kommandant.addCommand(CommandBuilder<Unit>("varargbuilder").withAliases("builder").build(execute { context, opt ->
            opt.forEach(::println)
        }))
        kommandant.addCommand(object : ICommand<Unit>(CommandDefaults.PREFIX, "varargconstructor", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, CommandDefaults.IS_DISABLED, "constructor") {
            override fun execute(context: CommandContext, vararg opt: Any?): Unit {
                opt.forEach(::println)
            }
        })
        kommandant.addAnnotatedCommands(VarargCommand::class)
        assertTrue(kommandant.process<Unit>("/builder", "this", "is", "a", "builder").success)
        assertTrue(kommandant.process<Unit>("/constructor", "this", "is", "a", "constructor").success)
        assertTrue(kommandant.process<Unit>("/annotation", "this", "is", "an", "annotation").success)
    }

    @Test
    fun testSubcommands() {
        kommandant.addCommand(CommandBuilder<Unit>("tests.main").withAliases("tests.main").build(execute { context, opt ->
            opt.forEach(::println)
        }).addSubcommand(CommandBuilder<Unit>("sub").withAliases("sub").build(execute { context, opt ->
            println("This is a subcommand.")
        })))

        assertTrue(kommandant.process<Unit>("/tests.main sub").success)
        assertTrue(kommandant.process<Unit>("/tests.main", 1, 2, 3).success)
    }

}