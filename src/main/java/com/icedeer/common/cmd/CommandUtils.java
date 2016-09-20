/**
 * 
 */
package com.icedeer.common.cmd;

import java.io.File;

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
public class CommandUtils {
	public static void validateFile(String filePath) throws ArgumentValidationException{
        if(filePath != null && !filePath.isEmpty()){
            File theFile = new File(filePath);
            if(! theFile.exists()){
                throw new ArgumentValidationException("The File of ["+filePath+"] doesn't exist");
            }
            if(! theFile.isFile()){
                throw new ArgumentValidationException("The File of ["+filePath+"] is not a readable file, it may be a directory, please double check!");
            }
        }
    }
    
    public static void validateDir(String dir) throws ArgumentValidationException{
        if(dir != null && !dir.isEmpty()){
            File theFile = new File(dir);
            if(! theFile.exists()){
                throw new ArgumentValidationException("The Directory of ["+dir+"] doesn't exist");
            }
            if( theFile.isFile()){
                throw new ArgumentValidationException("The Directory of ["+dir+"] is not a directory, it may be a file, please double check!");
            }
        }
    }
}
