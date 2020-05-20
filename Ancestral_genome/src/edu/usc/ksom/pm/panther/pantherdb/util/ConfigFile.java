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



import java.util.*;


public class ConfigFile{
  // only place the commonly used properties here
  // If you need to add a property used in only one site, e.g. paint,
  // please place it in that constant class file for that site, e.g. paint.properties.
  public static final String  KEY_LIBRARY_ROOT = "URL_PANTHER_ROOT";

  protected static String[] propertyFiles = {"treeViewer"};


  protected static Properties m_Properties = System.getProperties();
  protected static ReadResources rr = null;
  static{
    try{
      rr = new ReadResources(propertyFiles);
    }
    catch(Exception ex){
      ex.printStackTrace();
      rr = null;
    }
  }

  public static String getProperty(String key){
    if(rr == null)
      return OptionConverter.substVars(m_Properties.getProperty(key),m_Properties);
    else{
      try{
        return OptionConverter.substVars(rr.getKey(key),rr.getBundle());
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
      return null;
    }
  }

  public static Enumeration getProperties(){
    try {
      return rr.getKeys();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

}

