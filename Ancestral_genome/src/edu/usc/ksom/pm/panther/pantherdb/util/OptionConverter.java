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


import java.util.Properties;
import java.util.ResourceBundle;

public class OptionConverter{
	static String DELIM_START = "${";
	static char   DELIM_STOP = '}';
	static int DELIM_START_LEN = 2;
	static int DELIM_STOP_LEN  = 1;
	
	/* OptionConverter is a static class */
	private OptionConverter(){}
	
	public static String findAndSubst(String key, Properties props){
		String value = props.getProperty(key);
		if(value == null)
			return null;
		return substVars(value, props);
	}
	
	public static String findAndSubst(String key, ResourceBundle bundle){
		String value = bundle.getString(key);
		if(value == null)
			return null;
		return substVars(value, bundle);
	}


	public static String substVars(String val, Properties props){
		int j, k; // indexes for DELIM_START & DELIM_STOP
		
		j = val.indexOf(DELIM_START, 0);
		k = val.indexOf(DELIM_STOP, j);
		
		String key = val.substring(j + DELIM_START_LEN,k);
		// try the system properties
		String replacement = System.getProperty(key,null);
		// try the props parameter
		if(replacement == null && props != null){
			replacement = props.getProperty(key);
		}
		if(replacement != null){
			return (val.substring(0,j) + replacement + val.substring(k + DELIM_STOP_LEN,val.length()));
		}    
		
		return replacement;	
	}
	
	public static String substVars(String val, ResourceBundle bundle){
		int j, k; // indexes for DELIM_START & DELIM_STOP
		
		j = val.indexOf(DELIM_START, 0);
		if(j < 0)
			return val;
		k = val.indexOf(DELIM_STOP, j);
		
		String key = val.substring(j + DELIM_START_LEN,k);
		// try the system properties
		String replacement = System.getProperty(key,null);

		// try the props parameter
		if(replacement == null && bundle != null){
			replacement = bundle.getString(key);
		}
		if(replacement != null){
			return (val.substring(0,j) + replacement + val.substring(k + DELIM_STOP_LEN,val.length()));
		}
		
		return replacement;
	}
}
