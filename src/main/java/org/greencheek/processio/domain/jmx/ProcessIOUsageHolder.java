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

import org.greencheek.processio.domain.CurrentProcessIO;
import org.greencheek.processio.domain.ProcessIO;
import org.greencheek.processio.service.usage.BasicProcessIOUsage;
import org.greencheek.processio.service.usage.ProcessIOUsage;

import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * MXBean object that holds the amount of io that the process has done;
 * for easy registration in jmx.
 * </p>
 * <p>
 * This MX object holds the time that the first IO process information was read,
 * and a ProcessIO object that stores the current and previously recorded IO Usage
 * </p>
 * <p>
 * A ProcessIOUsage object is used to provide perform calculations on the ProcessIO object
 * in order to produce information such as the amount of IO done between two sample periods
 * or since the created on this holder object.
 * </p>
 * <p>
 * This implementation is used as a mechanism to obtain the ProcessIO object from the MBeanServer
 * in an atomic unit, so that obtaining the amount of read io performed by the jvm can be obtain from
 * the MBeanServer in the same atomic unit as when the amount of write i/o is read.
 * </p>
 *
 *
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 15:54
 */
public class ProcessIOUsageHolder implements ProcessIOUsageMXBean {

    // Used to indicate the start of the jvm
    private final long startMillis;

    // Stores a reference to the service object that is used to perform
    // calculations based on the values stored in the ProcessIO object.
    private final ProcessIOUsage usage;

    // The reference to the ProcessIO object, that is updated periodically with new
    // values from the currently read/sampled read/write io for the process.
    private final AtomicReference<ProcessIO> processIORef = new AtomicReference<ProcessIO>();

    public ProcessIOUsageHolder() {
        this(System.currentTimeMillis(),new BasicProcessIOUsage());
    }

    public ProcessIOUsageHolder(ProcessIOUsage usage) {
        this(System.currentTimeMillis(), usage);
    }

    public ProcessIOUsageHolder(long initialisationMillis,ProcessIOUsage usage) {
        this.usage = usage;
        processIORef.set(new ProcessIO());
        this.startMillis = initialisationMillis;
    }

    @Override
    public ProcessIO getProcessIO() {
        return processIORef.get();
    }

    @Override
    public double getSampleTimeKbPerSecondReadIO() {
        return usage.getSampleTimeKbPerSecondReadIO(getProcessIO());
    }

    @Override
    public double getSampleTimeKbPerSecondWriteIO() {
        return usage.getSampleTimeKbPerSecondWriteIO(getProcessIO());
    }

    @Override
    public double getSampleTimeMbPerSecondReadIO() {
        return usage.getSampleTimeMbPerSecondReadIO(getProcessIO());
    }

    @Override
    public double getSampleTimeMbPerSecondWriteIO() {
        return usage.getSampleTimeMbPerSecondWriteIO(getProcessIO());
    }

    @Override
    public double getAccumulatedKbPerSecondReadIO() {
        return usage.getAccumulatedKbPerSecondReadIO(startMillis, getProcessIO());
    }

    @Override
    public double getAccumulatedKbPerSecondWriteIO() {
        return usage.getAccumulatedKbPerSecondWriteIO(startMillis, getProcessIO());
    }

    @Override
    public double getAccumulatedMbPerSecondReadIO() {
        return usage.getAccumulatedMbPerSecondReadIO(startMillis, getProcessIO());
    }

    @Override
    public double getAccumulatedMbPerSecondWriteIO() {
        return usage.getAccumulatedMbPerSecondWriteIO(startMillis, getProcessIO());
    }

    /**
     * Updates the ProcessIO object reference to contain a new reference to a ProcessIO object
     * that has been populated with new read and write io information from the given CurrentProcessIO object.
     *
     * @param io The current amount of io that has been obtained for the process.
     */
    public void setProcessIO(CurrentProcessIO io) {
        ProcessIO previousIO = getProcessIO();
        ProcessIO updatedIO = previousIO.updateCurrentValues(io);
        processIORef.set(updatedIO);
    }


}
