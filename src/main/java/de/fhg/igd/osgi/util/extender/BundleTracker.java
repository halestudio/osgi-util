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

package de.fhg.igd.osgi.util.extender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: BundleTracker</p>
 * <p>Description: Tracks active bundles</p>
 * @author Simon Templer
 */
public abstract class BundleTracker implements BundleListener {
	
	private static final Logger log = LoggerFactory.getLogger(BundleTracker.class);
	
	/**
	 * The tracking mode
	 */
	public enum TrackingMode {
		/** Track active bundles */
		Active,
		/** Track resolved bundles */
		Resolved
	}
	
	/**
	 * The bundle context (if tracking is started)
	 */
	private BundleContext context;
	
	/**
	 * The bundles that were searched for mapping information
	 */
	private final Set<Bundle> added = new HashSet<Bundle>();
	
	/**
	 * The tracking mode
	 */
	private final TrackingMode mode;
	
	/**
	 * Constructor
	 * 
	 * @param mode the tracking mode to use
	 */
	public BundleTracker(TrackingMode mode) {
		this.mode = mode;
	}
	
	/**
	 * Start the bundle tracker
	 * 
	 * @param context the bundle context
	 */
	public void start(final BundleContext context) {
		if (this.context != null) {
			stop();
		}
		
		this.context = context;
		
		log.info("Started tracking bundles (" + mode + ").");
		
		context.addBundleListener(this);
		
		for (Bundle bundle : context.getBundles()) {
			switch (mode) {
			case Active:
				if ((bundle.getState() & (Bundle.STARTING | Bundle.ACTIVE)) != 0) {
					addBundle(bundle);
				}
				break;
			case Resolved:
				if ((bundle.getState() & (Bundle.STARTING | Bundle.ACTIVE | Bundle.RESOLVED)) != 0) {
					addBundle(bundle);
				}
				break;
			}
		}
	}
	
	/**
	 * @see BundleListener#bundleChanged(BundleEvent)
	 */
	@Override
	public void bundleChanged(final BundleEvent event) {
		switch (mode) {
		case Active:
			switch (event.getType()) {
			case BundleEvent.STARTED:
				addBundle(event.getBundle());
				break;
			case BundleEvent.STOPPED:
				removeBundle(event.getBundle());
				break;
			}
			break;
		case Resolved:
			switch (event.getType()) {
			case BundleEvent.RESOLVED:
				addBundle(event.getBundle());
				break;
			case BundleEvent.UNRESOLVED:
				removeBundle(event.getBundle());
				break;
			}
			break;
		}
	}

	/**
	 * Remove a bundle and its mapping information
	 * 
	 * @param bundle the bundle to remove
	 */
	private void removeBundle(final Bundle bundle) {
		synchronized (added) {
			if (!added.contains(bundle))
				return;
			
			added.remove(bundle);
		}
		
		deregister(bundle);
	}

	/**
	 * Called after a bundle has been removed
	 * 
	 * @param bundle the removed bundle
	 */
	protected abstract void deregister(Bundle bundle);

	/**
	 * Add a bundle that may contain mapping information
	 * 
	 * @param bundle the bundle
	 */
	private void addBundle(final Bundle bundle) {
		synchronized (added) {
			if (added.contains(bundle))
				return;
			else
				added.add(bundle);
		}
		
		register(bundle);
	}

	/**
	 * Called after a bundle was found that was not yet registered
	 * 
	 * @param bundle the bundle
	 */
	protected abstract void register(Bundle bundle);

	/**
	 * Stop bundle tracking and reset the tracker
	 */
	public void stop() {
		if (context != null) {
			context.removeBundleListener(this);
		}
		
		List<Bundle> removed = new ArrayList<Bundle>();
		synchronized (added) {
			removed.addAll(added);
			added.clear();
		}
		
		// call deregister for remaining bundles
		for (Bundle bundle : removed) {
			deregister(bundle);
		}
		
		context = null;
		
		log.info("Stopped tracking bundles.");
	}

	/**
	 * Get the bundle context
	 * 
	 * @return the bundle context (may be null)
	 */
	public BundleContext getContext() {
		return context;
	}

}
