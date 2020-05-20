/**
 *  Copyright 2020 University Of Southern California
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.usc.ksom.pm.panther.pantherdb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class Utils {
    public static final String OUTPUT_TYPE_XML = "application/xml";
    public static final String STR_EMPTY = "";
    
    public static List<String> readFromURL(String path) {
        InputStream in = null;
        List<String> rtnList = null;
        try {
            in = new URL(path).openStream();
            rtnList = IOUtils.readLines(in, StandardCharsets.UTF_8);
            in.close();
        } catch (Exception e) {
            rtnList = null;
        }
        return rtnList;
    }
    
    public static String convertDocToXMLStr(Document doc) {
        if (null == doc) {
            return null;
        }
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            return result.getWriter().toString();
//            String returnStr = result.getWriter().toString();
//            System.out.println(returnStr);
//            return returnStr;
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
      
    public static void outputDocumentinXMLFormat(HttpServletResponse response, Document doc) {
        try {
            response.setContentType(OUTPUT_TYPE_XML);
            PrintWriter out = response.getWriter();
            out.print(convertDocToXMLStr(doc));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean search(String[] searchList, String s) {
        if (null == s || null == searchList) {
            return false;
        }
        for (int i = 0; i < searchList.length; i++) {
            if (0 == searchList[i].compareTo(s)) {
                return true;
            }
        }
        return false;
    }
    
    public static Document createDocument() {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();  
            return doc;
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }         
        return null;
    }
    
    public static void addToElement(Document doc, Element parent, String label, String value) {
        if (null == value || 0 == value.length()) {
            return;
        }
        Element childElem = doc.createElement(label);
        Text childText = doc.createTextNode(value);
        childElem.appendChild(childText);
        parent.appendChild(childElem);
    }
    
    public static String listToString(String list[], String wrapper, String delim) {
        if (null == list) {
            return null;
        }
        else if (0 == list.length) {
            return STR_EMPTY;
        }
        int size = list.length;
        StringBuffer selection = new StringBuffer();

        // add each item in the Vector to the SB with wrapper and delimiter
        for (int i = 0; i < size - 1; i++) {
            selection.append(wrapper);
            selection.append((String) list[i]);
            selection.append(wrapper);
            selection.append(delim);
        }

        // add last item in the Vector to the SB with wrapper but no delimiter
        selection.append(wrapper);
        selection.append((String) list[size - 1]);
        selection.append(wrapper);
        return selection.toString();
    }    
    
}
