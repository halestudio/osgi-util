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

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Manifest;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ManifestTracker</p>
 * @author Simon Templer
 */
public abstract class ManifestTracker extends BundleTracker {
	
	/**
	 * Constructor
	 */
	public ManifestTracker() {
		super(TrackingMode.Resolved);
	}

	private static final Logger log = LoggerFactory.getLogger(ManifestTracker.class);

	/**
	 * @see BundleTracker#register(Bundle)
	 */
	@Override
	protected void register(Bundle bundle) {
		Enumeration<URL> manifestFiles = bundle.findEntries("META-INF", "MANIFEST.MF", false);
		List<Manifest> manifests = new ArrayList<Manifest>();
		while (manifestFiles.hasMoreElements()) {
			URL manifestFile = manifestFiles.nextElement();
			
			try {
				Manifest manifest = new Manifest(manifestFile.openStream());
				manifests.add(manifest);
			} catch(Exception e) {
				log.error("Error opening manifest file", e);
			}
		}
		
		register(bundle, manifests);
	}

	/**
	 * Called after a bundle was found that was not yet registered
	 * 
	 * @param bundle the bundle
	 * @param manifests the manifest files found
	 */
	protected abstract void register(Bundle bundle, List<Manifest> manifests);

}
