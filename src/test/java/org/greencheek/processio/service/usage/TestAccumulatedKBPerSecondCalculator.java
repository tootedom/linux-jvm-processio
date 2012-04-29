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

import org.greencheek.processio.domain.CurrentProcessIO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: dominictootell
 * Date: 25/04/2012
 * Time: 09:32
 */
public class TestAccumulatedKBPerSecondCalculator {

    private static final double DELTA = 1e-15;

    AccumulatedAverageKbPerSecondCalculator differenceCalc;

    private CurrentProcessIO oneMbObject;
    private CurrentProcessIO halfMbObject;
    private CurrentProcessIO oneGbObject;

    @Before
    public void setup() {
        differenceCalc = new PrimitiveBasedAccumulatedAverageKBPerSecondCalculator();
        oneMbObject  = new CurrentProcessIO(1000,1024*1024,1024*1024);
        halfMbObject = new CurrentProcessIO(2000,1024*1024,1024*1024);
        oneGbObject  = new CurrentProcessIO(10000,(1024l*1024l*1024l*10l),(1024l*1024l*1024l*10l));
    }

    @Test
    public void testCannotGetAverageKBPerSecondWhenPreviousTimeStampIsGreaterThanCurrent() {
        assertEquals(0.0,differenceCalc.getKbPerSecond(2000,oneMbObject.getCurrentSampleTimeInMillis(),oneMbObject.getCurrentWriteBytes()),DELTA);
    }

    @Test
    public void testAccumulatedKBPerSecondForOneMb() {

        assertEquals(1024.0, differenceCalc.getKbPerSecond(0,oneMbObject.getCurrentSampleTimeInMillis(),oneMbObject.getCurrentReadBytes()),DELTA);
        assertEquals(2048.0, differenceCalc.getKbPerSecond(500,oneMbObject.getCurrentSampleTimeInMillis(),oneMbObject.getCurrentReadBytes()),DELTA);
    }

    @Test
    public void testDifferenceInKBPerSecondForHalfAMb() {
        assertEquals(512.0, differenceCalc.getKbPerSecond(0,halfMbObject.getCurrentSampleTimeInMillis(),halfMbObject.getCurrentReadBytes()),DELTA);
    }

    @Test
    public void testDifferenceInKBPerSecondForOneGb() {
        assertEquals(1048576.0, differenceCalc.getKbPerSecond(0,oneGbObject.getCurrentSampleTimeInMillis(),oneGbObject.getCurrentReadBytes()),DELTA);
    }
}
