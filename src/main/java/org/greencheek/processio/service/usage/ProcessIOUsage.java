package org.greencheek.processio.service.usage;

import org.greencheek.processio.domain.ProcessIO;

/**
 *
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 13:45
 */
public interface ProcessIOUsage {
    double getSampleTimeMbPerSecondReadIO(ProcessIO ioUsage);
    double getSampleTimeKbPerSecondReadIO(ProcessIO ioUsage);

    double getSampleTimeMbPerSecondWriteIO(ProcessIO ioUsage);
    double getSampleTimeKbPerSecondWriteIO(ProcessIO ioUsage);

    /**
     * Returns the average amount of io that has been processed since the given
     * timestamp, in KB per second.  This is specifically for the amount of Write IO
     * that a process has produced
     *
     * @param since The time since IO usage was being recorded
     * @param io The io that has been recorded.
     * @return The kb (kilobytes) per second
     */
    double getAccumulatedKbPerSecondWriteIO(long since, ProcessIO io);

    /**
     * Returns the average amount of io that has been processed since the given
     * timestamp, in MB per second.  The is specifically the amount of write IO
     * that a process has produced
     *
     * @param since The time since IO usage was being recorded
     * @param io The io that has been recorded.
     * @return The mb (megabytes) per second
     */
    double getAccumulatedMbPerSecondWriteIO(long since, ProcessIO io);

    /**
     * Returns the average amount of io that has been processed since the given
     * timestamp, in KB per second. This is specifically the amoutn of read IO that
     * a process has produced
     *
     * @param since The time since IO usage was being recorded
     * @param io The io that has been recorded.
     * @return The kb (kilobytes) per second
     */
    double getAccumulatedKbPerSecondReadIO(long since, ProcessIO io);

    /**
     * Returns the average amount of io that has been processed since the given
     * timestamp, in MB per second.  This is specifically the amount of read IO that
     * a process has produced.
     *
     * @param since The time since IO usage was being recorded
     * @param io The io that has been recorded.
     * @return The mb (megabytes) per second
     */
    double getAccumulatedMbPerSecondReadIO(long since, ProcessIO io);


}
