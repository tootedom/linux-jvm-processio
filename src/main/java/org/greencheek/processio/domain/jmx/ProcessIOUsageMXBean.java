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
package org.greencheek.processio.domain.jmx;

import org.greencheek.processio.domain.ProcessIO;

/**
 * <p>
 * Used as the central object to communicate with the jvm MbeanServer.  The MXBean is used to obtain from the
 * MBeanServer in one finite unit the ProcessIO that is used to determine the IO that is used on a KB per
 * second ratio.  Without the MXBean we would have to obtain from the jmx server the individual values for the
 * ProcessIO object.  Such as the amount of IO currently used, and the amount of IO previously used.  If we had to
 * do this we'd have to obtain the values independently from each other, and as a result this risks having
 * the values update inbetween fetching the values.  As a result the MXBean, allows for the retrieval of the
 * ProcessIO object as a unique self contained unit.  Therefore, the previous value is related to the current value.
 * </p>
 * <p>
 * The MXBean returns:
 * <ul>
 *     <li>The amount of KB or MB per second that has occurred since that last time the process IO was read</li>
 *     <li>The amoutn of KB or MB, per second, that the process has resulted in.</li>
 * </ul>
 *
 * </p>
 *
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 15:53
 */
public interface ProcessIOUsageMXBean {
    ProcessIO getProcessIO();

    /**
     * The amount of read I/O that the jvm process has resulted in, since the last time the IO values were sampled for
     * the given project.
     *
     * @return The amount of IO that was occurred since the last time the IO for the process was read in KB per second
     */
    public double getSampleTimeKbPerSecondReadIO();

    /**
     * The amount of write I/O that the jvm process has resulted in, since the last time the IO values were sampled for
     * the given project.
     *
     * @return The amount of IO that was occurred since the last time the IO for the process was read in KB per second
     */
    public double getSampleTimeKbPerSecondWriteIO();


    /**
     * The amount of read I/O that the jvm process has resulted in, since the last time the IO values were sampled for
     * the given project.
     *
     * @return The amount of IO that was occurred since the last time the IO for the process was read in MB
     *         (megabytes) per second
     */
    public double getSampleTimeMbPerSecondReadIO();


    /**
     * The amount of write I/O that the jvm process has resulted in, since the last time the IO values were sampled for
     * the given project.
     *
     * @return The amount of IO that was occurred since the last time the IO for the process was read in MB
     *         (megabytes) per second
     */
    public double getSampleTimeMbPerSecondWriteIO();


    /**
     * The amount of read I/O that the jvm process has made since the start of the jvm (or when the counters
     * were initially measured).  The amount is measured in kb per second.
     *
     * @return The amount of IO that was occurred throughout the lifetime of the jvm
     */
    public double getAccumulatedKbPerSecondReadIO();

    /**
     * The amount of write I/O that the jvm process has made since the start of the jvm (or when the counters
     * were initially measured).   The amount is measured in kb per second.
     *
     * @return The amount of IO that was occurred throughout the lifetime of the jvm
     */
    public double getAccumulatedKbPerSecondWriteIO();

    /**
     * The amount of read I/O that the jvm process has made since the start of the jvm (or when the counters
     * were initially measured).  The amount is measured in mb per second.
     *
     * @return The amount of IO that was occurred throughout the lifetime of the jvm
     */
    public double getAccumulatedMbPerSecondReadIO();

    /**
     * The amount of write I/O that the jvm process has made since the start of the jvm (or when the counters
     * were initially measured). The amount is measured in mb per second.
     *
     * @return The amount of IO that was occurred throughout the lifetime of the jvm
     */
    public double getAccumulatedMbPerSecondWriteIO();

}
