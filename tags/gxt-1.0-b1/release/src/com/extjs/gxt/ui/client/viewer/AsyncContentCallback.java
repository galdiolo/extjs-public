/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.List;

/**
 * Asynchronous <code>ContentProvider</code> data callback.
 */
public interface AsyncContentCallback<E> {

  /**
   * Returns the elements to the source viewer. <code>null</code> if the data
   * is unable to be retrieved.
   * 
   * @param elements the elements
   */
  public void setElements(List<E> elements);

}
