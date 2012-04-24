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
package org.greencheek.processio.service.persistence;

import org.greencheek.processio.domain.CurrentProcessIO;
import org.greencheek.processio.domain.ProcessIO;
import org.greencheek.processio.domain.jmx.ProcessIOUsageMXBean;
import org.greencheek.processio.service.BasicProcessIOUsageCalculator;
import org.greencheek.processio.service.ProcessIOUsageCalculator;
import org.greencheek.processio.service.persistence.jmx.ProcessIOUsagePersistenceViaJmx;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import static org.junit.Assert.assertEquals;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 16:20
 */
public class TestJMXPersistenceOfProcessIOUsageMXBean {

    private static final double DELTA = 1e-15;

    private MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

    private ProcessIOUsagePersistence persistenceService;

    private CurrentProcessIO halfMbObject;
    private CurrentProcessIO oneGbObject;


    @Before
    public void setUp() {
        persistenceService = new ProcessIOUsagePersistenceViaJmx(new BasicProcessIOUsageCalculator(),"org.greencheek","iousage");
        persistenceService.init();

        halfMbObject = new CurrentProcessIO(2000,1024*1024,1024*1024);
        oneGbObject  = new CurrentProcessIO(10000,(1024l*1024l*1024l*10l),(1024l*1024l*1024l*10l));
    }

    @After
    public void tearDown() {
        persistenceService.destroy();
    }

    @Test
    public void testProcessIOUsageIsPersistedInJmx() {

        persistenceService.persist(halfMbObject);

        ObjectName name = ((ProcessIOUsagePersistenceViaJmx)persistenceService).getBeanObjectName();
        ProcessIOUsageMXBean proxy = JMX.newMXBeanProxy(mbeanServer, name, ProcessIOUsageMXBean.class, false);
        ProcessIO res = proxy.getProcessIO();

        double val = proxy.getTimeSliceKbPerSecondForReadIO();

        assertEquals(512.0,val,DELTA);
        persistenceService.persist(new CurrentProcessIO(0,0,0));

        persistenceService.persist(oneGbObject);

        val = proxy.getTimeSliceKbPerSecondForReadIO();

        assertEquals(1024.0*1024.0,val,DELTA);

    }
}
