/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeModel;

/**
 * A <code>TreeContentProvider</code> implementation for <code>Model</code>
 * instances.
 * 
 * @see TreeModel
 */
public class ModelTreeContentProvider implements TreeContentProvider {

  /**
   * Specifies if model changes should be monitored. Default value is
   * <code>true</code>.
   */
  public boolean monitorChanges = true;

  protected BaseTreeViewer viewer;

  public void getChildren(final Object parent, final AsyncContentCallback callback) {
    if (!(parent instanceof TreeModel)) throw new RuntimeException("parent isn't a TreeModel");
    callback.setElements(((TreeModel)parent).getChildren());
  }

  public Object getParent(Object element) {
    if (!(element instanceof TreeModel)) throw new RuntimeException("element isn't a TreeModel");
    return ((TreeModel)element).getParent();
  }

  public boolean hasChildren(Object element) {
    if (!(element instanceof TreeModel)) throw new RuntimeException("element isn't a TreeModel");
    return ((TreeModel)element).getChildCount() > 0;
  }

  public void getElements(Object input, AsyncContentCallback callback) {
    if (!(input instanceof TreeModel)) throw new RuntimeException("element isn't a TreeModel");
    callback.setElements(((TreeModel)input).getChildren());
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    if (!(viewer instanceof BaseTreeViewer)) throw new RuntimeException("viewer isn't a TreeViewer");
    this.viewer = (BaseTreeViewer) viewer;
    if (monitorChanges) {
      if (oldInput != null) {
        if (oldInput instanceof Model) {
          Model m = (Model) oldInput;
          m.removeChangeListener(changeListener);
        }
      }
      if (newInput instanceof Model) {
        Model m = (Model) newInput;
        m.addChangeListener(changeListener);
      }
    }
  }

  protected ChangeListener changeListener = new ChangeListener() {
    public void modelChanged(ChangeEvent event) {
      if (event.item != null && (!(event.item instanceof TreeModel))) {
        return;
      }
  
      int type = event.type;
      switch (type) {
        case ChangeEventSource.Add: {
          ModelData parent = (ModelData) event.source;
          ModelData add = (ModelData) event.item;
          viewer.insert(parent, add, event.index);
          break;
        }
        case ChangeEventSource.Remove: {
          viewer.remove((ModelData) event.item);
          break;
        }
        case ChangeEventSource.Update: {
          viewer.update((ModelData) event.source);
          break;
        }
  
      }
    }
  };
  
}
