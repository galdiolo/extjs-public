/*******************************************************************************
 * Copyright (c) 2007 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ext GWT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.List;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.data.TreeModel;

/**
 * A <code>ContentProvider</code> implementation for <code>Models</code>.
 */
@SuppressWarnings("unchecked")
public class ModelContentProvider implements StructuredContentProvider {

  private boolean monitorChanges = true;
  private boolean ensureChild = true;

  /**
   * Specifies if model changes should be monitored (defaults tor true).
   */
  public void setMonitorChanges(boolean monitorChanges) {
    this.monitorChanges = monitorChanges;
  }

  /**
   * Specifies if model changes should be monitored (defaults to true).
   */
  public boolean getMonitorChanges() {
    return monitorChanges;
  }

  /**
   * Specifies if model changes should be tested to ensure the updated model
   * instance is a direct child of the viewer's input. Default value is
   * <code>true</code>.
   */
  public void setEnsureChild(boolean ensureChild) {
    this.ensureChild = ensureChild;
  }

  /**
   * ensureChild specifies if model changes should be tested to ensure the
   * updated model instance is a direct child of the viewer's input. Default
   * value is <code>true</code>.
   */
  public boolean isEnsureChild() {
    return ensureChild;
  }

  protected Viewer viewer;

  public void getElements(Object input, AsyncContentCallback callback) {
    if (input instanceof TreeModel) {
      callback.setElements((List) ((TreeModel) input).getChildren());
      return;
    } else if (input instanceof List) {
      callback.setElements(((List) input));
      return;
    }
    throw new RuntimeException(
        "unsupported input, must be a TreeModel or a List<? extends ModelData>");
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.viewer = viewer;

    if (monitorChanges) {

      if (oldInput != null) {
        if (oldInput instanceof List) {
          for (Object o : (List) oldInput) {
            removeChangeListener(o);
          }
        } else {
          removeChangeListener(oldInput);
        }
      }

      if (newInput != null) {
        if (newInput instanceof List) {
          for (Object o : (List) newInput) {
            addChangeListener(o);
          }
        } else {
          addChangeListener(newInput);
        }
      }

    }
  }

  protected void addChangeListener(Object o) {
    if (o instanceof Model) {
      Model m = (Model) o;
      m.addChangeListener(changeListener);
    }
  }

  protected void removeChangeListener(Object o) {
    if (o instanceof Model) {
      Model m = (Model) o;
      m.removeChangeListener(changeListener);
    }
  }

  protected ChangeListener changeListener = new ChangeListener() {
    public void modelChanged(ChangeEvent event) {

      if (!monitorChanges) return;

      int type = event.type;
      switch (type) {
        case ChangeEventSource.Add: {
          if (!ensureChild || (ensureChild && event.source == viewer.getInput())) {
            viewer.insert(event.item, event.index);
          }
          break;
        }
        case ChangeEventSource.Remove: {
          if (!ensureChild || (ensureChild && event.source == viewer.getInput())) {
            viewer.remove(event.item);
          }
          break;
        }
        case ChangeEventSource.Update: {
          viewer.update(event.item);
          break;
        }
      }
    }
  };

}
