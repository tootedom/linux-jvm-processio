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
package org.greencheek.processio.service.scheduler;

import org.greencheek.processio.domain.CurrentProcessIO;
import org.greencheek.processio.domain.ProcessIO;
import org.greencheek.processio.service.io.FileSystemProcIOProcessIOReader;
import org.greencheek.processio.service.io.ProcessIOReader;
import org.greencheek.processio.service.persistence.ProcessIOUsagePersistence;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;


/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 18:15
 */
public class TestProcessIOScheduler {

    private Mockery context = new Mockery();
    private ProcessIOScheduler scheduler;
    private ProcessIOReader ioReader;
    private ProcessIOUsagePersistence persistence;
    private long frequencyInMillis = 1000;


    @Before
    public void setUp() {
        URL ioLocation = ClassLoader.getSystemResource("testIO.txt");
        File fileIOLocation = new File(ioLocation.getFile());
        persistence =  context.mock(ProcessIOUsagePersistence.class);

        ioReader = new FileSystemProcIOProcessIOReader(fileIOLocation);
        scheduler = new ScheduledExecutorServiceProcessIOScheduler(ioReader,persistence,frequencyInMillis);
    }

    @After
    public void tearDown() {
        scheduler.stop();
    }

    @Test
    public void testSchedulerReadsProcessIOValuesAndCallsPersistenceLayer() {

        context.checking(new Expectations() {{
            atLeast(2).of(persistence).persist(with(aNonNull(CurrentProcessIO.class)));
        }});

        scheduler.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // verify
        context.assertIsSatisfied();
    }
}
