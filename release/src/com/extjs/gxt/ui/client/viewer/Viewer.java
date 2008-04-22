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
 *     Tom Schindl <tom.schindl@bestsolution.at> - fix in issue: 38
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Observable;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Viewer is a model-based adapter for a <code>Widget</code>. Supports custom sorting, filtering.
 */

public abstract class Viewer<CP> extends Observable implements SelectionProvider {

  /**
   * True to stop the wrapped component from being rendered before the content has been returned (defaults to false).
   */
  public boolean preventRender = false;

  /**
   * This viewer's input, or <code>null</code> if none. The viewer's input provides the "model" for the viewer's
   * content.
   */
  protected Object input;
  protected List<Object> elements;

  protected boolean rendered;
  protected Selection renderSelection;
  protected boolean renderApplyFilters;
  protected List<Object> renderElements;

  private ElementComparer comparer = DefaultElemmentComparer.INSTANCE;
  private BaseLabelProvider labelProvider;
  private CP contentProvider;
  private List<ViewerFilter> filters;
  private ViewerSorter sorter;
  private List<SelectionChangedListener> selectionListeners;

  /**
   * Adds the given filter to this viewer, and triggers refiltering and resorting of the elements.
   * 
   * @param filter the filter to be added
   */
  public void addFilter(ViewerFilter filter) {
    if (filters == null) {
      filters = new ArrayList<ViewerFilter>();
    }
    filters.add(filter);
    applyFilters();
  }

  /**
   * Applies the viewer's filters.
   */
  public void applyFilters() {
    if (preventRender && !rendered) {
      renderApplyFilters = true;
      return;
    }
    if (elements == null) return;
    for (Object element : elements) {
      Widget w = findItem(element);
      if (w != null) {
        if (isFiltered(null, element)) {
          w.setVisible(false);
        } else {
          w.setVisible(true);
        }
      }
    }
  }

  public void addSelectionListener(SelectionChangedListener listener) {
    if (selectionListeners == null) {
      selectionListeners = new ArrayList<SelectionChangedListener>();
    }
    if (!selectionListeners.contains(listener)) {
      selectionListeners.add(listener);
    }
  }

  /**
   * Finds the widget which represents the given element.
   * 
   * @param element the element
   * @return the corresponding widget, or <code>null</code> if none
   */
  public abstract Widget findItem(Object element);

  /**
   * Returns the comparer to use for comparing elements, or <code>null</code> if none has been set. If specified, the
   * viewer uses this to compare and hash elements rather than the elements' own equals and hashCode methods.
   * 
   * @return the comparer to use for comparing elements or <code>null</code>
   */
  public ElementComparer<Object> getComparer() {
    return comparer;
  }

  /**
   * Returns the viewer's content provider.
   * 
   * @return the content provider
   */
  public CP getContentProvider() {
    return contentProvider;
  }

  public List<Object> getElements() {
    return elements;
  }

  /**
   * Returns the active elements in the order being displayed.
   * 
   * @return the active elements
   */
  public List<Object> getActiveElements() {
    ArrayList<Object> list = new ArrayList<Object>();
    for (Object element : elements) {
      if (isFiltered(null, element)) {
        list.add(element);
      }
    }
    return list;
  }

  /**
   * Returns the viewer's filters.
   * 
   * @return the filters
   */
  public List<ViewerFilter> getFilters() {
    if (filters == null) {
      return new ArrayList<ViewerFilter>();
    }
    return new ArrayList<ViewerFilter>(filters);
  }

  /**
   * Returns the viewer's input.
   * 
   * @return the input
   */
  public Object getInput() {
    return input;
  }

  /**
   * Returns the viewer's label provider.
   * 
   * @return the label provider
   */
  public BaseLabelProvider getLabelProvider() {
    return labelProvider;
  }

  /**
   * Returns the current selection for this provider.
   * 
   * 
   * @return the current selection
   */
  public Selection getSelection() {
    Widget widget = getWidget();
    if (widget == null) {
      return DefaultSelection.emptySelection();
    }
    List<Object> elements = getSelectedFromWidget();
    return new DefaultSelection(elements);
  }

  /**
   * Returns this viewer's sorter, or <code>null</code> if it does not have one.
   * 
   * @return a viewer sorter, or <code>null</code> if none
   */
  public ViewerSorter getSorter() {
    return sorter;
  }

  /**
   * Returns the primary widget associated with this viewer.
   * 
   * @return the widget which displays this viewer's content
   */
  public abstract Widget getWidget();

  public boolean isFiltered(Object parent, Object element) {
    if (filters != null) {
      for (ViewerFilter filter : filters) {
        boolean result = filter.select(this, parent, element);
        if (!result) {
          return true;
        }
      }
    }
    return false;
  }

  protected void preserveSelections(Selection selection) {

  }

  /**
   * Refreshes this viewer completely with information freshly obtained from this viewer's model.
   */
  public void refresh() {
    if (!rendered) return;
    Selection sel = getSelection();
    if (input != null) {
      setInput(input);
    }
    preserveSelections(sel);
    setSelection(sel);
  }

  /**
   * Removes the element.
   * 
   * @param element the element to be removed
   */
  public abstract void remove(Object element);

  /**
   * Removes the given filter from this viewer, and triggers refiltering and resorting of the elements if required.
   * 
   * @param filter the filter to be removed
   */
  public void removeFilter(ViewerFilter filter) {
    if (filters != null) {
      if (filters.remove(filter)) {
        refresh();
      }
    }
  }

  public void removeSelectionListener(SelectionChangedListener listener) {
    if (selectionListeners != null) {
      selectionListeners.remove(listener);
    }
  }

  /**
   * Sets the comparer to use for comparing elements, or <code>null</code> to use the default equals and hashCode
   * methods on the elements themselves.
   * 
   * @param comparer the comparer to use for comparing elements or null
   */
  public void setComparer(ElementComparer<Object> comparer) {
    this.comparer = comparer == null ? DefaultElemmentComparer.INSTANCE : comparer;
  }

  /**
   * Sets the viewer's content provider.
   * 
   * @param contentProvider the new content provider
   */
  public void setContentProvider(CP contentProvider) {
    this.contentProvider = contentProvider;
  }

  /**
   * Sets the viewer's input.
   * 
   * @param input the new input
   */
  public abstract void setInput(Object input);

  /**
   * Sets the viewer's label provider.
   * 
   * @param labelProvider the new label provider
   */
  public void setLabelProvider(BaseLabelProvider labelProvider) {
    this.labelProvider = labelProvider;
  }

  /**
   * The viewer implementation of this <code>ISelectionProvider</code> method make the new selection for this viewer
   * without making it visible.
   * <p>
   * This method is equivalent to <code>setSelection(selection,false)</code>.
   * </p>
   */
  public void setSelection(Selection selection) {
    if (!rendered) {
      renderSelection = selection;
      return;
    }
    setSelection(selection, false);
  }

  /**
   * Sets a new selection for this viewer and optionally makes it visible.
   * <p>
   * Subclasses must implement this method.
   * </p>
   * 
   * @param selection the new selection
   * @param reveal <code>true</code> if the selection is to be made visible, and <code>false</code> otherwise
   */
  public abstract void setSelection(Selection selection, boolean reveal);

  /**
   * Sets this viewer's sorter and triggers refiltering and resorting of this viewer's element. Passing
   * <code>null</code> turns sorting off.
   * 
   * @param sorter a viewer sorter, or <code>null</code> if none
   */
  public void setSorter(ViewerSorter sorter) {
    if (this.sorter != sorter) {
      this.sorter = sorter;
      refresh();
    }
  }

  /**
   * Refreshes labels with information from the viewer's label provider.
   */
  public abstract void update();

  /**
   * Refreshes labels with information from the viewer's label provider.
   * 
   * @param element the element to be updated
   */
  public abstract void update(Object element);

  /**
   * Adds the element.
   * 
   * @param element the element to be added
   */
  protected abstract void add(Object element);

  /**
   * Returns the result of running the given elements through the filters.
   * 
   * @param elements the elements to filter
   * @return only the elements which all filters accept
   */
  protected List<Object> filter(List<Object> elements) {
    if (filters != null) {
      ArrayList<Object> filtered = new ArrayList<Object>();
      Object root = getRoot();
      for (Object element : elements) {
        boolean add = true;
        for (int j = 0; j < filters.size(); j++) {
          add = filters.get(j).select(this, root, element);
          if (!add) {
            break;
          }
        }
        if (add) {
          filtered.add(element);
        }
      }
      return filtered;
    }
    return elements;
  }

  /**
   * Notifies any selection listeners that the viewer's selection has changed.
   * 
   * @param se the selection event
   */
  protected void fireSelectionChanged(SelectionChangedEvent se) {
    if (selectionListeners != null) {
      for (SelectionChangedListener listener : selectionListeners) {
        listener.selectionChanged(se);
      }
    }
  }

  /**
   * Returns the root element.
   * <p>
   * The default implementation of this framework method forwards to <code>getInput</code>. Override if the root
   * element is different from the viewer's input element.
   * </p>
   * 
   * @return the root element, or <code>null</code> if none
   */
  protected Object getRoot() {
    return getInput();
  }

  /**
   * Retrieves the selection of the viewer.
   * 
   * @return the list of selected elements
   */
  protected abstract List<Object> getSelectedFromWidget();

  protected void hookWidget(Component component) {
    if (!component.isRendered()) {
      final Listener l = new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent be) {
          if (be.type == Events.BeforeRender) {
            removeListener(Events.BeforeRender, this);
            rendered = true;
            if (preventRender && renderElements != null) {
              onElementsReceived(renderElements);
              renderElements = null;
            }
          } else {
            removeListener(Events.Render, this);
            if (renderApplyFilters) {
              renderApplyFilters = false;
              applyFilters();
            }
            if (renderSelection != null) {
              setSelection(renderSelection);
              renderSelection = null;
            }
          }
        }

      };
      component.addListener(Events.BeforeRender, l);
      component.addListener(Events.Render, l);
    } else {
      rendered = true;
    }

    component.addListener(Events.SelectionChange, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        SelectionChangedEvent se = new SelectionChangedEvent(Viewer.this, getSelection());
        fireSelectionChanged(se);
      }
    });
  }

  /**
   * Inserts the element.
   * 
   * @param elem the element
   * @param index the insert location
   */
  protected abstract void insert(Object elem, int index);

  /**
   * Internal hook method called when the elements for this viewer are provided during the setInput workflow
   * 
   * @param elements the new elements
   */
  protected abstract void onElementsReceived(List<Object> elements);

  protected void removeElement(Object element) {
    elements.remove(element);
  }

  protected void sortElements(List<Object> elements) {
    if (sorter != null) {
      sorter.sort(this, elements);
    }
  }

  protected Object getElementFromItem(Component item) {
    Object elem = (Object) item.getData();
    return elem;
  }

}
