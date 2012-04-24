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


    public long getCurrentSampleTimeInMillis() {
        return currentSampleTimeInMillis;
    }

    public long getCurrentReadBytes() {
        return currentReadBytes;
    }

    public long getCurrentWriteBytes() {
        return currentWriteBytes;
    }
}
