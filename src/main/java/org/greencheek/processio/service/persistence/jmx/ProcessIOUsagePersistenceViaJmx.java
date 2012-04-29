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
package org.greencheek.processio.service.persistence.jmx;

import org.greencheek.processio.domain.CurrentProcessIO;
import org.greencheek.processio.domain.jmx.ProcessIOUsageHolder;
import org.greencheek.processio.service.usage.ProcessIOUsage;
import org.greencheek.processio.service.persistence.ProcessIOUsagePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * User: dominictootell
 * Date: 22/04/2012
 * Time: 16:38
 */
public class ProcessIOUsagePersistenceViaJmx implements ProcessIOUsagePersistence {

    private static final Logger log = LoggerFactory.getLogger(ProcessIOUsagePersistenceViaJmx.class);

    private final ProcessIOUsageHolder ioUsageHolder;

    public static final String DEFAULT_JMX_DOMAIN_NAME="org.greencheek";
    public static final String DEFAULT_JMX_BEAN_NAME="processiousage";
    public static final String DEFAULT_COMPOSED_JMX_BEAN_NAME = DEFAULT_JMX_DOMAIN_NAME + ":type=" + DEFAULT_JMX_BEAN_NAME;
    public static final ObjectName DEFAULT_JMX_OBJECT_NAME;
    static {
        ObjectName o = null;
        try {
            o = new ObjectName(DEFAULT_JMX_DOMAIN_NAME + ":type=" + DEFAULT_JMX_BEAN_NAME);
        } catch(MalformedObjectNameException e) {
            // won't happen.  The default is safe.
        }
        DEFAULT_JMX_OBJECT_NAME = o;
    }

    private static final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    private final String beanObjectNameStringRepresentation;
    private final boolean registeredInJmx;
    private final ObjectName jmxRegisteredObjectName;


    public ProcessIOUsagePersistenceViaJmx(ProcessIOUsage ioUsage) {
        this(ioUsage,DEFAULT_JMX_OBJECT_NAME,DEFAULT_COMPOSED_JMX_BEAN_NAME);
    }

    public ProcessIOUsagePersistenceViaJmx(ProcessIOUsage ioUsage, ObjectName jmxObjectName,
                                           String beanObjectName) {

        ioUsageHolder = new ProcessIOUsageHolder(ioUsage);

        if (jmxObjectName == null) {
            log.error("Using Default ObjectName to register bean, the given object name ({}) could not be used. Registering under {}", beanObjectName, DEFAULT_COMPOSED_JMX_BEAN_NAME);
            jmxObjectName = DEFAULT_JMX_OBJECT_NAME;
            beanObjectNameStringRepresentation = DEFAULT_COMPOSED_JMX_BEAN_NAME;
        } else {
            this.beanObjectNameStringRepresentation = beanObjectName;
        }

        boolean registered = false;
        try {
            server.registerMBean(ioUsageHolder, jmxObjectName);
            registered = true;
        } catch (NullPointerException e) {
            if (log.isWarnEnabled()) {
                log.warn("Unable to register with jmx, invalid bean name:{}", beanObjectName, e);
            }
        } catch (InstanceAlreadyExistsException e) {
            if (log.isWarnEnabled()) {
                log.warn("Unable to register with jmx, bean already exists:{}", beanObjectName, e);
            }
        } catch (MBeanRegistrationException e) {
            if (log.isWarnEnabled()) {
                log.warn("Unable to register with jmx, exception during registration:{}", beanObjectName, e);
            }
        } catch (NotCompliantMBeanException e) {
            if (log.isWarnEnabled()) {
                log.warn("Unable to register with jmx, bean is not compatible:{}", beanObjectName, e);
            }
        } finally {
            registeredInJmx = registered;
            jmxRegisteredObjectName = jmxObjectName;
        }

    }

    public ProcessIOUsagePersistenceViaJmx(ProcessIOUsage ioUsage,
                                           String jmxDomainName, String beanName) {

        this(ioUsage,createObjectName(jmxDomainName,beanName),jmxDomainName+":type="+beanName);


    }

    private static ObjectName createObjectName(String jmxDomainName,String beanName) {
        String beanObjectName = jmxDomainName+":type="+beanName;

        ObjectName objName = null;
        try {
            objName = new ObjectName(beanObjectName);
        } catch (MalformedObjectNameException e) {
            if(log.isWarnEnabled()) {
                log.warn("Unable to register with jmx, invalid bean name:{}", beanObjectName, e);
            }
        }

        return objName;
    }



    public boolean isRegisteredInJmx() {
        return registeredInJmx;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
        if(isRegisteredInJmx()) {
            try {
                server.unregisterMBean(getBeanObjectName());
            } catch (MBeanRegistrationException e) {
                // TODO Auto-generated catch block
                if(log.isWarnEnabled()) {
                    log.warn("Unable to unregister object with jmx:{}", beanObjectNameStringRepresentation, e);
                }
            } catch (InstanceNotFoundException e) {
                // TODO Auto-generated catch block
                if(log.isWarnEnabled()) {
                    log.warn("Unable to unregister object with jmx, object not registered:{}", beanObjectNameStringRepresentation, e);
                }
            }
        }
    }

    @Override
    public void persist(CurrentProcessIO io) {
        ioUsageHolder.setProcessIO(io);
    }

    public ObjectName getBeanObjectName() {
        return jmxRegisteredObjectName;
    }
}
