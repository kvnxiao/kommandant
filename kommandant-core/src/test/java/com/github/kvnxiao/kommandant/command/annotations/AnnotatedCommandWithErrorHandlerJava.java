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
package com.github.kvnxiao.kommandant.command.annotations;

import com.github.kvnxiao.kommandant.command.Context;
import com.github.kvnxiao.kommandant.command.ExecutionErrorHandler;

import static org.junit.Assert.assertEquals;

public class AnnotatedCommandWithErrorHandlerJava {

    private final ExecutionErrorHandler errorHandler =
            (command, ex) -> {
                System.out.println("Calling custom error handler from Java");
                assertEquals(command.getProperties().getId(), "self_java");
            };

    @ErrorHandler
    public ExecutionErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    @Command(id = "self_java", aliases = "sj")
    public int commandErrorHandler(Context context, Object[] opt) {
        return 1;
    }
}
