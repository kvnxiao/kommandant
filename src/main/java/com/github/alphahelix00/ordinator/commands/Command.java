package com.github.alphahelix00.ordinator.commands;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class Command {

    private Map<String, Command> subCommandMap = new HashMap<>();
    private boolean isEnabled = true;

    public boolean isRepeating() {
        return subCommandMap.containsKey(name);
    }

    @Getter
    @NonNull
    private boolean isEssential;
    @Getter
    @NonNull
    private String prefix;
    @Getter
    @NonNull
    private String name;
    @Getter
    @NonNull
    private List<String> aliases;
    @Getter
    @NonNull
    private String description;
    @Getter
    @NonNull
    private List<String> subCommandNames;
//    private boolean isEssential;
//    private String prefix;
//    private String name;
//    private List<String> aliases;
//    private String description;
//    private List<String> subCommandNames;

//    public Command(boolean isEssential, String prefix, String name, List<String> aliases, String description, List<String> subCommandNames) {
//        this.isEssential = isEssential;
//        this.prefix = prefix;
//        this.name = name;
//        this.aliases = aliases;
//        this.description = description;
//        this.subCommandNames = subCommandNames;
//    }
//
//    public boolean isEssential() {
//        return isEssential;
//    }
//
//    public String getPrefix() {
//        return prefix;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public List<String> getAliases() {
//        return aliases;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public List<String> getSubCommandNames() {
//        return subCommandNames;
//    }


    @Override
    public String toString() {
        return name;
    }
}
