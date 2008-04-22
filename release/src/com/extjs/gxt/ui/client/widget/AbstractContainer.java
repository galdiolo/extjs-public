/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract base class for any {@link BoxComponent} that can contain other
 * components. Containers handle the basic behavior of containing components,
 * namely managing, attaching, and detaching the child widgets.
 * <p>
 * When children are added to a container they are not physcially added to the
 * DOM of the container. Subclasses are responsible for connecting the child
 * components.
 * </p>
 * 
 * @param <T> the child component type
 */
public abstract class AbstractContainer<T extends Component> extends BoxComponent {

  /**
   * True to the automatically destroy any child component when removed from the
   * container (defaults to false).
   */
  public boolean autoDestroy;

  /**
   * True to have the container's direct childrens attach state updated by the
   * container (defaults to true).
   */
  protected boolean attachChildren = true;

  /**
   * The container's children.
   */
  protected List<T> items;

  /**
   * Creates a new container.
   */
  public AbstractContainer() {
    items = new ArrayList<T>();
  }

  @Override
  public void destroy() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
    super.destroy();
  }

  /**
   * Returns the component whose element or child element matches the given
   * element.
   * 
   * @param elem the element
   * @return the matching component or <code>null</code> if no match
   */
  public T findItem(Element elem) {
    for (T c : items) {
      if (DOM.isOrHasChild(c.getElement(), elem)) {
        return c;
      }
    }
    return null;
  }

  /**
   * Returns the item at the given index or null if index out of bounds.
   * 
   * @param index the index
   * @return the item
   */
  public T getItem(int index) {
    return index < items.size() ? items.get(index) : null;
  }

  /**
   * Returns the number of children.
   * 
   * @return the component count
   */
  public int getItemCount() {
    return items.size();
  }

  /**
   * Returns the child items.
   * 
   * @return the children
   */
  public List<T> getItems() {
    return items;
  }

  /**
   * Returns the widget at the given index. If the child is a WidgetComponent,
   * the wrapped widget is returned.
   * 
   * @param index the index
   * @return the widget
   */
  public Widget getWidget(int index) {
    Component c = getItem(index);
    if (c != null && c instanceof WidgetComponent) {
      return ((WidgetComponent) c).getWidget();
    } else if (c != null) {
      return c;
    }
    return null;
  }

  /**
   * Returns the index of the item.
   * 
   * @param item the item
   * @return the index
   */
  public int indexOf(T item) {
    return items.indexOf(item);
  }

  /**
   * Returns an iterator over the container's children.
   * 
   * @return an iterator
   */
  public Iterator iterator() {
    return items.iterator();
  }

  /**
   * Removes a component from the container.
   * 
   * @param component the component to remove
   * @return <code>true</code> if the component was removed
   */
  public boolean remove(T component) {
    if (attachChildren) {
      if (component.getParent() != this) {
        return false;
      }
      orphan(component);
    }

    if (rendered) {
      Element elem = component.getElement();
      Element parent = DOM.getParent(elem);
      if (parent != null) {
        DOM.removeChild(parent, elem);
      }
    }

    items.remove(component);

    if (autoDestroy) {
      component.destroy();
    }
    return true;
  }

  /**
   * Sets the child's parent to this container. In order to support lazy
   * rendering this method uses JSNI to simply set the child parent without
   * effecting the attached state of the child.
   * 
   * @param child the child widget
   */
  protected void adopt(T child) {
    setParent(this, child);
  }

  @Override
  protected void doAttachChildren() {
    if (attachChildren) {
      for (T item : items) {
        // with lazy rendering, a widget may be a logical child of the
        // container but it may not have been rendered. only attach if the
        // child is rendered
        if (item.isRendered()) {
          item.onAttach();
        }
      }
    }
  }

  @Override
  protected void doDetachChildren() {
    if (attachChildren) {
      for (T item : items) {
        if (item.isAttached()) {
          item.onDetach();
        }
      }
    }
  }

  protected void insert(T component, int index) {
    items.add(index, component);
    adopt(component);
  }

  protected final void orphan(T child) {
    assert (child.getParent() == this);
    if (child.isAttached()) {
      WidgetHelper.doDetach(child);
      assert !child.isAttached() : "Failure of " + getClass()
          + " to call super.onDetach()";
    }
    setParent(null, child);
  }

  private native void setParent(Widget parent, Widget child) /*-{
      child.@com.google.gwt.user.client.ui.Widget::parent = parent;
    }-*/;

  @Override
  public String toString() {
    return "count: " + getItemCount() + " " + el != null ? el.toString()
        : super.toString();
  }

}
