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

import com.github.kvnxiao.kommandant.KommandantAsync
import com.github.kvnxiao.kommandant.command.CommandBuilder
import com.github.kvnxiao.kommandant.command.CommandBuilder.Companion.execute
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by kxiao on 3/9/17.
 */
class AsyncTest {

    companion object {
        private val kommandant: KommandantAsync = KommandantAsync()
    }

    @After
    fun after() {
        kommandant.clearAll()
    }

    @Test
    fun testAsync() {
        kommandant.addCommand(CommandBuilder<Unit>("test").withAliases("test").build(execute { context, opt ->
            Thread.sleep(1000)
            println("Hello world!")
        }))

        // Fire and forget
        kommandant.processAsync<Unit>("/test")
        kommandant.processAsync<Unit>("/test")
        runBlocking {
            assertTrue(kommandant.processAsync<Unit>("/test").await().success)
        }
    }

    @Test
    fun testSequential() {
        kommandant.addCommand(CommandBuilder<Unit>("test").withAliases("test").build(execute { context, opt ->
            Thread.sleep(1000)
            println("Hello world!")
        }))

        assertTrue(kommandant.process<Unit>("/test").success)
        assertTrue(kommandant.process<Unit>("/test").success)
    }

}