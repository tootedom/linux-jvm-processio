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
package org.greencheek.processio.service.usage;

import org.greencheek.processio.domain.ProcessIO;

/**
 * <p>
 * This implementation uses both {@link DifferenceInKBPerSecondCalculator} and {@link AccumulatedAverageKbPerSecondCalculator}
 * to return to the caller the amount of io that has been processed between two sample dates, and also since a certain
 * given time (usually the start of the process)
 * </p>
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 13:46
 * </p>
 */
public class BasicProcessIOUsage implements ProcessIOUsage {

    private final DifferenceInKBPerSecondCalculator differenceCalcuator;
    private final AccumulatedAverageKbPerSecondCalculator accummulationCalculator;

    public BasicProcessIOUsage() {
        this(new PrimitiveBasedDifferenceInKBPerSecondCalculator(),new PrimitiveBasedAccumulatedAverageKBPerSecondCalculator());
    }

    public BasicProcessIOUsage(DifferenceInKBPerSecondCalculator differenceInKBPerSecondCalculator,
                               AccumulatedAverageKbPerSecondCalculator accumulatedAverageKBPerSecondCalculator) {
        this.differenceCalcuator = differenceInKBPerSecondCalculator;
        this.accummulationCalculator = accumulatedAverageKBPerSecondCalculator;
    }

    @Override
    public double getSampleTimeMbPerSecondReadIO(ProcessIO io) {
        return (getKbPerSecondForReadIO(io) / 1024);
    }

    @Override
    public double getSampleTimeKbPerSecondReadIO(ProcessIO io) {
       return getKbPerSecondForReadIO(io);
    }

    @Override
    public double getSampleTimeMbPerSecondWriteIO(ProcessIO io) {
        return (getKbPerSecondForWriteIO(io) / 1024);
    }

    @Override
    public double getSampleTimeKbPerSecondWriteIO(ProcessIO io) {
        return getKbPerSecondForWriteIO(io);
    }

    @Override
    public double getAccumulatedKbPerSecondWriteIO(long since, ProcessIO io) {
        return getKbPerSecondForWriteIO(since,io);
    }

    @Override
    public double getAccumulatedMbPerSecondWriteIO(long since, ProcessIO io) {
        return (getKbPerSecondForWriteIO(since,io) / 1024);
    }

    @Override
    public double getAccumulatedKbPerSecondReadIO(long since, ProcessIO io) {
        return getKbPerSecondForReadIO(since, io);
    }

    @Override
    public double getAccumulatedMbPerSecondReadIO(long since, ProcessIO io) {
        return (getKbPerSecondForReadIO(since, io) / 1024);
    }

    /**
     * Simple internal method for accessing the given ProcessIO object to obtain the
     * current sample millis, and the write io for the associated millis.
     *
     * @param since The time since io recording started, from which we are to calculate the average io
     *              per second up until the current time.
     * @param io    The current process IO
     * @return the amount of kb per second of write io that is occurring
     */
    private double getKbPerSecondForWriteIO(long since,ProcessIO io) {
        return getTotalKbPerSecond(since, io.getCurrentSampleMs(),
                io.getCurrentSampleWriteBytes());
    }

    /**
     * Simple internal method for accessing the given ProcessIO object to obtain the
     * current sample millis, and the write io for the associated millis.  And the previous recorded io, and the time
     * that io value was recorded.
     *
     * @param io    The current process IO
     * @return the amount of kb per second of write io that occurred in a given time frame (since the previously
     *         recorded value)
     */
    private double getKbPerSecondForWriteIO(ProcessIO io) {
        return getKbPerSecond(io.getPreviousSampleMs(),io.getCurrentSampleMs(),
                io.getPreviousSampleWriteBytes(),io.getCurrentSampleWriteBytes());
    }

    /**
     * Simple internal method for accessing the given ProcessIO object to obtain the
     * current sample millis, and the read io for the associated millis.
     *
     * @param since The time since io recording started, from which we are to calculate the average io
     *              per second up until the current time.
     * @param io    The current process IO
     * @return the amount of kb per second of read io that is occurring
     */
    private  double getKbPerSecondForReadIO(long since, ProcessIO io) {
        return getTotalKbPerSecond(since, io.getCurrentSampleMs(),
                io.getCurrentSampleReadBytes());
    }

    /**
     * Simple internal method for accessing the given ProcessIO object to obtain the
     * current sample millis, and the read io for the associated millis.  And the previous recorded io, and the time
     * that io value was recorded.
     *
     * @param io    The current process IO
     * @return the amount of kb per second of read io that occurred in a given time frame (since the previously
     *         recorded value)
     */
    private  double getKbPerSecondForReadIO(ProcessIO io) {
        return getKbPerSecond(io.getPreviousSampleMs(),io.getCurrentSampleMs(),
                              io.getPreviousSampleReadBytes(),io.getCurrentSampleReadBytes());
    }


    private double getTotalKbPerSecond(long prevMillis, long currMillis, long currBytes) {
       return this.accummulationCalculator.getKbPerSecond(prevMillis,currMillis,currBytes);
    }

    /**
     * Makes sure that the process's io usage can only increase, not decrease.  I.E. when a process starts,
     * the counter value for the amount of read or write io that process has performed will only ever go up in value.
     * The values are always incrementing.  I.e. If a process started 2 mins ago, and wrote 5mb, and then for a further
     * 2 mins it performed no io; the process will have still written 5mb since that process started..
     *
     * @param prevMillis  The previous time stamp at which the process' io was read
     * @param currMillis  The current time stamp at which the process' current io was read
     * @param prevBytes   The amount of io that was recorded previously.
     * @param currBytes   The amount of io that is recorded now.
     * @return the amount of io, in kb per second, that has occurred in a given time frame (since the previously
     *         recorded value)
     */
    private double getKbPerSecond(long prevMillis, long currMillis, long prevBytes, long currBytes) {

       if(currBytes <= prevBytes) return 0.0;

       return this.differenceCalcuator.getDifferenceInKbPerSecond(prevMillis,prevBytes,currMillis,currBytes);


    }
}
