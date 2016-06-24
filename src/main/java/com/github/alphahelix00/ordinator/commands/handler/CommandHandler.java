package com.github.alphahelix00.ordinator.commands.handler;

import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class CommandHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger("CommandHandler");
    protected final CommandRegistry commandRegistry;

    public CommandHandler(final CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    public Tuple3<Boolean, Optional<String>, Optional<List<String>>> validateMessage(String message) {
        boolean isValid = false;
        Optional<String> optPrefix = Optional.empty();
        Optional<List<String>> optMessageArgs = Optional.empty();

        for (String prefix : commandRegistry.getPrefixMap().keySet()) {
            if (message.startsWith(prefix)) {
                isValid = true;
                optPrefix = Optional.of(prefix);
                optMessageArgs = Optional.of(splitMessage(message.substring(prefix.length())));
            }
        }
        // Check if message starts with a command prefix
        return Tuple.tuple(isValid, optPrefix, optMessageArgs);
    }

    public static List<String> splitMessage(String message) {
        return new ArrayList<>(Arrays.asList(message.split("\\s+")));
    }


}
