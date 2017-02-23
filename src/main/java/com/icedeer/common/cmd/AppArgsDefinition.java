package com.icedeer.common.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>: This class contains the command line definitions for a given java application.
 * <p/>The definition contains two parts: an application can have multiple command arguments; and each
 * command argument can have 0-multiple options.
 * <br/>The option is defined as inner class of <code>OptionDef</code>. It has some attributes like option 
 * description, if it requires a option value and if the option is mandatory to the command argument. <br/>
 * The option also can have sub-option which is depends on the current option (sub-option will be valid only
 * if it's parent options represents). The value of option also can be a enumeration choice. 
 * <p><b>Limitation:</b>
 * <ul><li>Since all the options and commands are from command line, so all the option value are accepted as 
 * String first and rely on the application to interpret it into appropriate value other than string</li>
 * <li>The option doesn't accept multiple values</li>
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2014 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class AppArgsDefinition {
    protected String appName;
    protected boolean caseSensitive;

    protected Map<String, Map<String, OptionDef>> defMap; // key is command name, value contains all associated options

    protected String[] commandDisplayOrder;

    public AppArgsDefinition(String appName, boolean caseSensitive) {
        this.appName = appName;
        this.defMap = new HashMap<String, Map<String, OptionDef>>();
        this.caseSensitive = caseSensitive;
    }

    public AppArgsDefinition(String appName) {
        this(appName, false);
    }

    public void setCmdDisplayOrder(String[] orders) {
        this.commandDisplayOrder = orders;
    }

    public void addCmdToEndOfDisplayOrder(String name) {
        if (commandDisplayOrder != null) {
            List<String> theList = new ArrayList<String>(Arrays.asList(commandDisplayOrder));
            if (theList.contains(name)) {
                return;
            }
            // since theList is final, can't add more
            theList.add(name);

            commandDisplayOrder = theList.toArray(new String[theList.size()]);
        } else {
            commandDisplayOrder = new String[] { name };
        }
    }

    /**
     * check if the definition contains input command name
     * @param commandName
     * @return
     */
    public boolean hasCommandDefined(String commandName) {
        if (commandName == null) {
            return false;
        }

        if (caseSensitive) {
            return defMap.containsKey(commandName);
        }
        for (String cmdName : defMap.keySet()) {
            if (cmdName.equalsIgnoreCase(commandName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * return the command name defined in this definition; in case sensitive situation, it
     *  will return null if no exactly matched name found; 
     * @param commandName
     * @return
     */
    public String getDefinedCommandName(String commandName) {
        if (commandName == null) {
            return null;
        }

        if (caseSensitive) {
            if (defMap.containsKey(commandName)) {
                return commandName;
            } else {
                return null;
            }
        }
        for (String cmdName : defMap.keySet()) {
            if (cmdName.equalsIgnoreCase(commandName)) {
                return cmdName;
            }
        }
        return null;
    }

    public String getDefinedOptionName(String commandName, String inputOption) {
        OptionDef optDef = lookupOption(commandName, inputOption);
        if (optDef != null) {
            return optDef.name;
        }
        return null;
    }

    public boolean hasOptionDefined(String commandName, String optionName) {
        OptionDef optDef = lookupOption(commandName, optionName);
        return optDef != null;
    }

    public boolean hasOptionValueRequired(String commandName, String optionName) {
        OptionDef optDef = lookupOption(commandName, optionName);
        if (optDef == null) {
            return false;
        }
        return optDef.requireValue;
    }

    public boolean hasOptionPreDefinedValueRequired(String commandName, String optionName) {
        OptionDef optDef = lookupOption(commandName, optionName);
        if (optDef == null) {
            return false;
        }
        return optDef.selectionValues != null && optDef.selectionValues.length > 0;
    }

    public String[] getOptionPreDefinedValue(String commandName, String optionName) {
        OptionDef optDef = lookupOption(commandName, optionName);
        if (optDef == null) {
            return null;
        }
        return optDef.selectionValues;
    }

    public String getDefaultValue(String commandName, String optionName) {
        OptionDef optDef = lookupOption(commandName, optionName);
        if (optDef == null) {
            return null;
        }
        return optDef.defaultValue;
    }

    public List<String> getMandatoryOptionNames(String commandName) {
        if (commandName == null || commandName.trim().length() == 0) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        Map<String, OptionDef> aMap = null;
        if (caseSensitive) {
            aMap = defMap.get(commandName);
        } else {
            for (String cmdName : defMap.keySet()) {
                if (cmdName.equalsIgnoreCase(commandName)) {
                    aMap = defMap.get(cmdName);
                    break;
                }
            }
        }
        if (aMap == null) {
            return null;
        }
        for (OptionDef optDef : aMap.values()) {
            if (optDef.mandatory) {
                result.add(optDef.name);
            }
        }
        return result;
    }

    public List<String> getAllOptionNames(String commandName) {
        if (commandName == null || commandName.trim().length() == 0) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        Map<String, OptionDef> aMap = null;
        if (caseSensitive) {
            aMap = defMap.get(commandName);
        } else {
            for (String cmdName : defMap.keySet()) {
                if (cmdName.equalsIgnoreCase(commandName)) {
                    aMap = defMap.get(cmdName);
                    break;
                }
            }
        }
        if (aMap == null) {
            return null;
        }
        for (OptionDef optDef : aMap.values()) {
            result.add(optDef.name);
        }
        return result;
    }

    public void addCommand(String commandName) {
        defMap.put(commandName, new HashMap<String, OptionDef>());
    }

    public void addOption(String commandName, String optionName, int index, boolean mandatory, boolean hasValue, String defaultValue) {
        addOption(commandName, optionName, index, null, mandatory, hasValue, null, defaultValue);
    }

    public void addOption(String commandName, String optionName, int index, String optionDescription, boolean mandatory, boolean hasValue) {
        addOption(commandName, optionName, index, optionDescription, mandatory, hasValue, null, null);
    }

    public void addOption(String commandName, String optionName, int index, boolean mandatory, boolean hasValue) {
        addOption(commandName, optionName, index, null, mandatory, hasValue, null, null);
    }

    public void addOption(String commandName, String optionName, int index, String optionDescription, boolean mandatory, boolean hasValue,
            String defaultValue) {
        addOption(commandName, optionName, index, optionDescription, mandatory, hasValue, null, defaultValue);
    }

    public void addSelectionValueOption(String commandName, String optionName, int index, String optionDescription, boolean mandatory,
            String[] selectionValues, String defaultValue) {
        addOption(commandName, optionName, index, optionDescription, mandatory, true, selectionValues, defaultValue);
    }

    protected void addOption(String commandName, String optionName, int index, String optionDescription, boolean mandatory,
            boolean hasValue, String[] selectionValues, String defaultValue) {
        Map<String, OptionDef> optMap = defMap.get(commandName);
        if (optMap == null) {
            optMap = new HashMap<String, OptionDef>();
            defMap.put(commandName, optMap);
        }

        OptionDef opt = new OptionDef();
        opt.name = optionName;
        opt.valueDescription = optionDescription;
        opt.mandatory = mandatory;
        opt.requireValue = hasValue;
        opt.defaultValue = defaultValue;
        opt.selectionValues = selectionValues;
        opt.index = index;

        optMap.put(optionName, opt);
    }

    public void addSubOption(String commandName, String parentOptionName, String optionName, String optionDescription, boolean mandatory,
            boolean hasValue, String[] selectionValues, String defaultValue) {
        Map<String, OptionDef> optMap = defMap.get(commandName);
        if (optMap == null) {
            optMap = new HashMap<String, OptionDef>();
            defMap.put(commandName, optMap);
        }
        OptionDef parentOption = lookupOption(commandName, parentOptionName);
        if (parentOption == null) {
            return;
        }

        OptionDef opt = new OptionDef();
        opt.name = optionName;
        opt.valueDescription = optionDescription;
        opt.mandatory = mandatory;
        opt.requireValue = hasValue;
        opt.defaultValue = defaultValue;
        opt.selectionValues = selectionValues;

        optMap.put(optionName, opt);
        parentOption.addSubOption(opt);
    }

    protected OptionDef lookupOption(String commandName, String optionName) {
        if (commandName == null || commandName.trim().length() == 0 || optionName == null || optionName.trim().length() == 0) {
            return null;
        }
        if (caseSensitive) {
            Map<String, OptionDef> aMap = defMap.get(commandName);
            if (aMap == null) {
                return null;
            }
            return aMap.get(optionName);
        } else {
            for (String cmdName : defMap.keySet()) {
                if (cmdName.equalsIgnoreCase(commandName)) {
                    Map<String, OptionDef> aMap = defMap.get(cmdName);
                    for (String optName : aMap.keySet()) {
                        if (optName.equalsIgnoreCase(optionName)) {
                            return aMap.get(optName);
                        }
                    }
                }
            }
            return null;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(appName + " Usage :" + ArgDefConst.NEW_LINE);
        String[] names = commandDisplayOrder;
        if (names == null || names.length == 0) {
            List<String> allCommandNames = new ArrayList<String>(defMap.keySet());
            Collections.sort(allCommandNames);
            names = allCommandNames.toArray(new String[allCommandNames.size()]);
        }
        for (String cmdName : names) {
            String commandUsageStr = getCommandUsage(cmdName);
            if (commandUsageStr != null) {
                sb.append(ArgDefConst.NEW_LINE + commandUsageStr);
            }
        }
        return sb.toString();
    }

    private String getCommandUsage(String commandName) {
        if (!hasCommandDefined(commandName)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(ArgDefConst.CMD_PREFIX);
        sb.append(commandName);
        int length = commandName.length() + 1;
        int indent = 15;
        if (length >= indent) {
            indent = length + 5;
        }

        boolean sameLine = true;
        Map<String, OptionDef> optMap = defMap.get(commandName);
        if (optMap != null && !optMap.isEmpty()) {
            List<OptionDef> allOptions = new ArrayList<OptionDef>(optMap.values());
            Collections.sort(allOptions);
            for (OptionDef optionDef : allOptions) {
                if (optionDef.parentOption != null) {
                    continue;
                }
                if (sameLine) {
                    sb.append(String.format("%" + (indent - length) + "s%s", " ", optionDef));
                } else {
                    sb.append(String.format("%" + indent + "s%s", " ", optionDef));
                }
                sb.append(ArgDefConst.NEW_LINE);
                sameLine = false;
            }
        } else {
            sb.append(ArgDefConst.NEW_LINE);
        }
        return sb.toString();
    }

    private class OptionDef implements Comparable<OptionDef> {
        public String name;
        public boolean mandatory;
        public boolean requireValue;
        public String valueDescription;
        public String defaultValue;
        public String[] selectionValues;
        public List<OptionDef> subOptionDefs;
        public OptionDef parentOption;
        public int index; // for display sequence

        public OptionDef() {
        }

        public void addSubOption(OptionDef optionDef) {
            if (optionDef != this) {
                if (subOptionDefs == null) {
                    subOptionDefs = new LinkedList<OptionDef>();
                }
                subOptionDefs.add(optionDef);
                optionDef.parentOption = this;
            }
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (!mandatory) {
                sb.append(ArgDefConst.OPTIONAL_START);
            }
            sb.append(ArgDefConst.OPT_PREFIX);
            sb.append(name);
            if (requireValue) {
                sb.append(" " + ArgDefConst.VALUE_START);
                if (selectionValues != null) {
                    boolean first = true;
                    for (String value : selectionValues) {
                        if (!first) {
                            sb.append(ArgDefConst.SELECTION_DELIMITER);
                        }
                        sb.append(value);
                        first = false;
                    }
                } else {
                    sb.append(valueDescription != null ? valueDescription : "value of " + name);
                }
                sb.append(ArgDefConst.VALUE_END);
            }
            if (defaultValue != null && !defaultValue.isEmpty()) {
                sb.append(ArgDefConst.DEFAULT_START);
                sb.append(defaultValue.trim());
                sb.append(ArgDefConst.DEFAULT_END);
            }

            if (subOptionDefs != null) {
                for (OptionDef subOption : subOptionDefs) {
                    sb.append(" ");
                    sb.append(subOption);
                }
            }

            if (!mandatory) {
                sb.append(ArgDefConst.OPTIONAL_END);
            }
            return sb.toString();
        }

        public int compareTo(OptionDef arg0) {
            if (index == arg0.index) {
                int onMandatatory = Boolean.valueOf(arg0.mandatory).compareTo(Boolean.valueOf(this.mandatory));
                if (onMandatatory == 0) {
                    return this.name.compareTo(arg0.name);
                } else {
                    return onMandatatory;
                }
            }
            return index - arg0.index;
        }

    }

    public String showAllCommand() {
        StringBuffer sb = new StringBuffer();
        sb.append(appName + " Usage :" + ArgDefConst.NEW_LINE);
        String[] names = null;
        if (names == null || names.length == 0) {
            List<String> allCommandNames = new ArrayList<String>(defMap.keySet());
            Collections.sort(allCommandNames);
            names = allCommandNames.toArray(new String[allCommandNames.size()]);
        }
        for (String cmdName : names) {
            String commandUsageStr = getCommandUsage(cmdName);
            if (commandUsageStr != null) {
                sb.append(ArgDefConst.NEW_LINE + commandUsageStr);
            }
        }
        return sb.toString();
    }

}
