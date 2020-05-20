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


public class LibraryHelper {
    
    public static final String URL_LIB_ROOT = ConfigFile.getProperty(ConfigFile.KEY_LIBRARY_ROOT);
    public static final String DIR_BOOKS = "/books/";
    public static final String FILE_NAME_TREE = "tree.tree";
    public static final String FILE_NAME_MSA = "tree.mia";
    public static final String FILE_NAME_FAMILY_NAME = "family.txt";
    public static final String FILE_NAME_ATTR = "attr.tab";
    public static final String URL_SEPARATOR = "/"; 
    

    public static final String FAMILY_ANNOT_NODE_SEPARATOR = ":";    
    
    public static String getURLTreeFile(String book) {
        if (null == book) {
            return null;
        }
        return URL_LIB_ROOT + DIR_BOOKS + book + URL_SEPARATOR + FILE_NAME_TREE;
    }
    
    public static String getURLForMSA(String book) {
        if (null == book) {
            return null;
        }
        return URL_LIB_ROOT + DIR_BOOKS + book + URL_SEPARATOR + FILE_NAME_MSA;
    }
    
    public static String getURLForFamilyName(String book) {
        if (null == book) {
            return null;
        }
        return URL_LIB_ROOT + DIR_BOOKS + book + URL_SEPARATOR + FILE_NAME_FAMILY_NAME; 
    }
    
    public static String getURLForAttr(String book) {
        if (null == book) {
            return null;
        }
        return URL_LIB_ROOT + DIR_BOOKS + book + URL_SEPARATOR + FILE_NAME_ATTR;         
    }
    
    public static String constructAnnotAcc(String family, String seqLbl) {
        return family + FAMILY_ANNOT_NODE_SEPARATOR + seqLbl;
    }
    
    
    public static String getURLFamilies() {
        return URL_LIB_ROOT + DIR_BOOKS;
    }
}
