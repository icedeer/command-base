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
public class CommandOption {
	private String commandName;

	private Map<String, String> optionMap; // key is option name, value is a string represented option value

	private Map<String, String> defaultMap; // key is option name, value is default value

	public CommandOption(String commandName) {
		this.commandName = commandName;
		this.optionMap = new HashMap<String, String>();
		this.defaultMap = new HashMap<String, String>();
	}

	public String getCommandName() {
		return commandName;
	}

	/**
	 * return value for given option; if it doesn't contain runtime value, use default value if it has
	 * 
	 * @param optionName
	 * @return
	 */
	public String getOptionValue(String optionName) {
		return getOptionValue(optionName, true);
	}

	public boolean containOption(String optionName) {
		return optionMap.containsKey(optionName);
	}

	public String getOptionValue(String optionName, boolean useDefault) {
		String result = null;
		result = optionMap.get(optionName);
		if (result == null && useDefault) {
			result = defaultMap.get(optionName);
		}
		return result;
	}

	public boolean getBooleanOptionValue(String optionName) {
		String value = getOptionValue(optionName);
		if (value != null) {
			return Boolean.parseBoolean(value);
		}
		return false;
	}

	protected void addOption(String optionName, String value) {
		optionMap.put(optionName, value);
	}

	protected void addSingleFlagOption(String optionName) {
		addOption(optionName, Boolean.TRUE.toString());

	}

	protected void addDefaultOption(String optionName, String defaultValue) {
		defaultMap.put(optionName, defaultValue);
	}

	public void overwriteDefaultOption(String optionName, String defaultValue) {
		defaultMap.put(optionName, defaultValue);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(ArgDefConst.NEW_LINE + "CommandOption : ");
		for (String optionName : optionMap.keySet()) {
			sb.append(ArgDefConst.NEW_LINE + "\t" + optionName + " : " + optionMap.get(optionName));
		}
		for (String optionName : defaultMap.keySet()) {
			sb.append(ArgDefConst.NEW_LINE + "\t" + optionName + " : " + defaultMap.get(optionName) + " \t(Default)");
		}
		return sb.toString();
	}

}
