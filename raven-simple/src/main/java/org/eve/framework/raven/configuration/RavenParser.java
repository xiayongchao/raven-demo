package org.eve.framework.raven.configuration;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eve.framework.raven.definition.*;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.util.VerifyUtils;
import org.eve.framework.raven.util.XmlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author yc_xia
 * @date 2018/5/24
 */
public class RavenParser {
    private final String configurationFile;
    /**
     * 创建SAXReader读取器，专门用于读取xml
     */
    private final SAXReader saxReader = new SAXReader();

    public RavenParser(String configurationFile) {
        this.configurationFile = configurationFile;
    }

    public Raven parse() throws RavenException {
        return this.parse(false);
    }

    public Raven parse(boolean isFile) throws RavenException {
        try {
            Document document = this.loadDocument(isFile);
            if (document == null) {
                return null;
            }
            Element root = document.getRootElement();
            return this.parseRaven(root);
        } catch (DocumentException | IOException e) {
            throw new RavenException("无法解析配置文件[" + this.configurationFile + "].", e);
        }
    }

    private Document loadDocument(boolean isFile) throws DocumentException, IOException {
        InputStream inputStream;
        if (!isFile) {
            inputStream = AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
                @Override
                public InputStream run() {
                    ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
                    URL url;
                    if (threadCL != null) {
                        url = threadCL.getResource(configurationFile);
                    } else {
                        url = ClassLoader.getSystemResource(configurationFile);
                    }
                    try {
                        return VerifyUtils.isNull(url) ? null : url.openStream();
                    } catch (IOException e) {
                        return null;
                    }
                }
            });
        } else {
            File file = new File(this.configurationFile);
            if (!file.exists() || !file.isFile()) {
                return null;
            }
            inputStream = new FileInputStream(file);
        }
        if (VerifyUtils.isNull(inputStream)) {
            return null;
        }
        //根据saxReader的read重写方法可知，既可以通过inputStream输入流来读取，也可以通过file对象来读取
        return this.saxReader.read(inputStream);
    }

    private Raven parseRaven(Element ravenElement) {
        if (Objects.isNull(ravenElement)) {
            return null;
        }
        Raven raven = new Raven();
        raven.setAppId(XmlUtils.getIntegerValue(ravenElement, "appId", null));
        raven.setAppName(XmlUtils.getStringValue(ravenElement, "appName", null));
        raven.setConfigurator(this.parseConfigurator(ravenElement));
        raven.setConfiguration(this.parseConfiguration(ravenElement));
        return raven;
    }

    private Configurator parseConfigurator(Element ravenElement) {
        Element configuratorElement = ravenElement.element("configurator");
        if (VerifyUtils.isNull(configuratorElement)) {
            return null;
        }
        Configurator configurator = new Configurator();
        configurator.setScan(XmlUtils.getBooleanValue(configuratorElement, "scan", null));
        configurator.setScanPeriod(XmlUtils.getLongValue(configuratorElement, "scanPeriod", null));
        configurator.setSchedulerClassName(XmlUtils.getStringAttribute(configuratorElement, "scheduler-class", null));
        configurator.setDescription(XmlUtils.getStringValue(configuratorElement, "description", null));
        return configurator;
    }

    private Configuration parseConfiguration(Element ravenElement) {
        Element configurationElement = ravenElement.element("configuration");
        if (VerifyUtils.isNull(configurationElement)) {
            return null;
        }
        Configuration configuration = new Configuration();
        configuration.setProcessorDefinitions(this.parseProcessors(configurationElement));
        configuration.setFormatterDefinitions(this.parseFormatters(configurationElement));
        configuration.setPrinterDefinitions(this.parsePrinters(configurationElement));
        configuration.setLoggerDefinitions(this.parseLoggerDefinitions(configurationElement));
        return configuration;
    }

    private List<ProcessorDefinition> parseProcessors(Element configurationElement) {
        List<ProcessorDefinition> processorDefinitions = new ArrayList<>();
        Element processorsElement = configurationElement.element("processors");
        if (VerifyUtils.isNull(processorsElement)) {
            return processorDefinitions;
        }
        Iterator<Element> processorIterator = processorsElement.elements("processor").iterator();
        while (processorIterator.hasNext()) {
            processorDefinitions.add(this.parseProcessor(processorIterator.next()));
        }
        return processorDefinitions;
    }

    private List<FormatterDefinition> parseFormatters(Element configurationElement) {
        List<FormatterDefinition> formatterDefinitions = new ArrayList<>();
        Element formattersElement = configurationElement.element("formatters");
        if (VerifyUtils.isNull(formattersElement)) {
            return formatterDefinitions;
        }
        Iterator<Element> formatterIterator = formattersElement.elements("formatter").iterator();
        while (formatterIterator.hasNext()) {
            formatterDefinitions.add(this.parseFormatter(formatterIterator.next()));
        }
        return formatterDefinitions;
    }

    private List<PrinterDefinition> parsePrinters(Element configurationElement) {
        List<PrinterDefinition> printerDefinitions = new ArrayList<>();
        Element printersElement = configurationElement.element("printers");
        if (VerifyUtils.isNull(printersElement)) {
            return printerDefinitions;
        }
        Iterator<Element> printerIterator = printersElement.elements("printer").iterator();
        while (printerIterator.hasNext()) {
            printerDefinitions.add(this.parsePrinter(printerIterator.next()));
        }
        return printerDefinitions;
    }

    private List<LoggerDefinition> parseLoggerDefinitions(Element configurationElement) {
        List<LoggerDefinition> loggerDefinitions = new ArrayList<>();
        Element loggersElement = configurationElement.element("loggers");
        if (VerifyUtils.isNull(loggersElement)) {
            return loggerDefinitions;
        }
        Iterator<Element> loggerIterator = loggersElement.elements("logger").iterator();
        while (loggerIterator.hasNext()) {
            loggerDefinitions.add(this.parseLogger(loggerIterator.next()));
        }
        return loggerDefinitions;

    }

    private ProcessorDefinition parseProcessor(Element processorElement) {
        ProcessorDefinition processorDefinition = new ProcessorDefinition();
        processorDefinition.setId(XmlUtils.getIntegerAttribute(processorElement, "id", null));
        processorDefinition.setClassName(XmlUtils.getStringAttribute(processorElement, "class", null));
        processorDefinition.setTypeName(XmlUtils.getStringAttribute(processorElement, "type", null));
        processorDefinition.setDescription(XmlUtils.getStringValue(processorElement, "description", null));
        return processorDefinition;
    }

    private FormatterDefinition parseFormatter(Element formatterElement) {
        FormatterDefinition formatterDefinition = new FormatterDefinition();
        formatterDefinition.setId(XmlUtils.getIntegerAttribute(formatterElement, "id", null));
        formatterDefinition.setClassName(XmlUtils.getStringAttribute(formatterElement, "class", null));
        formatterDefinition.setDateTimeFormat(XmlUtils.getStringValue(formatterElement, "dateTimeFormat", null));
        formatterDefinition.setShowDateTime(XmlUtils.getBooleanValue(formatterElement, "showDateTime", null));
        formatterDefinition.setShowThreadName(XmlUtils.getBooleanValue(formatterElement, "showThreadName", null));
        formatterDefinition.setShowLogName(XmlUtils.getBooleanValue(formatterElement, "showLogName", null));
        formatterDefinition.setShowShortLogName(XmlUtils.getBooleanValue(formatterElement, "showShortLogName", null));
        formatterDefinition.setShowAppId(XmlUtils.getBooleanValue(formatterElement, "showAppId", null));
        formatterDefinition.setShowAppName(XmlUtils.getBooleanValue(formatterElement, "showAppName", null));
        formatterDefinition.setShowLoggerId(XmlUtils.getBooleanValue(formatterElement, "showLoggerId", null));
        formatterDefinition.setLevelInBrackets(XmlUtils.getBooleanValue(formatterElement, "levelInBrackets", null));
        formatterDefinition.setWarnLevelString(XmlUtils.getStringValue(formatterElement, "warnLevelString", null));
        List<Integer> processorRef = null;
        Element processorRefElement = formatterElement.element("processorRef");
        if (VerifyUtils.notNull(processorRefElement)) {
            Iterator<Element> idIterator = processorRefElement.elements("id").iterator();
            processorRef = new ArrayList<>();
            while (idIterator.hasNext()) {
                processorRef.add(XmlUtils.getIntegerText(idIterator.next()));
            }
        }
        formatterDefinition.setProcessorRef(processorRef);
        return formatterDefinition;
    }

    private PrinterDefinition parsePrinter(Element printerElement) {
        PrinterDefinition printerDefinition = new PrinterDefinition();
        printerDefinition.setId(XmlUtils.getIntegerAttribute(printerElement, "id", null));
        printerDefinition.setClassName(XmlUtils.getStringAttribute(printerElement, "class", null));
        printerDefinition.setPath(XmlUtils.getStringAttribute(printerElement, "path", null));
        printerDefinition.setTypeName(XmlUtils.getStringAttribute(printerElement, "type", null));
        printerDefinition.setDescription(XmlUtils.getStringValue(printerElement, "description", null));
        List<Integer> processorRef = null;
        Element processorRefElement = printerElement.element("processorRef");
        if (VerifyUtils.notNull(processorRefElement)) {
            Iterator<Element> idIterator = processorRefElement.elements("id").iterator();
            processorRef = new ArrayList<>();
            while (idIterator.hasNext()) {
                processorRef.add(XmlUtils.getIntegerText(idIterator.next()));
            }
        }
        printerDefinition.setProcessorRef(processorRef);
        return printerDefinition;
    }

    private LoggerDefinition parseLogger(Element loggerElement) {
        LoggerDefinition loggerDefinition = new LoggerDefinition();
        loggerDefinition.setId(XmlUtils.getIntegerAttribute(loggerElement, "id", null));
        loggerDefinition.setPrinterRef(XmlUtils.getIntegerAttribute(loggerElement, "printer-ref", null));
        loggerDefinition.setFormatterRef(XmlUtils.getIntegerAttribute(loggerElement, "formatter-ref", null));
        loggerDefinition.setTransmit(XmlUtils.getBooleanAttribute(loggerElement, "transmit", null));
        loggerDefinition.setLevelName(XmlUtils.getStringValue(loggerElement, "level", null));
        loggerDefinition.setScope(XmlUtils.getStringValue(loggerElement, "scope", null));
        loggerDefinition.setDescription(XmlUtils.getStringValue(loggerElement, "description", null));
        return loggerDefinition;
    }
}
