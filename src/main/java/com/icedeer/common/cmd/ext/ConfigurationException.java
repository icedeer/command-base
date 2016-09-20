package com.icedeer.common.cmd.ext;

/**
 * <B>Description</B>: Any exception related to XML configuration 
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Oct, 2013 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class ConfigurationException extends Exception {

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ConfigurationException(String arg0) {
        super(arg0);
    }

    public ConfigurationException(Throwable arg0) {
        super(arg0);
    }

}
