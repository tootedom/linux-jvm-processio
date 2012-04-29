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
 * User: dominictootell
 * Date: 29/04/2012
 * Time: 14:32
 */
public class PrimitiveBasedAccumulatedAverageKBPerSecondCalculator implements AccumulatedAverageKbPerSecondCalculator {

    @Override
    /**
     * Calculates the average amount of kb per second that has occurred given
     *
     * @param sinceTimeStamp The time from which the counting of bytes was performed
     * @param currentTimeStamp The current time at which the passed in byte count was read
     * @param currentBytes The current bytes that have been record; for which we which to deduce the amount of kb
     *                     per second has been produced
     */
    public double getKbPerSecond(long sinceTimeStamp, long currentTimeStamp, long currentBytes) {
        if (currentTimeStamp <= sinceTimeStamp) return 0.0;

        long diffMs = currentTimeStamp - sinceTimeStamp;

        double diffBytes = (currentBytes) / 1024.0;

        double diffMillisInSeconds = diffMs / 1000.0;

        return diffBytes / diffMillisInSeconds;

    }
}
