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

import java.util.List;

/**
 * <p>Title: NamespaceConfigurationServiceDecorator</p>
 * <p>Description: Decorator for an {@link IConfigurationService}
 * that extends all keys by a namespace. If there is no value for
 * the extended key, the normal key will used to try to get a default
 * value</p>
 * @author Simon Templer
 */
public class NamespaceConfigurationServiceDecorator extends
		AbstractItemConfigurationService {
	
	/**
	 * The decorated configuration service
	 */
	private IConfigurationService configurationService;
	
	/**
	 * The namespace
	 */
	private String namespace;
	
	/**
	 * The delimiter between namespace and key
	 */
	private final String delimiter;
	
	/**
	 * Constructor
	 * 
	 * @param configurationService the inner configuration service
	 * @param namespace the namespace
	 * @param delimiter the delimiter
	 */
	public NamespaceConfigurationServiceDecorator(
			IConfigurationService configurationService, String namespace,
			String delimiter) {
		super();
		this.configurationService = configurationService;
		this.namespace = namespace;
		this.delimiter = delimiter;
	}

	/**
	 * Extend the key with the namespace
	 * 
	 * @param key the key
	 * 
	 * @return the extended key
	 */
	protected String extendKey(String key) {
		return namespace + delimiter + key;
	}

	/**
	 * @see IConfigurationService#get(String)
	 */
	@Override
	public String get(String key) {
		String result = configurationService.get(extendKey(key));
		if (result == null) {
			// fall back to normal key
			return configurationService.get(key);
		}
		else {
			return result;
		}
	}

	/**
	 * @see IConfigurationService#getInt(String)
	 */
	@Override
	public Integer getInt(String key) throws NumberFormatException {
		Integer result = configurationService.getInt(extendKey(key));
		if (result == null) {
			// fall back to normal key
			return configurationService.getInt(key);
		}
		else {
			return result;
		}
	}

	/**
	 * @see IConfigurationService#set(String, String)
	 */
	@Override
	public void set(String key, String value) {
		configurationService.set(extendKey(key), value);
	}

	/**
	 * @see IConfigurationService#setInt(String, int)
	 */
	@Override
	public void setInt(String key, int value) {
		configurationService.setInt(extendKey(key), value);
	}

	/**
	 * @see IConfigurationService#get(String, String)
	 */
	@Override
	public String get(String key, String defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}
		else {
			return value;
		}
	}

	/**
	 * @see IConfigurationService#getInt(String, Integer)
	 */
	@Override
	public Integer getInt(String key, Integer defaultValue)
			throws NumberFormatException {
		Integer value = getInt(key);
		if (value == null) {
			return defaultValue;
		}
		else {
			return value;
		}
	}

	/**
	 * @see IConfigurationService#getBoolean(String)
	 */
	@Override
	public Boolean getBoolean(String key) {
		Boolean result = configurationService.getBoolean(extendKey(key));
		if (result == null) {
			// fall back to normal key
			return configurationService.getBoolean(key);
		}
		else {
			return result;
		}
	}

	/**
	 * @see IConfigurationService#getBoolean(String, boolean)
	 */
	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		Boolean value = getBoolean(key);
		if (value == null) {
			return defaultValue;
		}
		else {
			return value;
		}
	}

	/**
	 * @see IConfigurationService#setBoolean(String, boolean)
	 */
	@Override
	public void setBoolean(String key, boolean value) {
		configurationService.setBoolean(extendKey(key), value);
	}

	/**
	 * @see IConfigurationService#getList(String, List)
	 */
	@Override
	public List<String> getList(String key, List<String> defaultValue) {
		return configurationService.getList(extendKey(key), defaultValue);
	}

	/**
	 * @see IConfigurationService#getList(String)
	 */
	@Override
	public List<String> getList(String key) {
		return configurationService.getList(extendKey(key));
	}

	/**
	 * @see IConfigurationService#setList(String, List)
	 */
	@Override
	public void setList(String key, List<String> value) {
		configurationService.setList(extendKey(key), value);
	}

}
