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
package com.github.kvnxiao.kommandant.utility

/**
 * Splits a string in two, based on the first occurring delimiter being either a space character or a new-line character.
 *
 * For example:
 * "Hello World" -> ("Hello", "World")
 * "Hello\r\nWorld" -> ("Hello", "World")
 *
 */
open class SplitString(content: String) {

    companion object {
        private const val R: Char = '\r'
        private const val N: Char = '\n'
        private const val SPACE: Char = ' '
        @JvmStatic
        private val LINE_SEPARATOR: String = System.lineSeparator()
    }

    val first: String
    val second: String?
    val delimiter: String

    init {
        var indexSpace = -1
        var indexNewLine = -1
        var nextIndex = -1
        val max = content.length

        // Find the first occuring instance of a delimiter
        for (i in 0 until max) {
            when (content[i]) {
                R -> {
                    indexNewLine = i
                    nextIndex = i + 2
                }
                N -> {
                    indexNewLine = i
                    nextIndex = i + 1
                }
                SPACE -> {
                    indexSpace = i
                    nextIndex = i + 1
                }
            }
            if (indexSpace > -1 || indexNewLine > -1) {
                break
            }
        }
        if (indexSpace > -1) {
            this.first = content.substring(0, indexSpace)
            this.second = content.substring(nextIndex, max)
            this.delimiter = SPACE.toString()
        } else if (indexNewLine > -1) {
            this.first = content.substring(0, indexNewLine)
            this.second = content.substring(nextIndex, max)
            this.delimiter = LINE_SEPARATOR
        } else {
            this.first = content
            this.second = null
            this.delimiter = ""
        }
    }

    operator fun component1(): String = this.first
    operator fun component2(): String? = this.second
}
