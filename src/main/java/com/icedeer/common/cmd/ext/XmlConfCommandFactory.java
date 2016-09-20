package com.icedeer.common.cmd.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icedeer.common.cmd.AbstractAppCommand;
import com.icedeer.common.cmd.Command;
import com.icedeer.common.cmd.CommandFactory;
import com.icedeer.common.cmd.CommandOption;

/**
 * <B>Description</B>: the command factory which generate command instance from XML configuration
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Oct, 2013 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */

public class XmlConfCommandFactory implements CommandFactory {
    private static Logger logger = LoggerFactory.getLogger(XmlConfCommandFactory.class);
    public Command createCommand(CommandOption cmdOption) {
        String cmdName = cmdOption.getCommandName();
        String cmdClassname = CommandDefinitionParser.getClassName(cmdName);
        if(cmdClassname == null || cmdClassname.isEmpty()){
            return null;
        }
        try {
            Class cmdClass = Class.forName(cmdClassname);
            Object cmdObj = cmdClass.getConstructor(String.class).newInstance(cmdName);
            if(cmdObj != null){
                if(cmdObj instanceof AbstractAppCommand){
                    AbstractAppCommand cmd = (AbstractAppCommand)cmdObj;
                    return cmd;
                }
                else{
                    throw new ConfigurationException("Can't support command class other then extends AbstractAppCommand for now");
                }
            }
        }catch(ConfigurationException cfe){ 
            logger.error("Could not initialize a command class [{}] for command name of [{}]; the command class is not extended AbstractAppCommand.", cmdClassname, cmdName);
        }catch (Exception e) {
            logger.error("Could not initialize a command class [{}] for command name of [{}]", cmdClassname, cmdName);
            return null;
        }
        
        return null;
    }

}
