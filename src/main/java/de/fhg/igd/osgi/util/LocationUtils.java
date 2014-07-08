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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.osgi.service.datalocation.Location;

/**
 * Utilities for OSGi 
 * @author Michel Kraemer
 */
public class LocationUtils {
	/**
	 * Converts the given location to a file
	 * @param loc the location
	 * @return the file object
	 */
	public static File toFile(Location loc) {
		return toFile(loc, "");
	}
	
	/**
	 * Converts the given location to a file and appends the given suffix. For
	 * example, this method can be used to get a file from a directory, where
	 * the directory is denoted by the given parent location and the file's
	 * name is denoted by the suffix.
	 * @param parentLoc the location
	 * @param suffix the suffix to append
	 * @return the file object
	 */
	public static File toFile(Location parentLoc, String suffix) {
		URL u;
		try {
			u = new URL(parentLoc.getURL(), suffix);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		File r;
		try {
			r = new File(u.toURI());
		} catch (URISyntaxException e) {
			r = new File(u.getPath());
		}
		return r;
	}
}
