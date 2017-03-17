package com.github.kvnxiao.kommandant.command

/**
 * A data class for containing the context of the command. It parses a string and splits it in two based on the first
 * available space. The first string represents the alias, and the second string represents the arguments leftover.
 *
 * @property[alias] The alias or prefixed-alias used to call the command.
 * @property[args] The (nullable) arguments provided for the command.
 * @property[command] The command being called.
 */
data class CommandContext(val alias: String,
                          val args: String?,
                          val command: ICommand<*>) {

    companion object {
        /**
         * The string literal to split the incoming input string by, defined as a single space character " ".
         */
        const val SPACE_LITERAL = " "
    }

    /**
     * Whether the command context contains arguments for the command.
     *
     * @return[Boolean] Whether the args property in the command context is not null.
     */
    fun hasArgs(): Boolean = args !== null

}