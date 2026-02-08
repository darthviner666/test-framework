package com.framework.utils.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:config.properties",
        "system:properties",
        "system:env"
})
public interface JenkinsConfig extends Config {

    @Key("jenkins.browser")
    @DefaultValue("chrome")
    String browser();

    @Key("jenkins.remote.url")
    @DefaultValue("http://localhost:4444/wd/hub")
    String remoteUrl();

    @Key("jenkins.headless")
    @DefaultValue("true")
    boolean headless();

    @Key("CI")
    @DefaultValue("false")
    String ci();

    @Key("JENKINS_HOME")
    String jenkinsHome();
}
