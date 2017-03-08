package com.github.kvnxiao.kommandant.command;

/**
 * Created on:   2017-01-30
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class CommandContext {

    private final String alias;
    private final String args;
    private static final String SPLIT_REGEX = " ";

    public CommandContext(final String message) {
        if (message != null) {
            final String[] context = message.split(SPLIT_REGEX, 2);
            this.alias = (context.length > 0) ? context[0] : null;
            this.args = (context.length == 2) ? context[1] : null;
        } else {
            this.args = null;
            this.alias = null;
        }
    }

    public String getAlias() {
        return alias;
    }

    public String getArgs() {
        return args;
    }

    public boolean hasAlias() {
        return alias != null;
    }

    public boolean hasArgs() {
        return args != null;
    }

}

