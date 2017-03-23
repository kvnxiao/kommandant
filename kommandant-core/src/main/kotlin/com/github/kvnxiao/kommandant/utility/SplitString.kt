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
package com.github.kvnxiao.kommandant.utility

/**
 * A container utility class for strings split into two.
 *
 * @param[content] The string to split into two based on the first available whitespace.
 * @property[first] The first portion of the string before the first whitespace char.
 * @property[second] The latter potion of the string after the first whitespace char.
 */
open class SplitString(content: String) {

    val first: String?
    val second: String?

    init {
        val splitInput = StringSplitter.split(content, StringSplitter.SPACE_LITERAL, 2)
        this.first = if (splitInput.isNotEmpty()) splitInput[0] else null
        this.second = if (splitInput.size == 2) splitInput[1] else null
    }

    /**
     * Whether the first portion of the split string is not null or not empty.
     *
     * @return[Boolean] Whether the first portion is not null or not empty.
     */
    fun hasFirst(): Boolean = !first.isNullOrEmpty()

    /**
     * Whether the latter portion of the split string is not null or not empty.
     *
     * @return[Boolean] Whether the latter portion is not null or not empty.
     */
    fun hasSecond(): Boolean = !second.isNullOrEmpty()

    /**
     * Kotlin helper method for destructuring support.
     */
    operator fun component1(): String? = this.first

    /**
     * Kotlin helper method for destructuring support.
     */
    operator fun component2(): String? = this.second

}