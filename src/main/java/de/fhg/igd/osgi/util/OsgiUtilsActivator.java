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

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * <p>Title: OsgiUtilsActivator</p>
 * <p>Description: The activator for the Osgi Utils bundle</p>
 * @author Simon Templer
 * @author Michel Kraemer
 */
public class OsgiUtilsActivator extends AbstractBundleActivator {
	
	/**
	 * The singleton instance of this activator
	 */
	private static OsgiUtilsActivator instance;
	
	private final Map<Class<?>, SingleServiceTracker<?>> trackers
		= new HashMap<Class<?>, SingleServiceTracker<?>>();
	
	private final Map<Class<?>, MultiServiceTracker<?>> multiTrackers
		= new HashMap<Class<?>, MultiServiceTracker<?>>();
	
	private final Map<Object, ServiceRegistration<?>> registrations
		= new IdentityHashMap<Object, ServiceRegistration<?>>();
	
	/**
	 * @see AbstractBundleActivator#start(BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		instance = this;
	}

	/**
	 * @see BundleActivator#stop(BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		
		// stop and remove all trackers
		synchronized (trackers) {
			for (SingleServiceTracker<?> tracker : trackers.values()) {
				tracker.stop();
			}
			trackers.clear();
		}
		
		// stop and remove all multi service trackers
		synchronized (trackers) {
			for (MultiServiceTracker<?> tracker : multiTrackers.values()) {
				tracker.stop();
			}
			multiTrackers.clear();
		}
	}
	
	/**
	 * @return the singleton instance of this activator
	 */
	public static OsgiUtilsActivator getInstance() {
		return instance;
	}
	
	/**
	 * Get the service with the given type. Only use the returned instance
	 * while you are sure it is still valid.
	 * 
	 * @param <T> the service type
	 * @param serviceType the service type
	 * @return the available service of this type or null
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> serviceType) {
		OsgiUtilsActivator instance = OsgiUtilsActivator.getInstance();
		if (instance == null) {
			return null;
		}
		
		BundleContext context = instance.getContext();
		
		SingleServiceTracker<T> tracker;
		
		synchronized (trackers) {
			tracker = (SingleServiceTracker<T>) trackers.get(serviceType);
			if (tracker == null) {
				tracker = new SingleServiceTracker<T>(serviceType);
				trackers.put(serviceType, tracker);
				tracker.start(context);
			}
		}
		
		return tracker.getService();
	}
	
	/**
	 * Add a service listener
	 * 
	 * @param <T> the service type
	 * @param listener the listener
	 * @param serviceType the service type
	 */
	@SuppressWarnings("unchecked")
	public <T> void addServiceListener(SingleServiceListener<T> listener, Class<T> serviceType) {
		OsgiUtilsActivator instance = OsgiUtilsActivator.getInstance();
		
		BundleContext context = instance.getContext();
		
		SingleServiceTracker<T> tracker;
		
		synchronized (trackers) {
			tracker = (SingleServiceTracker<T>) trackers.get(serviceType);
			if (tracker == null) {
				tracker = new SingleServiceTracker<T>(serviceType);
				trackers.put(serviceType, tracker);
				tracker.start(context);
			}
		}
		
		tracker.addListener(listener);
	}
	
	/**
	 * Remove a service listener 
	 * 
	 * @param <T> the service type
	 * @param listener the listener
	 * @param serviceType the service type
	 */
	@SuppressWarnings("unchecked")
	public <T> void removeServiceListener(SingleServiceListener<T> listener, Class<T> serviceType) {
		SingleServiceTracker<T> tracker;
		
		synchronized (trackers) {
			tracker = (SingleServiceTracker<T>) trackers.get(serviceType);
			if (tracker == null) {
				return;
			}
		}
		
		tracker.removeListener(listener);
	}
	
	/**
	 * Get the services with the given type. Only use the returned instances
	 * while you are sure it is still valid.
	 * 
	 * @param <T> the service type
	 * @param serviceType the service type
	 * @return the available services of this type or null
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getServices(Class<T> serviceType) {
		OsgiUtilsActivator instance = OsgiUtilsActivator.getInstance();
		if (instance == null) {
			return null;
		}
		
		BundleContext context = instance.getContext();
		
		MultiServiceTracker<T> tracker;
		
		synchronized (multiTrackers) {
			tracker = (MultiServiceTracker<T>) multiTrackers.get(serviceType);
			if (tracker == null) {
				tracker = new MultiServiceTracker<T>(serviceType);
				multiTrackers.put(serviceType, tracker);
				tracker.start(context);
			}
		}
		
		return tracker.getServices();
	}
	
	/**
	 * Register a service
	 * 
	 * @param <T> the service type
	 * @param serviceType the service type
	 * @param service the service implementation
	 */
	public <T> void registerService(Class<T> serviceType, T service) {
		ServiceRegistration<T> serviceReg = instance.getContext().registerService(
				serviceType, service, new Hashtable<String, Object>());
		
		synchronized (registrations) {
			registrations.put(service, serviceReg);
		}
	}
	
	/**
	 * Unregister a previously registered service
	 * 
	 * @param service the service implementation
	 */
	public void unregisterService(Object service) {
		ServiceRegistration<?> serviceReg;
		
		synchronized (registrations) {
			serviceReg = registrations.remove(service);
		}
		
		if (serviceReg != null) {
			serviceReg.unregister();
		}
	}
	
}
