package org.greencheek.processio.service.usage;

/**
 * <p>
 * Given two sets of data:
 * <ul>
 *     <li>A time</li>
 *     <li>The amount of bytes used by the process at that time period</li>
 * </ul>
 * </p>
 * <p>
 * The two sets of data vary such that one set represents the amount of data used
 * at that time period, previous to the amount of bytes used at the time period in the
 * future presented by the second data set
 * </p>
 * <p>
 * Given the sets of data the implementing class will return in kb (kilobytes), per second,
 * the amount of io that has occurred between the two time periods
 * </p>
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 09:34
 * </p>
 */
public interface DifferenceInKBPerSecondCalculator {

    /**
     * Returns the amount of io, in kb per second, that has occurred between the two given timestamps.
     * The amount of io recorded at those two times is passed to the method, and the difference between them
     * in kb per second is returned.
     *
     * @param previousTimeStamp The previous timestamp in millis
     * @param previousBytesUsed The amount of io used by the process at that given time
     * @param currentTimeStamp The current timestamp in millis
     * @param currentBytesUsed The amount of io used by the process at the given recent (current) time
     * @return The difference in kb per second
     */
    double getDifferenceInKbPerSecond(long previousTimeStamp,
                                      long previousBytesUsed,
                                      long currentTimeStamp,
                                      long currentBytesUsed);
}
