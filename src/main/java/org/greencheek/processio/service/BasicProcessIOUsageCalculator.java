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
package org.greencheek.processio.service;

import org.greencheek.processio.domain.ProcessIO;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 13:46
 */
public class BasicProcessIOUsageCalculator implements ProcessIOUsageCalculator {

    public static void main(String[] args) {
        System.out.println(""+getKbPerSecond(0,3000,0,8192));
    }

    @Override
    public double getMbPerSecondReadIO(ProcessIO io) {
        return (getKbPerSecondForReadIO(io) / 1024);
    }

    @Override
    public double getKbPerSecondReadIO(ProcessIO io) {
       return getKbPerSecondForReadIO(io);
    }

    @Override
    public double getMbPerSecondWriteIO(ProcessIO io) {
        return (getKbPerSecondForWriteIO(io) / 1024);
    }

    @Override
    public double getKbPerSecondWriteIO(ProcessIO io) {
        return getKbPerSecondForWriteIO(io);
    }

    @Override
    public double getKbPerSecondWriteIO(long since,ProcessIO io) {
        return getKbPerSecondForWriteIO(since,io);
    }

    @Override
    public double getMbPerSecondWriteIO(long since,ProcessIO io) {
        return (getKbPerSecondForWriteIO(since,io) / 1024);
    }

    @Override
    public double getKbPerSecondReadIO(long since,ProcessIO io) {
        return getKbPerSecondForReadIO(since,io);
    }

    @Override
    public double getMbPerSecondReadIO(long since,ProcessIO io) {
        return (getKbPerSecondForReadIO(since,io) / 1024);
    }

    private static double getKbPerSecondForWriteIO(long since,ProcessIO io) {
        return getTotalKbPerSecond(since,io.getCurrentSampleMs(),
                io.getCurrentSampleWriteBytes());
    }


    private static double getKbPerSecondForWriteIO(ProcessIO io) {
        return getKbPerSecond(io.getPreviousSampleMs(),io.getCurrentSampleMs(),
                io.getPreviousSampleWriteBytes(),io.getCurrentSampleWriteBytes());
    }


    private static double getKbPerSecondForReadIO(long since, ProcessIO io) {
        return getTotalKbPerSecond(since,io.getCurrentSampleMs(),
                io.getCurrentSampleReadBytes());
    }

    private static double getKbPerSecondForReadIO(ProcessIO io) {
        return getKbPerSecond(io.getPreviousSampleMs(),io.getCurrentSampleMs(),
                              io.getPreviousSampleReadBytes(),io.getCurrentSampleReadBytes());
    }

    private static double getTotalKbPerSecond(long prevMillis, long currMillis, long currBytes) {
        if(currMillis <= prevMillis) return 0.0;

        long diffMs = currMillis - prevMillis;

        double diffBytes = (currBytes)/1024.0;

        double diffMillisInSeconds = diffMs/1000.0;

        return diffBytes/diffMillisInSeconds;
    }

    private static double getKbPerSecond(long prevMillis, long currMillis, long prevBytes, long currBytes) {
       if(currMillis <= prevMillis) return 0.0;

       if(currBytes <= prevBytes) return 0.0;

       long diffMs = currMillis - prevMillis;

       double diffBytes = (currBytes - prevBytes)/1024.0;

       double diffMillisInSeconds = diffMs/1000.0;

       return diffBytes/diffMillisInSeconds;
    }
}
