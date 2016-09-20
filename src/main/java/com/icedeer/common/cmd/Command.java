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
public interface Command {
	public void initial() throws Exception;
	
	public AppContext getAppContext();
	
	public void setAndValidateCommandOptions(CommandOption cmdOption) throws ArgumentValidationException;
	
	public void execute() throws Exception;
	
	public String getName();
	
	public CommandOption getCommandOption();
	
}
