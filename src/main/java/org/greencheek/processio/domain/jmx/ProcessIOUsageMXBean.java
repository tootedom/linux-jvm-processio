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
package org.greencheek.processio.domain.jmx;

import org.greencheek.processio.domain.ProcessIO;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 15:53
 */
public interface ProcessIOUsageMXBean {
    ProcessIO getProcessIO();
    double getTimeSliceKbPerSecondForReadIO();
    double getTimeSliceKbPerSecondForWriteIO();
    double getTimeSliceMbPerSecondForReadIO();
    double getTimeSliceMbPerSecondForWriteIO();

    double getRunningTimeKbPerSecondForReadIO();
    double getRunningTimeKbPerSecondForWriteIO();
    double getRunningTimeMbPerSecondForReadIO();
    double getRunningTimeMbPerSecondForWriteIO();

}
