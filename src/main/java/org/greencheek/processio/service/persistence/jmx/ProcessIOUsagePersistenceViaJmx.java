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
 * Persists a {@link ProcessIOUsageHolder} object in the JMXServer.  This holder object holds onto a {@link org.greencheek.processio.domain.ProcessIO}
 * object that stores the current and previous io read and written by the process.  The  {@link ProcessIOUsageHolder} object
 * is a JMX MXBean
 *
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

    /**
     * Persist/Register the given ProcessIOUsage object with the jvm's MBeanServer
     *
     * @param ioUsage The {@link ProcessIOUsageHolder} object to persist in the Jvm MBean Server
     */
    public ProcessIOUsagePersistenceViaJmx(ProcessIOUsage ioUsage) {
        this(ioUsage,DEFAULT_JMX_OBJECT_NAME,DEFAULT_COMPOSED_JMX_BEAN_NAME);
    }

    /**
     * Persist/Register the given ProcessIOUsage object with the jvm's MBeanServer
     *
     * @param ioUsage  The io object to persist
     * @param jmxObjectName the jmx name to persist the ProcessIOUsage object under
     * @param beanObjectName the jmx domain under which to register the  {@link ProcessIOUsageHolder} object
     */
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
            log.warn("Unable to register with jmx, invalid bean name:{}", beanObjectName, e);

        } catch (InstanceAlreadyExistsException e) {
            log.warn("Unable to register with jmx, bean already exists:{}", beanObjectName, e);
        } catch (MBeanRegistrationException e) {
            log.warn("Unable to register with jmx, exception during registration:{}", beanObjectName, e);
        } catch (NotCompliantMBeanException e) {
            log.warn("Unable to register with jmx, bean is not compatible:{}", beanObjectName, e);
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
            log.warn("Unable to register with jmx, invalid bean name:{}", beanObjectName, e);
        }

        return objName;
    }



    public boolean isRegisteredInJmx() {
        return registeredInJmx;
    }

    @Override
    public void init() {

    }

    /**
     * If the bean was registered in the constructor, then an attempt will be made to remove the bean from
     * the jmx server on shutdown
     */
    @Override
    public void destroy() {
        if(isRegisteredInJmx()) {
            try {
                server.unregisterMBean(getBeanObjectName());
            } catch (MBeanRegistrationException e) {
                log.warn("Unable to unregister object with jmx:{}", beanObjectNameStringRepresentation, e);
            } catch (InstanceNotFoundException e) {
                log.warn("Unable to unregister object with jmx, object not registered:{}", beanObjectNameStringRepresentation, e);
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
