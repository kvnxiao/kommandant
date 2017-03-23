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

import com.github.kvnxiao.kommandant.KommandantConfigurable
import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.ICommand
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import testcommands.EnabledDisabledCommands

class TestConfigurable {

    companion object {
        private val kommandant: KommandantConfigurable = KommandantConfigurable(true)
    }

    @Before
    fun setup() {
        kommandant.addCommand(object : ICommand<String>("/", "main", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, CommandDefaults.IS_DISABLED, "asdf", "test") {
            override fun execute(context: CommandContext, vararg opt: Any?): String {
                println("asdf 1")
                return "a"
            }
        }.addSubcommand(object : ICommand<String>("/", "subcommand", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, CommandDefaults.IS_DISABLED, "asdf", "test") {
            override fun execute(context: CommandContext, vararg opt: Any?): String {
                println("asdf 2")
                return "a"
            }
            // This next subcommand should fail to link
        }).addSubcommand(object : ICommand<String>("/", "subcommand2", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, CommandDefaults.IS_DISABLED, "asdf", "test2") {
            override fun execute(context: CommandContext, vararg opt: Any?): String {
                println("asdf 3")
                return "a"
            }
        }).addSubcommand(object : ICommand<String>("/", "subcommand3", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, CommandDefaults.IS_DISABLED, "asdf3", "test3") {
            override fun execute(context: CommandContext, vararg opt: Any?): String {
                println("asdf 4")
                return "a"
            }
        }))
        kommandant.addAnnotatedCommands(EnabledDisabledCommands::class)
    }

    @After
    fun after() {
        kommandant.clearAll()
    }

    @Test
    fun test() {
        kommandant.process<Any>("/asdf asdf")
        kommandant.process<Any>("/asdf test")
        kommandant.process<Any>("/asdf test2")
        kommandant.process<Any>("/asdf asdf3")
    }

    @Test
    fun testEnabledDisabledCommand() {
        // Assert enable command success, disable command fail
        val enabled = kommandant.process<String>("/enabled")
        assertTrue(enabled.success)
        val disabled = kommandant.process<String>("/disabled")
        assertFalse(disabled.success)
        val parent = kommandant.process<String>("/parent")
        assertTrue(parent.success)
        val child = kommandant.process<String>("/parent child")
        assertFalse(child.success)
    }

    @Test
    fun testDisablingCommands() {
        // Test enabling disabled commands & disabling enabled commands
        var enabled = kommandant.process<String>("/enabled")
        assertTrue(enabled.success)
        kommandant.disableCommand(kommandant.getCommand("/enabled"))
        enabled = kommandant.process<String>("/enabled")
        assertFalse(enabled.success)
        kommandant.enableCommand(kommandant.getCommand("/enabled"))
        enabled = kommandant.process<String>("/enabled")
        assertTrue(enabled.success)

        var disabled = kommandant.process<String>("/disabled")
        assertFalse(disabled.success)
        kommandant.enableCommand(kommandant.getCommand("/disabled"))
        disabled = kommandant.process<String>("/disabled")
        assertTrue(disabled.success)
    }

}