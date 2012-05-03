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
package org.greencheek.processio.service.io;

import org.greencheek.processio.domain.CurrentProcessIO;

/**
 * Interface for reading the read and write io for the process
 *
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 17:41
 * </p>
 */
public interface ProcessIOReader {

    /**
     * Represents that the IO for the current process could not be read/obtained
     */
    public static final CurrentProcessIO NON_READABLE_PROCESS_IO =  new CurrentProcessIO(-1l,Long.MIN_VALUE,Long.MIN_VALUE);

    /**
     * Read the current io (read_bytes and write_bytes) that a process has perform (accumulative), since it started.
     *
     * @return A CurrentProcessIO object that represents the io for the process, or if unable to read the io for the process
     * will return {@link ProcessIOReader#NON_READABLE_PROCESS_IO}
     */
    CurrentProcessIO getCurrentProcessIO();
}
