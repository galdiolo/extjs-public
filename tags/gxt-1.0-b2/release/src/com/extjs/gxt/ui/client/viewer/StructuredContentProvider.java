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
 * An interface to content providers for structured viewers.
 */
public interface StructuredContentProvider<I, E> extends ContentProvider<I> {

  /**
   * Returns the elements to display in the viewer when its input is set to the
   * given element.
   * 
   * @param input the input element
   * @param callback the content callback
   */
  public void getElements(I input, AsyncContentCallback<E> callback);
}
