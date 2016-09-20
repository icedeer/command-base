package com.icedeer.common.cmd;

import java.util.HashMap;
import java.util.Map;


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
public class SimpleAppContext implements AppContext {
	private AppArgsDefinition argsDefinition;
	private Map<String, Object> attrMap;
	
	/**
	 * 
	 */
	public SimpleAppContext(AppArgsDefinition argsDefinition) {
		this.argsDefinition = argsDefinition;
		attrMap = new HashMap<String, Object>();
	}
	

	/* (non-Javadoc)
	 * @see com.icedeer.cmd.AppContext#getAppArgsDefinition()
	 */
	public AppArgsDefinition getAppArgsDefinition() {
		return argsDefinition;
	}

	/* (non-Javadoc)
	 * @see com.icedeer.cmd.AppContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String attrName) {
		return attrMap.get(attrName);
	}
	
	public void setAttribute(String attrName, Object value){
		attrMap.put(attrName, value);
	}

}
