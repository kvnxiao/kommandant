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
package testcommands

import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.CommandContext

class TertiaryCommand {

    @CommandAnn(uniqueName = "primary", aliases = arrayOf("primary"))
    fun mainCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a primary command!"
    }

    @CommandAnn(uniqueName = "secondary", aliases = arrayOf("secondary"), parentName = "primary")
    fun subCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a secondary command!"
    }

    @CommandAnn(uniqueName = "tertiary", aliases = arrayOf("tertiary"), parentName = "secondary")
    fun tertiaryCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a tertiary command!"
    }

    @CommandAnn(uniqueName = "quaternary", aliases = arrayOf("quaternary", "quad"), parentName = "tertiary")
    fun quaternaryCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a quaternary command!"
    }

}