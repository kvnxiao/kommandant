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