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

/*+-------------+----------------------------------------------------------*
 *|__|__|_|_|_|_|     (Fraunhofer Institute for Computer Graphics)         *
 *|  |  |_|_|_|_|                                                          *
 *|__|__|_|_|_|_|                                                          *
 *|  __ |    ___|                                                          *
 *| /_  /_  / _ |     Fraunhoferstrasse 5                                  *
 *|/   / / /__/ |     D-64283 Darmstadt, Germany                           *
 *+-------------+----------------------------------------------------------*/
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ServiceTracker</p>
 * <p>Description: Tracks active services of a certain type</p>
 * @author Simon Templer
 * @param <T> the type of services to track
 */
public abstract class ServiceTracker<T> implements ServiceListener {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceTracker.class);
	
	/**
	 * The bundle context (if tracking is started)
	 */
	private BundleContext context;
	
	/**
	 * The service class
	 */
	private final Class<T> serviceClass;
	
	/**
	 * If the tracker is running
	 */
	private boolean running;
	
	/**
	 * The bundles that were searched for mapping information
	 */
	private final Set<ServiceReference<T>> added = new HashSet<ServiceReference<T>>();
	
	/**
	 * Creates a service tracker for the given service class
	 * 
	 * @param serviceClass the service class
	 */
	public ServiceTracker(final Class<T> serviceClass) {
		this.serviceClass = serviceClass;
	}
	
	/**
	 * Start the service tracker
	 * 
	 * @param context the bundle context
	 */
	public void start(final BundleContext context) {
		if (this.context != null) {
			stop();
		}
		
		this.context = context;
		
		log.info("Started tracking services: " + serviceClass.getName());
		
		try {
			context.addServiceListener(this, "(" + Constants.OBJECTCLASS + "=" + serviceClass.getName() + ")");
		} catch (InvalidSyntaxException e) {
			log.error("Error adding service listener.", e);
		}
		
		Collection<ServiceReference<T>> services;
		try {
			services = context.getServiceReferences(serviceClass, null);
		} catch (InvalidSyntaxException e) {
			services = null;
			log.error("Error getting service references.", e);
		}
		
		if (services != null) {
			for (ServiceReference<T> service : services) {
				addService(service);
			}
		}
		
		running = true;
	}

	/**
	 * @see ServiceListener#serviceChanged(ServiceEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void serviceChanged(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			addService((ServiceReference<T>)event.getServiceReference());
			break;
		case ServiceEvent.UNREGISTERING:
			removeService((ServiceReference<T>)event.getServiceReference());
		}
	}

	/**
	 * Remove a service
	 * 
	 * @param service the service reference
	 */
	private void removeService(final ServiceReference<T> service) {
		synchronized (added) {
			if (!added.contains(service))
				return;
			
			added.remove(service);
		}
		
		deregister(service);
	}

	/**
	 * Called after a service has been removed
	 * 
	 * @param service the reference of the removed service
	 */
	protected abstract void deregister(ServiceReference<T> service);

	/**
	 * Add a service
	 * 
	 * @param service the service reference
	 */
	private void addService(final ServiceReference<T> service) {
		synchronized (added) {
			if (added.contains(service))
				return;
			else
				added.add(service);
		}
		
		register(service);
	}

	/**
	 * Called after a service was found that was not yet registered
	 * 
	 * @param service the service reference
	 */
	protected abstract void register(ServiceReference<T> service);

	/**
	 * Stop bundle tracking and reset the tracker
	 */
	public void stop() {
		if (context != null) {
			context.removeServiceListener(this);
		}
		
		List<ServiceReference<T>> removed = new ArrayList<ServiceReference<T>>();
		synchronized (added) {
			removed.addAll(added);
			added.clear();
		}
		
		// call deregister for remaining services
		for (ServiceReference<T> service : removed) {
			deregister(service);
		}
		
		context = null;
		
		running = false;
		
		log.info("Stopped tracking services: " + serviceClass.getName());
	}

	/**
	 * Get the bundle context
	 * 
	 * @return the bundle context (may be null)
	 */
	public BundleContext getContext() {
		return context;
	}
	
	/**
	 * Get the properties of a service
	 * 
	 * @param service the service reference
	 * 
	 * @return a new properties instance containing the service properties
	 */
	public static Properties getProperties(ServiceReference<?> service) {
		Properties properties = new Properties();
		
		for (String key : service.getPropertyKeys()) {
			properties.setProperty(key, service.getProperty(key).toString());
		}
		
		return properties;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

}
