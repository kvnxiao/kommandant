package com.github.kvnxiao.kommandant.command

/**
 * A data class containing the properties of the command.
 */
data class CommandProperties(
        /**
         * The command's prefix. Defaults to [CommandDefaults.PREFIX].
         */
        var prefix: String = CommandDefaults.PREFIX,
        /**
         * The command's unique name / identifier.
         */
        val uniqueName: String,
        /**
         * The command's description. Defaults to [CommandDefaults.NO_DESCRIPTION].
         */
        var description: String = CommandDefaults.NO_DESCRIPTION,
        /**
         * The command's usage information. Defaults to [CommandDefaults.NO_USAGE].
         */
        var usage: String = CommandDefaults.NO_USAGE,
        /**
         * Specifies whether to execute the command alongside its subcommands. Defaults to [CommandDefaults.EXEC_WITH_SUBCOMMANDS].
         */
        var execWithSubcommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
        /**
         * Specifies whether the command is disabled or not. Defaults to [CommandDefaults.IS_DISABLED].
         */
        var isDisabled: Boolean = CommandDefaults.IS_DISABLED,
        /**
         * The command's aliases. Defaults to a singleton list containing the [uniqueName].
         */
        var aliases: List<String> = listOf(uniqueName)) {

    /**
     * Overriden toString method returns the [uniqueName].
     *
     * @return[uniqueName] The command's unique name / identifier.
     */
    override fun toString(): String {
        return this.uniqueName
    }

}