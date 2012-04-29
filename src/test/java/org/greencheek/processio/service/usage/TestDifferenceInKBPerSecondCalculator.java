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
public class TestDifferenceInKBPerSecondCalculator {

    private static final double DELTA = 1e-15;

    DifferenceInKBPerSecondCalculator differenceCalc;

    private CurrentProcessIO oneMbObject;
    private CurrentProcessIO halfMbObject;
    private CurrentProcessIO oneGbObject;

    @Before
    public void setup() {
        differenceCalc = new PrimitiveBasedDifferenceInKBPerSecondCalculator();
        oneMbObject  = new CurrentProcessIO(1000,1024*1024,1024*1024);
        halfMbObject = new CurrentProcessIO(2000,1024*1024,1024*1024);
        oneGbObject  = new CurrentProcessIO(10000,(1024l*1024l*1024l*10l),(1024l*1024l*1024l*10l));
    }

    @Test
    public void testDifferenceInKBPerSecondForOneMb() {

        assertEquals(1024.0, differenceCalc.getDifferenceInKbPerSecond(0l, 0l,oneMbObject.getCurrentSampleTimeInMillis(), oneMbObject.getCurrentWriteBytes()),DELTA);
        assertEquals(-1024.0, differenceCalc.getDifferenceInKbPerSecond(0l, (1024*1024)*2,oneMbObject.getCurrentSampleTimeInMillis(), oneMbObject.getCurrentWriteBytes()),DELTA);
    }

    @Test
    public void testDifferenceInKBPerSecondForHalfAMb() {
        assertEquals(512.0, differenceCalc.getDifferenceInKbPerSecond(0l, 0l,halfMbObject.getCurrentSampleTimeInMillis(), halfMbObject.getCurrentWriteBytes()),DELTA);
        assertEquals(-512.0, differenceCalc.getDifferenceInKbPerSecond(0l, (1024*1024)*2,halfMbObject.getCurrentSampleTimeInMillis(), halfMbObject.getCurrentWriteBytes()),DELTA);
    }

    @Test
    public void testDifferenceInKBPerSecondForOneGb() {

        assertEquals(1048576.0, differenceCalc.getDifferenceInKbPerSecond(0l, 0l,oneGbObject.getCurrentSampleTimeInMillis(), oneGbObject.getCurrentWriteBytes()),DELTA);
        // If we start at 20gb, and go down to 10gb... then that is -1gb per second for the 10 seconds
        assertEquals(-1048576.0, differenceCalc.getDifferenceInKbPerSecond(0l, (1024l*1024l*1024l)*20l,oneGbObject.getCurrentSampleTimeInMillis(), oneGbObject.getCurrentWriteBytes()),DELTA);
    }
}
