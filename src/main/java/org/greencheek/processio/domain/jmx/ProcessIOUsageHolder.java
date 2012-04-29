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
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 15:54
 */
public class ProcessIOUsageHolder implements ProcessIOUsageMXBean {

    private final long startMillis;
    private final ProcessIOUsage usage;
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

    public void setProcessIO(CurrentProcessIO io) {
        ProcessIO previousIO = getProcessIO();
        ProcessIO updatedIO = previousIO.updateCurrentValues(io);
        processIORef.set(updatedIO);
    }


}
