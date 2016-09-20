package com.icedeer.common.cmd.ext;


import java.util.HashMap;
import java.util.Map;

/**
 * <B>Description</B>: object contains additional information of the command 
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2014 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class AdditionInfo {

    private String owner;
    private Map<String, Object> attrMap;
    
    public AdditionInfo(String owner){
        this.owner = owner;
        this.attrMap = new HashMap<String, Object>();
    }
    
    public void addAttribute(String attrName, Object attrObj) {
        attrMap.put(attrName, attrObj);
    }
    
    public void addProperty(String name, String value){
        attrMap.put("Prop:"+name, value);
    }

    public String getProperty(String name){
        if(attrMap.containsKey("Prop:"+name)){
            return (String) attrMap.get("Prop:"+name);
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<AdditionInfo owner=\""+owner+"\">\n");
        for(String name : attrMap.keySet()){
            sb.append("<Info name=\""+name+"\">");
            sb.append(attrMap.get(name));
            sb.append("</Info>\n");
        }
        sb.append("</AddtionInfo>\n");
        return sb.toString();
    }
}
