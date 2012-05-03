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
package org.greencheek.processio.service.pid;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * <p>
 * Used JMX to obtain the PID of the JVM process.
 * Parsing the Name attribute of the java.lang:type=Runtime JMX Mbean.
 * </p>
 * <p>
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 12:59
 * </p>
 */
public class JMXJVMProcessIdObtainer implements JVMProcessIdObtainer {

    private static final int CURRENT_JVM_PID;
    static {
        CURRENT_JVM_PID = parsePid(ManagementFactory.getRuntimeMXBean());
    }

    @Override
    public int getProcessId() {
        return CURRENT_JVM_PID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private static int parsePid(RuntimeMXBean rtbean) {
        String s = rtbean.getName();
        int index = s.indexOf('@');
        String pid;
        if (index != -1) {
            pid = s.substring(0, index);
            try
            {
                return Integer.parseInt(pid);
            }
            catch(NumberFormatException e){
                return PID_NOT_FOUND;
            }
        } else {
            return PID_NOT_FOUND;
        }
    }
}
