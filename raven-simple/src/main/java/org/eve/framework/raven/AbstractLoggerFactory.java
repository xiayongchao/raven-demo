package org.eve.framework.raven;

import org.eve.framework.raven.bean.DefaultBeanFactory;
import org.eve.framework.raven.constant.Constants;
import org.eve.framework.raven.constant.PrinterType;
import org.eve.framework.raven.constant.ProcessorType;
import org.eve.framework.raven.definition.*;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.formatter.AbstractFormatPostProcessor;
import org.eve.framework.raven.formatter.AbstractFormatter;
import org.eve.framework.raven.formatter.DefaultFormatter;
import org.eve.framework.raven.formatter.FormatChain;
import org.eve.framework.raven.logger.RavenLogger;
import org.eve.framework.raven.printer.AbstractPrintPostProcessor;
import org.eve.framework.raven.printer.AbstractPrinter;
import org.eve.framework.raven.printer.DefaultPrinter;
import org.eve.framework.raven.printer.PrintChain;
import org.eve.framework.raven.util.VerifyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 抽象logger工厂
 *
 * @author yc_xia
 * @date 2018/6/22
 */
public abstract class AbstractLoggerFactory extends DefaultBeanFactory {
    /**
     * logger工厂启动时间
     */
    private static long START_TIME = System.currentTimeMillis();

    private final Map<Integer, FormatterDefinition> formatterDefinitionMap = new HashMap<>();
    private final Map<String, AbstractFormatter> formatterMap = new HashMap<>();

    private final Map<Integer, PrinterDefinition> printerDefinitionMap = new HashMap<>();
    private final Map<String, AbstractPrinter> printerMap = new HashMap<>();

    private final Map<Integer, ProcessorDefinition> processorDefinitionMap = new HashMap<>();
    private final Map<String, AbstractFormatPostProcessor> formatPostProcessorMap = new HashMap<>();
    private final Map<String, AbstractPrintPostProcessor> printPostProcessorMap = new HashMap<>();

    private final Map<String, LoggerDefinition> loggerDefinitionMap = new HashMap<>();
    private final Map<String, RavenLogger> ravenLoggerMap = new HashMap<>();

    protected Raven raven;
    /**
     * 读写锁,用于互斥 初始化和获取Logger 以及 更新Logger和Logger打印
     */
    protected final ReadWriteLock updateLock = new ReentrantReadWriteLock();

    /**
     * 初始化logger工厂
     *
     * @param raven
     * @throws RavenException
     */
    protected void initLoggerFactory(Raven raven) throws RavenException {
        if (VerifyUtils.isNull(raven)) {
            throw new RavenException("配置信息raven实例对象不能为空");
        }
        this.raven = raven;
        this.loadConfiguration(raven.getConfiguration());
    }

    /**
     * 刷新logger工厂
     *
     * @param configuration
     * @throws RavenException
     */
    public void refresh(Configuration configuration) throws RavenException {
        this.clear();
        this.loadConfiguration(configuration);
        if (VerifyUtils.notEmpty(this.ravenLoggerMap)) {
            for (Map.Entry<String, RavenLogger> entry : this.ravenLoggerMap.entrySet()) {
                this.refreshRavenLogger(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 清空logger工厂
     */
    private void clear() {
        this.formatterDefinitionMap.clear();
        this.printerDefinitionMap.clear();
        this.loggerDefinitionMap.clear();
        this.processorDefinitionMap.clear();
        this.printerMap.clear();
        this.printPostProcessorMap.clear();
        this.formatterMap.clear();
        this.formatPostProcessorMap.clear();
    }

    /**
     * 刷新Raven logger信息
     *
     * @param logName
     * @param ravenLogger
     * @throws RavenException
     */
    private void refreshRavenLogger(String logName, RavenLogger ravenLogger) throws RavenException {
        LoggerDefinition loggerDefinition = this.vote(logName, null);
        if (VerifyUtils.isNull(loggerDefinition)) {
            return;
        }
        FormatChain formatChain = this.getFormatChain(loggerDefinition.getFormatterRef());
        PrintChain printChain = this.getPrintChain(loggerDefinition.getPrinterRef());
        Metadata metadata = this.getMetadata(logName, loggerDefinition);

        ravenLogger.refresh(metadata, formatChain, printChain);
        if (Boolean.TRUE.equals(loggerDefinition.getTransmit())) {
            ravenLogger.setTransmitLogger(this.getRavenLogger(logName, loggerDefinition.getScope()));
        }
    }

    /**
     * 加载配置信息
     *
     * @param configuration
     */
    private void loadConfiguration(Configuration configuration) {
        List<ProcessorDefinition> processorDefinitions = configuration.getProcessorDefinitions();
        if (VerifyUtils.notEmpty(processorDefinitions)) {
            for (ProcessorDefinition processorDefinition : processorDefinitions) {
                this.processorDefinitionMap.put(processorDefinition.getId(), processorDefinition);
            }
        }

        List<FormatterDefinition> formatterDefinitions = configuration.getFormatterDefinitions();
        this.formatterDefinitionMap.put(null, FormatterDefinition.getDefault());
        if (VerifyUtils.notEmpty(formatterDefinitions)) {
            for (FormatterDefinition formatterDefinition : formatterDefinitions) {
                this.formatterDefinitionMap.put(formatterDefinition.getId(), formatterDefinition);
            }
        }

        List<PrinterDefinition> printerDefinitions = configuration.getPrinterDefinitions();
        if (VerifyUtils.notEmpty(printerDefinitions)) {
            for (PrinterDefinition printerDefinition : printerDefinitions) {
                this.printerDefinitionMap.put(printerDefinition.getId(), printerDefinition);
            }
        }

        List<LoggerDefinition> loggerDefinitions = configuration.getLoggerDefinitions();
        if (VerifyUtils.notEmpty(loggerDefinitions)) {
            for (LoggerDefinition loggerDefinition : loggerDefinitions) {
                this.loggerDefinitionMap.put(loggerDefinition.getScope(), loggerDefinition);
            }
        }
    }

    /**
     * 根据logName选举出LoggerDefinition
     *
     * @param logName
     * @param exclusionScope
     * @return
     */
    private LoggerDefinition vote(String logName, String exclusionScope) {
        int weight = -1;
        int tmpWeight;
        LoggerDefinition loggerDefinition = null;
        for (Map.Entry<String, LoggerDefinition> entry : this.loggerDefinitionMap.entrySet()) {
            if (VerifyUtils.notNull(exclusionScope) && exclusionScope.equals(entry.getKey())) {
                continue;
            }
            if (!Constants.ASTERISK.equals(entry.getKey()) && !logName.startsWith(entry.getKey())) {
                continue;
            }
            tmpWeight = Constants.ASTERISK.equals(entry.getKey()) ? entry.getKey().length() : logName.length() - entry.getKey().length();
            if (weight == -1 || tmpWeight < weight) {
                loggerDefinition = entry.getValue();
                weight = tmpWeight;
            }
        }
        return loggerDefinition;
    }

    /**
     * 获取format调用链
     *
     * @param formatterRef
     * @return
     * @throws RavenException
     */
    private FormatChain getFormatChain(Integer formatterRef) throws RavenException {
        FormatterDefinition formatterDefinition = this.formatterDefinitionMap.get(formatterRef);
        if (VerifyUtils.isNull(formatterDefinition)) {
            return null;
        }
        AbstractFormatter formatter = this.formatterMap.get(formatterDefinition.getClassName());
        if (VerifyUtils.isNull(formatter)) {
            if (VerifyUtils.isNull(formatterDefinition.getClassName())) {
                formatter = super.getBean(AbstractFormatter.class);
            } else {
                formatter = super.getBean(formatterDefinition.getClassName());
            }
            if (VerifyUtils.isNull(formatter)) {
                return null;
            } else {
                if (VerifyUtils.isNull(formatterDefinition.getClassName())) {
                    formatterDefinition.setClassName(DefaultFormatter.class.getName());
                }
                this.formatterMap.put(formatterDefinition.getClassName(), formatter);
            }
        }
        List<AbstractFormatPostProcessor> formatPostProcessors = null;
        if (VerifyUtils.notEmpty(formatterDefinition.getProcessorRef())) {
            ProcessorDefinition processorDefinition;
            AbstractFormatPostProcessor formatPostProcessor;
            formatPostProcessors = new ArrayList<>();
            for (Integer processorId : formatterDefinition.getProcessorRef()) {
                if (VerifyUtils.isNull(processorId)) {
                    continue;
                }
                processorDefinition = this.processorDefinitionMap.get(processorId);
                if (!ProcessorType.FORMAT.equals(processorDefinition.getType())) {
                    continue;
                }
                formatPostProcessor = this.formatPostProcessorMap.get(processorDefinition.getClassName());
                if (VerifyUtils.isNull(formatPostProcessor)) {
                    formatPostProcessor = super.getBean(processorDefinition.getClassName());
                    this.formatPostProcessorMap.put(processorDefinition.getClassName(), formatPostProcessor);
                }
                if (VerifyUtils.notNull(formatPostProcessor)) {
                    formatPostProcessors.add(formatPostProcessor);
                }
            }
        }
        return new FormatChain(formatter, formatPostProcessors);
    }

    /**
     * 获取print打印输出流
     *
     * @param printerDefinition
     * @return
     * @throws RavenException
     */
    private PrintStream getPrintStream(PrinterDefinition printerDefinition) throws RavenException {
        PrintStream printStream = null;
        if (printerDefinition.getType().equals(PrinterType.SYS_ERR) || printerDefinition.getType().equals(PrinterType.CACHED_SYS_ERR)) {
            printStream = System.err;
        } else if (printerDefinition.getType().equals(PrinterType.SYS_OUT) || printerDefinition.getType().equals(PrinterType.CACHED_SYS_OUT)) {
            printStream = System.out;
        } else if (printerDefinition.getType().equals(PrinterType.FILE)) {
            File file = new File(printerDefinition.getPath());
            try {
                FileOutputStream fos = new FileOutputStream(file);
                printStream = new PrintStream(fos);
            } catch (FileNotFoundException e) {
                throw new RavenException("无法打开[" + printerDefinition.getPath() + "]. 请检查文件路径是否正确", e);
            }
        }
        return printStream;
    }

    /**
     * 获取print调用链
     *
     * @param printerRef
     * @return
     * @throws RavenException
     */
    private PrintChain getPrintChain(Integer printerRef) throws RavenException {
        PrinterDefinition printerDefinition = this.printerDefinitionMap.get(printerRef);
        if (VerifyUtils.isNull(printerDefinition)) {
            return null;
        }
        AbstractPrinter printer = this.printerMap.get(printerDefinition.getClassName());
        if (VerifyUtils.isNull(printer)) {
            if (VerifyUtils.isNull(printerDefinition.getClassName()) || DefaultPrinter.class.getName().equals(printerDefinition.getClassName())) {
                printer = super.getBean(AbstractPrinter.class, Arrays.asList(PrintStream.class), Arrays.asList(this.getPrintStream(printerDefinition)));
            } else {
                printer = super.getBean(printerDefinition.getClassName());
            }
            if (VerifyUtils.isNull(printer)) {
                return null;
            } else {
                if (VerifyUtils.isNull(printerDefinition.getClassName())) {
                    printerDefinition.setClassName(DefaultPrinter.class.getName());
                }
                this.registerObject(printerDefinition.getClassName(), printer);
            }
        }
        List<AbstractPrintPostProcessor> printPostProcessors = null;
        if (VerifyUtils.notEmpty(printerDefinition.getProcessorRef())) {
            ProcessorDefinition processorDefinition;
            AbstractPrintPostProcessor printPostProcessor;
            printPostProcessors = new ArrayList<>();
            for (Integer processorId : printerDefinition.getProcessorRef()) {
                if (VerifyUtils.isNull(processorId)) {
                    continue;
                }
                processorDefinition = this.processorDefinitionMap.get(processorId);
                if (!ProcessorType.PRINT.equals(processorDefinition.getType())) {
                    continue;
                }
                printPostProcessor = this.printPostProcessorMap.get(processorDefinition.getClassName());
                if (VerifyUtils.isNull(printPostProcessor)) {
                    printPostProcessor = super.getBean(processorDefinition.getClassName());
                    this.registerObject(processorDefinition.getClassName(), printPostProcessor);
                }
                if (VerifyUtils.notNull(printPostProcessor)) {
                    printPostProcessors.add(printPostProcessor);
                }
            }
        }
        return new PrintChain(printer, printPostProcessors);
    }

    /**
     * 注册RavenBean对象
     *
     * @param className
     * @param beanName
     * @param object
     */
    public void registerObject(String className, String beanName, Object object) {
        if (VerifyUtils.isNull(object)) {
            return;
        }
        if (VerifyUtils.notNull(beanName)) {
            this.className2BeanNameMap.put(className, beanName);
        }
        if (object instanceof AbstractFormatter) {
            this.formatterMap.put(className, (AbstractFormatter) object);
        } else if (object instanceof AbstractPrinter) {
            this.printerMap.put(className, (AbstractPrinter) object);
        } else if (object instanceof AbstractFormatPostProcessor) {
            this.formatPostProcessorMap.put(className, (AbstractFormatPostProcessor) object);
        } else if (object instanceof AbstractPrintPostProcessor) {
            this.printPostProcessorMap.put(className, (AbstractPrintPostProcessor) object);
        }
    }

    /**
     * 注册RavenBean对象
     *
     * @param className
     * @param object
     */
    public void registerObject(String className, Object object) {
        this.registerObject(className, null, object);
    }

    /**
     * 获取元数据信息
     *
     * @param logName
     * @param loggerDefinition
     * @return
     */
    private Metadata getMetadata(String logName, LoggerDefinition loggerDefinition) throws RavenException {
        FormatterDefinition formatterDefinition = this.formatterDefinitionMap.get(loggerDefinition.getFormatterRef());
        if (VerifyUtils.isNull(formatterDefinition)) {
            return null;
        }
        Metadata metadata = new Metadata(START_TIME, this.raven.getAppId(), this.raven.getAppName());
        metadata.setLoggerId(loggerDefinition.getId());
        metadata.setLogName(logName);
        metadata.setCurrentLogLevel(loggerDefinition.getLevel());
        metadata.setDateTimeFormat(formatterDefinition.getDateTimeFormat());
        metadata.setDateTimeFormatter(new SimpleDateFormat(formatterDefinition.getDateTimeFormat()));
        metadata.setLevelInBrackets(formatterDefinition.getLevelInBrackets());
        metadata.setWarnLevelString(formatterDefinition.getWarnLevelString());
        metadata.setShowAppId(formatterDefinition.getShowAppId());
        metadata.setShowAppName(formatterDefinition.getShowAppName());
        metadata.setShowLoggerId(formatterDefinition.getShowLoggerId());
        metadata.setShowThreadName(formatterDefinition.getShowThreadName());
        metadata.setShowDateTime(formatterDefinition.getShowDateTime());
        metadata.setShowShortLogName(formatterDefinition.getShowShortLogName());
        metadata.setShowLogName(formatterDefinition.getShowLogName());
        metadata.appendExpansionMap(formatterDefinition, loggerDefinition);
        return metadata;
    }

    /**
     * 获取raven Logger
     *
     * @param logName
     * @return
     * @throws RavenException
     */
    public RavenLogger getRavenLogger(String logName) throws RavenException {
        return this.getRavenLogger(logName, null);
    }

    /**
     * 获取raven Logger
     *
     * @param logName
     * @param exclusionScope
     * @return
     * @throws RavenException
     */
    private RavenLogger getRavenLogger(String logName, String exclusionScope) throws RavenException {
        RavenLogger ravenLogger = this.ravenLoggerMap.get(logName);
        if (VerifyUtils.notNull(ravenLogger)) {
            return ravenLogger;
        }
        LoggerDefinition loggerDefinition = this.vote(logName, exclusionScope);
        if (VerifyUtils.isNull(loggerDefinition)) {
            return null;
        }
        FormatChain formatChain = this.getFormatChain(loggerDefinition.getFormatterRef());
        PrintChain printChain = this.getPrintChain(loggerDefinition.getPrinterRef());
        Metadata metadata = this.getMetadata(logName, loggerDefinition);

        ravenLogger = new RavenLogger(metadata, formatChain, printChain, this.updateLock);
        if (Boolean.TRUE.equals(loggerDefinition.getTransmit())) {
            ravenLogger.setTransmitLogger(this.getRavenLogger(logName, loggerDefinition.getScope()));
        }
        this.ravenLoggerMap.put(logName, ravenLogger);
        return ravenLogger;
    }
}
