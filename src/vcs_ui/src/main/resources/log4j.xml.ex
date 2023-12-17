<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd" >
<log4j:configuration debug="false">

    <!--Console appender -->
    <appender name="stdout" class="org.apache.log4j.ConsoleAppender">
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} %p %m%n"/>
        </layout>
    </appender>

    <!-- File appender -->
    <appender name="fout" class="org.apache.log4j.FileAppender">
        <param name="file" value="log4j/target/baeldung-log4j.log"/>
        <param name="append" value="false"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} %-5p %m%n"/>
        </layout>
    </appender>

    <!-- Rolling appenders -->
    <appender name="roll-by-size" class="org.apache.log4j.RollingFileAppender">
        <param name="file" value="target/log4j/roll-by-size/app.log"/>
        <param name="MaxFileSize" value="5KB"/>
        <param name="MaxBackupIndex" value="2"/> <!-- It's one by default. -->
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} %-5p %m%n"/>
        </layout>
    </appender>
    <appender name="roll-by-size-2" class="org.apache.log4j.RollingFileAppender">
        <param name="file" value="target/log4j/roll-by-size-2/app.log"/>
        <param name="MaxFileSize" value="5KB"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} %-5p %m%n"/>
        </layout>
    </appender>
    <appender name="roll-by-window"
              class="org.apache.log4j.rolling.RollingFileAppender">
        <rollingPolicy
                class="org.apache.log4j.rolling.FixedWindowRollingPolicy">
            <param name="ActiveFileName" value="target/log4j/roll-by-window/app.log"/>
            <param name="FileNamePattern"
                   value="target/log4j/roll-by-window/app.%i.log.gz"/>
            <param name="MinIndex" value="7"/>
            <param name="MaxIndex" value="17"/>
        </rollingPolicy>
        <triggeringPolicy
                class="org.apache.log4j.rolling.SizeBasedTriggeringPolicy">
            <param name="MaxFileSize" value="50000"/>
        </triggeringPolicy>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} %-5p %m%n"/>
        </layout>
    </appender>
    <appender name="roll-by-time"
              class="org.apache.log4j.rolling.RollingFileAppender">
        <rollingPolicy class="org.apache.log4j.rolling.TimeBasedRollingPolicy">
            <param name="FileNamePattern"
                   value="target/log4j/roll-by-time/app.%d{HH-mm}.log.gz"/>
        </rollingPolicy>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} %-5p - %m%n"/>
        </layout>
    </appender>
    <appender name="roll-by-time-and-size"
              class="org.apache.log4j.rolling.RollingFileAppender">
        <rollingPolicy class="org.apache.log4j.rolling.TimeBasedRollingPolicy">
            <param name="FileNamePattern"
                   value="target/log4j/roll-by-time-and-size/app.%d{HH-mm}.%i.log.gz"/>
        </rollingPolicy>
        <triggeringPolicy
                class="org.apache.log4j.rolling.SizeBasedTriggeringPolicy">
            <param name="MaxFileSize" value="50000"/>
        </triggeringPolicy>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} %-5p - %m%n"/>
        </layout>
    </appender>

    <!--Override log level for specified package -->
    <category name="com.baeldung.log4j">
        <priority value="TRACE"/>
    </category>

    <category name="com.baeldung.log4j.Log4jRollingExample">
        <priority value="TRACE"/>
        <appender-ref ref="roll-by-size"/>
        <appender-ref ref="roll-by-size-2"/>
        <appender-ref ref="roll-by-window"/>
        <appender-ref ref="roll-by-time"/>
        <appender-ref ref="roll-by-time-and-size"/>
    </category>

    <logger name="com.baeldung.log4j.NoAppenderExample"/>

    <root>
        <level value="DEBUG"/>
        <appender-ref ref="stdout"/>
        <appender-ref ref="fout"/>
    </root>

</log4j:configuration>