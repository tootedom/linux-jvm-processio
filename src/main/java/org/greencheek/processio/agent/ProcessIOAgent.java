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

import org.greencheek.processio.service.usage.BasicProcessIOUsage;
import org.greencheek.processio.service.JMXJVMProcessIdObtainer;
import org.greencheek.processio.service.JVMProcessIdObtainer;
import org.greencheek.processio.service.usage.ProcessIOUsage;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.MBeanServer;


/**
 * <p>
 * Agent that is responsible for setting up a scheduler that periodically reads
 * the amount of read and write io that the current jvm process has performed.
 * The java is enabled via the -javaagent flag on the jvm.  For example on a tomcat it can be
 * controlled via the following (example for enabling in tomcat):
 * <pre>
 *        export CATALINA_OPTS="-javaagent:$CATALINA_HOME/lib/processio-0.0.1-SNAPSHOT-relocated-shade.jar=frequency=300000,jmxbeanname=io,jmxdomainname=my.domain"
 * </pre>
 * </p>
 * <p>
 * The following arguments are available:
 * <ul>
 *     <li>frequency : The frequency of the io collection in millis</li>
 *     <li>jmxbeanname : The name of the bean to register in jmx</li>
 *     <li>jmxdomainname : Then name of the domain under which to register the bean in jmx</li>
 * </ul>
 * </p>
 * <p>
 * The agent registers the io that is available in the jvm's MBeanServer
 * </p>
 */
public class ProcessIOAgent {
    //
    // order by greatest (top 5), just for example of using /proc/PID/io
    // grep read_bytes /proc/[0-9]*/io | sort -nrk2 | head -5
    //
    private static final Logger log = LoggerFactory.getLogger(ProcessIOAgent.class);


    private static final Pattern FREQUENCY_OPTION = Pattern.compile(".*frequency=(\\d+).*");
    private static final Pattern JMX_BEAN_NAME = Pattern.compile(".*jmxbeanname=(\\w+).*");
    private static final Pattern JMX_DOMAIN_NAME = Pattern.compile(".*jmxdomainname=([\\w\\.]+).*");
    private static final long DEFAULT_SCHEDULING_FREQUENCY = ProcessIOScheduler.DEFAULT_FREQUENCY_IN_MILLIS;
    private static final JVMProcessIdObtainer pidParser = new JMXJVMProcessIdObtainer();
    private static final ProcessIOReader processIOReader;
    private static volatile ProcessIOScheduler scheduler;
    private static final ProcessIOUsage calculator = new BasicProcessIOUsage();
    private static volatile ProcessIOUsagePersistence persistence;

	private static final int CURRENT_JVM_PID;
	private static boolean foundPID;
	static {
        CURRENT_JVM_PID = pidParser.getProcessId();
        foundPID = (CURRENT_JVM_PID!=JVMProcessIdObtainer.PID_NOT_FOUND);
        ProcessIOReader ioReader = null;
        if(foundPID) {
            ioReader = new FileSystemProcIOProcessIOReader(CURRENT_JVM_PID);
        }
        processIOReader = ioReader;


	}
	
	
	// Required method for instrumentation agent.
    public static void premain(String arglist, Instrumentation inst) {
        if(!foundPID) {
            log.error("Unabled to Determine pid for current jvm process to agent has no effect");
        } else {
            String beanName = null;
            String domainName = null;

            long frequencyOfScheduler = DEFAULT_SCHEDULING_FREQUENCY;
            if(arglist!=null && arglist.trim().length()>0) {
                Matcher m = FREQUENCY_OPTION.matcher(arglist);
                if(m.matches()) {
                    try {
                        frequencyOfScheduler = Long.parseLong(m.group(1));
                    } catch(NumberFormatException e) {}
                }

                m = JMX_BEAN_NAME.matcher(arglist);
                // got a bean name
                if(m.matches()) {
                    beanName = m.group(1);
                }

                m = JMX_DOMAIN_NAME.matcher(arglist);
                if(m.matches()) domainName = m.group(1);

                if(beanName == null || beanName.trim().length()==0) beanName = ProcessIOUsagePersistenceViaJmx.DEFAULT_JMX_BEAN_NAME;
                if(domainName == null || domainName.trim().length()==0) domainName = ProcessIOUsagePersistenceViaJmx.DEFAULT_JMX_DOMAIN_NAME;
                persistence = new ProcessIOUsagePersistenceViaJmx(calculator,domainName,beanName);
            } else {
                persistence = new ProcessIOUsagePersistenceViaJmx(calculator);
            }


            scheduler = new ScheduledExecutorServiceProcessIOScheduler(processIOReader,persistence);

            scheduler.start(frequencyOfScheduler);


            log.info("ProcessIOAgent Running, io will be obtain every {}ms",frequencyOfScheduler);
        }
    }
    
    


	
}
