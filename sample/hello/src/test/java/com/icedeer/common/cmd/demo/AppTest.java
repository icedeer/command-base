package com.icedeer.common.cmd.demo;

import com.icedeer.common.cmd.ArgumentValidationException;
import com.icedeer.common.cmd.ext.XmlConfGenericApp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     *  Test :-)
     */
    public void testApp() {
        XmlConfGenericApp app = new XmlConfGenericApp();
        
        String[] args = new String[]{};
        try {
            app.executeApp(args);
            
            assert(true);
        } catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }
    
    public void testHelp() {
        XmlConfGenericApp app = new XmlConfGenericApp();
        
        String[] args = new String[]{"-help"};
        try {
            app.executeApp(args);
            
            assert(true);
        } catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }
    
    public void testHello1() {
        XmlConfGenericApp app = new XmlConfGenericApp();
        
        String[] args = new String[]{"-hello"};
        try {
            app.executeApp(args);
            
            assert(false);
        } catch (ArgumentValidationException e) {
            
            assert(true);
        } catch (Exception err){
            assert(false);
        }
    }
    
    public void testHello2() {
        XmlConfGenericApp app = new XmlConfGenericApp();
        
        String[] args = new String[]{"-hello", "-name"};
        try {
            app.executeApp(args);
            
            assert(false);
        } catch (ArgumentValidationException e) {
            
            assert(true);
        } catch (Exception err){
            assert(false);
        }
    }
    
    public void testHello3() {
        XmlConfGenericApp app = new XmlConfGenericApp();
        
        String[] args = new String[]{"-hello", "-name", "Peter", "-country", "China"};
        try {
            app.executeApp(args);
            
            assert(false);
        } catch (ArgumentValidationException e) {
            
            assert(true);
        } catch (Exception err){
            assert(false);
        }
    }
    
    public void testHello4() {
        XmlConfGenericApp app = new XmlConfGenericApp();
        
        String[] args = new String[]{"-hello", "-name", "Peter", "-country", "US"};
        try {
            app.executeApp(args);
            
            assert(true);
        } catch (ArgumentValidationException e) {
            
            assert(false);
        } catch (Exception err){
            assert(false);
        }
    }
    
    public void testHello5() {
        XmlConfGenericApp app = new XmlConfGenericApp();
        
        String[] args = new String[]{"-hello", "-name", "Peter"};
        try {
            app.executeApp(args);
            
            assert(true);
        } catch (ArgumentValidationException e) {
            
            assert(false);
        } catch (Exception err){
            assert(false);
        }
    }
}
