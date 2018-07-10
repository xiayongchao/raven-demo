package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.DefaultImplementation;
import org.eve.framework.raven.constant.Constants;
import org.eve.framework.raven.constant.LogLevel;
import org.eve.framework.raven.constant.PrinterType;
import org.eve.framework.raven.constant.ProcessorType;
import org.eve.framework.raven.definition.*;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.formatter.DefaultFormatter;
import org.eve.framework.raven.printer.DefaultPrinter;
import org.eve.framework.raven.util.Collections;
import org.eve.framework.raven.util.VerifyUtils;

import java.util.List;

/**
 * @author yc_xia
 * @date 2018/6/20
 */
@DefaultImplementation
public class DefaultConfiguratorValidator implements ConfiguratorValidator {
    @Override
    public void validate(Raven raven) throws RavenException {
        if (VerifyUtils.isNull(raven)) {
            throw new RavenException("<raven></raven>不能为空");
        }
        if (VerifyUtils.isNull(raven.getAppId())) {
            throw new RavenException("<raven><appId></appId></raven>不能为空");
        }
        this.validate(raven.getConfigurator());
        this.validate(raven.getConfiguration());
    }

    public void validate(Configurator configurator) throws RavenException {
        if (VerifyUtils.isNull(configurator)) {
            return;
        }
        //默认不进行刷新
        if (VerifyUtils.isNull(configurator.getScan())) {
            configurator.setScan(false);
        }
        //默认5分钟刷新一次
        if (VerifyUtils.isNull(configurator.getScanPeriod())) {
            configurator.setScanPeriod(5 * 60 * 1000L);
        }
        if (VerifyUtils.isBlank(configurator.getSchedulerClassName())) {
            configurator.setSchedulerClassName(null);
        }
    }

    @Override
    public void validate(Configuration configuration) throws RavenException {
        if (VerifyUtils.isNull(configuration)) {
            throw new RavenException("<raven><configuration></configuration></raven>不能为空");
        }
        this.validateProcessor(configuration.getProcessorDefinitions());
        this.validateFormatter(configuration.getFormatterDefinitions());
        this.validatePrinter(configuration.getPrinterDefinitions());
        this.validateLogger(configuration.getLoggerDefinitions());
    }

    private void validateLogger(List<LoggerDefinition> loggerDefinitions) throws RavenException {
        Collections.removeNullElement(loggerDefinitions);
        if (VerifyUtils.isEmpty(loggerDefinitions)) {
            throw new RavenException("<raven><configuration><loggers></loggers></configuration></raven>不能为空");
        }

        for (LoggerDefinition loggerDefinition : loggerDefinitions) {
            if (VerifyUtils.isNull(loggerDefinition.getId())) {
                throw new RavenException("<logger id=\"\">不能为空");
            }
            if (VerifyUtils.isNull(loggerDefinition.getPrinterRef())) {
                throw new RavenException("<logger printer-ref=\"\">不能为空");
            }
            if (!LogLevel.contains(loggerDefinition.getLevelName())) {
                throw new RavenException(String.format("未知的<logger><level></level></logger>值[%s],请参考{%s}",
                        loggerDefinition.getLevelName(), LogLevel.class.getName()));
            }
            loggerDefinition.setLevel(LogLevel.valueOf(loggerDefinition.getLevelName()));
            if (VerifyUtils.isNull(loggerDefinition.getTransmit())) {
                loggerDefinition.setTransmit(false);
            }
            if (VerifyUtils.isBlank(loggerDefinition.getScope())) {
                loggerDefinition.setScope(Constants.ASTERISK);
            }
        }

        LoggerDefinition ild, jld;
        for (int i = 0; i < loggerDefinitions.size(); i++) {
            ild = loggerDefinitions.get(i);
            for (int j = 0; j < loggerDefinitions.size(); j++) {
                if (j == i) {
                    continue;
                }
                jld = loggerDefinitions.get(j);
                if (jld.equals(ild)) {
                    throw new RavenException("<logger id=\"\"><logger>和<logger><scope></scope><logger>不能重复");
                }
            }
        }
    }

    private void validatePrinter(List<PrinterDefinition> printerDefinitions) throws RavenException {
        Collections.removeNullElement(printerDefinitions);
        if (VerifyUtils.isEmpty(printerDefinitions)) {
            throw new RavenException("<raven><configuration><printers></printers></configuration></raven>不能为空");
        }
        for (PrinterDefinition printerDefinition : printerDefinitions) {
            if (VerifyUtils.isNull(printerDefinition.getId())) {
                throw new RavenException("<printer id=\"\">不能为空");
            }
            if (!PrinterType.contains(printerDefinition.getTypeName())) {
                throw new RavenException(String.format("未知的<printer type=\"\">值[%s],请参考{%s}",
                        printerDefinition.getTypeName(), PrinterType.class.getName()));
            }
            printerDefinition.setType(PrinterType.get(printerDefinition.getTypeName()));
            if (PrinterType.FILE.equals(printerDefinition.getType()) && VerifyUtils.isBlank(printerDefinition.getPath())) {
                throw new RavenException(String.format("<printer type=\"%s\"><path></path><printer>不能为空", PrinterType.FILE.toString()));
            } else if (PrinterType.EXTENSION.equals(printerDefinition.getType()) && VerifyUtils.isBlank(printerDefinition.getClassName())) {
                throw new RavenException(String.format("<printer type=\"%s\" class=\"\">不能为空", PrinterType.EXTENSION.toString()));
            }
            if (VerifyUtils.isBlank(printerDefinition.getClassName())) {
                printerDefinition.setClassName(DefaultPrinter.class.getName());
            }
        }
    }

    private void validateFormatter(List<FormatterDefinition> formatterDefinitions) throws RavenException {
        Collections.removeNullElement(formatterDefinitions);
        if (VerifyUtils.isEmpty(formatterDefinitions)) {
            return;
        }
        int count = 0;
        for (FormatterDefinition formatterDefinition : formatterDefinitions) {
            if (VerifyUtils.isNull(formatterDefinition.getId())) {
                throw new RavenException("<formatter id=\"\">不能为空");
            }
            if (VerifyUtils.isBlank(formatterDefinition.getClassName())) {
                if (count >= 1) {
                    throw new RavenException("<formatters></formatters>中不能有多于两个的<formatter class=\"\">class为空");
                }
                count++;
                formatterDefinition.setClassName(DefaultFormatter.class.getName());
            }

            if (VerifyUtils.isBlank(formatterDefinition.getDateTimeFormat())) {
                formatterDefinition.setDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS");
            }
            if (VerifyUtils.isBlank(formatterDefinition.getWarnLevelString())) {
                formatterDefinition.setWarnLevelString("WARN");
            }
            if (VerifyUtils.isNull(formatterDefinition.getLevelInBrackets())) {
                formatterDefinition.setLevelInBrackets(true);
            }
            if (VerifyUtils.isNull(formatterDefinition.getShowAppId())) {
                formatterDefinition.setShowAppId(true);
            }
            if (VerifyUtils.isNull(formatterDefinition.getDateTimeFormat())) {
                formatterDefinition.setShowDateTime(true);
            }
            if (VerifyUtils.isNull(formatterDefinition.getShowAppName())) {
                formatterDefinition.setShowAppName(true);
            }
            if (VerifyUtils.isNull(formatterDefinition.getShowLoggerId())) {
                formatterDefinition.setShowLoggerId(true);
            }
            if (VerifyUtils.isNull(formatterDefinition.getShowLogName())) {
                formatterDefinition.setShowLogName(true);
            }
            if (VerifyUtils.isNull(formatterDefinition.getShowShortLogName())) {
                formatterDefinition.setShowShortLogName(!formatterDefinition.getShowLogName());
            }
            if (VerifyUtils.isNull(formatterDefinition.getShowThreadName())) {
                formatterDefinition.setShowThreadName(true);
            }
        }
    }

    private void validateProcessor(List<ProcessorDefinition> processorDefinitions) throws RavenException {
        Collections.removeNullElement(processorDefinitions);
        if (VerifyUtils.isEmpty(processorDefinitions)) {
            return;
        }
        for (ProcessorDefinition processorDefinition : processorDefinitions) {
            if (VerifyUtils.isNull(processorDefinition.getId())) {
                throw new RavenException("<processor id=\"\">不能为空");
            }
            if (!ProcessorType.contains(processorDefinition.getTypeName())) {
                throw new RavenException(String.format("未知的<processor type=\"\">值[%s],请参考{%s}",
                        processorDefinition.getTypeName(), ProcessorType.class.getName()));
            }
            processorDefinition.setType(ProcessorType.valueOf(processorDefinition.getTypeName().toUpperCase()));
            if (VerifyUtils.isEmpty(processorDefinition.getClassName())) {
                throw new RavenException("<processor class=\"\">不能为空");
            }
        }
    }

}
