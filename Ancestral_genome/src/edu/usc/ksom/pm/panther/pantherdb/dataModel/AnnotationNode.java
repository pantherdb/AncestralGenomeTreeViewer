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
package edu.usc.ksom.pm.panther.pantherdb.dataModel;


import java.util.Vector;

public class AnnotationNode {

    String accession;   //PTHR10000:AN5
    String publicId;    // PTN1234
    String eventType;   // Speciation, duplication
    String nodeType;    // Information from tree file
    String treeNodeType;    // Internal, root, etc
    
    String familyId;    // PTHR10000
    String familyName;  // protease
    String annotationNodeId;    // AN5
    String branchLength;    //0.123
    String parentId;    // public id, null for root nodes
    String sequence;    // DELAXXXGVXXXVAXITXXAMXGEXDFXXXLXXRLXLLXGXXXXXXXXXXXKXXXTXGXXXLXXXLKXXGXXXXLVSGGFXXXAXXVAXXLGXDYAYANXLEFXXDXGXL
    Vector children;    // Vector of annotation nodes
    AnnotationNode parent;
    String type;        // Info from tree file such as [&&NHX:Ev=0>1:S=LUCA:ID=AN109]
    String nodeName;    // YEAST|SGD=S000003440|UniProtKB=P42941 (for leaves)
    String referenceSpeciationEvent;
    String geneSymbol;
    String geneId;
    String sfId;
    String sfName;
    String mf;
    String bp;
    String cc;
    String pc;
    String organism;
    String definition;    
    
    public static final String DELIM_FAM_ANNOT_ID = ":";    
    public static final String NODE_TYPE_DUPLICATION = "1>0";
    public static final String NODE_TYPE_HORIZONTAL_TRANSFER="0>0";
    public static final String NODE_TYPE_SPECIES = "S=";
    public static final String NODE_TYPE_ANNOTATION = "ID=";
    public static final int NODE_TYPE_ANNOTATION_LENGTH = NODE_TYPE_ANNOTATION.length();
    public static final String NODE_TYPE_INFO_SEPARATOR = ":";
    public static final String NODE_TYPE_INFO_PREFIX = "[";
    public static final String NODE_TYPE_INFO_SUFFIX = "]";

    public AnnotationNode() {
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setAnnotationNodeId(String annotationNodeId) {
        this.annotationNodeId = annotationNodeId;
    }

    public String getAnnotationNodeId() {
        return annotationNodeId;
    }

    public void setBranchLength(String branchLength) {
        this.branchLength = branchLength;
    }

    public String getBranchLength() {
        return branchLength;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getSequence() {
        return sequence;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getAccession() {
        return accession;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setChildren(Vector children) {
        this.children = children;
    }

    public Vector getChildren() {
        return children;
    }

    public void setParent(AnnotationNode parent) {
        this.parent = parent;
    }

    public AnnotationNode getParent() {
        return parent;
    }

    public void setType(String type) {
        this.nodeType = type;
        String annotId = this.getAnnotIdFromNodeInfo();
        if (null != annotId) {
            this.annotationNodeId = annotId;
        }
    }

    private String getAnnotIdFromNodeInfo() {
        if (null == nodeType) {
            return null;
        }
        int index = nodeType.indexOf(NODE_TYPE_ANNOTATION);
        if (index < 0) {
            return null;
        }
        int endIndex = nodeType.indexOf(NODE_TYPE_INFO_SEPARATOR, index);
        if (-1 == endIndex) {
            endIndex = nodeType.indexOf(NODE_TYPE_INFO_SUFFIX);
        }
        return new String(nodeType.substring(index + NODE_TYPE_ANNOTATION_LENGTH, endIndex));

    }

    public boolean isDuplicationNode() {
        if (null == nodeType) {
            return false;
        }
        int index = nodeType.indexOf(NODE_TYPE_DUPLICATION);
        if (index < 0) {
            return false;
        }
        return true;
    }
    
    public boolean isHorizontalTransferNode() {
            if (null == nodeType) {
                    return false;
            }
            int index = nodeType.indexOf(NODE_TYPE_HORIZONTAL_TRANSFER);
            if (index < 0) {  
                    return false;
            }
            return true;       
    }

    public String getSpecies() {
        if (null == nodeType) {
            return null;
        }
        int length = nodeType.length();
        int index = nodeType.indexOf(NODE_TYPE_SPECIES);

        if (index < 0 || index >= length) {
            return null;
        }
        index +=  NODE_TYPE_SPECIES.length();
        int endIndex = nodeType.indexOf(NODE_TYPE_INFO_SEPARATOR, index);
        if (-1 == endIndex) {
            endIndex = nodeType.indexOf(NODE_TYPE_INFO_SUFFIX);
        }
        if (endIndex < index || endIndex >= length) {
            return null;
        }
        return new String(nodeType.substring(index, endIndex));
    }

    public String getType() {
        return type;
    }
    
        public String getSfId() {
        return sfId;
    }

    public void setSfId(String sfId) {
        this.sfId = sfId;
    }

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public String getMf() {
        return mf;
    }

    public void setMf(String mf) {
        this.mf = mf;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
    

    public static String constructFullAnIdFromShort(String familyId, String shortAnnotationId) {
        return familyId + DELIM_FAM_ANNOT_ID + shortAnnotationId;
    }    


    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setReferenceSpeciationEvent(String referenceSpeciationEvent) {
        this.referenceSpeciationEvent = referenceSpeciationEvent;
    }

    public String getReferenceSpeciationEvent() {
        return referenceSpeciationEvent;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }
    
    public String getTreeNodeType() {
        return treeNodeType;
    }

    public void setTreeNodeType(String treeNodeType) {
        this.treeNodeType = treeNodeType;
    }    
}
