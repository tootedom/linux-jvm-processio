Linux JVM IO Usage Agent.
---------------------------------

A simple javaagent (http://docs.oracle.com/javase/6/docs/api/java/lang/instrument/package-summary.html)
that allows you to recording jmx the current io that a JVM process is performing.

The agent relies on the existance of the /procfs filesystem and the availability of the /proc/PID/io per process
file (http://www.kernel.org/doc/Documentation/filesystems/proc.txt), making use of the two counters:

* read_bytes
* write_bytes


> read_bytes
> ----------
>
> I/O counter: bytes read
> Attempt to count the number of bytes which this process really did cause to
> be fetched from the storage layer. Done at the submit_bio() level, so it is
> accurate for block-backed filesystems. <please add status regarding NFS and
> CIFS at a later time>
>
>
> write_bytes
> -----------
>
> I/O counter: bytes written
> Attempt to count the number of bytes which this process caused to be sent to
> the storage layer. This is done at page-dirtying time.


The java agent annotation is found in the following package.  It is only this dependency that you need to include in your
application in order to load the agent from the system class loader.  The processio is shaded jar (http://maven.apache.org/plugins/maven-shade-plugin/)
, or uber jar, as it uses slf4j to log; and therefore has a dependency on the slf4j api.  If you don't include slf4j on
the system classloader along with the agent; the agent would fail to load and it would affect your application.  Therefore
the shade packages 1.6.4 of slf4j relocated under the org.greencheek package.

```xml
		<dependency>
			<groupId>org.greencheek</groupId>
  			<artifactId>processio</artifactId>
  			<version>0.0.1</version>
  			<classifier>relocated-shade</classifier>
		</dependency>
```

You can get the normal jar if you do not need this relocated package at:

```xml
		<dependency>
			<groupId>org.greencheek</groupId>
  			<artifactId>processio</artifactId>
  			<version>0.0.1</version>
  			<classifier>relocated-shade</classifier>
		</dependency>
```


The libraries are available in the mvn repo at:

	https://raw.github.com/tootedom/tootedom-mvn-repo/master/releases/
	https://raw.github.com/tootedom/tootedom-mvn-repo/master/snapshots/

For example:

	https://raw.github.com/tootedom/tootedom-mvn-repo/master/releases/org/greencheek/processio/0.0.1/processio-0.0.1-relocated-shade.jar

## Install/Usage

Use of the javaagent is by adding an appropriate *-javaagent* to jvm startup.  I.e. for tomcat, create a bin/setenv.sh like as follows:

```
    export CATALINA_OPTS="-javaagent:$CATALINA_HOME/lib/processio-0.0.1-SNAPSHOT-relocated-shade.jar"
```

Upon startup the agent will start a background thread that by default reads the /procfs files system every minute (sample time/frequency)
It will register in the JMX server a bean containing the current process io usage.  The bean will be registered under the domain
*org.greencheek* with the name *processiousage*.

![Bean Registered in JMX](./linux-jvm-processio/processiobean.png)

The bean provides the following counters:

* Average KB (Read and Write) per second since startup of the jvm
* Average MB (Read and Write) per second since startup of the jvm
* IO Usage in KB (Read and Write) per second, that has occurred since the last sample
* IO Usage in MB (Read and Write) per second, that has occurred since the last sample

These can be seen below:

![Bean Registered in JMX](./linux-jvm-processio/processiobean-counters.png)


You can change the refrequency of the sampling time with the option *frequency*; on the *-javaagent* command line.
You can also change the name of the bean (*jmxbeanname*) and/or the domain (*jmxdomainname*) under which it is registered, for example:

```
   export CATALINA_OPTS="-javaagent:$CATALINA_HOME/lib/processio-0.0.1-SNAPSHOT-relocated-shade.jar=frequency=5000,jmxbeanname=io,jmxdomainname=my.domain
```

