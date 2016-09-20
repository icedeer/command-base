
package com.icedeer.common.cmd.ext;

import com.icedeer.common.cmd.AppArgsDefinition;
import com.icedeer.common.cmd.AppContext;
import com.icedeer.common.cmd.Command;
import com.icedeer.common.cmd.CommandFactory;
import com.icedeer.common.cmd.GenericApp;
import com.icedeer.common.cmd.SimpleAppContext;

/**
 * <B>Description</B>: A generic application with loading all command definitions from xml file.
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Oct, 2013 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class XmlConfGenericApp extends GenericApp {
    private CommandProcessHandler processHandler;
    private CommandFactory commandFactory;
    private AppArgsDefinition appArgsDef;
    private AppContext appContext;
    private CommandDefinitionParser defParser;

    
    public XmlConfGenericApp() {
        super();
        defParser = new CommandDefinitionParser("command_def.xml");
        try {
            appArgsDef = defParser.parse();
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
        this.appContext = new SimpleAppContext(appArgsDef);
        this.commandFactory = new XmlConfCommandFactory();
        
    }

    /* (non-Javadoc)
     * @see com.icedeer.common.cmd.GenericApp#getAppArgsDefinition()
     */
    @Override
    public AppArgsDefinition getAppArgsDefinition() {
        return appArgsDef;
    }

    /* (non-Javadoc)
     * @see com.icedeer.common.cmd.GenericApp#getAppContext()
     */
    @Override
    public AppContext getAppContext() {
        return appContext;
    }

    /* (non-Javadoc)
     * @see com.icedeer.common.cmd.GenericApp#getCommandFactory()
     */
    @Override
    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    /* (non-Javadoc)
     * @see com.icedeer.common.cmd.GenericApp#initialAppConfiguration()
     */
    @Override
    public void initialAppConfiguration() throws Exception {
        
    }

    /* (non-Javadoc)
     * @see com.icedeer.common.cmd.GenericApp#postProcess(com.icedeer.common.cmd.Command)
     */
    @Override
    public void postProcess(Command command) throws Exception {
        if(processHandler != null){
            processHandler.postProcess();
        }
    }

    /* (non-Javadoc)
     * @see com.icedeer.common.cmd.GenericApp#preProcess(com.icedeer.common.cmd.Command)
     */
    @Override
    public void preProcess(Command command) throws Exception {
        if(processHandler != null){
            processHandler.preProcess();
        }
    }
    
    public CommandProcessHandler createProcessHandler(Command command) throws ConfigurationException{
        // TODO load configuration from xml file and create an instance for it.
        return null;
    }

}
