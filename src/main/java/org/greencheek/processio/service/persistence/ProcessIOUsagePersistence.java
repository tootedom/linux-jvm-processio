package org.greencheek.processio.service.persistence;

import org.greencheek.processio.domain.CurrentProcessIO;

/**
 * <p>
 * Saves to somewhere, currently the default implementation is JMX, the CurrentProcessIO that
 * the process has performed (accumulated value) since the process started.
 * </p>
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 16:32
 * </p>
 */
public interface ProcessIOUsagePersistence {

    /**
     * Can be used to initial the persistence mechanism
     */
    void init();

    /**
     * Used to shutdown the persistence mechanism, so that it can clean up any resource.
     * For example unregistering any ProcessIO object from the jmx mbean server
     */
    void destroy();

    /**
     * Save the current status of the ProcessIO object
     * @param io
     */
    void persist(CurrentProcessIO io);
}
