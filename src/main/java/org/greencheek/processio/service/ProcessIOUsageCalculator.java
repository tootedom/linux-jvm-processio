package org.greencheek.processio.service;

import org.greencheek.processio.domain.ProcessIO;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 13:45
 */
public interface ProcessIOUsageCalculator {
    double getMbPerSecondReadIO(ProcessIO halfMbObject);
    double getKbPerSecondReadIO(ProcessIO oneKbObject);

    double getMbPerSecondWriteIO(ProcessIO halfMbObject);
    double getKbPerSecondWriteIO(ProcessIO oneKbObject);

    double getKbPerSecondWriteIO(long since, ProcessIO io);

    double getMbPerSecondWriteIO(long since, ProcessIO io);

    double getKbPerSecondReadIO(long since, ProcessIO io);

    double getMbPerSecondReadIO(long since, ProcessIO io);
}
