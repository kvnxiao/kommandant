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
package com.github.kvnxiao.kommandant.annotations

import com.github.kvnxiao.kommandant.command.Context
import com.github.kvnxiao.kommandant.command.annotations.Command
import com.github.kvnxiao.kommandant.command.annotations.CommandGroup
import com.github.kvnxiao.kommandant.command.annotations.Prefix
import com.github.kvnxiao.kommandant.command.parser.AnnotationParser
import com.github.kvnxiao.kommandant.command.parser.AnnotationParserImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TestAnnotationParser {

    @Test
    fun `test annotation parsing with global prefix`() {
        val annotationParser: AnnotationParser = AnnotationParserImpl()
        val instance = AnnotatedCommandTestWithGlobalPrefix()
        val list = annotationParser.parseAnnotations(instance).sortedBy { it.properties.id }
        assertEquals(4, list.size)

        val commandReturnsInt = list[0]
        val commandReturnsIntChild = list[1]
        val commandReturnsIntGrandChild = list[2]
        val commandReturnsString = list[3]

        // Check prefixes
        assertEquals("--", commandReturnsInt.properties.prefix)
        assertEquals("--", commandReturnsIntChild.properties.prefix)
        assertEquals("--", commandReturnsIntGrandChild.properties.prefix)
        assertEquals("--", commandReturnsString.properties.prefix)
        // Check aliases
        assertTrue(commandReturnsInt.properties.aliases.containsAll(setOf("int", "i")))
        assertTrue(commandReturnsIntChild.properties.aliases.containsAll(setOf("child", "c")))
        assertTrue(commandReturnsIntGrandChild.properties.aliases.containsAll(setOf("nextChild", "nc")))
        assertTrue(commandReturnsString.properties.aliases.containsAll(setOf("string", "s")))
        // Check parent-child linkage
        assertEquals("test.returns_int", commandReturnsInt.properties.id)
        assertEquals("test.returns_int", commandReturnsIntChild.properties.parentId)
        assertEquals("test.returns_int.child", commandReturnsIntGrandChild.properties.parentId)
        assertEquals("", commandReturnsInt.properties.parentId)
        assertEquals("", commandReturnsString.properties.parentId)
    }

    // Expect same result as from global prefix
    @Test
    fun `test annotation parsing with local prefix`() {
        val annotationParser: AnnotationParser = AnnotationParserImpl()
        val instance = AnnotatedCommandTestWithLocalPrefix()
        val list = annotationParser.parseAnnotations(instance).sortedBy { it.properties.id }
        assertEquals(4, list.size)

        val commandReturnsInt = list[0]
        val commandReturnsIntChild = list[1]
        val commandReturnsIntGrandChild = list[2]
        val commandReturnsString = list[3]

        // Check prefixes
        assertEquals("--", commandReturnsInt.properties.prefix)
        assertEquals("--", commandReturnsIntChild.properties.prefix)
        assertEquals("--", commandReturnsIntGrandChild.properties.prefix)
        assertEquals("--", commandReturnsString.properties.prefix)
        // Check aliases
        assertTrue(commandReturnsInt.properties.aliases.containsAll(setOf("int", "i")))
        assertTrue(commandReturnsIntChild.properties.aliases.containsAll(setOf("child", "c")))
        assertTrue(commandReturnsIntGrandChild.properties.aliases.containsAll(setOf("nextChild", "nc")))
        assertTrue(commandReturnsString.properties.aliases.containsAll(setOf("string", "s")))
        // Check parent-child linkage
        assertEquals("test.returns_int", commandReturnsInt.properties.id)
        assertEquals("test.returns_int", commandReturnsIntChild.properties.parentId)
        assertEquals("test.returns_int.child", commandReturnsIntGrandChild.properties.parentId)
        assertEquals("", commandReturnsInt.properties.parentId)
        assertEquals("", commandReturnsString.properties.parentId)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test annotation parsing non-unique id`() {
        val annotationParser: AnnotationParser = AnnotationParserImpl()
        val instance = AnnotatedCommandNonUniqueId()
        annotationParser.parseAnnotations(instance)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test annotation parsing self-reference id error`() {
        val annotationParser: AnnotationParser = AnnotationParserImpl()
        val instance = AnnotatedCommandSelfReference()
        annotationParser.parseAnnotations(instance)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test annotation parsing invalid parent id`() {
        val annotationParser: AnnotationParser = AnnotationParserImpl()
        val instance = AnnotatedCommandInvalidParentId()
        annotationParser.parseAnnotations(instance)
    }
}

@CommandGroup("test")
@Prefix("--")
class AnnotatedCommandTestWithGlobalPrefix {
    @Command(
        id = "returns_int",
        aliases = ["int", "i"]
    )
    fun commandReturnsInt(context: Context, opt: Array<Any>?): Int {
        return 1
    }

    @Command(
        id = "child",
        aliases = ["child", "c"],
        parentId = "returns_int"
    )
    fun commandReturnsIntChild(context: Context, opt: Array<Any>?): Int {
        return 2
    }

    @Command(
        id = "nextChild",
        aliases = ["nextChild", "nc"],
        parentId = "child"
    )
    fun commandReturnsIntGrandChild(context: Context, opt: Array<Any>?): Int {
        return 3
    }

    @Command(
        id = "returns_string",
        aliases = ["string", "s"]
    )
    fun commandReturnsString(context: Context, opt: Array<Any>?): String {
        return "string"
    }
}

@CommandGroup("test")
class AnnotatedCommandTestWithLocalPrefix {
    @Command(
        id = "returns_int",
        aliases = ["int", "i"]
    )
    @Prefix("--")
    fun commandReturnsInt(context: Context, opt: Array<Any>?): Int {
        return 1
    }

    @Command(
        id = "child",
        aliases = ["child", "c"],
        parentId = "returns_int"
    )
    @Prefix("--")
    fun commandReturnsIntChild(context: Context, opt: Array<Any>?): Int {
        return 2
    }

    @Command(
        id = "nextChild",
        aliases = ["nextChild", "nc"],
        parentId = "child"
    )
    @Prefix("--")
    fun commandReturnsIntGrandChild(context: Context, opt: Array<Any>?): Int {
        return 3
    }

    @Command(
        id = "returns_string",
        aliases = ["string", "s"]
    )
    @Prefix("--")
    fun commandReturnsString(context: Context, opt: Array<Any>?): String {
        return "string"
    }
}

class AnnotatedCommandNonUniqueId {
    @Command(
        id = "same",
        aliases = ["s1"]
    )
    fun commandSame1(context: Context, opt: Array<Any>?): Int {
        return 1
    }
    @Command(
        id = "same",
        aliases = ["s2"]
    )
    fun commandSame2(context: Context, opt: Array<Any>?): Int {
        return 1
    }
}

class AnnotatedCommandSelfReference {
    @Command(
        id = "self",
        aliases = ["s"],
        parentId = "self"
    )
    fun commandSelf(context: Context, opt: Array<Any>?): Int {
        return 1
    }
}

class AnnotatedCommandInvalidParentId {
    @Command(
        id = "self",
        aliases = ["s"],
        parentId = "nobody"
    )
    fun commandSelf(context: Context, opt: Array<Any>?): Int {
        return 1
    }
}
