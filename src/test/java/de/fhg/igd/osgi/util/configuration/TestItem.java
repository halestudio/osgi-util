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
import de.fhg.igd.osgi.util.configuration.NamespaceConfigurationItem;

/**
 * <p>Title: TestItem</p>
 * @author Simon Templer
 */
public class TestItem extends NamespaceConfigurationItem {
	
	private static final long serialVersionUID = 9144469944659344218L;
	
	private String value;
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @see NamespaceConfigurationItem#restore(IConfigurationService)
	 */
	@Override
	protected void restore(IConfigurationService scs) {
		value = scs.get("testkey");
	}

	/**
	 * @see NamespaceConfigurationItem#store(IConfigurationService)
	 */
	@Override
	protected void store(IConfigurationService scs) {
		scs.set("testkey", value);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestItem other = (TestItem) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
