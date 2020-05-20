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

import edu.usc.ksom.pm.panther.pantherdb.MSA.MSAParser;
import edu.usc.ksom.pm.panther.pantherdb.dataModel.AnnotationNode;
import edu.usc.ksom.pm.panther.pantherdb.dataModel.MSA;
import edu.usc.ksom.pm.panther.pantherdb.dataModel.Tree;
import edu.usc.ksom.pm.panther.pantherdb.util.LibraryHelper;
import java.util.List;


public class TreeManager {
    
    public static final String DELIM_FAMILY_ID_NAME = "\t";
    public static final String WRAPPER_FAMILY = "'";
    
    public static final String HEADER_DEFINITION = "definition";
    public static final String HEADER_SPECIES = "species";
    public static final String HEADER_GENE_SYMBOL = "gene_symbol";
    public static final String HEADER_PUBLIC_NODE_ID = "public_node_id";    
    
    public static final String DELIM_ATTR = "\t";
    public static final String WRAPPER_ATTR = "'";    

    public static Tree getTree(String book) {
        if (null == book) {
            return null;
        }
        
        String url = LibraryHelper.getURLTreeFile(book);
        List<String> contents = edu.usc.ksom.pm.panther.pantherdb.util.Utils.readFromURL(url);
        if (null == contents) {
            return null;
        }
        Tree tree = TreeParser.getTree(book, contents);

        // Add attribute values
        if (null != tree && null != tree.getRoot()) {
            List<String> attrContents = edu.usc.ksom.pm.panther.pantherdb.util.Utils.readFromURL(LibraryHelper.getURLForAttr(book));
            if (null != attrContents && attrContents.size() > 0) {

                String headers = attrContents.get(0);
                headers = headers.trim();
                String parts[] = headers.split(DELIM_ATTR);
                if (null != parts && 0 != parts.length) {

                    int header_length = parts.length;
                    int index_def = -1;
                    int index_species = -1;
                    int index_gene_symbol = -1;
                    int index_public_node_id = -1;
                    for (int i = 0; i < header_length; i++) {
                        String curHeader = parts[i];
                        curHeader = curHeader.trim();
                        if (true == curHeader.startsWith(WRAPPER_ATTR)) {
                            curHeader = curHeader.substring(WRAPPER_ATTR.length());
                        }
                        if (true == curHeader.endsWith(WRAPPER_ATTR)) {
                            curHeader = curHeader.substring(0, curHeader.length() - WRAPPER_ATTR.length());
                        }
                        parts[i] = curHeader;
                        if (HEADER_DEFINITION.equalsIgnoreCase(parts[i])) {
                            index_def = i;
                        } else if (HEADER_SPECIES.equalsIgnoreCase(parts[i])) {
                            index_species = i;
                        } else if (HEADER_GENE_SYMBOL.equalsIgnoreCase(parts[i])) {
                            index_gene_symbol = i;
                        } else if (HEADER_PUBLIC_NODE_ID.equalsIgnoreCase(parts[i])) {
                            index_public_node_id = i;
                        }
                    }

                    for (int i = 1; i < attrContents.size(); i++) {
                        String current = attrContents.get(i);
                        if (null == current) {
                            continue;
                        }
                        current = current.trim();
                        String[] cparts = current.split(DELIM_ATTR);
                        if (header_length != cparts.length) {
                            continue;
                        }
                        String publicNodeId = cparts[index_public_node_id];
                        publicNodeId = publicNodeId.trim();
                        if (true == publicNodeId.startsWith(WRAPPER_ATTR)) {
                            publicNodeId = publicNodeId.substring(WRAPPER_ATTR.length());
                        }
                        if (true == publicNodeId.endsWith(WRAPPER_ATTR)) {
                            publicNodeId = publicNodeId.substring(0, publicNodeId.length() - WRAPPER_ATTR.length());
                        }

                        AnnotationNode n = tree.getNode(publicNodeId);
                        if (null == n) {
                            continue;
                        }
                        n.setPublicId(publicNodeId);
                        n.setGeneId(publicNodeId);
                        for (int j = 0; j < header_length; j++) {
                            if (j != index_def && j != index_species && j != index_gene_symbol) {
                                continue;
                            }
                            String curPart = cparts[j];
                            if (true == curPart.startsWith(WRAPPER_ATTR)) {
                                curPart = curPart.substring(WRAPPER_ATTR.length());
                            }
                            if (true == curPart.endsWith(WRAPPER_ATTR)) {
                                curPart = curPart.substring(0, curPart.length() - WRAPPER_ATTR.length());
                            }
                            if (j == index_def) {
                                n.setDefinition(curPart);
                            } else if (j == index_species) {
                                n.setOrganism(curPart);
                            } else {
                                n.setGeneSymbol(curPart);
                            }
                        }
                    }
                }
            }
        }
        return tree;
    }
    
   
    
    public static MSA getMSA(String book) {
        String url = LibraryHelper.getURLForMSA(book);
        
        List<String> seqInfo = edu.usc.ksom.pm.panther.pantherdb.util.Utils.readFromURL(url);
        return MSAParser.getMSA(book, seqInfo);
    }
    
    
  
    public static String getFamilyName(String book) {

        String familyUrl = LibraryHelper.getURLForFamilyName(book);
        try {
            List<String> familyContents = edu.usc.ksom.pm.panther.pantherdb.util.Utils.readFromURL(familyUrl);
            if (null == familyContents) {
                System.out.println("Unable to read tree information from url " + familyUrl);
                return null;
            }
            if (familyContents.size() >= 1) {
                String contents = familyContents.get(0);
                String[] parts = contents.split(DELIM_FAMILY_ID_NAME);
                if (parts.length == 2) {
                    String name = parts[1];
                    if (true == name.startsWith(WRAPPER_FAMILY)) {
                        name = name.substring(WRAPPER_FAMILY.length());
                    }
                    if (true == name.endsWith(WRAPPER_FAMILY)) {
                        name = name.substring(0, name.length() - WRAPPER_FAMILY.length());
                    }
                    return name;
                }
                else {
                    return contents;
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("Unable to read attribute information from url " + familyUrl);
            return null;
        }
    }    
}
