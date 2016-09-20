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
public class ArgDefConst {
	public static final char CMD_PREFIX = '-';
	public static final char OPT_PREFIX = '-';
	public static final char VALUE_START = '<';
	public static final char VALUE_END = '>';
	public static final char OPTIONAL_START = '[';
	public static final char OPTIONAL_END = ']';
	public static final char SELECTION_DELIMITER = '|';
	
	public static final String DEFAULT_START = " ------- (default value is \"";
	public static final String DEFAULT_END = "\")";
	
	public static String NEW_LINE = System.getProperty("line.separator");
}
