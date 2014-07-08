// Fraunhofer Institute for Computer Graphics Research (IGD)
// Department Spatial Information Management (GEO)
//
// Copyright (c) 2008-2014 Fraunhofer IGD
//
// This file is part of osgi-util.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package de.fhg.igd.osgi.util.configuration;

import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * <p>Title: JavaPreferencesConfigurationService</p>
 * <p>Description: {@link IConfigurationService} implementation using
 * Java {@link Preferences}</p>
 * @author Michel Kraemer
 */
public class JavaPreferencesConfigurationService extends AbstractDefaultConfigurationService {
	
	/**
	 * The preferences service
	 */
	private final Preferences root;
	
	/**
	 * The delimiter between namespace and the rest of the key
	 */
	private final String nodeDelimiter;
	
	/**
	 * Constructor
	 * 
	 * @param useSystemPrefs if the system root shall be used
	 *   instead of the user root
	 * @param defaultProperties the default properties, may be <code>null</code>
	 * @param fallBackToSystemProperties if the service may fall back to the system
	 *   properties for retrieving default values
	 */
	public JavaPreferencesConfigurationService(final boolean useSystemPrefs,
			final Properties defaultProperties,
			final boolean fallBackToSystemProperties) {
		this(useSystemPrefs, NamespaceConfigurationItem.DELIMITER,
				defaultProperties, fallBackToSystemProperties);
	}
	
	/**
	 * Constructor
	 * 
	 * @param useSystemPrefs if the system root shall be used
	 *   instead of the user root
	 * @param nodeDelimiter the delimiter between namespace
	 *   and the rest of the key
	 * @param defaultProperties the default properties, may be <code>null</code>
	 * @param fallBackToSystemProperties if the service may fall back to the system
	 *   properties for retrieving default values
	 */
	public JavaPreferencesConfigurationService(final boolean useSystemPrefs,
			final String nodeDelimiter, final Properties defaultProperties,
			final boolean fallBackToSystemProperties) {
		super(defaultProperties, fallBackToSystemProperties);
		
		if (useSystemPrefs) {
			root = Preferences.systemRoot();
		} else {
			root = Preferences.userRoot();
		}
		
		this.nodeDelimiter = nodeDelimiter;
	}
	
	/**
	 * Get the node for a given key
	 * 
	 * @param key the key
	 * 
	 * @return the node for the key
	 */
	protected Preferences getNode(String key) {
		int index = key.lastIndexOf(nodeDelimiter);
		if (index <= 0) {
			return root;
		}
		else {
			String nodeName = "/" + key.substring(0, index);
			return root.node(nodeName);
		}
	}
	
	/**
	 * Get the key for a given key, where the namespace
	 *   is removed
	 * 
	 * @param key the key
	 * 
	 * @return the key without namespace
	 */
	protected String getKey(String key) {
		int index = key.lastIndexOf(nodeDelimiter);
		if (index < 0) {
			return key;
		}
		else {
			return key.substring(index + nodeDelimiter.length());
		}
	}

	/**
	 * @see AbstractConfigurationService#getValue(String)
	 */
	@Override
	protected String getValue(String key) {
		Preferences pref = getNode(key);
		try {
			pref.sync();
		} catch (BackingStoreException e) {
			// ignore
		}
		return pref.get(getKey(key), null);
	}

	/**
	 * @see AbstractConfigurationService#removeValue(String)
	 */
	@Override
	protected void removeValue(String key) {
		Preferences pref = getNode(key);
		pref.remove(getKey(key));
		
		//write settings to persistent store
		try {
			pref.sync();
		} catch (BackingStoreException e) {
			throw new IllegalStateException("Could not save preferences", e);
		}
	}

	/**
	 * @see AbstractConfigurationService#setValue(String, String)
	 */
	@Override
	protected void setValue(String key, String value) {
		Preferences pref = getNode(key);
		pref.put(getKey(key), value);
		
		//write settings to persistent store
		try {
			pref.sync();
		} catch (BackingStoreException e) {
			throw new IllegalStateException("Could not save preferences", e);
		}
	}
	
	
}
