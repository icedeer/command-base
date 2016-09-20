/*
 *  Icedeer 2010 All Rights Reserved.   Classification – Confidential.
 */

package com.icedeer.common.util.xml;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Description: a utility class for manipulate XML DOM tree by using DOM2 mode, and JAXP API.
 * <P>
 * Revision:
 * <UL>
 * <LI> Peter W -- Apr 8, 2010 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class DomHelper {

    private static DocumentBuilder domBuilder = null;

    /**
     * Parses an XML input stream and returns a DOM document. If validating is true, the contents is validated against the DTD specified in
     * the file.
     * 
     * @param inputStream
     * @param validating
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document parseXml(InputStream inputStream, boolean validating) throws DOMException, ParserConfigurationException,
            SAXException, IOException {
        if (domBuilder == null) {
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validating);
            // Configure it to ignore comments
            factory.setIgnoringComments(true);
            // check namespace
            factory.setNamespaceAware(true);

            // Create the builder and parse the file
            domBuilder = factory.newDocumentBuilder();
            // if (!domBuilder.getDOMImplementation().hasFeature("traversal", "2.0")) {
            // supportTraversal = false;
            // }
        }
        Document doc = domBuilder.parse(inputStream);
        // do the normalization
        doc.normalize();

        return doc;

    }

    public static Document parseXml(String xmlContent, boolean validating) throws DOMException, ParserConfigurationException, SAXException,
            IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(xmlContent.getBytes());
        return parseXml(bis, validating);
    }

    /**
     * serializing a XML DOM <code>Document</code> into XML String
     * 
     * @param doc
     * @return the string in XML format
     * @throws TransformerException
     */
    public static String saveToXmlStr(Document doc) throws DOMException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String xmlString = sw.toString();
        return xmlString;

    }

    public static void saveToXml(Document doc, OutputStream os) throws DOMException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        // factory.setAttribute("indent-number", new Integer(2));

        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(os);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);

    }

    public static List<Element> getElementByFilter(Document doc, NodeFilter filter) throws DOMException {
        List<Element> result = new ArrayList<Element>();
        DocumentTraversal traversable = (DocumentTraversal) doc;
        int whatToShow = NodeFilter.SHOW_ELEMENT;
        NodeIterator iterator = traversable.createNodeIterator(doc, whatToShow, filter, true);
        Node current = null;
        while ((current = iterator.nextNode()) != null) {
            result.add((Element) current);
        }
        return result;
    }

    /**
     * a utility method for finding the first child XML element of given XML element
     * 
     * @param ele
     * @return
     */
    public static Element getFirstChildElement(Element ele) {
        Element resultEle = null;
        NodeList childNodes = ele.getChildNodes();
        if (childNodes != null) {
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    resultEle = (Element) node;
                    break;
                }
            }
        }
        return resultEle;
    }

    /**
     * return all the child Elements under given Element <code>parentNode</code> and with the same tag name of <code>tagName</code> ;
     * it's not deep search, so only the first generation children list is scanned <br/>Note: the given <code>tagName</code> is just local
     * name; namespace is not support right now.
     * 
     * @param parentNode
     * @param tagName
     * @return
     * @throws DOMException
     */
    public static List<Element> getChildElementsByTagName(Element parentNode, String tagName) {
        if (parentNode == null) {
            return null;
        }
        // if there is no tag name given, treat it as returning all the child Elements
        if (tagName == null || tagName.trim().length() == 0) {
            return getChildElements(parentNode);
        }
        List<Element> resultList = new ArrayList<Element>();
        NodeList childNodes = parentNode.getChildNodes();
        if (childNodes != null) {
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getParentNode() == parentNode && node.getLocalName().equals(tagName)) {
                    resultList.add((Element) node);
                }
            }
        }
        return resultList;
    }

    /**
     * return all the child Elements of given Element; it's not deep search, so only the first generation children list is scanned.
     * 
     * @param parentNode
     * @return an empty list if nothing found
     * @throws DOMException
     */
    public static List<Element> getChildElements(Element parentNode) throws DOMException {
        if (parentNode == null) {
            return null;
        }
        List<Element> resultList = new ArrayList<Element>();
        NodeList childNodes = parentNode.getChildNodes();
        if (childNodes != null) {
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getParentNode() == parentNode) {
                    resultList.add((Element) node);
                }
            }
        }
        return resultList;
    }

    /**
     * create an empty DOM <code>Document</code> with root tag as given in the parameter <code>rootTag</code>
     * 
     * @param rootTag
     * @return
     * @throws ParserConfigurationException
     */
    public static Document createDocument(String rootTag) throws ParserConfigurationException {
        // Create a builder factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImpl = builder.getDOMImplementation();

        Document doc = domImpl.createDocument(null, rootTag, null);
        return doc;
    }

    public static Element createElement(Document doc, String tagName) {
        return doc.createElementNS(null, tagName);
    }

    /**
     * Parses a string containing XML and returns a DocumentFragment containing the nodes of the parsed XML.
     * 
     * @param doc
     * @param fragment
     * @return
     */
    public static DocumentFragment parseXmlFragmentStr(Document doc, String fragment) {
        // Wrap the fragment in an arbitrary element
        fragment = "<fragment>" + fragment + "</fragment>";
        try {
            // Create a DOM builder and parse the fragment
            Document d = domBuilder.parse(new InputSource(new StringReader(fragment)));

            // Import the nodes of the new document into doc so that they
            // will be compatible with doc
            Node node = doc.importNode(d.getDocumentElement(), true);

            // Create the document fragment node to hold the new nodes
            DocumentFragment docfrag = doc.createDocumentFragment();

            // Move the nodes into the fragment
            while (node.hasChildNodes()) {
                docfrag.appendChild(node.removeChild(node.getFirstChild()));
            }

            // Return the fragment
            return docfrag;
        } catch (SAXException e) {
            // A parsing error occurred; the xml input is not valid
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * generating simple XML string for element with following limitation: 1) only generate non-namespace elements and attributes 2) only
     * consider element, text, attribute nodes
     * 
     * @param ele
     * @return
     */
    public static String simpleElementToString(Element ele) {
        StringBuffer sb = new StringBuffer();
        sb.append("<" + ele.getLocalName());
        NamedNodeMap attMap = ele.getAttributes();
        if (attMap != null) {
            for (int i = 0; i < attMap.getLength(); i++) {
                Node att = attMap.item(i);
                sb.append(" " + att.getNodeName() + "=\"");
                sb.append(att.getNodeValue() + "\"");
            }
        }
        sb.append(">");
        List<Element> childEleList = getChildElements(ele);
        if (childEleList != null && !childEleList.isEmpty()) {
            for (Element childEle : childEleList) {
                sb.append(simpleElementToString(childEle));
            }
        } else {
            sb.append(ele.getTextContent());
        }
        sb.append("</" + ele.getLocalName() + ">");
        return sb.toString();
    }
    
    
    public static String getValueOfChildElement(Element ele, String tagName){
        List<Element> childElements = getChildElementsByTagName(ele, tagName);
        if(childElements == null || childElements.isEmpty()){
            return null;
        }
        Element childEle = childElements.get(0);
        NodeList childNodes = childEle.getChildNodes();
        if (childNodes != null) {
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.TEXT_NODE ){
                    return node.getTextContent();
                }
                else if(node.getNodeType() == Node.CDATA_SECTION_NODE){
                    return node.getNodeValue();
                }
            }
        }
        return null;
    }

}
