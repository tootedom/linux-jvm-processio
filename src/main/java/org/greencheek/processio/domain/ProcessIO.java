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
 * Two values are stored so that any {@link org.greencheek.processio.service.usage.ProcessIOUsage}
 * object can make calculations based on the two values, for example
 * the amount of IO processed in X seconds.
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 13:29
 * </p>
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

    /**
     * If the ProcessIO object is stored in a Jmx MBean Server, the @ConstructorProperties
     * are used in order to allow a JMX MXBean object to reconstruct the ProcessIO object to map the
     * constructor arguments to match the jmx properties (accessor method) of the object.
     *
     * @param previousSampleMills The previous time in millis the IO values for the process were read
     * @param previousSampleReadBytes The amount of IO the process had performed previously, at the previous time stamp
     * @param previousSampleWriteBytes The amount of IO the process had performed previously, at the previous time stamp
     * @param currentSampleMillis  The most recent time in millis the IO values for the process were read
     * @param currentSampleReadBytes The amount of read IO the process has performed from the most recent sample of it's io usage
     * @param currentSampleWriteBytes The amount of write IO the process had performed from the most recent sample of it's io usage
     */
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

    /**
     * Given the current amount of IO that the process has been recorded as caused, This will return a new ProcessIO
     * object with the previous (this object's) current io values , as the previous values, and the
     * values in the given {@link CurrentProcessIO} object as the most recent, current, values.
     *
     * @param currentIO The current io that has been recorded for the object
     * @return
     */
    public ProcessIO updateCurrentValues(CurrentProcessIO currentIO) {
        return updateCurrentValues(currentIO.getCurrentSampleTimeInMillis(),currentIO.getCurrentReadBytes(),
                                   currentIO.getCurrentWriteBytes());
    }

    /**
     *
     * Given the current amount of IO that the process has been recorded as caused, This will return a new ProcessIO
     * object with the previous (this object's) current io values , as the previous values, and the
     * values in the given as the most recent, current, values.
     *
     *
     * @param currentSampleMillis  The time the latest (current) io value were read.
     * @param currentSampleReadBytes The current amount of read io the process has performed since it started
     * @param currentSampleWriteBytes The current amount of write io the process has performed since it started.  The
     *                                io value was retrieve at the given millis
     * @return A new ProcessIO object that is build from the current objects current io values, which will be set as the
     *         previous io, and it's current values taken from the given parameters
     */
    public ProcessIO updateCurrentValues(long currentSampleMillis,
                                         long currentSampleReadBytes,
                                         long currentSampleWriteBytes) {
        if(currentSampleReadBytes == Long.MIN_VALUE && currentSampleWriteBytes == Long.MIN_VALUE) return this;
        return new ProcessIO(this.getCurrentSampleMs(), this.getCurrentSampleReadBytes(), this.getCurrentSampleWriteBytes(),
                             currentSampleMillis,currentSampleReadBytes,currentSampleWriteBytes);
    }

    /**
     * Returns the previous time that the read and write bytes were obtained.
     * @return  The time in millis that the previous IO usage was read
     */
    public long getPreviousSampleMs() {
        return previousSampleMs;
    }

    /**
     * Returns the number of bytes that where read previously by the process
     * @return the number of bytes the process had previously read
     */
    public long getPreviousSampleReadBytes() {
        return previousSampleReadBytes;
    }

    /**
     * Returns the number of byte the where written previously by the process
     * @return the number of bytes the process had previously written
     */
    public long getPreviousSampleWriteBytes() {
        return previousSampleWriteBytes;
    }

    /**
     * Returns the last time, in millis, that the read and write bytes were obtained
     * @return The latest time in millis that the process IO usage was read
     */
    public long getCurrentSampleMs() {
        return currentSampleMs;
    }

    /**
     * Returns the number of bytes the process has read, that were last sampled
     * @return the number of bytes read by the process currently, as determined by the last time the process was sampled
     */
    public long getCurrentSampleReadBytes() {
        return currentSampleReadBytes;
    }

    /**
     * Returns the number of bytes the process has written, that were last sampled
     * @return the number of bytes written by the process currently, as determined by the last time the process was sampled
     */
    public long getCurrentSampleWriteBytes() {
        return currentSampleWriteBytes;
    }

    /**
     * Returns the difference in write bytes between the previous time stamp and the current time stamp
     * @return The difference in bytes between the current sampled read bytes and the previous sample
     */
    public long getDifferenceInWriteBytes() {
        return getCurrentSampleWriteBytes() - getPreviousSampleWriteBytes();
    }

    /**
     * Returns the difference in read bytes between the previous time stamp and the current time stamp
     * @return The difference in bytes between the current sampled written bytes and the previous sample
     */
    public long getDifferenceInReadBytes() {
        return getCurrentSampleReadBytes() - getPreviousSampleReadBytes();
    }
}
