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
 * Uses a single threaded executor service the contains a runnable task that simply:
 * <ul>
 *     <li>calls {@link org.greencheek.processio.service.io.ProcessIOReader#getCurrentProcessIO()}</li>
 *     <li>asks for the resulting {@link CurrentProcessIO} to be persisted by the implementation of {@link ProcessIOUsagePersistence}</li>
 *     <li>The {@link CurrentProcessIO} is only requested to be persisted if is not the {@link ProcessIOReader#NON_READABLE_PROCESS_IO} instance</li>
 * </ul>
 *
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
    private volatile boolean stopped = false;

    public ScheduledExecutorServiceProcessIOScheduler(ProcessIOReader ioReader, ProcessIOUsagePersistence persistence) {
        this(ioReader,persistence,DEFAULT_FREQUENCY_IN_MILLIS);
    }

    public ScheduledExecutorServiceProcessIOScheduler(ProcessIOReader ioReader, ProcessIOUsagePersistence persistence, long frequencyInMillis) {

        this.reader = ioReader;
        this.persistence = persistence;
        this.frequencyInMillis = frequencyInMillis;
    }


    /**
     * Can only be called once.  This stops the executor from running; shutting it down
     * In order for the executor to re-run, and new instance of the class should be created.
     */
    @Override
    public synchronized void stop() {
        if(stopped) return;
        try {
            if(submittedTask!=null) {
                submittedTask.cancel(true);
            }
            submittedTask = null;

            scheduler.shutdownNow();
        } finally {
            stopped = true;
        }
    }

    /**
     * Starts the process of periodically obtaining the io usage of the current jvm processing, and
     * asking for that to be persisted.
     *
     * @param frequencyInMillis The frequency in millis to run the process of obtaining process io info
     */
    public synchronized void start(long frequencyInMillis) {
        if(!stopped) {
            submittedTask =  scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("Obtaining process io");
                        CurrentProcessIO io = reader.getCurrentProcessIO();
                        if(io != ProcessIOReader.NON_READABLE_PROCESS_IO) persistence.persist(io);
                        else log.warn("Unable to read process io");
                    } catch (Exception e) {
                        log.error("Exception occurred whilst reading current process io",e);
                    }
                }
            },0,frequencyInMillis, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void start() {
        start(this.frequencyInMillis);
    }

    /**
     * Makes sure that the thread in the Scheduled Executor don't stop the JVM from exiting
     */
    private static class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        }
    }
}
