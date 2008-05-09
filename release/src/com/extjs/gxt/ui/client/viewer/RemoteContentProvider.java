/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import com.extjs.gxt.ui.client.data.BaseLoadConfig;
import com.extjs.gxt.ui.client.data.BaseLoader;
import com.extjs.gxt.ui.client.data.DataCallback;

/**
 * A <code>IStructuredContentProvider</code> implementation that supports the
 * remote loading of data and paging.
 */
public abstract class RemoteContentProvider<I, E, C extends BaseLoadConfig> extends
    BaseLoader<C> implements StructuredContentProvider<I, E> {

  protected Viewer viewer;

  public void inputChanged(Viewer viewer, I oldInput, I newInput) {
    this.viewer = viewer;
  }

  /**
   * Returns the elements to display in the viewer when its input is set to the
   * given element.
   * 
   * implementors must take input and come up with a call to
   * callback.setElements
   * 
   * @param input the input element
   * @param callback the content callback
   */
  public abstract void getElements(I input, AsyncContentCallback<E> callback);

  /**
   * Subclasses must implement and return the loaded data to the
   * callback.setResult
   */
  protected abstract void loadData(C config, DataCallback callback);

}
