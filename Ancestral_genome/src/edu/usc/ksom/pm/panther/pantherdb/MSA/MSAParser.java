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
package edu.usc.ksom.pm.panther.pantherdb.MSA;

import edu.usc.ksom.pm.panther.pantherdb.dataModel.AnnotationNode;
import edu.usc.ksom.pm.panther.pantherdb.dataModel.MSA;
import edu.usc.ksom.pm.panther.pantherdb.util.LibraryHelper;
import java.util.HashMap;
import java.util.List;


public class MSAParser {
    public static final String PREFIX_SEQ_START = ">";        
    public static MSA getMSA(String book, List<String> seqInfo) {
        if (null == seqInfo) {
            return null;
        }

        HashMap<String, AnnotationNode> nodeLookup = new HashMap<String, AnnotationNode>();
        int numLines = seqInfo.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numLines; i++) {
            if ((true == seqInfo.get(i).startsWith(PREFIX_SEQ_START))
                    && (i + 1 < numLines)
                    && (false == seqInfo.get(i + 1).startsWith(PREFIX_SEQ_START))) {

                String seqLbl = seqInfo.get(i).substring(1, seqInfo.get(i).length());
                if (null == seqLbl) {
                    continue;
                }

                // Check for case where sequence does not end in current line.
                int start = i + 1;
                sb.setLength(0);
                while (start < numLines) {
                    if (null != seqInfo.get(start)) {
                        if (true == seqInfo.get(start).startsWith(PREFIX_SEQ_START)) {
                            break;
                        }
                        sb.append(seqInfo.get(start).trim());
                        start++;
                        i++;
                    }
                }

                String accession = LibraryHelper.constructAnnotAcc(book, seqLbl);
                if (null != accession) {
                    AnnotationNode an = new AnnotationNode();
                    an.setAccession(accession);
                    an.setAnnotationNodeId(seqLbl);
                    an.setSequence(sb.toString());
                    nodeLookup.put(accession, an);
                }
            }
        }
        MSA msa = new MSA();
        msa.setFamilyId(book);
        msa.setNodeLookup(nodeLookup);
        return msa;
    }
    
}
