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

/**
 * <p>Title: NamespaceConfigurationItem</p>
 * <p>Description: Configuration item where all keys will be extended
 * by a namespace. The default namespace is the package of the item class.</p>
 * @author Simon Templer
 */
public abstract class NamespaceConfigurationItem extends ConfigurationItem {
	
	private static final long serialVersionUID = 9024126421198212638L;
	
	/**
	 * Delimiter between namespace and key
	 */
	public static final String DELIMITER = "/";
	
	/**
	 * Get the configuration namespace
	 * 
	 * @return the configuration namespace
	 */
	protected String getNamespace() {
		return getClass().getPackage().getName().replace(".", DELIMITER);
	}

	/**
	 * @see ConfigurationItem#load(IConfigurationService)
	 */
	@Override
	protected final void load(IConfigurationService scs) {
		restore(new NamespaceConfigurationServiceDecorator(scs, getNamespace(), DELIMITER));
	}
	
	/**
	 * Fills the configuration item with value retrieved from
	 * the configuration service
	 * 
	 * @param scs the configuration service
	 */
	protected abstract void restore(IConfigurationService scs);

	/**
	 * @see ConfigurationItem#save(IConfigurationService)
	 */
	@Override
	protected final void save(IConfigurationService scs) {
		store(new NamespaceConfigurationServiceDecorator(scs, getNamespace(), DELIMITER));
	}
	
	/**
	 * Stores the configuration item in the configuration
	 * service
	 * 
	 * @param scs the configuration service
	 */
	protected abstract void store(IConfigurationService scs);

}
