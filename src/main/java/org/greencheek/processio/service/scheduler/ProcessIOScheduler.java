package org.greencheek.processio.service.scheduler;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 18:38
 */
public interface ProcessIOScheduler {

    public static final long DEFAULT_FREQUENCY_IN_MILLIS = (1000 * 60);

    void stop();
    void start(long frequencyInMillis);
    void start();
}
