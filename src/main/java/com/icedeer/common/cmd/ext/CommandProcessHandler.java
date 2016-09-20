
package com.icedeer.common.cmd.ext;


/**
 * <B>Description</B>: interface for process handler
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2014 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */

public interface CommandProcessHandler {
    public void preProcess() throws Exception;
    
    public void postProcess() throws Exception;
}
