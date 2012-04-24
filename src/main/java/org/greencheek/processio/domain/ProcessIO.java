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
package org.greencheek.processio.domain;

import java.beans.ConstructorProperties;

/**
 * Represents the amount of IO that a process has performed.
 * The object stores two values.  The previous state and the current state.
 * Two values are stored that any processor can make calculations based on the values, for example
 * the amount of IO processed in X seconds for instance.
 *
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 13:29
 */
public class ProcessIO {
    private final long previousSampleMs;
    private final long previousSampleReadBytes;
    private final long previousSampleWriteBytes;

    private final long currentSampleMs;
    private final long currentSampleReadBytes;
    private final long currentSampleWriteBytes;

    public ProcessIO() {
        this(0,0,0,0,0,0);
    }

    @ConstructorProperties({"previousSampleMs","previousSampleReadBytes","previousSampleWriteBytes",
                            "currentSampleMs","currentSampleReadBytes","currentSampleWriteBytes"})
    public ProcessIO(long previousSampleMills,
                     long previousSampleReadBytes,
                     long previousSampleWriteBytes,
                     long currentSampleMillis,
                     long currentSampleReadBytes,
                     long currentSampleWriteBytes) {
        this.previousSampleMs = previousSampleMills;
        this.previousSampleReadBytes = previousSampleReadBytes;
        this.previousSampleWriteBytes = previousSampleWriteBytes;
        this.currentSampleMs = currentSampleMillis;
        this.currentSampleReadBytes = currentSampleReadBytes;
        this.currentSampleWriteBytes = currentSampleWriteBytes;

    }

    public ProcessIO updateCurrentValues(CurrentProcessIO currentIO) {
        return updateCurrentValues(currentIO.getCurrentSampleTimeInMillis(),currentIO.getCurrentReadBytes(),
                                   currentIO.getCurrentWriteBytes());
    }

    public ProcessIO updateCurrentValues(long currentSampleMillis,
                                         long currentSampleReadBytes,
                                         long currentSampleWriteBytes) {
        if(currentSampleReadBytes == Long.MIN_VALUE && currentSampleWriteBytes == Long.MIN_VALUE) return this;
        return new ProcessIO(this.getCurrentSampleMs(), this.getCurrentSampleReadBytes(), this.getCurrentSampleWriteBytes(),
                             currentSampleMillis,currentSampleReadBytes,currentSampleWriteBytes);
    }

    /**
     * Returns the previous time that the read and write bytes were obtained.
     * @return
     */
    public long getPreviousSampleMs() {
        return previousSampleMs;
    }

    /**
     * Returns the number of bytes that where read previously by the process
     * @return
     */
    public long getPreviousSampleReadBytes() {
        return previousSampleReadBytes;
    }

    /**
     * Returns the number of byte the where written previously by the process
     * @return
     */
    public long getPreviousSampleWriteBytes() {
        return previousSampleWriteBytes;
    }

    /**
     * Returns the last time, in millis, that the read and write bytes were obtained
     * @return
     */
    public long getCurrentSampleMs() {
        return currentSampleMs;
    }

    /**
     * Returns the number of bytes the process has read, that were last sampled
     * @return
     */
    public long getCurrentSampleReadBytes() {
        return currentSampleReadBytes;
    }

    /**
     * Returns the number of bytes the process has written, that were last sampled
     * @return
     */
    public long getCurrentSampleWriteBytes() {
        return currentSampleWriteBytes;
    }
}
