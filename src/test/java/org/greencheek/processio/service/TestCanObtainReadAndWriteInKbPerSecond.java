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
import org.greencheek.processio.service.usage.BasicProcessIOUsage;
import org.greencheek.processio.service.usage.ProcessIOUsage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 13:18
 */
public class TestCanObtainReadAndWriteInKbPerSecond {

    private static final double DELTA = 1e-15;

    private ProcessIO oneMbObject;
    private ProcessIO halfMbObject;
    private ProcessIO oneKbObject;
    private ProcessIO oneGbReadAndHalfGbWriteObject;
    private ProcessIOUsage processIOUsage;

    @Before
    public void setUp() {
        oneMbObject  = new ProcessIO(0,0,0,1000,1024*1024,1024*1024);
        halfMbObject = new ProcessIO(0,0,0,2000,1024*1024,1024*1024);
        oneKbObject  = new ProcessIO(0,0,0,1000,1024,1024);
        oneGbReadAndHalfGbWriteObject  = new ProcessIO(0,0,0,10000,(1024l*1024l*1024l*10l),(1024l*1024l*1024l*5l));

        processIOUsage = new BasicProcessIOUsage();
    }

    @Test
    public void testOneMbPerSecondIsReturnedByCalculator() {
        double value = processIOUsage.getSampleTimeMbPerSecondReadIO(oneMbObject);
        assertEquals(1.0,value,DELTA);
        value = processIOUsage.getSampleTimeMbPerSecondWriteIO(oneMbObject);
        assertEquals(1.0,value,DELTA);
    }

    @Test
    public void testHalfMbPerSecondIsReturnedByCalculator() {
        double value = processIOUsage.getSampleTimeMbPerSecondReadIO(halfMbObject);
        assertEquals(0.5,value,DELTA);
        value = processIOUsage.getSampleTimeMbPerSecondWriteIO(halfMbObject);
        assertEquals(0.5,value,DELTA);
    }


    @Test
    public void testOneKBPerSecondIsReturnedByCalculator() {

        double value = processIOUsage.getSampleTimeKbPerSecondReadIO(oneKbObject);
        assertEquals(1.0,value,DELTA);

        value = processIOUsage.getSampleTimeKbPerSecondWriteIO(oneKbObject);
        assertEquals(1.0,value,DELTA);
    }


    @Test
    public void testOneKBPerSecondsIsReturnedInMBByCalculator() {
        double value = processIOUsage.getSampleTimeMbPerSecondReadIO(oneKbObject);
        assertEquals(0.0009765625,value,DELTA);

        value = processIOUsage.getSampleTimeMbPerSecondWriteIO(oneKbObject);
        assertEquals(0.0009765625,value,DELTA);
    }

    @Test
    public void testOneGBPerSecondsIsReturnedInMBByCalculator() {
        double value = processIOUsage.getSampleTimeMbPerSecondReadIO(oneGbReadAndHalfGbWriteObject);
        assertEquals(1024.0,value,DELTA);

        value = processIOUsage.getSampleTimeKbPerSecondReadIO(oneGbReadAndHalfGbWriteObject);
        assertEquals(1048576.0,value,DELTA);
    }

}
