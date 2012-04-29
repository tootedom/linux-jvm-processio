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
import org.greencheek.processio.service.io.ProcessIOReader;
import org.greencheek.processio.service.persistence.ProcessIOUsagePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 18:38
 */
public class ScheduledExecutorServiceProcessIOScheduler implements ProcessIOScheduler {


    private static final Logger log = LoggerFactory.getLogger(ScheduledExecutorServiceProcessIOScheduler.class);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    private final ProcessIOReader reader;
    private final ProcessIOUsagePersistence persistence;
    private final long frequencyInMillis;
    private volatile ScheduledFuture<?> submittedTask;

    public ScheduledExecutorServiceProcessIOScheduler(ProcessIOReader ioReader, ProcessIOUsagePersistence persistence) {
        this(ioReader,persistence,DEFAULT_FREQUENCY_IN_MILLIS);
    }

    public ScheduledExecutorServiceProcessIOScheduler(ProcessIOReader ioReader, ProcessIOUsagePersistence persistence, long frequencyInMillis) {

        this.reader = ioReader;
        this.persistence = persistence;
        this.frequencyInMillis = frequencyInMillis;
    }


    @Override
    public void stop() {
        if(submittedTask!=null) {
            submittedTask.cancel(true);
        }

        scheduler.shutdownNow();

    }

    public void start(long frequencyInMillis) {
        submittedTask =  scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    CurrentProcessIO io = reader.getCurrentProcessIO();
                    persistence.persist(io);
                } catch (Exception e) {
                    log.error("Exception occurred whilst reading current process io",e);
                }
            }
        },0,frequencyInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public void start() {
        start(this.frequencyInMillis);
    }

    private static class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        }
    }
}
