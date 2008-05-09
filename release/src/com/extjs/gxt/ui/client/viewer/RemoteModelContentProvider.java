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
 * A <code>RemoteContentProvider</code> for model instances.
 */
public abstract class RemoteModelContentProvider extends
    BaseLoader implements StructuredContentProvider {

  protected ModelContentProvider modelContentProvider = new ModelContentProvider();

  /**
   * ensureChild specifies if model changes should be tested to ensure the
   * updated model instance is a direct child of the viewer's input. Default
   * value is <code>true</code>.
   */
  public void setEnsureChild(boolean ensureChild) {
    modelContentProvider.setEnsureChild(ensureChild);
  }

  /**
   * ensureChild specifies if model changes should be tested to ensure the
   * updated model instance is a direct child of the viewer's input. Default
   * value is <code>true</code>.
   */
  public boolean isEnsureChild() {
    return modelContentProvider.isEnsureChild();
  }

  /**
   * monitorChanges specifies if model changes should be monitored. Default
   * value is <code>true</code>.
   */
  public void setMonitorChanges(boolean monitorChanges) {
    modelContentProvider.setMonitorChanges(monitorChanges);
  }

  /**
   * monitorChanges specifies if model changes should be monitored. Default
   * value is <code>true</code>.
   */
  public boolean isMonitorChanges() {
    return modelContentProvider.getMonitorChanges();
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    modelContentProvider.inputChanged(viewer, oldInput, newInput);
  }

  public void getElements(Object input, AsyncContentCallback callback) {
    modelContentProvider.getElements(input, callback);
  }

  @Override
  protected abstract void loadData(BaseLoadConfig config, DataCallback callback);

}
