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

class EnabledDisabledCommands {

    @CommandAnn(
            uniqueName = "enabled",
            aliases = arrayOf("enabled"),
            isDisabled = false
    )
    fun enabled(context: CommandContext, vararg opt: Any?): String {
        return "This command is enabled"
    }

    @CommandAnn(
            uniqueName = "disabled",
            aliases = arrayOf("disabled"),
            isDisabled = true
    )
    fun disabled(context: CommandContext, vararg opt: Any?): String {
        return "This command is disabled"
    }

    @CommandAnn(
            uniqueName = "parentenabled",
            aliases = arrayOf("parent"),
            isDisabled = false
    )
    fun parentEnabled(context: CommandContext, vararg opt: Any?): String {
        return "This main command is enabled"
    }

    @CommandAnn(
            uniqueName = "childdisabled",
            aliases = arrayOf("child"),
            isDisabled = true,
            parentName = "parentenabled"
    )
    fun childDisabled(context: CommandContext, vararg opt: Any?): String {
        return "This sub command is disabled"
    }

}