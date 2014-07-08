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

import de.fhg.igd.osgi.util.configuration.IConfigurationService;
import de.fhg.igd.osgi.util.configuration.JavaPreferencesConfigurationService;

/**
 * <p>Title: JavaPreferencesConfigurationTest</p>
 * @author Simon Templer
 */
public class JavaPreferencesConfigurationTest extends ConfigurationTestSupport {

	/**
	 * @see ConfigurationTestSupport#getConfigurationService()
	 */
	@Override
	protected IConfigurationService getConfigurationService() {
		return new JavaPreferencesConfigurationService(false, null, false);
	}

}
