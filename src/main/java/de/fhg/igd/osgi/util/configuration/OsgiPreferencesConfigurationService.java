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

/*+-------------+----------------------------------------------------------*
 *|__|__|_|_|_|_|     (Fraunhofer Institute for Computer Graphics)         *
 *|  |  |_|_|_|_|                                                          *
 *|__|__|_|_|_|_|                                                          *
 *|  __ |    ___|                                                          *
 *| /_  /_  / _ |     Fraunhoferstrasse 5                                  *
 *|/   / / /__/ |     D-64283 Darmstadt, Germany                           *
 *+-------------+----------------------------------------------------------*/
import java.util.Properties;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

/**
 * <p>Title: PreferenceConfigurationService</p>
 * <p>Description: {@link IConfigurationService} implementation using
 * a {@link PreferencesService}. Default value for all keys is
 * <code>null</code>.</p>
 * @author Michel Kraemer
 */
public class OsgiPreferencesConfigurationService extends AbstractDefaultConfigurationService {
	/**
	 * The preferences service
	 */
	private PreferencesService _ps;
	
	/**
	 * Creates the configuration service
	 * 
	 * @param defaultProperties the default properties, may be <code>null</code>
	 * @param fallBackToSystemProperties if the service may fall back to the system
	 *   properties for retrieving default values
	 */
	public OsgiPreferencesConfigurationService(final Properties defaultProperties,
			final boolean fallBackToSystemProperties) {
		super(defaultProperties, fallBackToSystemProperties);
	}
	
	/**
	 * Sets the preferences service
	 * @param ps the service
	 */
	public void setPreferencesService(PreferencesService ps) {
		_ps = ps;
	}
	
	/**
	 * @return the preferences service
	 */
	public PreferencesService getPreferencesService() {
		return _ps;
	}
	
	/**
	 * @see AbstractConfigurationService#getValue(java.lang.String)
	 */
	@Override
	protected String getValue(String key) {
		String value;
		if (_ps == null) {
			value = null;
		}
		else {
			Preferences prefs = _ps.getSystemPreferences();
			try {
				prefs.sync();
			} catch (BackingStoreException e) {
				// ignore
			}
			value = prefs.get(key, null);
		}
		
		return value;
	}

	/**
	 * @see AbstractConfigurationService#removeValue(String)
	 */
	@Override
	protected void removeValue(String key) {
		if (_ps == null) {
			return;
		}
		Preferences prefs = _ps.getSystemPreferences();
		prefs.remove(key);
		
		//write settings to persistent store
		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			throw new IllegalStateException("Could not save preferences", e);
		}
	}

	/**
	 * @see AbstractConfigurationService#setValue(String, String)
	 */
	@Override
	protected void setValue(String key, String value) {
		if (_ps == null) {
			return;
		}
		Preferences prefs = _ps.getSystemPreferences();
		prefs.put(key, value);
		
		//write settings to persistent store
		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			throw new IllegalStateException("Could not save preferences", e);
		}
	}

}
