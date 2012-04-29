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
 *
 * User: dominictootell
 * Date: 24/04/2012
 * Time: 09:34
 */
public interface DifferenceInKBPerSecondCalculator {
    double getDifferenceInKbPerSecond(long previousTimeStamp,
                                      long previousBytesUsed,
                                      long currentTimeStamp,
                                      long currentBytesUsed);
}
