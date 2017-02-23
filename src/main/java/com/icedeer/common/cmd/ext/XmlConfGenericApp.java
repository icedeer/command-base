package com.icedeer.common.cmd.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icedeer.common.cmd.AppArgsDefinition;
import com.icedeer.common.cmd.AppContext;
import com.icedeer.common.cmd.Command;
import com.icedeer.common.cmd.CommandFactory;
import com.icedeer.common.cmd.GenericApp;
import com.icedeer.common.cmd.SimpleAppContext;


/**
 * <B>Description</B>: A generic application with loading all command definitions from
 * xml file.
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Oct, 2014 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class XmlConfGenericApp extends GenericApp {
    private static Logger logger = LoggerFactory.getLogger("XmlConf");

    private CommandProcessHandler processHandler;
    private CommandFactory commandFactory;
    private AppArgsDefinition appArgsDef;
    private SimpleAppContext appContext;
    private CommandDefinitionParser defParser;

    public XmlConfGenericApp() {
        super();
        defParser = new CommandDefinitionParser("command_def.xml");
        try {
            logger.debug("Start to parsing command_def.xml >>");

            appArgsDef = defParser.parse();

            logger.debug("Finishing parsing the command_def.xml <<");
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
        this.appContext = new SimpleAppContext(appArgsDef);
        this.commandFactory = new XmlConfCommandFactory();

    }

    /*
     * (non-Javadoc)
     */
    @Override
    public AppArgsDefinition getAppArgsDefinition() {
        return appArgsDef;
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public AppContext getAppContext() {
        return appContext;
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    protected void setCommandFactory(CommandFactory factory) {
        this.commandFactory = factory;
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public void initialAppConfiguration() throws Exception {

    }

    /*
     * (non-Javadoc)
     */
    @Override
    public void postProcess(Command command) throws Exception {
        if (processHandler != null) {
            processHandler.postProcess();
        }
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public void preProcess(Command command) throws Exception {
        if (processHandler != null) {
            processHandler.preProcess();
        }
    }

    public CommandProcessHandler createProcessHandler(Command command) throws ConfigurationException {
        // TODO load configuration from xml file and create an instance for it.
        return null;
    }

    protected void addContextAttribute(String key, Object value) {
        this.appContext.setAttribute(key, value);
    }

}
