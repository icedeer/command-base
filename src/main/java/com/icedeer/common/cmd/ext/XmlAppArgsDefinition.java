package com.icedeer.common.cmd.ext;


import java.util.HashMap;
import java.util.Map;

import com.icedeer.common.cmd.AppArgsDefinition;


/**
 * <B>Description</B>: the command argument definition XML extension (all command & otpions are come from XML configuration)
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Oct, 2013 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class XmlAppArgsDefinition extends AppArgsDefinition {
    protected Map<String, AdditionInfo> commandAdditionInfoMap; // key is command name
    protected Map<String, Map<String, AdditionInfo>> optionAdditionInfoMap; // key is command name and option name
    /**
     * @param appName
     * @param caseSensitive
     */
    public XmlAppArgsDefinition(String appName, boolean caseSensitive) {
        super(appName, caseSensitive);
        commandAdditionInfoMap = new HashMap<String, AdditionInfo>();
        optionAdditionInfoMap = new HashMap<String, Map<String, AdditionInfo>>();
    }

    /**
     * @param appName
     */
    public XmlAppArgsDefinition(String appName) {
        this(appName, false);
    }
    

    public void addAdditionalInfo(String cmdName, String optName, String attrName, Object infoObj) {
        Map<String, AdditionInfo> optionMap = optionAdditionInfoMap.get(cmdName);
        if(optionMap == null){
            optionMap = new HashMap<String, AdditionInfo>();
            optionAdditionInfoMap.put(cmdName, optionMap);
        }
        AdditionInfo optInfo = optionMap.get(optName);
        if(optInfo == null){
            optInfo = new AdditionInfo(cmdName+"."+optName);
            optionMap.put(optName, optInfo);
        }
        optInfo.addAttribute(attrName, infoObj);
    }
    
    public void addCmdProperty(String cmdName, String propName, String propValue){
        AdditionInfo cmdInfo = commandAdditionInfoMap.get(cmdName);
        if(cmdInfo == null){
            cmdInfo = new AdditionInfo(cmdName);
            commandAdditionInfoMap.put(cmdName, cmdInfo);
        }
        if(propValue != null){
            cmdInfo.addProperty(propName, propValue.trim());
        }
    }
    
    public AdditionInfo getCmdAdditionInfo(String cmdName){
        return commandAdditionInfoMap.get(cmdName);
    }

}
