package com.icedeer.common.cmd;

import java.util.HashSet;
import java.util.Set;

/**
 * <B>Description</B>:
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2017 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class SystemCommandOption extends CommandOption {
    private static Set<String> definedCommandSet = new HashSet<String>();

    protected static void addSystemCommandName(String name) {
        // in order to avoid duplication name defined with customized name,
        // all system command has to be less than two characters like '-h'
        if (name != null && name.length() <= 2) {
            definedCommandSet.add(name);
        }
    }

    protected static boolean isSystemCommandName(String theName) {
        for (String value : definedCommandSet) {
            if (value.equalsIgnoreCase(theName)) {
                return true;
            }
        }
        return false;
    }

    private String[] args;

    public SystemCommandOption(String commandName) {
        super(commandName);

    }

    public void setArgs(String[] args) {
        this.args = args;
    }

}
