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

/**
 * Given the amount of io that has been processed on a previous date, this class calculates the amount of io that has
 * occurred since that date, and the given date; for which X amount of accumulated io has been recorded
 *
 * User: dominictootell
 * Date: 25/04/2012
 * Time: 09:31
 */
public class PrimitiveBasedDifferenceInKBPerSecondCalculator implements DifferenceInKBPerSecondCalculator {

    /**
     * <p>
     * Given two sets of data:
     * <ul>
     *   <li>A time</li>
     *   <li>The amount of bytes used by the process at that time period</li>
     * </ul>
     * </p>
     * <p>
     * The two sets of data vary such that one set represents the amount of data used
     * at that time period, previous to the amount of bytes used at the time period in the
     * future presented by the second data set
     * </p>
     * <p>
     * Given the sets of data the implementing class will return in kb (kilobytes), per second,
     * the amount of io that has occurred between the two time periods
     * </p>
     * <p>
     *     A prerequisite is that the previousTimeStamp by less than the currentTimeStamp
     * </p>
     *
     * @param previousTimeStamp The time of the previous bytes recorded
     * @param previousBytesUsed The amount of bytes used at that previous point in time
     *
     * @param currentTimeStamp  The time of the current reading of the bytes.
     * @param currentBytesUsed  The amount of byte used at the current point in time.
     *
     *
     */
    @Override
    public double getDifferenceInKbPerSecond(long previousTimeStamp, long previousBytesUsed, long currentTimeStamp, long currentBytesUsed) {
        if (currentTimeStamp <= previousTimeStamp) return 0.0;

        //if(currBytes <= prevBytes) return 0.0;

        long diffMs = currentTimeStamp - previousTimeStamp;

        double diffBytes = (currentBytesUsed - previousBytesUsed) / 1024.0;

        double diffMillisInSeconds = diffMs / 1000.0;

        return diffBytes / diffMillisInSeconds;

    }
}
