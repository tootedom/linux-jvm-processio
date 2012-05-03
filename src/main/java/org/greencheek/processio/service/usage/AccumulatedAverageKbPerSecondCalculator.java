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
 * <p>
 * Given a starting time in the past and a current timestamp, and
 * given the number representing the amount of bytes used by the process
 * between those two times; the implementing class will work out the amount of
 * Kb (Kilobytes) per second that equates to.
 * </p>
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 09:38
 * </p>
 */
public interface AccumulatedAverageKbPerSecondCalculator {
    /**
     * Returns the amount of kb per second has been processed/performed between the two time stamps
     * @param sinceTimeStamp  The first time stamp (usually the time the jvm started)
     * @param currentTimeStamp A recent time stamp (greater than the sinceTimeStamp)
     * @param currentBytes The amount of io that the process has consumed in total since the start of it
     * @return  The amount of IO that has occurred in kb per second between the two times.
     */
    double getKbPerSecond(long sinceTimeStamp, long currentTimeStamp,long currentBytes);
}
