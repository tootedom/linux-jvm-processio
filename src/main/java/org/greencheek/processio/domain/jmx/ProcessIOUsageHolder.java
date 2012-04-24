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
import org.greencheek.processio.service.BasicProcessIOUsageCalculator;
import org.greencheek.processio.service.ProcessIOUsageCalculator;

import java.util.concurrent.atomic.AtomicReference;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 15:54
 */
public class ProcessIOUsageHolder implements ProcessIOUsageMXBean {

    private final long startMillis;
    private final ProcessIOUsageCalculator usageCalculator;
    private final AtomicReference<ProcessIO> processIORef = new AtomicReference<ProcessIO>();

    public ProcessIOUsageHolder() {
        this(System.currentTimeMillis(),new BasicProcessIOUsageCalculator());
    }

    public ProcessIOUsageHolder(ProcessIOUsageCalculator usageCalculator) {
        this(System.currentTimeMillis(),usageCalculator);
    }

    public ProcessIOUsageHolder(long initialisationMillis,ProcessIOUsageCalculator usageCalculator) {
        this.usageCalculator = usageCalculator;
        processIORef.set(new ProcessIO());
        this.startMillis = initialisationMillis;
    }

    @Override
    public ProcessIO getProcessIO() {
        return processIORef.get();
    }

    @Override
    public double getTimeSliceKbPerSecondForReadIO() {
        return usageCalculator.getKbPerSecondReadIO(getProcessIO());
    }

    @Override
    public double getTimeSliceKbPerSecondForWriteIO() {
        return usageCalculator.getKbPerSecondWriteIO(getProcessIO());
    }

    @Override
    public double getTimeSliceMbPerSecondForReadIO() {
        return usageCalculator.getMbPerSecondReadIO(getProcessIO());
    }

    @Override
    public double getTimeSliceMbPerSecondForWriteIO() {
        return usageCalculator.getMbPerSecondWriteIO(getProcessIO());
    }

    @Override
    public double getRunningTimeKbPerSecondForReadIO() {
        return usageCalculator.getKbPerSecondReadIO(startMillis,getProcessIO());
    }

    @Override
    public double getRunningTimeKbPerSecondForWriteIO() {
        return usageCalculator.getKbPerSecondWriteIO(startMillis,getProcessIO());
    }

    @Override
    public double getRunningTimeMbPerSecondForReadIO() {
        return usageCalculator.getMbPerSecondReadIO(startMillis,getProcessIO());
    }

    @Override
    public double getRunningTimeMbPerSecondForWriteIO() {
        return usageCalculator.getMbPerSecondWriteIO(startMillis,getProcessIO());
    }

    public void setProcessIO(CurrentProcessIO io) {
        ProcessIO previousIO = getProcessIO();
        ProcessIO updatedIO = previousIO.updateCurrentValues(io);
        processIORef.set(updatedIO);
    }


}
