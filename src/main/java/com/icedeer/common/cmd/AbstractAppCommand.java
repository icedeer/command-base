package com.icedeer.common.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Description</B>: 
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2014 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */

public abstract class AbstractAppCommand implements Command {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private AppContext appContext;
    protected CommandOption cmdOption;
    private String name;

    public AbstractAppCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     */
    public AppContext getAppContext() {
        return appContext;
    }

    /*
     * (non-Javadoc)
     */
    public void setAndValidateCommandOptions(CommandOption cmdOption) throws ArgumentValidationException {
        this.cmdOption = cmdOption;
    }

    protected void setAppContext(AppContext appContext) {
        this.appContext = appContext;
    }

    public CommandOption getCommandOption() {
        return cmdOption;
    }

    public void initial() throws Exception {
        // do nothing
    }

    // client can fill in some logic of close resource, like close DB connection
    public void finalizeBeforeExit() {
        // do nothing
    }

    /**
     * save the message into the log file regarding the logging level; print the same message to the console
     * 
     * @param message
     */
    protected void logInfo(String message) {
        logger.info(message);
    }

    protected void logError(String message, Exception e) {
        logger.error(message, e);
    }

    public boolean isDebugEnabled() {
        // check commandOption contains "debug"
        return getCommandOption().getBooleanOptionValue(GlobalOptionEnum.DEBUG.name());
    }
}
