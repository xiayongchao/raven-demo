package org.eve.framework.raven.definition;

import java.io.Serializable;
import java.util.List;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class Configuration extends AbstractExpansion implements Serializable {
    List<ProcessorDefinition> processorDefinitions;
    List<FormatterDefinition> formatterDefinitions;
    List<PrinterDefinition> printerDefinitions;
    List<LoggerDefinition> loggerDefinitions;

    public List<ProcessorDefinition> getProcessorDefinitions() {
        return processorDefinitions;
    }

    public void setProcessorDefinitions(List<ProcessorDefinition> processorDefinitions) {
        this.processorDefinitions = processorDefinitions;
    }

    public List<FormatterDefinition> getFormatterDefinitions() {
        return formatterDefinitions;
    }

    public void setFormatterDefinitions(List<FormatterDefinition> formatterDefinitions) {
        this.formatterDefinitions = formatterDefinitions;
    }

    public List<PrinterDefinition> getPrinterDefinitions() {
        return printerDefinitions;
    }

    public void setPrinterDefinitions(List<PrinterDefinition> printerDefinitions) {
        this.printerDefinitions = printerDefinitions;
    }

    public List<LoggerDefinition> getLoggerDefinitions() {
        return loggerDefinitions;
    }

    public void setLoggerDefinitions(List<LoggerDefinition> loggerDefinitions) {
        this.loggerDefinitions = loggerDefinitions;
    }
}
