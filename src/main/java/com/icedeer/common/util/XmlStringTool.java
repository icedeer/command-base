package com.icedeer.common.util;

/**
 * The Tool of formatting XML string, it doesn't parse the XML string but dealing with
 * it directly. Therefore the assumption is that the XML string is valid and well formatted
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>@author Peter Wang</p>
 * <p>@version 1.0</p>
 * @since jdk 1.3
 */

public class XmlStringTool {

    /**
     * format a flat XML string into easy look XML String with indent
     * <p><b>It is not fully test, so has limit usage</b></p>
     * @param xmlStr the flat XML string
     * @param indent the indent string like "  " or "\t"
     * @return formatted XML String
     */
    // public static String formatXMLString(String xmlStr, String indent){
    // StringBuffer sb = new StringBuffer();
    // List elementList = new ArrayList();
    // // put all the element into list
    // int len = xmlStr.length();
    // int index =0;
    // int index2 = 0;
    // int depth = -1;
    // boolean previousIsEnd = false;
    //
    // while( index <= len){
    // boolean thisIsEnd = false;
    // index2 = xmlStr.indexOf(">", index+1);
    // if( index2 <= 0 ){
    // break;
    // }
    // String elementStr = xmlStr.substring(index, index2+1).trim();
    // boolean isCDATA = isCDATA(elementStr);
    //
    // if( !elementStr.startsWith("<") ){
    // thisIsEnd = true;
    // sb.deleteCharAt(sb.length()-1);
    // sb.append(elementStr+"\n");
    // }else{
    // // if it's a start tag and the previous one is not end Tag
    // if( isStartElement(elementStr ) && !previousIsEnd ){
    // depth++;
    // }
    // if( isEndElement(elementStr) ){
    // thisIsEnd = true;
    // // only decrease indent when it's a pure end tag, not like <xxx/>
    // if( elementStr.charAt(1) == '/' && previousIsEnd ){
    // depth--;
    // }
    // }
    // else if( isComment(elementStr) ){
    // thisIsEnd = previousIsEnd;
    // }
    //
    // String indentStr = getIndent(depth, indent);
    // sb.append(indentStr);
    // sb.append(elementStr+"\n");
    // }
    // index = index2+1;
    // previousIsEnd = thisIsEnd;
    // }
    // return sb.toString();
    // }

    /**
     * used in formatXMLString()
     * @param elementStr the xml formatted element string
     * @return true if it's a comment element
     */
    // private static boolean isComment(String elementStr){
    // if( elementStr.startsWith("<!--") && elementStr.endsWith("-->") ){
    // return true;
    // }
    // return false;
    // }

    /**
     * used in formatXMLString()
     * @param elementStr the xml formatted element string
     * @return true if it's a CDATA type
     */
    // private static boolean isCDATA(String elementStr){
    // if( (elementStr.startsWith("<![CDATA[") || elementStr.startsWith("<![cdata[")) && elementStr.endsWith("]]>") ){
    // return true;
    // }
    // return false;
    // }

    /**
     * used in formatXMLString()
     * @param elementStr the xml formatted element string
     * @return true if it's the start element tag
     */
    // private static boolean isStartElement(String elementStr){
    // char secondChar = elementStr.charAt(1);
    // if( secondChar != '/' && secondChar != '?' ){
    // return true;
    // }
    // return false;
    // }

    /**
     * used in formatXMLString()
     * @param elementStr the xml formatted element string
     * @return true if it's end element tag
     */
    // private static boolean isEndElement(String elementStr){
    // char secondChar = elementStr.charAt(1);
    // char lastSecondChar = elementStr.charAt(elementStr.length() -2 );
    // if( secondChar == '/' || lastSecondChar == '/' || elementStr.endsWith("-->") || elementStr.endsWith("]]>")){
    // return true;
    // }
    // return false;
    // }

    /**
     * used in formatXMLString()
     * @param depth how many times of indent
     * @param indent the indent string
     * @return the string contain mutiple times (depth) of indent
     */
    private static String getIndent(int depth, String indent) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < depth; i++) {
            sb.append(indent);
        }
        return sb.toString();
    }

    /**
     * format a flat XML string into easy look XML String with indent
     * <p><b>It is not fully test, so has limit usage</b></p>
     * @param xmlStr the flat XML string
     * @param indent the indent string like "  " or "\t"
     * @return formatted XML String
     */
    public static String formatXMLString(String xmlStr, String indent) {
        StringBuffer sb = new StringBuffer();
        int len = xmlStr.length();
        int index = 0;
        int index2 = 0;
        int depth = 0;
        int previousNodeType = 0;
        boolean ispossible = false;
        int textNodeIndex = 0;
        String textNodeValue = null;

        while (index <= len) {
            String nodeStr = null;
            index2 = xmlStr.indexOf(">", index + 1);
            if (index2 <= 0) {
                break;
            }
            nodeStr = xmlStr.substring(index, index2 + 1);
            int index3 = nodeStr.indexOf("<", 1);
            if (index3 > 0) {
                nodeStr = nodeStr.substring(0, index3);
                index2 = index + index3 - 1;
                if (nodeStr.startsWith("<!--") && nodeStr.length() > 4) {
                    nodeStr = "<!--";
                    index2 = index + 4;
                } else if (nodeStr.toUpperCase().startsWith("<![CDATA[") && nodeStr.length() > 9) {
                    nodeStr = "<![CDATA[";
                    index2 = index + 9;
                }
            }
            nodeStr = nodeStr.trim();
            int nodeType = 0;
            if (nodeStr.length() > 0) {
                nodeType = getNodeType(nodeStr);
                // handling <xx>test</xx> situation
                if ((nodeType == TEXT_NODE && previousNodeType == START_ELEMENT)) {
                    ispossible = true;
                    textNodeIndex = sb.length() - 1;
                    textNodeValue = nodeStr;
                    depth++;
                    String indentStr = getIndent(depth, indent);
                    sb.append(indentStr);
                    sb.append(nodeStr + "\n");
                } else if ((nodeType == END_ELEMENT && previousNodeType == TEXT_NODE && ispossible)) {
                    sb.delete(textNodeIndex, sb.length());
                    sb.append(textNodeValue + nodeStr + "\n");
                    depth--;
                }// end handling <xx>test</xx> situation
                 // handling <tag></tag> situation
                else if (nodeType == END_ELEMENT && previousNodeType == START_ELEMENT) {
                    sb.delete(sb.length() - 1, sb.length());
                    sb.append(nodeStr + "\n");
                } else {
                    ispossible = false;
                    depth = getDepth(nodeType, previousNodeType, depth);
                    String indentStr = getIndent(depth, indent);
                    sb.append(indentStr);
                    sb.append(nodeStr + "\n");
                }
                // remember the previous node type
                previousNodeType = nodeType;
            }
            index = index2 + 1;

        }
        return sb.toString();

    }

    /**
     * decide the indent level basic on the context and node type
     * @param thisNodeType this one
     * @param previousNodeType the previous one
     * @param depth current indent level
     * @return the new indent level
     */
    private static int getDepth(int thisNodeType, int previousNodeType, int depth) {
        int theDepth = depth;
        switch (thisNodeType) {
        case PI_NODE:
            return theDepth;
        case TEXT_NODE:
            if (previousNodeType == OPEN_COMMENT || previousNodeType == OPEN_CDATA || previousNodeType == START_ELEMENT) {
                theDepth++;
            }
            break;
        case SELF_CLOSE_CDATA:
        case SELF_CLOSE_COMMENT:
        case SELF_CLOSE_ELMENT:
        case OPEN_CDATA:
        case OPEN_COMMENT:
        case START_ELEMENT:
            if (previousNodeType == OPEN_COMMENT || previousNodeType == OPEN_CDATA || previousNodeType == START_ELEMENT) {
                theDepth++;
            }
            break;
        case END_ELEMENT:
        case CLOSE_CDATA:
        case CLOSE_COMMENT:
            if (previousNodeType == OPEN_COMMENT || previousNodeType == OPEN_CDATA) {
                theDepth++;
            }
            theDepth--;
            break;
        }
        return theDepth;
    }

    /**
     * return the node type (pre-defined types)
     * @param nodeStr the node fragment string
     * @return the type
     */
    private static int getNodeType(String nodeStr) {
        // <?xml version="1.0"?>
        if (nodeStr.startsWith("<?") && nodeStr.endsWith("?>")) {
            return PI_NODE;
        }
        if ((!nodeStr.startsWith("<")) && (!nodeStr.endsWith(">"))) {
            return TEXT_NODE;
        }
        // </test>
        if (nodeStr.startsWith("</") && !nodeStr.endsWith("-->") && nodeStr.endsWith(">")) {
            return END_ELEMENT;
        }
        // <test/>
        if (nodeStr.startsWith("<") && nodeStr.endsWith("/>")) {
            return SELF_CLOSE_ELMENT;
        }
        if (nodeStr.startsWith("<!--")) {
            if (nodeStr.endsWith("-->")) {
                // <!-- comment -->
                return SELF_CLOSE_COMMENT;
            }
            // <!-- <test/> -->
            return OPEN_COMMENT;
        }
        if (nodeStr.endsWith("-->")) {
            return CLOSE_COMMENT;
        }
        if (nodeStr.startsWith("<![CDATA[")) {
            if (nodeStr.endsWith("]]>")) {
                // <![CDATA[ comment ]]>
                return SELF_CLOSE_CDATA;
            }
            // <![CDATA[ <test/> ]]>
            return OPEN_CDATA;
        }
        if (nodeStr.endsWith("]]>")) {
            return CLOSE_CDATA;
        }
        // all the < > condition passed, it should be a start element <sss>
        if (nodeStr.startsWith("<") && nodeStr.endsWith(">")) {
            return START_ELEMENT;
        }
        // it should be text node
        return TEXT_NODE;

    }

    private static final int PI_NODE = 1;
    private static final int START_ELEMENT = 2;
    private static final int END_ELEMENT = 3;
    private static final int SELF_CLOSE_ELMENT = 4;
    private static final int OPEN_COMMENT = 5;
    private static final int CLOSE_COMMENT = 6;
    private static final int OPEN_CDATA = 7;
    private static final int CLOSE_CDATA = 8;
    private static final int SELF_CLOSE_COMMENT = 9;
    private static final int SELF_CLOSE_CDATA = 10;
    private static final int TEXT_NODE = 12;
}