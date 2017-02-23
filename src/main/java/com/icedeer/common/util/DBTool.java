package com.icedeer.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Description: a utility class for controlling JDBC connection.
 * <P>
 * Revision:
 * <UL>
 * <LI> Peter W -- Feb 9, 2017 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class DBTool {

    public static Connection getConnection(String driver, String url, String user, String password) throws Exception {
        Properties props = new Properties();

        props.put("user", user);
        props.put("password", password);
        // Display proper error messages instead of error codes
        props.put("retrieveMessagesFromServerOnGetMessage", "true");
        Class.forName(driver);

        Connection conn = DriverManager.getConnection(url, props);

        conn.setAutoCommit(false);

        return conn;
    }

    public static Connection getDB2Connection(String url, String user, String password) throws Exception {
        return getConnection("com.ibm.db2.jcc.DB2Driver", url, user, password);
    }

    public static void closeConnection(Connection conn) throws Exception {
        if (null != conn) {
            conn.close();
        }
    }

    public static void commit(Connection conn) throws Exception {
        if (null != conn) {
            conn.commit();
        }
    }

}
