package com.icedeer.common.cmd.ext;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.icedeer.common.cmd.AppArgsDefinition;
import com.icedeer.common.util.xml.DomHelper;



/**
 * <B>Description</B>: XML parser for parsing command_def.xml to get command definition
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2014 -- Initial Draft</LI>
 * <LI> Peter W --- Aug, 2016 -- to provide generic properties applying for all commands and it can be override on each command level</LI>
 * </UL>
 * 
 */

public class CommandDefinitionParser {
    public static final String OPTION_TYPE = "OPTION_TYPE";
    
    private String filename;
    private static Map<String, String> cmdClassnameMap = new HashMap<String, String>();
    
    public CommandDefinitionParser(String filename){
        this.filename = filename;
    }
    
    public AppArgsDefinition parse()throws ConfigurationException{
        // update AppArgsDefinition from xml and update command-class-mapping
        try{
            // search the file from classpath first; then the working directory
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
            if(is == null){
                is = new FileInputStream(filename);
            }
            
            Document doc = DomHelper.parseXml(is, false);
            Element appEle = doc.getDocumentElement();
            String appName = appEle.getAttribute("name");
            XmlAppArgsDefinition appArgsDef = new XmlAppArgsDefinition(appName);
            
            /** adding global property
             *  Aug 23, 2016 ---- to provide generic properties applying for all commands and it can be overide on each command level
             */
            List<Element> globalPropertyEleList = DomHelper.getChildElementsByTagName(appEle, "GlobalProperty");
            Map<String, String> globalPropertyMap = new HashMap<String, String>();
            if(globalPropertyEleList != null && !globalPropertyEleList.isEmpty()){
                for(Element globalPropertyEle : globalPropertyEleList){
                    String name = globalPropertyEle.getAttribute("name");
                    String value = globalPropertyEle.getTextContent();
                    if(name != null && name.trim().length() > 0){
                        globalPropertyMap.put(name.trim(), value == null ?"":value.trim());
                    }
                }
            }
            
            // get all command element
            List<Element> cmdEleList = DomHelper.getChildElementsByTagName(appEle, "Command");
            if(cmdEleList != null && !cmdEleList.isEmpty()){
                Map<Integer, String> cmdOrderMap = new TreeMap<Integer, String>();
                
                for(Element cmdEle : cmdEleList){
                    String cmdName = cmdEle.getAttribute("name");
                    String cmdClassname = cmdEle.getAttribute("class");
                    int index = Integer.parseInt(cmdEle.getAttribute("index"));
                    cmdClassnameMap.put(cmdName.toLowerCase(), cmdClassname);
                    
                    cmdOrderMap.put(index, cmdName);
                    
                    appArgsDef.addCommand(cmdName);
                    
                    //check options
                    List<Element> optEleList = DomHelper.getChildElementsByTagName(cmdEle, "Option");
                    if(optEleList != null){
                        for(Element optEle : optEleList){
                            String optName = optEle.getAttribute("name");
                            int optIndex = Integer.parseInt(optEle.getAttribute("index"));
                            String optDesc = optEle.getAttribute("desc");
                            boolean optMandatory=Boolean.parseBoolean(optEle.getAttribute("mandatory"));
                            boolean optHasValue = Boolean.parseBoolean(optEle.getAttribute("hasValue"));
                            String type = optEle.getAttribute("type");
                            if(type != null && type.trim().length() >0 ){
                                // since type is additional field, add it to the additional info
                                appArgsDef.addAdditionalInfo(cmdName, optName, OPTION_TYPE, type);
                            }
                            
                            // check if has "value (default or list)
                            List<Element> valueEleList = DomHelper.getChildElementsByTagName(optEle, "Value");
                            if(valueEleList == null || valueEleList.isEmpty()){
                                appArgsDef.addOption(cmdName, optName, optIndex, optDesc, optMandatory, optHasValue);
                            }
                            else if(valueEleList.size() == 1){
                                // only one value so it's always the default one
                                String defValue = valueEleList.get(0).getTextContent();
                                appArgsDef.addOption(cmdName, optName, optIndex, optDesc, optMandatory, optHasValue, defValue);
                            }
                            else{
                                String defValue = null;
                               
                                // has selection values
                                String[] values = new String[valueEleList.size()];
                                for(int i=0; i<valueEleList.size(); i++){
                                    Element valueEle = valueEleList.get(i);
                                    values[i] = valueEle.getTextContent().trim();
                                    if(valueEle.getAttribute("default") != null && "true".equalsIgnoreCase(valueEle.getAttribute("default"))){
                                        defValue = values[i];
                                    }
                                }
                                
                                appArgsDef.addSelectionValueOption(cmdName, optName, optIndex, optDesc, optMandatory, values, defValue);
                            }
                            
                        }
                    }
                    
                    // apply global properties first 
                    if(!globalPropertyMap.isEmpty()){
                        for(String propName: globalPropertyMap.keySet()){
                            appArgsDef.addCmdProperty(cmdName, propName, globalPropertyMap.get(propName));
                        }
                        
                    }
                    
                    // adding addition information of the command if it applies
                    List<Element> propsEleList = DomHelper.getChildElementsByTagName(cmdEle, "Properties");
                    if(propsEleList != null && !propsEleList.isEmpty()){
                        for(Element propsEle : propsEleList){
                            List<Element> propEleList = DomHelper.getChildElementsByTagName(propsEle, "Property");
                            if(propEleList != null && !propEleList.isEmpty()){
                                for(Element propEle : propEleList){
                                    appArgsDef.addCmdProperty(cmdName, propEle.getAttribute("name"), propEle.getTextContent());
                                }
                            }
                        }
                    }
                }
                String[] orderedNames = new String[cmdOrderMap.size()];
                new ArrayList<String>(cmdOrderMap.values()).toArray(orderedNames);
                appArgsDef.setCmdDisplayOrder(orderedNames);
            }
            
            return appArgsDef;
        }catch(Exception e){
            throw new ConfigurationException("Got error while loading command-definition xml", e);
        }
    }
    
    public static String getClassName(String cmdName){
        if(cmdName != null){
            return cmdClassnameMap.get(cmdName.trim().toLowerCase());
        }
        return null;
    }
}
