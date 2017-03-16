package com.github.kvnxiao.kommandant.command;

import org.jetbrains.annotations.Nullable;

/**
 * A data class for containing the context of the command. It parses a string and splits it in two based on the first
 * available space. The first string represents the alias, and the second string represents the arguments leftover.
 * <p>
 * NOTE: This class was written in Java. And why, you might ask? As of Kotlin 1.10, there is still some performance
 * overhead in splitting strings by a literal. As such, string splitting in Java is currently still more performant
 * than in Kotlin.
 */
public class CommandContext {

    /**
     * The first string after splitting the input string.
     */
    private final String alias;

    /**
     * The second string after splitting the input string.
     */
    private final String args;

    /**
     * The command associated with this context.
     */
    private ICommand command = null;

    /**
     * The string literal to split the incoming input string by, defined as a single space character " ".
     */
    private static final String SPLIT_REGEX = " ";

    /**
     * Constructor which takes in a string to split in two. Splits based on the first available space character.
     * If a space does not exist, the args string will be null. If the input string is null, both resultant args and
     * aliases will be null.
     *
     * @param message The message string to split into alias and args.
     */
    public CommandContext(@Nullable final String message) {
        if (message != null) {
            final String[] context = message.split(SPLIT_REGEX, 2);
            this.alias = (context.length > 0) ? context[0] : null;
            this.args = (context.length == 2) ? context[1] : null;
        } else {
            this.args = null;
            this.alias = null;
        }
    }

    /**
     * Secondary constructor which sets the alias and args of the context based on the provided input parameters.
     *
     * @param alias The alias for the command context.
     * @param args The arguments for the command context.
     */
    public CommandContext(final String alias, final String args) {
        this.alias = alias;
        this.args = args;
    }

    /**
     * Gets the alias for this CommandContext. Can be null.
     *
     * @return The (nullable) alias.
     */
    @Nullable
    public String getAlias() {
        return alias;
    }

    /**
     * Gets the args for this CommandContext. Can be null.
     *
     * @return The (nullable) args.
     */
    @Nullable
    public String getArgs() {
        return args;
    }

    /**
     * A CommandContext has an alias if the alias string is not null and not empty.
     *
     * @return Whether an alias exists.
     */
    public boolean hasAlias() {
        return alias != null && !alias.isEmpty();
    }

    /**
     * A CommandContext has args if the args string is not null. The args can be null if the input string that was
     * split had no space character in it.
     *
     * @return Whether args exist.
     */
    public boolean hasArgs() {
        return args != null;
    }

    /**
     * Gets the command this context is associated with.
     *
     * @return The associated command for this context.
     */
    @Nullable
    public ICommand getCommand() {
        return this.command;
    }

    /**
     * Sets the command this context is associated with.
     *
     * @param command The command to associate with this context.
     */
    public void setCommand(ICommand command) {
        this.command = command;
    }

}

