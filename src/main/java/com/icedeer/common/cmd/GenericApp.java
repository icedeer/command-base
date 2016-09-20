package com.icedeer.common.cmd;


/**
 * <B>Description</B>: 
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2011 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public abstract class GenericApp {
	
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
	public void executeApp(String[] args) throws ArgumentValidationException, Exception{
	    // load initial configuration first
	    initialAppConfiguration();
	    
		AppArgsDefinition argsDefinition = getAppArgsDefinition();
		
		// Step 1:  get command options
		ArgsParser argsParser = new ArgsParser(argsDefinition);
		CommandOption commandOption = argsParser.parseArguments(args);
		
		// STEP 2: construct a customized implementation of Command
		Command targetCommand = null;
	
		targetCommand = getCommandFactory().createCommand(commandOption);
		if(targetCommand != null){
			// initial command
			targetCommand.initial();
			// set ApplicationContext
			if(targetCommand instanceof AbstractAppCommand){
				((AbstractAppCommand)targetCommand).setAppContext(getAppContext());
			}
			// validate runtime command options 
			targetCommand.setAndValidateCommandOptions(commandOption);
		}
		else{
			throw new ArgumentValidationException("Couldn't support command \""+commandOption.getCommandName()+"\" for now.");
		}
		//	STEP 3: execute pre-process
		preProcess(targetCommand);
		
		// STEP 4: execute command
		targetCommand.execute();
		
		// STEP 5: execute post-process
		postProcess(targetCommand);
	}
	
}
