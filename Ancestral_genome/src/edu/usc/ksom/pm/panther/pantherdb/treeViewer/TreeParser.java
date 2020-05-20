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
package edu.usc.ksom.pm.panther.pantherdb.treeViewer;



import edu.usc.ksom.pm.panther.pantherdb.dataModel.AnnotationNode;
import edu.usc.ksom.pm.panther.pantherdb.dataModel.Tree;
import java.util.StringTokenizer;
import java.util.Vector;
import edu.usc.ksom.pm.panther.pantherdb.util.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class TreeParser {
    protected String familyId;


    public static final String ERROR_MSG_DATA_FOR_NON_AN = "Found data for non-existant annotation node ";

    protected static final String DELIM_SEMI_COLON = ";";
    protected static final String DELIM_BRACKET_OPEN = "(";
    protected static final String DELIM_BRACKET_CLOSE = ")";
    protected static final String DELIM_COMMA = ",";
    protected static final String DELIM_COLON = ":";
    protected static final String DELIM_SQUARE_BRACKET_OPEN = "[";
    protected static final String DELIM_SQUARE_BRACKET_CLOSE = "]";
    public static final String DELIM_TREE = ",();";
    
    public static final String STR_EMPTY = "";
    public static final String SEPARATOR_SPECIES = ",";

    public static Tree getTree(String familyId, List<String> treeStrs) {

        if (null == familyId || null == treeStrs) {
            return null;
        }

        String[] contents = treeStrs.toArray(new String[0]);
        HashMap <String, AnnotationNode> idToNodeTbl = new HashMap<String, AnnotationNode>();
        AnnotationNode root = getRoot(contents, idToNodeTbl);
        return new Tree(familyId, root, idToNodeTbl);
    }
    


    public static AnnotationNode getRoot(String[] treeContents, HashMap<String, AnnotationNode> idToNodeTbl) {
        if (null == treeContents) {
            return null;
        }
        if (0 == treeContents.length) {
            return null;
        }
        // Modify, if there are no line returns
        if (1 == treeContents.length) {
            treeContents = treeContents[0].split(DELIM_SEMI_COLON);
        }
        AnnotationNode mainTree = parse(treeContents[0], idToNodeTbl);


        setNodes(mainTree, idToNodeTbl);

        int index;
        String anName;
        AnnotationNode subtree;
        AnnotationNode annotationNode;
        for (int i = 1; i < treeContents.length; i++) {
            index = treeContents[i].indexOf(DELIM_COLON);
            anName = treeContents[i].substring(0, index);
            subtree =
                    parse(treeContents[i].substring(index + 1, treeContents[i].length()), idToNodeTbl);
            annotationNode = (AnnotationNode)idToNodeTbl.get(anName);
            if (null == annotationNode) {
                System.out.println(ERROR_MSG_DATA_FOR_NON_AN + anName);
                continue;
            }
            subtree.setAnnotationNodeId(annotationNode.getNodeName());
            subtree.setParent(annotationNode.getParent());

            String branchLength = annotationNode.getBranchLength();
            if (null != branchLength) {
                subtree.setBranchLength(branchLength);
            }

            AnnotationNode parent = annotationNode.getParent();
            if (null != parent) {
                Vector children = parent.getChildren();
                int j = 0;
                while (j < children.size()) {
                    AnnotationNode tmp = (AnnotationNode)children.elementAt(j);
                    if (tmp.equals(annotationNode)) {
                        children.setElementAt(subtree, j);
                        break;
                    }
                    j++;
                }
            }

            else {
                mainTree = subtree;
            }
        }
        initNodeProperties(mainTree, idToNodeTbl);


        return mainTree;

    }


    protected static AnnotationNode parse(String s, HashMap<String, AnnotationNode> idToNodeTbl) {
        AnnotationNode node = null;
        AnnotationNode root = null;
        StringTokenizer st = new StringTokenizer(s, DELIM_TREE, true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals(DELIM_BRACKET_OPEN)) {
                if (null == node) {
                    node = new AnnotationNode();
                    root = node;
                }
                else {
                    AnnotationNode newChild = new AnnotationNode();
                    Vector children = node.getChildren();
                    if (null == children) {
                        children = new Vector();
                    }
                    children.addElement(newChild);
                    newChild.setParent(node);
                    node.setChildren(children);
                    node = newChild;
                }
            } else if ((token.equals(DELIM_BRACKET_CLOSE)) ||
                       (token.equals(DELIM_COMMA)) ||
                       (token.equals(DELIM_SEMI_COLON))) {
            } else {
                int index = token.indexOf(DELIM_COLON);
                int squareIndexStart =
                    token.indexOf(DELIM_SQUARE_BRACKET_OPEN);
                int squareIndexEnd = token.indexOf(DELIM_SQUARE_BRACKET_CLOSE);
                if (0 == squareIndexStart) {
                    node.setType(token.substring(squareIndexStart, squareIndexEnd + 1));
                } else {
                    if (-1 == index) {
                        if (null == node) {
                            node = new AnnotationNode();
                            node.setNodeName(token);
                            root = node;
                        }
                    }
                    else if (0 == index) {
                        if (-1 == squareIndexStart) {
                            node.setBranchLength(token.substring(index + 1));                        }
                        else {
                            node.setBranchLength(token.substring((index + 1), squareIndexStart));
                            node.setType(token.substring(squareIndexStart, squareIndexEnd + 1));
                        }
                        node = node.getParent();
                    }
                    else {
                        AnnotationNode newChild = new AnnotationNode();
                        newChild.setNodeName(token.substring(0, index));
                        if (-1 == squareIndexStart) {
                            newChild.setBranchLength(token.substring(index + 1));
                        }
                        else {
                            newChild.setBranchLength(token.substring((index + 1), squareIndexStart));
                            newChild.setType(token.substring(squareIndexStart, squareIndexEnd + 1));
                        }

                        Vector children = node.getChildren();
                        if (null == children) {
                            children = new Vector();
                        }
                        children.addElement(newChild);
                        newChild.setParent(node);
                        node.setChildren(children);
                    }
                }
            }
        }
        return root;
    }


    protected static void setNodes(AnnotationNode node, HashMap<String, AnnotationNode> idToNodeTbl) {
        if (null == node) {
            return;
        }
        String nodeName = node.getNodeName();
        if (null != nodeName) {
            idToNodeTbl.put(nodeName, node);
        }
        Vector v = node.getChildren();
        if (null == v) {
            return;
        }
        for (int i = 0; i < v.size(); i++) {
            setNodes((AnnotationNode)v.elementAt(i), idToNodeTbl);
        }
    }


    protected static void initNodeProperties(AnnotationNode n, HashMap<String, AnnotationNode> idToNodeTbl) {
        String id = n.getAnnotationNodeId();
        if (null != id) {
            idToNodeTbl.put(id, n);
        }
        n.setReferenceSpeciationEvent(getReferenceSpeciationEvent(n));
        if (null == n.getBranchLength()) {
            n.setBranchLength(Integer.toString(0));
        }
        Vector children = n.getChildren();
        if (null == children) {
            return;
        }
        for (int i = 0; i < children.size(); i++) {
            AnnotationNode child = (AnnotationNode)children.get(i);
            initNodeProperties(child, idToNodeTbl);
        }

    }
  
    public static String getReferenceSpeciationEvent(AnnotationNode an) {
        if (true == an.isDuplicationNode()) {
            Vector children = an.getChildren();
            if (null != children) {
                HashSet tmpList = new HashSet();
                for (int i = 0; i < children.size(); i++) {
                    AnnotationNode child = (AnnotationNode)children.get(i);
                    String species = child.getSpecies();
                    if (null != species) {
                        tmpList.add(species);
                    }
                }
                int num = tmpList.size();
                if (num > 0) {
                    String[] strArray = new String[num];
                    tmpList.toArray(strArray);
                    return Utils.listToString(strArray, STR_EMPTY, SEPARATOR_SPECIES);
                }
            }
        }
        return an.getSpecies();
    }    

}

