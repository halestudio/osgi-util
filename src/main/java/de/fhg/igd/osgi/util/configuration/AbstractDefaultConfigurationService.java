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

/**
 * <p>Title: AbstractDefaultConfigurationService</p>
 * <p>Description: Configuration service that can be initialized
 * with a set of default {@link Properties}</p>
 * @author Simon Templer
 */
public abstract class AbstractDefaultConfigurationService extends AbstractConfigurationService {
	
	/**
	 * If the service may fall back to the system properties for retrieving default values
	 */
	private final boolean fallBackToSystemProperties;
	
	/**
	 * The default properties
	 */
	private final Properties defaultProperties;
	
	/**
	 * Create the configuration service. The service will not use
	 * the system properties to determine default values
	 * 
	 * @param defaultProperties the default properties
	 */
	public AbstractDefaultConfigurationService(final Properties defaultProperties) {
		this(defaultProperties, false);
	}
	
	/**
	 * Create the configuration service
	 * 
	 * @param defaultProperties the default properties, may be <code>null</code>
	 * @param fallBackToSystemProperties if the service may fall back to the system
	 *   properties for retrieving default values
	 */
	public AbstractDefaultConfigurationService(final Properties defaultProperties, 
			final boolean fallBackToSystemProperties) {
		super();
		
		if (defaultProperties == null) {
			this.defaultProperties = new Properties();
		}
		else {
			this.defaultProperties = new Properties();
			this.defaultProperties.putAll(defaultProperties);
		}
		this.fallBackToSystemProperties = fallBackToSystemProperties;
	}

	/**
	 * Get the default value using the default properties
	 * 
	 * @param key the property key
	 */
	@Override
	protected String getDefault(String key) {
		if (fallBackToSystemProperties && !defaultProperties.containsKey(key)) {
			// return system property
			return System.getProperty(key);
		}
		else {
			// return default property
			return defaultProperties.getProperty(key);
		}
	}
	
	/**
	 * Set a default value
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public void setDefault(String key, String value) {
		defaultProperties.setProperty(key, value);
	}

}
