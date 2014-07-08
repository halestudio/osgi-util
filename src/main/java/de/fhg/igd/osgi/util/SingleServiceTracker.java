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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.osgi.framework.ServiceReference;

/**
 * <p>Title: SingleServiceTracker</p>
 * <p>Description: </p>
 * @author Simon Templer
 * @param <T> the service type
 */
public class SingleServiceTracker<T> extends ServiceTracker<T> {
	
	private T service;
	private ServiceReference<T> serviceRef;
	
	private final Set<SingleServiceListener<T>> listeners = new HashSet<SingleServiceListener<T>>();
	
	private final Set<ServiceReference<T>> queuedServices = new LinkedHashSet<ServiceReference<T>>();

	/**
	 * Constructor
	 * 
	 * @param serviceClass the service type to track
	 */
	public SingleServiceTracker(Class<T> serviceClass) {
		super(serviceClass);
	}

	/**
	 * @see ServiceTracker#deregister(ServiceReference)
	 */
	@Override
	protected void deregister(ServiceReference<T> service) {
		if (serviceRef.equals(service)) {
			ServiceReference<T> newService = null;
			synchronized (queuedServices) {
				Iterator<ServiceReference<T>> it = queuedServices.iterator();
				if (it.hasNext())
					newService = it.next();
			}
			
			updateService(newService);
		}
		else {
			synchronized (queuedServices) {
				queuedServices.remove(service);
			}
		}
	}

	/**
	 * @see ServiceTracker#register(ServiceReference)
	 */
	@Override
	protected void register(ServiceReference<T> service) {
		if (this.service == null) {
			// set new service
			updateService(service);
		}
		else {
			// queue service for later
			synchronized (queuedServices) {
				queuedServices.add(service);
			}
		}
	}

	/**
	 * Update the service instance
	 * 
	 * @param newService the new service reference
	 */
	private void updateService(ServiceReference<T> newService) {
		if (service != null && serviceRef != null) {
			for (SingleServiceListener<T> listener : listeners) {
				listener.beforeServiceRemove(service);
			}
			
			// clean up old service
			getContext().ungetService(serviceRef);
		}
		
		service = null;
		serviceRef = null;
		
		if (newService != null) {
			serviceRef = newService;
			service = getContext().getService(newService);
		}
		
		for (SingleServiceListener<T> listener : listeners) {
			listener.afterServiceChange(service);
		}
	}
	
	/**
	 * Adds a listener
	 * 
	 * @param listener the listener
	 */
	public void addListener(SingleServiceListener<T> listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener
	 * 
	 * @param listener the listener
	 */
	public void removeListener(SingleServiceListener<T> listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Get the current service instance
	 * 
	 * @return the service instance (may be null)
	 */
	public T getService() {
		return service;
	}

}
