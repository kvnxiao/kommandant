package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.utility.ImmutableCommandMap

/**
 * An interface that defines the methods a command registry needs.
 */
interface ICommandBank {

    /**
     * Adds a command to the registry.
     *
     * @return[Boolean] Whether the command was successfully added to the registry.
     */
    fun addCommand(command: ICommand<*>): Boolean

    /**
     * Removes a command from the registry, but does not destroy it.
     *
     * @return[Boolean] Whether the command was successfully removed from the bank.
     */
    fun removeCommand(command: ICommand<*>): Boolean

    /**
     * Deletes a command from the registry and destroys all subcommand references.
     *
     * @return[Boolean] Whether the command and all its subcommands were successfully deleted.
     */
    fun deleteCommand(command: ICommand<*>): Boolean

    /**
     * Gets a command from the bank by providing a single string (usually the "prefix + alias" together).
     *
     * @return[ICommand] The (nullable) command.
     */
    fun getCommand(singleString: String): ICommand<*>?

    /**
     * Returns whether a command queried using this single string exists in the registry.
     *
     * @return[Boolean] Whether the command exists in the registry.
     */
    fun hasCommand(singleString: String): Boolean

    /**
     * Adds a prefix to the bank to keep track of all prefixes used by commands.
     *
     * @return[Boolean] Whether the prefix was added successfully. Returns false if it already exists.
     */
    fun addPrefix(prefix: String): Boolean

    /**
     * Change an existing command's prefix.
     *
     * @return[Boolean] Whether the prefix change was successful.
     */
    fun changePrefix(command: ICommand<*>, newPrefix: String): Boolean

    /**
     * Gets an immutable set of all prefixes registered in the bank.
     *
     * @return[Set] The set of all prefixes.
     */
    fun getPrefixes(): Set<String>

    /**
     * Gets an immutable map of all single-string identifiers to commands (usually all "prefix + alias" strings to commands)
     *
     * @return[ImmutableCommandMap] The immutable map of all single-string identifiers to commands.
     */
    fun getCommands(): ImmutableCommandMap

    /**
     * Gets an immutable list of all commands
     *
     * @return[List] The immutable list of all registered commands.
     */
    fun getCommandsUnique(): List<ICommand<*>>

    /**
     * Gets an immutable map of alias to commands for the given prefix.
     *
     * @return[ImmutableCommandMap] The immutable map of all aliases to commands under the given prefix.
     */
    fun getCommandsForPrefix(prefix: String): ImmutableCommandMap

    /**
     * Deletes all commands from the registry.
     */
    fun clearAll()

}