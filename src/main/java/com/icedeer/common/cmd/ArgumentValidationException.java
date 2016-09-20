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
public class ArgumentValidationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3241087611034411133L;

	
	/**
	 * 
	 */
	public ArgumentValidationException() {
	}

	/**
	 * @param arg0
	 */
	public ArgumentValidationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ArgumentValidationException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ArgumentValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public ArgumentValidationException(ArgErrorEnum errorType, Object... values){
		super(parseErrorMsg(errorType, values));
		
	}

	private static String parseErrorMsg(ArgErrorEnum errorType, Object[] values) {
		switch(errorType){
		case EMPTY_ARG_LIST:
			return "Invalid Arguments : no argument passed in!";
		case INVALID_COMMAND_NAME:
			return "Invalid Arguments : [" + values[0] + "] is not valid command.";
		case INVALID_OPTION_NAME:
			return "Invalid Option : \"" + values[0] + "\" is not valid option for command [" + values[1] + "]";
		case REQUIRE_OPTION_VALUE:
			return "Invalid Option Value: require input for option \"" + values[0] + "\"";
		}
		return null;
	}

}
