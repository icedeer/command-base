package com.icedeer.common.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

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
public abstract class GenericApp {
    private static Logger logger = LoggerFactory.getLogger(GenericApp.class);

    public abstract CommandFactory getCommandFactory();

    public abstract AppArgsDefinition getAppArgsDefinition();

    public abstract AppContext getAppContext();

    public abstract void preProcess(Command command) throws Exception;

    public abstract void postProcess(Command command) throws Exception;

    public abstract void initialAppConfiguration() throws Exception;

    /**
     * template method
     *
     */
    public void executeApp(String[] args) throws ArgumentValidationException, Exception {

        // load initial configuration first
        initialAppConfiguration();

        AppArgsDefinition argsDefinition = getAppArgsDefinition();
        
        // check args == null; by default, display the usage
        if(args == null || args.length == 0){
            System.out.println(getAppArgsDefinition());
            return;
        }

        // Step 1: get command options
        ArgsParser argsParser = new ArgsParser(argsDefinition);
        
        CommandOption commandOption = argsParser.parseArguments(args);

        // Step 1.1 set up debug mode
        if (commandOption.getBooleanOptionValue(GlobalOptionEnum.DEBUG.name())) {
            // turn on the debug logging, for now
            logger.info("Turn on Debug model");
            ch.qos.logback.classic.Logger rootLog = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            rootLog.setLevel(Level.DEBUG);

            logger.debug("CommandOption instance for {} -->{}", commandOption.getCommandName(), commandOption);
        }

        // STEP 2: construct a customized implementation of Command
        AbstractAppCommand targetCommand = null;

        targetCommand = (AbstractAppCommand) getCommandFactory().createCommand(commandOption);
        if (targetCommand != null) {
            // initial command
            targetCommand.initial();
            // set ApplicationContext
            targetCommand.setAppContext(getAppContext());
            // validate runtime command options
            targetCommand.setAndValidateCommandOptions(commandOption);
        } else {
            throw new ArgumentValidationException("Couldn't support command \"" + commandOption.getCommandName() + "\" for now.");
        }

        try {

            // STEP 3: execute pre-process
            preProcess(targetCommand);

            // STEP 4: execute command
            targetCommand.execute();

            // STEP 5: execute post-process
            postProcess(targetCommand);
        } finally {
            if (targetCommand != null) {
                logger.debug("Finalize the command process...");
                targetCommand.finalizeBeforeExit();
            }
        }
    }

    protected void addSystemCommandName(String theName) {
        SystemCommandOption.addSystemCommandName(theName);
    }
}
