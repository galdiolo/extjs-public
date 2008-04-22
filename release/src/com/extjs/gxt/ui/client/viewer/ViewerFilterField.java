/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.tree.Tree;

/**
 * A specialized text field that filters a viewer's input.
 * 
 * @param <P> the parent type
 * @param <E> the elmenet type
 */
public abstract class ViewerFilterField<P, E> extends TriggerField {

  /**
   * True to animate tree expand / collapse when no filters are applied (defaults to true).
   */
  public boolean animateTree = true;
  
  protected ViewerFilter filter;
  protected List<Viewer> viewers = new ArrayList<Viewer>();

  public ViewerFilterField() {
    triggerStyle = "x-form-clear-trigger";
    filter = new ViewerFilter<P, E>() {
      @Override
      public boolean select(Viewer viewer, P parentElement, E element) {
        if (!rendered) {
          return true;
        }
        String value = getValue().toString();
        if (value.length() == 0) {
          return true;
        }
        return doSelect(parentElement, element, getValue().toString());
      }
    };
  }

  @Override
  protected void onTriggerClick(ComponentEvent ce) {
    super.onTriggerClick(ce);
    setValue("");
    onFilter();
  }

  /**
   * Binds the viewer to the field.
   * 
   * @param viewer the viewer to add
   */
  public void bind(Viewer viewer) {
    viewer.addFilter(filter);
    viewers.add(viewer);
  }

  /**
   * Unbinds the viewer from the field.
   * 
   * @param viewer the viewer to be removed
   */
  public void unbind(Viewer viewer) {
    viewer.removeFilter(filter);
    viewers.remove(viewer);
  }

  /**
   * Subclasses must implement and determine if the given element should be
   * selected.
   * 
   * @param parent the parent element
   * @param element the element
   * @param filter the filter text
   * @return true to select, false to hide
   */
  protected abstract boolean doSelect(P parent, E element, String filter);

  @Override
  protected boolean validateValue(String value) {
    boolean ret = super.validateValue(value);
    onFilter();
    return ret;
  }

  protected void onFilter() {
    for (Viewer v : viewers) {
      applyFilters(v);
    }
    focus();
  }

  protected void applyFilters(Viewer viewer) {
    viewer.applyFilters();
    if (viewer instanceof TreeViewer) {
      Tree tree = ((TreeViewer) viewer).getTree();
      if (!getValue().equals("")) {
        tree.animate = false;
        tree.expandAll();
      } else {
        tree.animate = animateTree;
        tree.collapseAll();
      }
    }
  }

}
