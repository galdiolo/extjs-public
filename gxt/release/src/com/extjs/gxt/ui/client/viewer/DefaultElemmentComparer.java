/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

/**
 * Default implementation of the ElementComparer interface
 * 
 * @param <E> the element type
 */
public class DefaultElemmentComparer<E> implements ElementComparer<E> {

  public static final DefaultElemmentComparer INSTANCE = new DefaultElemmentComparer();

  public boolean equals(E a, E b) {
    return (a == b || (a != null && a.equals(b)));
  }

}
