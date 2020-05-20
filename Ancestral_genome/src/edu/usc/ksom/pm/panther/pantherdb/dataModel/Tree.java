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

import java.util.HashMap;


public class Tree implements ITree{
    private AnnotationNode root;
    private HashMap<String, AnnotationNode> nodeLookup;
    private String id;
    
    public static String DELIM_FAMILY_ANNOT_ID = ":";
    
    public Tree(String id, AnnotationNode root, HashMap<String, AnnotationNode> nodeLookup) {
        this.id = id;
        this.root = root;
        this.nodeLookup = nodeLookup;
    }
    
    public AnnotationNode getNode(String annotId) {
        if (null == annotId) {
            return null;
        }
        AnnotationNode an = nodeLookup.get(annotId);
        if (null != an || null == id) {
            return an;
        }
        
        // Check by removing family portion of id
        int index = annotId.indexOf(DELIM_FAMILY_ANNOT_ID);
        index++;
        if (index >= 0 && index < annotId.length()) {
            return nodeLookup.get(annotId.substring(index));
        }
        return null;
        
    }    

    @Override
    public AnnotationNode getRoot() {
        return root;
    }

    @Override
    public HashMap<String, AnnotationNode> getNodeLookup() {
        return nodeLookup;
    }

    @Override
    public String getId() {
        return id;
    }
    
}
