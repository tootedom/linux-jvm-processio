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
import org.greencheek.processio.service.io.ProcessIOReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 17:46
 */
public class FileSystemProcIOProcessIOReader implements ProcessIOReader {

    private static final Logger log = LoggerFactory.getLogger(FileSystemProcIOProcessIOReader.class);
    private static final String PROC_IO_LOCATION = "/proc/%d/io";

    private static final Pattern READ_BYTES_PATTERN = Pattern.compile("^\\s*read_bytes\\s*:\\s*(\\d+)[^\\d]*$");
    private static final Pattern WRITE_BYTES_PATTERN = Pattern.compile("^\\s*write_bytes\\s*:\\s*(\\d+)[^\\d]*$");


    private final File procIOLocation;

    public FileSystemProcIOProcessIOReader(File location) {
        procIOLocation = new File(location.getAbsolutePath());
    }

    public FileSystemProcIOProcessIOReader(int pid) {
        this(new File(String.format(PROC_IO_LOCATION,pid)));
    }

    /**
     * Reads the read_bytes and write_bytes from the current process io /proc file.
     * If the /proc/PID/io file is not readable then it returns
     * {@value org.greencheek.processio.service.io.ProcessIOReader#NON_READABLE_PROCESS_IO}
     *
     * @return  The current io of the jvm process
     */
    @Override
    public CurrentProcessIO getCurrentProcessIO() {
        if(!procIOLocation.canRead()) {
            log.warn("Unable to read process io location: {}",procIOLocation.getAbsolutePath());
            return NON_READABLE_PROCESS_IO;
        }


        FileInputStream fis = null;

        try {
            fis = new FileInputStream(procIOLocation);
        } catch (FileNotFoundException e) {
            log.warn("Not found process io location: {}",procIOLocation.getAbsolutePath());
            return NON_READABLE_PROCESS_IO;
        }

        InputStreamReader is = null;
        BufferedReader br = null;

        Long readBytes = null;
        Long writeBytes = null;
        try
        {
            is = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(is);
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                Matcher readMatcher = READ_BYTES_PATTERN.matcher(strLine);
                Matcher writeMatcher = WRITE_BYTES_PATTERN.matcher(strLine);

                if(readMatcher.matches()) {
                    try {
                        readBytes = Long.parseLong(readMatcher.group(1));
                    } catch (NumberFormatException e) {}
                }

                if(writeMatcher.matches()) {
                    try {
                        writeBytes = Long.parseLong(writeMatcher.group(1));
                    } catch (NumberFormatException e) {}
                }
            }
        } catch (IOException e) {
            log.warn("Unable to read process io location: {}",procIOLocation.getAbsolutePath());
            return NON_READABLE_PROCESS_IO;
        } finally {
            if(br!=null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }

        if(readBytes==null || writeBytes==null) {
            log.warn("Unable read with the read_bytes or write_bytes from io location: {}",procIOLocation.getAbsolutePath());

            return NON_READABLE_PROCESS_IO;
        } else {
            return new CurrentProcessIO(System.currentTimeMillis(),readBytes,writeBytes);
        }
    }
}
