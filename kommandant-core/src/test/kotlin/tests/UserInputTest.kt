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

fun main(args: Array<String>) {
    val kommandant = Kommandant()

    kommandant.addCommand(CommandBuilder<Unit>("hello_world")
            .withPrefix("-")
            .withAliases("hello", "print")
            .build(execute {
        context, opt ->
        println("Hello world!")
    }))

    while (true) {
        println("Please enter your input:")
        kommandant.process<Any?>(readLine()!!)
    }

}