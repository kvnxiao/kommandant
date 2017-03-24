package com.github.kvnxiao.kommandant.command

/**
 * A data class containing the properties of the command.
 *
 * @property[prefix] The command's prefix. Defaults to [CommandDefaults.PREFIX].
 * @property[uniqueName] The command's unique name / identifier.
 * @property[description] The command's description. Defaults to [CommandDefaults.NO_DESCRIPTION].
 * @property[usage] The command's usage information. Defaults to [CommandDefaults.NO_USAGE].
 * @property[execWithSubcommands] Specifies whether to execute the command alongside its subcommands. Defaults to [CommandDefaults.EXEC_WITH_SUBCOMMANDS].
 * @property[disabled] Specifies whether the command is disabled or not. Defaults to [CommandDefaults.IS_DISABLED].
 * @property[aliases] The command's aliases. Defaults to a singleton list containing the [uniqueName].
 */
data class CommandProperties(
    val prefix: String = CommandDefaults.PREFIX,
    val uniqueName: String,
    val description: String = CommandDefaults.NO_DESCRIPTION,
    val usage: String = CommandDefaults.NO_USAGE,
    val execWithSubcommands: Boolean = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
    val disabled: Boolean = CommandDefaults.IS_DISABLED,
    val aliases: Set<String> = setOf(uniqueName)) {

    /**
     * Overrided toString method returns the [uniqueName].
     *
     * @return[uniqueName] The command's unique name / identifier.
     */
    override fun toString(): String {
        return this.uniqueName
    }

}