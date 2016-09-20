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
public interface AppContext {
	public AppArgsDefinition getAppArgsDefinition();
	
	public Object getAttribute(String attrName);
}
