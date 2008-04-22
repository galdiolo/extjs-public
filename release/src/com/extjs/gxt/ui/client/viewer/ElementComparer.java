/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ext GWT - derived implementation
 *******************************************************************************/

package com.extjs.gxt.ui.client.viewer;

/**
 * This interface is used to compare elements in a viewer for equality. This
 * allows the client of the viewer to specify different equality criteria
 * implementation than the equals implementations of the elements themselves.
 */
public interface ElementComparer<E> {

  /**
   * Compares two elements for equality.
   * 
   * @param a the first element
   * @param b the second element
   * @return whether a is equal to b
   */
  public boolean equals(E a, E b);

}
