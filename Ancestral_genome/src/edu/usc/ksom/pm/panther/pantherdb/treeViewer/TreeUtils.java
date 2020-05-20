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
import edu.usc.ksom.pm.panther.pantherdb.dataModel.MSA;
import edu.usc.ksom.pm.panther.pantherdb.util.Utils;
import java.util.HashMap;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class TreeUtils {
    private static final String ELEMENT_ANNOTATION_NODE = "annotation_node";
    private static final String ELEMENT_ANNOTATION_ACCESSION = "accession";
    private static final String ELEMENT_ANNOTATION_NODE_ID = "annotation_node_id";
    private static final String ELEMENT_ANNOTATION_NODE_NAME = "annotation_node_name";
    private static final String ELEMENT_ANNOTATION_NODE_SF_ID = "sf_id";
    private static final String ELEMENT_ANNOTATION_NODE_SF_NAME = "sf_name";
    private static final String ELEMENT_ANNOTATION_NODE_GENE_ID = "gene_id";
    private static final String ELEMENT_ANNOTATION_NODE_GENE_SYMBOL = "gene_symbol";
    private static final String ELEMENT_ANNOTATION_NODE_DEFINITION = "definition";
    private static final String ELEMENT_ANNOTATION_NODE_MF = "MF";
    private static final String ELEMENT_ANNOTATION_NODE_BP = "BP";
    private static final String ELEMENT_ANNOTATION_NODE_CC = "CC";
    private static final String ELEMENT_ANNOTATION_NODE_PC = "PC";    
    private static final String ELEMENT_ANNOTATION_PUBLIC_ID = "public_id";
    private static final String ELEMENT_ANNOTATION_PARENT = "parent";
    private static final String ELEMENT_FAMILY_NAME = "family_name";
    private static final String ELEMENT_FAMILY_ID = "family_id";
    private static final String ELEMENT_NODE_NAME = "node_name";
    private static final String ELEMENT_NODE_SPECIES = "species";
    private static final String ELEMENT_NODE_ORGANISM = "organism";
    private static final String ELEMENT_REFERENCE_SPECIATION_EVENT= "reference_speciation_event";
    private static final String ELEMENT_GENE_SYMBOL = "gene_symbol";
    private static final String ELEMENT_ANNOTATION_NODE_TYPE = "node_type";
    private static final String ELEMENT_ANNOTATION_TREE_NODE_TYPE = "tree_node_type";
    private static final String ELEMENT_ANNOTATION_EVENT_TYPE = "event_type";
    private static final String ELEMENT_ANNOTATION_BRANCH_LENGTH = "branch_length";
    private static final String ELEMENT_ANNOTATION_SEQUENCE = "sequence";
    private static final String ELEMENT_MSA = "msa";
    private static final String ELEMENT_WEIGHT = "weight";
    private static final String ELEMENT_CHILDREN = "children";
    private static final String ELEMENT_SEQUENCE_LIST = "sequence_list";
    private static final String ELEMENT_WEIGHT_LIST = "weight_list";
    private static final String ELEMENT_SEQUENCE_INFO = "sequence_info";
    private static final String ELEMENT_WEIGHT_INFO = "weight_info";
    private static final String ELEMENT_FULL_SEQUENCE = "full_sequence";
    
    public static final String DUPLICATION = "DUPLICATION";
    public static final String SPECIATION = "SPECIATION";
    public static final String HORIZONTAL_TRANSFER = "HORIZONTAL_TRANSFER";    
    public static final String EXTANT = "EXTANT"; 
    public static final String LEAF = "LEAF";     
    
    public static Element getTree(Document doc, AnnotationNode an) {
        if (null == an) {
            return null;
        }
        return getAnnotationNodeElemAndDesc(doc, an);
    }
    
    public static Element getMSA(Document doc, MSA msa) {
        Element msaElem = doc.createElement(ELEMENT_MSA);
        HashMap<String, AnnotationNode> nodeLookup = msa.getNodeLookup();
        if (null == nodeLookup) {
            return msaElem;
        }

        Element seqListElem = doc.createElement(ELEMENT_SEQUENCE_LIST);
        msaElem.appendChild(seqListElem);
        for (AnnotationNode an : nodeLookup.values()) {
            Element anElem = getAnnotationNodeElemForMSA(doc, an);
            seqListElem.appendChild(anElem);
        }
        return msaElem;
    }
    
    public static Element getFamilyName(Document doc, String familyName) {
        Element familyNameElem = doc.createElement(ELEMENT_FAMILY_NAME);
        if (null != familyName) {
            familyNameElem.appendChild(doc.createTextNode(familyName));
        }
        return familyNameElem;        
    }
    
    private static Element getAnnotationNodeElemForMSA(Document doc, AnnotationNode an) {
        if (null == an) {
            return null;
        }
        
        Element annotationNodeElement = doc.createElement(ELEMENT_ANNOTATION_NODE);
        String accession = an.getAccession();
        if (null != accession) {
            Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_ACCESSION, accession);
        }
        String sequence = an.getSequence();
        if (null != sequence) {
            Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_SEQUENCE, sequence);
        }
        return annotationNodeElement;
    }
    
    private static Element getAnnotationNodeElem(Document doc, AnnotationNode an) {
        if (null == an) {
            return null;
        }
        
        Element annotationNodeElement = doc.createElement(ELEMENT_ANNOTATION_NODE);
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_ACCESSION, an.getAnnotationNodeId());                 
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_NODE_NAME, an.getNodeName());          
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_NODE_SPECIES, an.getSpecies());
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_NODE_GENE_SYMBOL, an.getGeneSymbol());
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_NODE_DEFINITION, an.getDefinition());
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_NODE_ORGANISM, an.getOrganism());        
      

        Utils.addToElement(doc, annotationNodeElement, ELEMENT_REFERENCE_SPECIATION_EVENT, an.getReferenceSpeciationEvent());          
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_TREE_NODE_TYPE, an.getTreeNodeType());
        if (true == an.isDuplicationNode()) {
            Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_NODE_TYPE, DUPLICATION);
        }
        else if (true == an.isHorizontalTransferNode()) {
            Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_NODE_TYPE, HORIZONTAL_TRANSFER);    
        }
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_EVENT_TYPE, an.getEventType()); 
        Utils.addToElement(doc, annotationNodeElement, ELEMENT_ANNOTATION_BRANCH_LENGTH, an.getBranchLength());
        return annotationNodeElement;
    }
    
    private static Element getAnnotationNodeElemAndDesc(Document doc, AnnotationNode an) {

        Element annotationNodeElement = getAnnotationNodeElem(doc, an);
        if (null == annotationNodeElement) {
            return null;
        }
        
        Vector children = an.getChildren();
        if (null != children) {
            int num = children.size();
            if (0 != num) {
                Element childrenElem = doc.createElement(ELEMENT_CHILDREN);
                annotationNodeElement.appendChild(childrenElem);
                for (int i = 0; i < num; i++) {
                    AnnotationNode child = (AnnotationNode)children.get(i);
                    Element childElem = getAnnotationNodeElemAndDesc(doc, child);
                    if (null != childElem) {
                        childrenElem.appendChild(childElem);
                    }
                }
                
            }
        }
        
        return annotationNodeElement;        
    }
        
          
}
