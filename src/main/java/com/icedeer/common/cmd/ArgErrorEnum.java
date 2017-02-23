package com.icedeer.common.cmd;

/**
 * <B>Description</B>: 
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Feb, 2011 -- Initial Draft</LI>
 * <LI> Peter W -- Feb, 2017 -- adding MINIMUM_LENGTH_NAME</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public enum ArgErrorEnum {
    EMPTY_ARG_LIST, 
    INVALID_COMMAND_NAME, 
    INVALID_OPTION_NAME, 
    REQUIRE_OPTION_VALUE, 
    MINIMUM_LENGTH_NAME;

}
