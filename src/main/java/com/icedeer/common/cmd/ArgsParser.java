package com.icedeer.common.cmd;

import static com.icedeer.common.cmd.ArgErrorEnum.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <B>Description</B>:
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- May, 2014 -- Initial Draft</LI>
 * <LI> Peter W -- Feb, 2017 -- adding -d debug option as built in Global option</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class ArgsParser {
    private AppArgsDefinition argsDefinition;

    public ArgsParser(AppArgsDefinition argsDefinition) {
        this.argsDefinition = argsDefinition;
    }

    /**
     * return a CommandOption object by parsing the input command line arguments
     * @param args
     * @return
     * @throws ArgumentValidationException
     * TODO 1) handle Empty Option (no argument)
     *      2) support relationship between CommandOptions like dependency (A has to be there if B presents)
     *      3) validation on situation of missing value "-command -optionA -optionB value"; if -optionA is missing value it reports that "-optionB" as the value
     */
    public CommandOption parseArguments(String[] args) throws ArgumentValidationException {
        if (args == null || args.length < 1) {
            throw new ArgumentValidationException(EMPTY_ARG_LIST);
        }
        String commandName = removePrefix(args[0], ArgDefConst.CMD_PREFIX);

        // check System option
        if (SystemCommandOption.isSystemCommandName(commandName)) {
            SystemCommandOption sco = new SystemCommandOption(commandName);
            sco.setArgs(args);
            return sco;
        }

        if (!argsDefinition.hasCommandDefined(commandName)) {
            throw new ArgumentValidationException(INVALID_COMMAND_NAME, commandName);
        }
        // adding additional length check, command or option has to be more than 2 characters
        else if (commandName.trim().length() <= 2) {
            throw new ArgumentValidationException(MINIMUM_LENGTH_NAME, commandName);
        } else {
            // use command name defined in Definition since it could have case difference
            commandName = argsDefinition.getDefinedCommandName(commandName);
        }
        CommandOption cmdOption = new CommandOption(commandName);

        int left = args.length - 1;
        int index = 1;
        while (index <= left) {
            index = parseOption(cmdOption, args, index);
        }

        // check if mandatory options are missing
        List<String> mandatoryOptionNames = argsDefinition.getMandatoryOptionNames(commandName);
        List<String> missingList = new ArrayList<String>();
        if (mandatoryOptionNames != null) {
            for (String optionName : mandatoryOptionNames) {
                String defaultValue = argsDefinition.getDefaultValue(commandName, optionName);
                if (!cmdOption.containOption(optionName) && defaultValue == null) {
                    missingList.add(optionName);
                }
            }

            if (!missingList.isEmpty()) {
                throw new ArgumentValidationException("Missing required option(s) -->" + missingList);
            }
        }

        // append default value for NOT-INPUTED options
        List<String> allOptions = argsDefinition.getAllOptionNames(commandName);
        if (allOptions != null) {
            for (String optionName : allOptions) {
                if (!cmdOption.containOption(optionName)) {
                    // try to get default value
                    String defaultValue = argsDefinition.getDefaultValue(commandName, optionName);
                    if (defaultValue != null && defaultValue.trim().length() > 0) {
                        cmdOption.addDefaultOption(optionName, defaultValue);
                    }
                } else {
                    // try to check if option is enumeration and the given value is correct
                    if (argsDefinition.hasOptionPreDefinedValueRequired(commandName, optionName)) {
                        String[] preDefinedValues = argsDefinition.getOptionPreDefinedValue(commandName, optionName);
                        String value = cmdOption.getOptionValue(optionName);
                        boolean found = false;
                        for (String preDefinedValue : preDefinedValues) {
                            if (value.equalsIgnoreCase(preDefinedValue)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            throw new ArgumentValidationException("Input option value [" + value + "] is not valid");
                        }
                    }
                }
            }
        }

        return cmdOption;
    }

    protected int parseOption(CommandOption commandOption, String args[], int index) throws ArgumentValidationException {
        String commandName = commandOption.getCommandName();

        String optionName = removePrefix(args[index], ArgDefConst.OPT_PREFIX);
        if (optionName != null) {
            optionName = optionName.trim();
            // adding global option check
            if (isGlobalOption(commandName, optionName)) {
                return setGlobalOption(commandOption, commandName, optionName, index);
            }

        }
        // check if the option name is valid
        if (!argsDefinition.hasOptionDefined(commandName, optionName)) {
            throw new ArgumentValidationException(INVALID_OPTION_NAME, optionName, commandName);
        }

        if (optionName.length() <= 2) {
            // adding additional length check, command or option has to be more than 2 characters
            throw new ArgumentValidationException(MINIMUM_LENGTH_NAME, optionName);
        }

        if (argsDefinition.hasOptionValueRequired(commandName, optionName)) {
            if (args.length <= index + 1) {
                throw new ArgumentValidationException(REQUIRE_OPTION_VALUE, optionName);
            }
            String value = args[index + 1];
            commandOption.addOption(argsDefinition.getDefinedOptionName(commandName, optionName), value);
            return index + 2;
        } else {
            commandOption.addSingleFlagOption(argsDefinition.getDefinedOptionName(commandName, optionName));
            return index + 1;
        }

    }

    private int setGlobalOption(CommandOption commandOption, String commandName, String optionName, int index) {
        if (optionName.equalsIgnoreCase("d")) {
            commandOption.addOption(GlobalOptionEnum.DEBUG.name(), "true");
            return index + 1;
        }
        return index;
    }

    private boolean isGlobalOption(String commandName, String optionName) {
        // for now, adding '-d" as debug; user may set customized option as "debug", so enforce check on 'd' only
        if (optionName.equalsIgnoreCase("d")) {
            return true;
        }
        return false;
    }

    private String removePrefix(String name, char prefix) {
        if (name == null || name.trim().length() == 0) {
            return name;
        }
        if (name.charAt(0) != prefix) {
            return name;
        }
        return name.substring(1);
    }

}
