package com.icedeer.common.cmd.ext;

import com.icedeer.common.cmd.AbstractAppCommand;
import com.icedeer.common.cmd.ArgumentValidationException;
import com.icedeer.common.cmd.CommandOption;


/**
 * <B>Description</B>: abstract command for adding support of configuration from XML
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2014 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */

public abstract class AbstractXmlConfCommand extends AbstractAppCommand {

    
    /**
     * @param name
     */
    public AbstractXmlConfCommand(String name) {
        super(name);
    }

    

    @Override
    public void setAndValidateCommandOptions(CommandOption cmdOption) throws ArgumentValidationException {
        super.setAndValidateCommandOptions(cmdOption);
        // beside, it needs to validate any additional parameter passed in via XML configuration
        
        // check option "type", apply basic data type validation
    }
    
    public String getCmdAddtionInfoProperty(String propName){
        XmlAppArgsDefinition appDef = (XmlAppArgsDefinition) this.getAppContext().getAppArgsDefinition();
        AdditionInfo info = appDef.getCmdAdditionInfo(this.getName());
        if(info !=null ){
            return info.getProperty(propName);
        }
        return null;
    }

    
}
