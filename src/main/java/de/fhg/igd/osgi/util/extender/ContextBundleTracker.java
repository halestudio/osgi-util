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

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;

/**
 * This class tracks bundles and manages tracking-related context information.
 * The tracking context is not related to the OSGi BundleContext.
 * @author Simon Thum
 */
public abstract class ContextBundleTracker extends BundleTracker {

	/**
	 * @param mode the tracking mode to use
	 */
	public ContextBundleTracker(TrackingMode mode) {
		super(mode);
	}

	/**
	 * map bundle ids to contexts
	 */
	private Map<Long, Object> _trackingContext = new HashMap<Long, Object>();

	@Override
	protected void register(Bundle bundle) {
		Object context = registerBundleContextual(bundle);
		if (context == null)
			return; // don't really track bundle
		synchronized (_trackingContext) {
			if (_trackingContext.put(bundle.getBundleId(), context) != null)
				throw new IllegalStateException("a registration context was already present");
		}
	}

	@Override
	protected void deregister(Bundle bundle) {
		Object context;
		synchronized (_trackingContext) {
			context = _trackingContext.get(bundle.getBundleId());
			_trackingContext.remove(bundle.getBundleId());
		}
		if (context != null)
			unregisterBundleContextual(bundle, context);
	}

	/**
	 * @param bundle the bundle to register
	 * @return an arbitrary non-null context object,
	 * 		   or null not to call {@link #unregisterBundleContextual(Bundle, Object)}
	 */
	protected abstract Object registerBundleContextual(Bundle bundle);

	/**
	 * @param bundle the bundle to unregister
	 * @param context the context object returned from registration, never null
	 */
	protected abstract void unregisterBundleContextual(Bundle bundle, Object context);

}
