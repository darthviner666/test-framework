package com.framework.utils.logger;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

public class AllureLog4jAppender extends AbstractAppender {
    protected AllureLog4jAppender(String name, Filter filter) {
        super(name, filter, null);
    }

    @Override
    public void append(LogEvent event) {
        Allure.addAttachment("Лог",
                "text/plain",
                event.getMessage().getFormattedMessage());
    }

    @PluginFactory
    public static AllureLog4jAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new AllureLog4jAppender(name, filter);
    }
}
