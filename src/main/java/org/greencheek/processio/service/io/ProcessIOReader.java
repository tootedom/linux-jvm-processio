package org.greencheek.processio.service.io;

import org.greencheek.processio.domain.CurrentProcessIO;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 17:41
 */
public interface ProcessIOReader {

    /**
     * Represents that the IO for the current process could not be read/obtained
     */
    public static final CurrentProcessIO NON_READABLE_PROCESS_IO =  new CurrentProcessIO(-1l,Long.MIN_VALUE,Long.MIN_VALUE);

    CurrentProcessIO getCurrentProcessIO();
}
