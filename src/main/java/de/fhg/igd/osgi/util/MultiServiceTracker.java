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

package de.fhg.igd.osgi.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.ServiceReference;

/**
 * <p>Title: MultiServiceTracker</p>
 * <p>Description: Tracks service instances</p>
 * @author Simon Templer
 * @param <T> the service type
 */
public class MultiServiceTracker<T> extends ServiceTracker<T> {
	
	private final Map<ServiceReference<T>, T> services = new HashMap<ServiceReference<T>, T>();
	
	private final Set<MultiServiceListener<T>> listeners = new HashSet<MultiServiceListener<T>>();

	/**
	 * Constructor
	 * 
	 * @param serviceClass the service type to track
	 */
	public MultiServiceTracker(Class<T> serviceClass) {
		super(serviceClass);
	}

	/**
	 * @see ServiceTracker#deregister(ServiceReference)
	 */
	@Override
	protected void deregister(ServiceReference<T> service) {
		T serviceInstance; 
		
		synchronized (services) {
			serviceInstance = services.get(service);
			if (serviceInstance == null) {
				return;
			}
			else {
				services.remove(service);
			}
		}
		
		for (MultiServiceListener<T> listener : listeners) {
			listener.serviceRemoved(serviceInstance);
		}
	}

	/**
	 * @see ServiceTracker#register(ServiceReference)
	 */
	@Override
	protected void register(ServiceReference<T> service) {
		T serviceInstance = getContext().getService(service);
		
		if (serviceInstance != null) {
			synchronized (services) {
				services.put(service, serviceInstance);
			}
			
			for (MultiServiceListener<T> listener : listeners) {
				listener.serviceAdded(serviceInstance);
			}
		}

	}
	
	/**
	 * Get the currently available service instances
	 * 
	 * @return the set of currently available service instances
	 */
	public Set<T> getServices() {
		Set<T> result;
		
		synchronized (services) {
			result = new HashSet<T>(services.values());
		}
		
		return result;
	}
	
	/**
	 * Adds a {@link MultiServiceListener}
	 * 
	 * @param listener the listener to add
	 */
	public void addListener(MultiServiceListener<T> listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a {@link MultiServiceListener}
	 * 
	 * @param listener the listener to remove
	 */
	public void removeListener(MultiServiceListener<T> listener) {
		listeners.remove(listener);
	}

}
