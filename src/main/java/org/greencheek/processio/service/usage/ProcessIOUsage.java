package org.greencheek.processio.service.usage;

import org.greencheek.processio.domain.ProcessIO;

/**
 * <p>
 * Returns the usage information for the given {@link ProcessIO} object, returning the amoutn of kb or mb that
 * has been generated.  This usage is either:
 * <ul>
 *     <li>The average usage in kb or mb that has occurred between the sampling times (the time the previous and current io info was obtained)</li>
 *     <li>The average usage in kb or mb that has occurred Since the start of a given time in millis (i.e. the jvm startup)</li>
 * </ul>
 * </p>
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 13:45
 * </p>
 */
public interface ProcessIOUsage {
    /**
     * Returns the average amount of io that has been processed since the two time periods
     * recorded in the {@link ProcessIO} object, for the given read io data for the two periods
     * {@link org.greencheek.processio.domain.ProcessIO#getPreviousSampleReadBytes()}  and
     * {@link org.greencheek.processio.domain.ProcessIO#getCurrentSampleReadBytes()}
     *
     * @param io The io that has been recorded.
     * @return The mb (megabytes) per second that has occurred between the two periods
     */
    double getSampleTimeMbPerSecondReadIO(ProcessIO ioUsage);

    /**
     * Returns the average amount of io that has been processed since the two time periods
     * recorded in the {@link ProcessIO} object, for the given read io data for the two periods
     * {@link org.greencheek.processio.domain.ProcessIO#getPreviousSampleReadBytes()}  and
     * {@link org.greencheek.processio.domain.ProcessIO#getCurrentSampleReadBytes()}
     *
     * @param io The io that has been recorded.
     * @return The kb (kilobytes) per second that has occurred between the two periods
     */
    double getSampleTimeKbPerSecondReadIO(ProcessIO ioUsage);

    /**
     * Returns the average amount of io that has been processed since the two time periods
     * recorded in the {@link ProcessIO} object, for the given read io data for the two periods
     * {@link org.greencheek.processio.domain.ProcessIO#getPreviousSampleWriteBytes()}  and
     * {@link org.greencheek.processio.domain.ProcessIO#getCurrentSampleWriteBytes()}
     *
     * @param io The io that has been recorded.
     * @return The mb (megabytes) per second that has occurred between the two periods
     */
    double getSampleTimeMbPerSecondWriteIO(ProcessIO ioUsage);

    /**
     * Returns the average amount of io that has been processed since the two time periods
     * recorded in the {@link ProcessIO} object, for the given read io data for the two periods
     * {@link org.greencheek.processio.domain.ProcessIO#getPreviousSampleWriteBytes()}  and
     * {@link org.greencheek.processio.domain.ProcessIO#getCurrentSampleWriteBytes()}
     *
     * @param io The io that has been recorded.
     * @return The kb (kilobytes) per second that has occurred between the two periods
     */
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
