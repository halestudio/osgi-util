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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;

/**
 * <p>Title: ChainedClassLoader</p>
 * <p>Description: Chaining classloader implementation that delegates the
 * resource and class loading to a number of class loaders passed in.</p>
 * @author Simon Thum
 * @author Michel Kraemer
 */
public class ChainedClassLoader extends ClassLoader {
	/**
	 * The actual class loaders
	 */
	private final ClassLoader[] loaders;
	
	/**
	 * Constructs a new chained class loader that uses the given class loaders 
	 * @param loaders an array of class loaders to use
	 */
	public ChainedClassLoader(ClassLoader... loaders) {
		assert loaders != null;
		
		LinkedHashSet<ClassLoader> clset = new LinkedHashSet<ClassLoader>();
		for (ClassLoader cl : loaders) {
			if (cl != null && !clset.contains(cl)) {
				clset.add(cl);
			}
		}
		
		this.loaders = clset.toArray(new ClassLoader[clset.size()]);
	}

	/**
	 * @see ClassLoader#findResource(String)
	 */
	@Override
	protected URL findResource(String name) {
		URL url = null;
		for (ClassLoader loader : loaders) {
			url = loader.getResource(name);
			if (url != null) {
				return url;
			}
		}
		return url;
	}
	
	/**
	 * @see ClassLoader#findResources(String)
	 */
	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		Enumeration<URL> urls = null;
		for (ClassLoader loader : loaders) {
			urls = loader.getResources(name);
			if (urls != null) {
				return urls;
			}
		}
		return urls;
	}

	/**
	 * @see ClassLoader#findClass(String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		for (ClassLoader loader : loaders) {
			try {
				return loader.loadClass(name);
			} catch (ClassNotFoundException e) {
				//ignore
			}
		}
		
		throw new ClassNotFoundException(name);
	}
}
