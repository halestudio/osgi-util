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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: AbstractConfigurationService</p>
 * <p>Description: Implements all basic configuration service
 * methods. Default value for all keys is <code>null</code>.
 * @author Michel Kraemer, Simon Templer
 */
public abstract class AbstractConfigurationService extends AbstractItemConfigurationService {
	
	/**
	 * Get the default value for a key. Override to provide
	 * a different default value than <code>null</code>.<br>
	 * 
	 * @param key the key
	 * 
	 * @return <code>null</code>
	 */
	protected String getDefault(String key) {
		return null;
	}
	
	/**
	 * Get the configuration value for the given key
	 * 
	 * @param key the key
	 * 
	 * @return the value or <code>null</code> if it is
	 *   not set
	 */
	protected abstract String getValue(String key);
	
	/**
	 * @see IConfigurationService#get(String)
	 */
	@Override
	public String get(String key) {
		String value = getValue(key);
		
		if (value == null) {
			return getDefault(key);
		}
		else {
			return value;
		}
	}
	
	/**
	 * @see IConfigurationService#getInt(String)
	 */
	@Override
	public Integer getInt(String key) throws NumberFormatException {
		String r = get(key);
		if (r == null) {
			return null;
		}
		return Integer.parseInt(r);
	}
	
	/**
	 * Remove the value for the given key
	 * 
	 * @param key the key
	 */
	protected abstract void removeValue(String key);
	
	/**
	 * Set the value for the given key
	 * 
	 * @param key the key
	 * @param value the value
	 */
	protected abstract void setValue(String key, String value);

	/**
	 * @see IConfigurationService#set(String, String)
	 */
	@Override
	public void set(String key, String value) {
		if (value == null) {
			removeValue(key);
		} else {
			setValue(key, value);
		}
	}
	
	/**
	 * @see IConfigurationService#setInt(String, int)
	 */
	@Override
	public void setInt(String key, int value) {
		set(key, String.valueOf(value));
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
		String r = get(key);
		if (r == null) {
			return null;
		}
		return Boolean.parseBoolean(r);
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
		return value;
	}

	/**
	 * @see IConfigurationService#setBoolean(String, boolean)
	 */
	@Override
	public void setBoolean(String key, boolean value) {
		set(key, String.valueOf(value));
	}

	/**
	 * @see IConfigurationService#getList(String, List)
	 */
	@Override
	public List<String> getList(String key, List<String> defaultValue) {
		List<String> result = getList(key);
		if (result == null) {
			return defaultValue;
		}
		else {
			return result;
		}
	}

	/**
	 * @see IConfigurationService#getList(String)
	 */
	@Override
	public List<String> getList(String key) {
		Integer count = getInt(key + "/count");
		if (count == null) {
			return null;
		}
		else {
			List<String> result = new ArrayList<String>(count);
			for (int i = 1; i <= count; i++) {
				String v = get(key + "/" + i);
				if (v != null) {
					result.add(v);
				}
			}
			return result;
		}
	}

	/**
	 * @see IConfigurationService#setList(String, List)
	 */
	@Override
	public void setList(String key, List<String> value) {
		Integer count = getInt(key + "/count");
		
		if (count != null && (value == null || value.size() < count)) {
			int start = (value == null)?(1):(value.size() + 1);
			
			// remove values
			set(key + "/count", null);
			for (int i = start; i <= count; i++) {
				set(key + "/" + i, null);
			}
		}
		
		if (value != null) {
			setInt(key + "/count", value.size());
			int i = 0;
			for (String v : value) {
				set(key + "/" + (++i), v);
			}
		}
	}

}
