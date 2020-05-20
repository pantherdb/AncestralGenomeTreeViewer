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

import java.util.Hashtable;


public class PantherGeneHelper {
    protected String longGeneName;
    protected String organism;
    protected String geneSource;
    protected String geneId;
    protected String proteinSource;
    protected String proteinId;
    
    public static final String DELIM_NAME = "\\|";
    public static final String DELIM_DB = "=";

    public static final String DATABASE_PREFIX_MGI = "MGI:";
    
    public static final String SPECIAL_CASE_TAIR = "TAIR";
    public static final String SPECIAL_CASE_MGI = "MGI";
    public static final String SPECIAL_CASE_SOURCES[] = {SPECIAL_CASE_TAIR, SPECIAL_CASE_MGI};     // These evidence sources contain extra ":=", etc
    protected static final String REPLACE_STRING = "XXX";
    
    protected static final String ENSEMBL_ORGANISM_SUB = "ORG_SUB";
    
    protected static final String SUPPORTED_PROTEIN_SOURCE_UNIPROTKB = "UniProtKB";
    protected static final String SUPPORTED_PROTEIN_SOURCE_NCBI = "NCBI";
    protected static final String SUPPORTED_PROTEIN_SOURCE_ENSEMBL = "ENSEMBL";
    
    protected static final String[] LIST_SUPPORTED_PROTEIN_SOURCE = {SUPPORTED_PROTEIN_SOURCE_UNIPROTKB, SUPPORTED_PROTEIN_SOURCE_NCBI,SUPPORTED_PROTEIN_SOURCE_ENSEMBL};
    
    protected static final String PROTEIN_ID_URL_NCBI = "http://www.ncbi.nlm.nih.gov/protein/XXX";
    protected static final String PROTEIN_ID_URL_UNIPROTKB = "http://www.uniprot.org/uniprot/XXX";
    protected static final String PROTEIN_ID_URL_ENSEMBL = "http://www.ensembl.org/" + ENSEMBL_ORGANISM_SUB + "/Transcript/ProteinSummary?db=core;p=XXX";
    

    
    protected static final String SUPPORTED_GENE_SOURCE_ENSEMBL = "ENSEMBL";
    protected static final String SUPPORTED_GENE_SOURCE_ENTREZ = "ENTREZ";
    protected static final String SUPPORTED_GENE_SOURCE_WORMBASE = "WormBase";
    protected static final String SUPPORTED_GENE_SOURCE_FYLBASE = "FlyBase";
    
    protected static final String[] LIST_KNOWN_GENE_SOURCES = {SUPPORTED_GENE_SOURCE_ENSEMBL, SUPPORTED_GENE_SOURCE_ENTREZ, SUPPORTED_GENE_SOURCE_WORMBASE, SUPPORTED_GENE_SOURCE_FYLBASE};
    protected static final String GENE_ID_URL_ENSEMBL = "http://www.ensembl.org/" + ENSEMBL_ORGANISM_SUB + "/Gene/Summary?g=XXX";
    protected static final String GENE_ID_URL_ENTREZ = "http://www.ncbi.nlm.nih.gov/gene/XXX";
    
    
    public PantherGeneHelper(String longGeneName) {
        this.longGeneName = longGeneName;
        if (null == longGeneName) {
            return;
        }
        String parts[] = longGeneName.split(DELIM_NAME);
        int length = parts.length;
        if (length < 2) {
            return;
        }
        organism = parts[0];
        
        // Gene part
        String geneParts[] = parts[1].split(DELIM_DB);
        if (geneParts.length < 2) {
            return;
        }
        geneSource = geneParts[0];
        geneId = geneParts[1];
        
        /*
         * Check for special cases
         * MGI id is of the form MOUSE|MGI=MGI=97788|UniProtKB=Q99LS3
         */
         if (true == Utils.search(SPECIAL_CASE_SOURCES, geneSource)) {
            if (geneSource.equals(SPECIAL_CASE_MGI) && !geneSource.startsWith(DATABASE_PREFIX_MGI)) {
                if (geneParts.length >= 3) {
                    geneId = DATABASE_PREFIX_MGI + geneParts[2];
                }
                else {
                    geneId = DATABASE_PREFIX_MGI + geneId;
                }
            }
            if (geneSource.equals(SPECIAL_CASE_TAIR)) {
                if (geneParts.length >= 3) {
                    geneId = geneParts[2];
                }
            }
         }
        
        
        // Protein part 
        String proteinParts[] = parts[2].split(DELIM_DB);
        if (proteinParts.length < 2) {
            return;
        }
        proteinSource = proteinParts[0];
        proteinId = proteinParts[1];
        
    }    

    public String getLongGeneName() {
        return longGeneName;
    }

    public String getOrganism() {
        return organism;
    }

    public String getGeneSource() {
        return geneSource;
    }

    public String getGeneId() {
        return geneId;
    }

    public String getProteinSource() {
        return proteinSource;
    }

    public String getProteinId() {
        return proteinId;
    }
    
}
