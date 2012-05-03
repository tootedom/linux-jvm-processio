/*
 * Copyright 2012 dominictootell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.greencheek.processio.service.pid;

/**
 * <p>
 * Implementations can use what ever means to obtain the PID of the currently operating JVM process
 * </p>
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 12:12
 * </p>
 */
public interface JVMProcessIdObtainer {
    public static final int PID_NOT_FOUND = -1;

    /**
     * Returns the process ID of the current JVM process, otherwise SHOULD
     * return {@value #PID_NOT_FOUND}
     *
     * @return The pid of the current jvm process
     */
    int getProcessId();
}
