package com.icedeer.common.cmd.ext;

import com.icedeer.common.cmd.AbstractAppCommand;

/**
 * <B>Description</B>: 
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Oct, 2014 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class HelpCommand extends AbstractAppCommand {
    private boolean showHiddenCommand = false;

    /**
     * @param name
     */
    public HelpCommand(String name) {
        super(name);
    }

    public HelpCommand(String name, boolean showHidden) {
        super(name);
        this.showHiddenCommand = showHidden;
    }

    protected void setShowHiddenCommand(boolean showHidden) {
        this.showHiddenCommand = showHidden;
    }

    /*
     * (non-Javadoc)
     */
    public void execute() throws Exception {
        if (showHiddenCommand) {
            System.out.println(getAppContext().getAppArgsDefinition().showAllCommand());
        } else {
            System.out.println(getAppContext().getAppArgsDefinition());
        }
    }

}
