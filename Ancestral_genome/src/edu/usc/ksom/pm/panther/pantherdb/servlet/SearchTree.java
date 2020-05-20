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
package edu.usc.ksom.pm.panther.pantherdb.servlet;

import edu.usc.ksom.pm.panther.pantherdb.dataModel.AnnotationNode;
import edu.usc.ksom.pm.panther.pantherdb.dataModel.MSA;
import edu.usc.ksom.pm.panther.pantherdb.dataModel.Tree;
import edu.usc.ksom.pm.panther.pantherdb.treeViewer.TreeManager;
import edu.usc.ksom.pm.panther.pantherdb.treeViewer.TreeUtils;
import edu.usc.ksom.pm.panther.pantherdb.util.Utils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class SearchTree extends HttpServlet {
    
    public static final String PARAM_BOOK = "book";
    public static final String PARAM_MSA = "msa";
    public static final String PARAM_TYPE = "type";
    
    public static final String TYPE_TREE_INFO = "tree_info";
    public static final String TYPE_MSA_INFO = "msa_info";
    public static final String TYPE_FAMILY_NAME = "family_name";
    
    public static final String ELEMENT_PARAMETERS = "parameters";
    public static final String ELEMENT_TYPE = "type";
    public static final String ELEMENT_TREE = "tree";
    public static final String ELEMENT_MSA = "msa";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Search servlet request query string is " + request.getQueryString());
        String type = request.getParameter(PARAM_TYPE);
        if (null == type) {
            return;
        }
        if (type.equals(TYPE_TREE_INFO)) {
            getTreeInfo(request, response);
        }
        else if (type.equals(TYPE_MSA_INFO)) {
            getMSAInfo(request, response);
        }
        else if (type.equals(TYPE_FAMILY_NAME)) {
            getFamilyName(request, response);
        }
    }

    
    
    private void getTreeInfo(HttpServletRequest request, HttpServletResponse response) {
        Document doc = Utils.createDocument();
        if (null == doc) {
            return;
        }
        Element infoElem = doc.createElement(TYPE_TREE_INFO);
        doc.appendChild(infoElem);
        Element paramElem = doc.createElement(ELEMENT_PARAMETERS);
        infoElem.appendChild(paramElem);
        
        String treeId = request.getParameter(PARAM_BOOK);
        if (null != treeId) {
             Utils.addToElement(doc, paramElem, PARAM_BOOK, treeId);
        }
        
        Tree tree = TreeManager.getTree(treeId);
        if (null != tree) {

            AnnotationNode root = tree.getRoot();
            Element rootElem = TreeUtils.getTree(doc, root);
            if (null != rootElem) {
                infoElem.appendChild(rootElem);
            }
        }
        Utils.outputDocumentinXMLFormat(response, doc);
    }
    
    private void getMSAInfo(HttpServletRequest request, HttpServletResponse response) {
        Document doc = Utils.createDocument();
        if (null == doc) {
            return;
        }
        Element infoElem = doc.createElement(TYPE_MSA_INFO);
        doc.appendChild(infoElem);
        Element paramElem = doc.createElement(ELEMENT_PARAMETERS);
        infoElem.appendChild(paramElem);

        String book = request.getParameter(PARAM_BOOK);
        if (null != book) {
            Utils.addToElement(doc, paramElem, PARAM_BOOK, book);
        }

        MSA msa = TreeManager.getMSA(book);
        if (null != msa) {

            Element msaElem = TreeUtils.getMSA(doc, msa);
            if (null != msaElem) {
                infoElem.appendChild(msaElem);
            }
        }
        Utils.outputDocumentinXMLFormat(response, doc);
    }
    
    private void getFamilyName(HttpServletRequest request, HttpServletResponse response) {
        Document doc = Utils.createDocument();
        if (null == doc) {
            return;
        }
        Element infoElem = doc.createElement(TYPE_FAMILY_NAME);
        doc.appendChild(infoElem);
        Element paramElem = doc.createElement(ELEMENT_PARAMETERS);
        infoElem.appendChild(paramElem);

        String book = request.getParameter(PARAM_BOOK);
        if (null != book) {
            Utils.addToElement(doc, paramElem, PARAM_BOOK, book);
        }

        String familyName = TreeManager.getFamilyName(book);
        if (null != familyName) {

            Element familyNameElem = TreeUtils.getFamilyName(doc, familyName);
            if (null != familyNameElem) {
                infoElem.appendChild(familyNameElem);
            }
        }
        Utils.outputDocumentinXMLFormat(response, doc);
    }    
}