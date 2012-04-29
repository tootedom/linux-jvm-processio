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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 15:45
 */
public class TestProcessIOUsageHolder {
    private static final double DELTA = 1e-15;

    private ProcessIOUsageMXBean usageMXBean;
    private CurrentProcessIO oneMbObject;
    private CurrentProcessIO halfMbObject;
    private CurrentProcessIO oneGbObject;
    private long startMillis = System.currentTimeMillis();

    @Before
    public void setUp() {

        usageMXBean = new ProcessIOUsageHolder(startMillis,new BasicProcessIOUsage());
        oneMbObject  = new CurrentProcessIO(1000,1024*1024,1024*1024);
        halfMbObject = new CurrentProcessIO(2000,1024*1024,1024*1024);
        oneGbObject  = new CurrentProcessIO(10000,(1024l*1024l*1024l*10l),(1024l*1024l*1024l*10l));

    }

    @Test
    public void testSetProcessIOUsage() {
        CurrentProcessIO io = new CurrentProcessIO(0,0,0);

        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(io);

        ProcessIO heldIO = usageMXBean.getProcessIO();

        assertEquals(0,heldIO.getCurrentSampleMs());
        assertEquals(0,heldIO.getCurrentSampleReadBytes());
        assertEquals(0,heldIO.getCurrentSampleWriteBytes());
        assertEquals(0,heldIO.getPreviousSampleMs());
        assertEquals(0,heldIO.getPreviousSampleReadBytes());
        assertEquals(0,heldIO.getPreviousSampleWriteBytes());

    }


    @Test
    public void testProcessIOUsageIsUpdateableAndReturnsCorrectUsage() {
        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(oneMbObject);

        assertEquals(1.0,usageMXBean.getSampleTimeMbPerSecondReadIO(),DELTA);
        assertEquals(1.0,usageMXBean.getSampleTimeMbPerSecondWriteIO(),DELTA);

        assertEquals(1024.0,usageMXBean.getSampleTimeKbPerSecondReadIO(),DELTA);
        assertEquals(1024.0,usageMXBean.getSampleTimeKbPerSecondWriteIO(),DELTA);

        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(new CurrentProcessIO(0,0,0));
        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(oneGbObject);

        assertEquals(1024.0,usageMXBean.getSampleTimeMbPerSecondReadIO(),DELTA);
        assertEquals(1024.0,usageMXBean.getSampleTimeMbPerSecondWriteIO(),DELTA);

        assertEquals(1024.0*1024.0,usageMXBean.getSampleTimeKbPerSecondReadIO(),DELTA);
        assertEquals(1024.0*1024.0,usageMXBean.getSampleTimeKbPerSecondWriteIO(),DELTA);

        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(new CurrentProcessIO(0,0,0));
        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(halfMbObject);

        assertEquals(0.5,usageMXBean.getSampleTimeMbPerSecondReadIO(),DELTA);
        assertEquals(0.5,usageMXBean.getSampleTimeMbPerSecondWriteIO(),DELTA);

        assertEquals(512.0,usageMXBean.getSampleTimeKbPerSecondReadIO(),DELTA);
        assertEquals(512.0,usageMXBean.getSampleTimeKbPerSecondWriteIO(),DELTA);
    }

    @Test
    public void testRunningTotalIOUsage() {
        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(new CurrentProcessIO(0,0,0));

        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(new CurrentProcessIO(startMillis+865919l,0l,5399044096l));

        assertEquals(5.946202459467918,usageMXBean.getAccumulatedMbPerSecondWriteIO(),DELTA);

    }

    @Test
    public void testProcessIOUsageIsCalculatedWhenUsingDefaultConstructor() {
        ((ProcessIOUsageHolder)usageMXBean).setProcessIO(oneMbObject);

        assertEquals(1.0,usageMXBean.getSampleTimeMbPerSecondReadIO(),DELTA);
        assertEquals(1.0,usageMXBean.getSampleTimeMbPerSecondWriteIO(),DELTA);

    }
}
