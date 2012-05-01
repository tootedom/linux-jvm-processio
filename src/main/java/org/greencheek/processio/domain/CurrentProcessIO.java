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
package org.greencheek.processio.domain;

/**
 * Stores the current io that has been recorded for the given process.
 * Stores the time the io numbers were obtained, the amount of read io the process has done,
 * and the amount of write io.
 *
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 17:42
 */
public class CurrentProcessIO {

    private final long currentSampleTimeInMillis;
    private final long currentReadBytes;
    private final long currentWriteBytes;

    public CurrentProcessIO(long millis,long readBytes, long writeBytes) {
       currentSampleTimeInMillis = millis;
       currentReadBytes = readBytes;
       currentWriteBytes = writeBytes;
    }

    /**
     * Returns the time in millis the io information was obtained.
     * @return
     */
    public long getCurrentSampleTimeInMillis() {
        return currentSampleTimeInMillis;
    }

    /**
     * Returns the amount of read io that the process has done, as captured at {@link #getCurrentSampleTimeInMillis()}
     * @return
     */
    public long getCurrentReadBytes() {
        return currentReadBytes;
    }

    /**
     * Returns the amount write io that the process has done, as captured at {@link #getCurrentSampleTimeInMillis()}
     * @return
     */
    public long getCurrentWriteBytes() {
        return currentWriteBytes;
    }
}
