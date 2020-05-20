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


import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

public class ReadResources
{
	protected boolean isInitialized = false;
	protected String[] resourceNames;
	private ResourceBundle[] bundles;
	private ResourceBundle bundle;  //bundle with found property


	public ReadResources()
	{
	}

	public ReadResources(String name) throws Exception {
		String[] names = {name};
		initialize(names);
	}

	public ReadResources(String[] names) throws Exception {
		initialize(names);
	}

	protected void initialize(String[] names) throws Exception{
		resourceNames = names;
		isInitialized = false;               // default to false
		bundles = new ResourceBundle[names.length];

		String resourceName = null;
		try{
			for (int i=0; i < resourceNames.length; i++) {
				resourceName = resourceNames[i];
				bundles[i] = ResourceBundle.getBundle(resourceName);
			}
		} catch(MissingResourceException mrex){
			StringBuffer error = new StringBuffer("Can't find ");
			error.append(resourceName).append(" Maybe not in your CLASSPATH").append("\r\n");
			error.append("CLASSPATH used : ").append(System.getProperty("java.class.path"));
			throw new Exception(error.toString());
		}    
		isInitialized = true;
	}

	public void setResource(String name) throws Exception {
		String[] names = {name};
		setResource(names);
	}

	public void setResource(String[] names) throws Exception 
	{
		boolean reinitializeFlag = false;
		if (resourceNames == null)
			reinitializeFlag = true;
		else if (names.length != resourceNames.length)
			reinitializeFlag = true;
		else {
			//loop through both arrays of resource names and make sure they are identical
			for (int i=0; i < names.length; i++) {
				if (! names[i].equals(resourceNames[i])) {
					reinitializeFlag = true;
					break;
				}
			}
		}

		if (reinitializeFlag)
			initialize(names);
	}


	public boolean isInitialized() {
		return isInitialized;
	}

	public String getKey(String key) throws Exception { 
		return getKey(key,null);
	}

	public String getKey(String key, String defaultValue) throws Exception{
		bundle = null;
		String result = null;
		for (int i=0; i < bundles.length; i++) {
			try {  
				result = bundles[i].getString(key);
				if (result != null) {
					//for result, need to know corresponding bundle
					//store so can get later
					bundle = bundles[i];
					break;
				}
			}  catch(MissingResourceException mrex){
				//no point in doing anything with te exception.  if not get result
				//print throw exception below
			}
		}

		if (result == null) {
			if (defaultValue == null) 
				throw new Exception("Key " + key + " doesn't exist in property file(s)"); 
			else
				result = defaultValue;
		}

		if(bundle == null)
			throw new Exception("resource Name not Setup");

		return result;
	}


	public Enumeration<String> getKeys() throws Exception{
		if (bundles == null){
			throw new Exception("resource Name not Setup");
		}
		else{
			Vector<String> allKeys = new Vector<String> ();
			//loop through all bundles and get all keys
			Enumeration<String> e;
			for (int i=0; i < bundles.length; i++) { 
				e = bundles[i].getKeys();
				while(e.hasMoreElements()) {
					allKeys.addElement(e.nextElement());
				}
			}
			return allKeys.elements();
		}
	}

	//use to get at bundle with property
	public ResourceBundle getBundle(){
		return bundle;
	}


}
