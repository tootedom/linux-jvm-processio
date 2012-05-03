package org.greencheek.processio.service.scheduler;

/**
 * <p>
 * Interface that abstracts the way by which the scheduling of the
 * continuous acquistion of process io information is obtained.
 * </p>
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 18:38
 * </p>
 */
public interface ProcessIOScheduler {

    // Default check every minute
    public static final long DEFAULT_FREQUENCY_IN_MILLIS = (1000 * 60);

    /**
     * Start the scheduler; it should run with the above default frequency.
     */
    void stop();

    /**
     * Start the scheduler with the given frequency
     * @param frequencyInMillis The frequency in millis to run the process of obtaining process io info
     */
    void start(long frequencyInMillis);

    /**
     * Stop the scheduler, i.e. shut it down
     */
    void start();
}
