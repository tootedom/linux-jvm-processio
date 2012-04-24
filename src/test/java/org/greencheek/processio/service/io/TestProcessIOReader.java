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
package org.greencheek.processio.service.io;

import org.greencheek.processio.domain.CurrentProcessIO;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 17:23
 */
public class TestProcessIOReader{

    private ProcessIOReader ioReader;
    private ProcessIOReader nonExistingIOReader;

    @Before
    public void setUp() {
        URL ioLocation = ClassLoader.getSystemResource("testIO.txt");
        File fileIOLocation = new File(ioLocation.getFile());
        ioReader = new FileSystemProcIOProcessIOReader(fileIOLocation);

        File nonExistentIOFileIOLocation = new File("testIO.txt.does.not.exist");
        nonExistingIOReader = new FileSystemProcIOProcessIOReader(nonExistentIOFileIOLocation);
    }

    @Test
    public void testProcessIOIsReturnedFromIOReader() {

        CurrentProcessIO currentIO = ioReader.getCurrentProcessIO();

        assertEquals(18790563840l,currentIO.getCurrentReadBytes());
        assertEquals(2250752000l,currentIO.getCurrentWriteBytes());
    }

    @Test
    public void testProcessIOIsReturnedFromIOReaderWhenFileDoesNotExist() {

        CurrentProcessIO currentIO = nonExistingIOReader.getCurrentProcessIO();

        assertEquals(Long.MIN_VALUE,currentIO.getCurrentReadBytes());
        assertEquals(Long.MIN_VALUE,currentIO.getCurrentWriteBytes());
    }
}
