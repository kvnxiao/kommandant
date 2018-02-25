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
package com.github.kvnxiao.kommandant.command

object CommandDefaults {
    /**
     * The default parent id, set as an empty string.
     */
    const val PARENT_ID = ""
    /**
     * The default prefix, set as an empty string.
     */
    const val NO_PREFIX = ""
    /**
     * The default command description, set as "No description available."
     */
    const val NO_DESCRIPTION = "No description available."
    /**
     * The default command usage information, set as "No usage information available."
     */
    const val NO_USAGE = "No usage information available."
    /**
     * The default setting for whether commands should execute alongside their sub-commands, set as false.
     */
    const val EXEC_WITH_SUBCOMMANDS = false
    /**
     * The default setting for whether the command is disabled, set as false (commands default to enabled).
     */
    const val IS_DISABLED = false
    /**
     * The default setting for whether commands can read the command registry (default to false).
     */
    const val IS_REGISTRY_AWARE = false
}
