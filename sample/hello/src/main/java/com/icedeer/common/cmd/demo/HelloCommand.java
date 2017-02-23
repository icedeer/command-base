package com.icedeer.common.cmd.demo;

import com.icedeer.common.cmd.ext.AbstractXmlConfCommand;

/**
 * Hello world!
 *
 */
public class HelloCommand extends AbstractXmlConfCommand{

    public HelloCommand(String name) {
        super(name);
    }

    public void execute() throws Exception {
        String name = getCommandOption().getOptionValue("name");
        String country = getCommandOption().getOptionValue("country");
        
        logger.info("Your name is [{}] and your country is [{}]", name, country);
        
        System.out.println("Hello "+name + (country == null ? "": " from "+country));
        
    }
}
