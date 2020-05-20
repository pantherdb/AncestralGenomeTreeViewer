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

import edu.usc.ksom.pm.panther.pantherdb.util.LibraryHelper;
import java.util.HashMap;


public class MSA {
    private HashMap<String, AnnotationNode> nodeLookup;
    private String familyId;

    
    
    public AnnotationNode getNode(String annotId) {
        if (null == annotId) {
            return null;
        }
        AnnotationNode an = nodeLookup.get(annotId);
        if (null != an || null == annotId) {
            return an;
        }
        
        // Check by removing family portion of id
        int index = annotId.indexOf(LibraryHelper.FAMILY_ANNOT_NODE_SEPARATOR);
        index++;
        if (index >= 0 && index < annotId.length()) {
            return nodeLookup.get(annotId.substring(index));
        }
        return null;
        
    }
    
    public String getSequence(String annotId) {
        if (null == annotId) {
            return null;
        }
        AnnotationNode an = nodeLookup.get(annotId);
        if (null != an || null == annotId) {
            return an.getSequence();
        }

        // Check by removing family portion of id
        int index = annotId.indexOf(LibraryHelper.FAMILY_ANNOT_NODE_SEPARATOR);
        index++;
        if (index >= 0 && index < annotId.length()) {
            AnnotationNode node = nodeLookup.get(annotId.substring(index));
            if (null != node) {
                return node.getSequence();
            }
        }
        return null;
    }

    public HashMap<String, AnnotationNode> getNodeLookup() {
        return nodeLookup;
    }

    public void setNodeLookup(HashMap<String, AnnotationNode> nodeLookup) {
        this.nodeLookup = nodeLookup;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
    
    
}
