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

import org.junit.Assert;
import org.junit.Test;

import de.fhg.igd.osgi.util.configuration.IConfigurationService;

/**
 * <p>Title: JavaPreferencesConfigurationTest</p>
 * @author Simon Templer
 */
public abstract class ConfigurationTestSupport {
	
	/**
	 * Simple test for {@link IConfigurationService#set(String, String)}
	 * and {@link IConfigurationService#get(String)}
	 */
	@Test
	public void simpleTest() {
		IConfigurationService cs = getConfigurationService();
		
		String key = "test/x.y.z";
		String value = "testval";
		cs.set(key, value);
	
		Assert.assertEquals(value, cs.get(key));
		
		cs.set(key, null);
		
		Assert.assertEquals(null, cs.get(key));
	}
	
	/**
	 * Test setting and getting a configuration item
	 */
	@Test
	public void itemTest() {
		IConfigurationService cs = getConfigurationService();
		
		String value = "xyz";
		
		TestItem item1 = new TestItem();
		item1.setValue(value);
		
		cs.set(item1);
		
		TestItem item2 = cs.get(TestItem.class);
		
		Assert.assertEquals(item1, item2);
		
		// remove key
		item1 = new TestItem();
		item1.setValue(null);
		
		cs.set(item1);
		
		item2 = cs.get(TestItem.class);
		
		Assert.assertEquals(item1, item2);
	}

	/**
	 * @return the configuration service to use for testing
	 */
	protected abstract IConfigurationService getConfigurationService();

}
