package com.icedeer.common.cmd.ext;

import com.icedeer.common.cmd.AbstractAppCommand;


/**
 * <B>Description</B>: a default help command implementation, to print out the command definition usage 
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Oct, 2013 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class HelpCommand extends AbstractAppCommand {

        /**
         * @param name
         */
        public HelpCommand(String name) {
            super(name);
        }

        /* (non-Javadoc)
         * @see com.cibc.ecif.report.cmd.Command#execute()
         */
        public void execute() throws Exception {
            System.out.println(getAppContext().getAppArgsDefinition());
        }

}
