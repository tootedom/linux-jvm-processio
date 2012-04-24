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
package org.greencheek.processio.agent;

import org.greencheek.processio.service.BasicProcessIOUsageCalculator;
import org.greencheek.processio.service.JMXJVMProcessIdObtainer;
import org.greencheek.processio.service.JVMProcessIdObtainer;
import org.greencheek.processio.service.ProcessIOUsageCalculator;
import org.greencheek.processio.service.io.FileSystemProcIOProcessIOReader;
import org.greencheek.processio.service.io.ProcessIOReader;
import org.greencheek.processio.service.persistence.ProcessIOUsagePersistence;
import org.greencheek.processio.service.persistence.jmx.ProcessIOUsagePersistenceViaJmx;
import org.greencheek.processio.service.scheduler.ProcessIOScheduler;
import org.greencheek.processio.service.scheduler.ScheduledExecutorServiceProcessIOScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;


public class ProcessIOAgent {

    // grep read_bytes /proc/[0-9]*/io | sort -nrk2 | head -5
    private static final Pattern FREQUENCY_OPTION = Pattern.compile("frequency=(\\d+)");
    private static final long DEFAULT_SCHEDULING_FREQUENCY = ProcessIOScheduler.DEFAULT_FREQUENCY_IN_MILLIS;
    private static final Logger log = LoggerFactory.getLogger(ProcessIOAgent.class);
    private static final String PROC_IO_LOCATION = "/proc/%d/io";
	private static final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    private static final JVMProcessIdObtainer pidParser = new JMXJVMProcessIdObtainer();
    private static final ProcessIOReader processIOReader;
    private static final ProcessIOScheduler scheduler;
    private static final ProcessIOUsageCalculator calculator = new BasicProcessIOUsageCalculator();
    private static final ProcessIOUsagePersistence persistence = new ProcessIOUsagePersistenceViaJmx(calculator);

	private static final int CURRENT_JVM_PID;
	private static boolean foundPID;
	static {
        CURRENT_JVM_PID = pidParser.getProcessId();
        foundPID = (CURRENT_JVM_PID==JVMProcessIdObtainer.PID_NOT_FOUND) ? false : true;
        ProcessIOReader ioReader = null;
        if(foundPID) {
            ioReader = new FileSystemProcIOProcessIOReader(new File(String.format(PROC_IO_LOCATION,CURRENT_JVM_PID)));
        }
        processIOReader = ioReader;

        scheduler = new ScheduledExecutorServiceProcessIOScheduler(ioReader,persistence);

	}
	
	
	// Required method for instrumentation agent.
    public static void premain(String arglist, Instrumentation inst) {
        if(!foundPID) {
            log.error("Unabled to Determine pid for current jvm process to agent has no effect");
        } else {

            long frequencyOfScheduler = DEFAULT_SCHEDULING_FREQUENCY;
            if(arglist!=null && arglist.trim().length()>0) {
                Matcher m = FREQUENCY_OPTION.matcher(arglist);
                if(m.matches()) {
                    try {
                        frequencyOfScheduler = Long.parseLong(m.group(1));
                    } catch(NumberFormatException e) {}
                }
            }

            scheduler.start(frequencyOfScheduler);



        }
    }
    
    


	
}
