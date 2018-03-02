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
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.kvnxiao.kommandant.utility

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class TestSplitString {

    @Test
    fun `test split by space simple`() {
        val split = SplitString("Hello world")
        assertEquals("Hello", split.first)
        assertNotNull(split.second)
        assertEquals("world", split.second)
        assertEquals(" ", split.delimiter)
    }

    @Test
    fun `test split by new-line simple`() {
        val split = SplitString("""Hello
            |world""".trimMargin())
        assertEquals("Hello", split.first)
        assertNotNull(split.second)
        assertEquals("world", split.second)
        assertEquals(System.lineSeparator(), split.delimiter)
    }

    @Test
    fun `test kotlin destructuring on SplitString`() {
        val split = SplitString("Hello world")
        val (first, second) = split
        assertEquals("Hello", first)
        assertEquals("world", second)
    }

    @Test
    fun `test multiple space splitting`() {
        val (first, second) = SplitString("Hello, hello world!")
        assertEquals("Hello,", first)
        assertNotNull(second)
        assertEquals("hello world!", second)
        val (nextFirst, nextSecond) = SplitString(second!!)
        assertEquals("hello", nextFirst)
        assertNotNull(nextSecond)
        assertEquals("world!", nextSecond)
    }

    @Test
    fun `test multiple new-line splitting`() {
        val (first, second) = SplitString("""Hello,
            |hello
            |world!""".trimMargin())
        assertEquals("Hello,", first)
        assertNotNull(second)
        assertEquals("hello${System.lineSeparator()}world!", second)
        val (nextFirst, nextSecond) = SplitString(second!!)
        assertEquals("hello", nextFirst)
        assertNotNull(nextSecond)
        assertEquals("world!", nextSecond)
    }

    @Test
    fun `test no splitting`() {
        val (first, second) = SplitString("hello")
        assertEquals("hello", first)
        assertNull(second)
    }

    @Test
    fun `test new-line splitting with new-line characters in string`() {
        val (first, second) = SplitString("Hello\r\nworld!")
        assertEquals("Hello", first)
        assertEquals("world!", second)
        val (third, fourth) = SplitString("Hello\nworld!")
        assertEquals("Hello", third)
        assertEquals("world!", fourth)
    }
}
