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

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p/> Class for any {@link BoxComponent} that can contain other components.
 * Containers handle the basic behavior of containing components, namely
 * managing, attaching, and detaching the child widgets.
 * 
 * <p/> When children are added to a container they are not physcially added to
 * the DOM of the container. Subclasses are responsible for connecting the child
 * components.
 * 
 * <p/> Container does not define a root element. setElement must be called by
 * any subclass to ensure the container has an element.
 * 
 * @param <T> the child component type
 */
public abstract class Container<T extends Component> extends BoxComponent {

  /**
   * True to attach the container's children (defaults to true).
   */
  protected boolean attachChildren = true;

  /**
   * False to disable the container's layout, stopping it from executing
   * (defaults to false).
   */
  protected boolean enableLayout = false;

  /**
   * True to execute the container's layout when children are inserted and
   * removed (defaults to false).
   */
  protected boolean layoutOnChange;

  /**
   * True to execute the container's layout when the container is resized
   * (defaults to true).
   */
  protected boolean monitorResize = true;

  /**
   * True to monitor window resizing (defaults to false).
   */
  protected boolean monitorWindowResize = false;

  protected boolean layoutExecuted;
  
  private Layout layout;
  private List<T> items;

  /**
   * Creates a new container.
   */
  public Container() {
    items = new ArrayList<T>();
  }

  /**
   * Returns the component whose element, or child element, matches the given
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
   * Returns the item with the specified item id.
   * 
   * @param itemId the item id
   * @return the button or <code>null</code> if no match
   */
  public T getItemByItemId(String itemId) {
    for (T item : getItems()) {
      if (item.getItemId().equals(itemId)) {
        return item;
      }
    }
    return null;
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
  public Iterator<T> iterator() {
    return items.iterator();
  }

  /**
   * Removes all the container's items.
   * 
   * @return true if all items where removed
   */
  public boolean removeAll() {
    return removeAll(false);
  }

  /**
   * Scrolls the item into view.
   * 
   * @param item the item
   */
  public void scrollIntoView(T item) {
    if (rendered) {
      item.el().scrollIntoView(el().dom, false);
    }
  }

  /**
   * Adds a item to the container. Fires the <i>BeforeAdd</i> event before
   * inserting, then fires the <i>Add</i> event after the widget has been
   * inserted.
   * 
   * @param item the item to be added
   */
  protected boolean add(T item) {
    return insert(item, getItemCount());
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

  protected ContainerEvent createContainerEvent(T item) {
    return new ContainerEvent(this, item);
  }

  @Override
  protected void doAttachChildren() {
    if (attachChildren) {
      for (T item : items) {
        if (item.isRendered() && !item.isAttached()) {
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

  protected boolean doLayout() {
    if (!enableLayout) return false;

    layoutExecuted = true;

    if (layout == null) {
      setLayout(new FlowLayout());
    }

    // execute the layout
    layout.layout();

    for (Component c : items) {
      if (attachChildren && c.isRendered() && !c.isAttached()) {
        ComponentHelper.doAttach(c);
      }
      if (c instanceof LayoutContainer) {
        ((LayoutContainer) c).layout();
      } else if (c instanceof Container) {
        Container con = (Container) c;
        if (con.layout != null) {
          con.layout();
        }
      } else {
        c.recalculate();
      }
    }

    ContainerEvent ce = new ContainerEvent(this);
    fireEvent(Events.AfterLayout, ce);
    return true;
  }

  protected Layout getLayout() {
    return layout;
  }

  protected El getLayoutTarget() {
    return el();
  }

  /**
   * Addss a item into the container. Fires the <i>BeforeAdd</i> event before
   * inserting, then fires the <i>Add</i> event after the widget has been
   * inserted.
   * 
   * @param item the item to insert
   * @param index the insert location
   */
  protected boolean insert(T item, int index) {
    ContainerEvent containerEvent = createContainerEvent(item);
    containerEvent.index = index;
    if (fireEvent(Events.BeforeAdd, containerEvent)) {
      ComponentEvent componentEvent = item.createComponentEvent(null);
      if (item.fireEvent(Events.BeforeAdopt, componentEvent)) {
        onInsert(item, index);
        items.add(index, item);
        adopt(item);
        item.fireEvent(Events.Adopt, componentEvent);
        fireEvent(Events.Add, containerEvent);
        if (isRendered() && layoutOnChange) {
          layout();
        }
        return true;
      }
    }
    return false;
  }

  /**
   * Returns true if the container's layout is executed when the container is
   * resized (default to true).
   * 
   * @return true to enable resize monitoring
   */
  protected boolean isMonitorResize() {
    return monitorResize;
  }

  /**
   * Returns true if the container's layout is executed when the browser window
   * is resized (defaults to false).
   * 
   * @return true if window resize monitoring is enabled
   */
  protected boolean isMonitorWindowResize() {
    return monitorWindowResize;
  }

  /**
   * Executes the container's layout. If a layout has not been set a
   * <code>FlowLayout</code> will be used.
   */
  protected boolean layout() {
    if (!rendered) {
      return false;
    }
    return doLayout();
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    if (!layoutExecuted) {
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          if (!layoutExecuted) {
            layout();
          } 
        }
      });
    }
  }

  protected void onInsert(T item, int index) {

  }

  protected void onRemove(T item) {

  }

  protected final void orphan(T child) {
    assert (child.getParent() == this);
    if (child.isAttached()) {
      ComponentHelper.doDetach(child);
      assert !child.isAttached() : "Failure of " + getClass() + " to call super.onDetach()";
    }
    setParent(null, child);
  }

  /**
   * Removes the item from the container. Fires the <i>BeforeRemove</i> event
   * before removing, then fires the <i>Remove</i> event after the widget has
   * been removed.
   * 
   * @param item the item to remove
   * @return <code>true</code> if the item was removed
   */
  protected boolean remove(T item) {
    return remove(item, false);
  }

  protected boolean remove(T component, boolean force) {
    ContainerEvent containerEvent = createContainerEvent(component);
    containerEvent.item = component;
    if (fireEvent(Events.BeforeRemove, containerEvent) || force) {
      ComponentEvent componentEvent = component.createComponentEvent(null);
      if (component.fireEvent(Events.BeforeOrphan, componentEvent) || force) {
        onRemove(component);

        if (attachChildren) {
          if (component.getParent() != this) {
            throw new RuntimeException("component is not a child of this container");
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

        component.fireEvent(Events.Orphan, componentEvent);
        fireEvent(Events.Remove, containerEvent);

        if (rendered && layoutOnChange) {
          layout();
        }
        return true;
      }
    }
    return false;
  }

  protected boolean removeAll(boolean force) {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0), force);
    }
    return getItemCount() == 0;
  }

  /**
   * Sets the container's layout.
   * 
   * @param layout the new layout
   */
  protected void setLayout(Layout layout) {
    this.layout = layout;
    layout.setContainer(this);
  }

  /**
   * True to execute the container's layout when the container is resized
   * (defaults to true).
   * 
   * @param monitorResize true to monitor container resizing
   */
  protected void setMonitorResize(boolean monitorResize) {
    this.monitorResize = monitorResize;
  }

  /**
   * True to have the container's layout executed when the browser window is
   * resized (default to false).
   * 
   * @param monitorWindowResize true to monitor window resizing
   */
  protected void setMonitorWindowResize(boolean monitorWindowResize) {
    this.monitorWindowResize = monitorWindowResize;
  }

  /**
   * Helper Method for the subclasses that wish to support automatic wrapping of
   * Widget instances in WidgetComponents
   * 
   * <p/> If the widget is a component, no wrapping is performed
   * 
   * @param widget the widget to be wrapped
   * @return the new component
   */
  protected Component wrapWidget(Widget widget) {
    if (widget instanceof Component) {
      return (Component) widget;
    } else {
      return new WidgetComponent(widget);
    }
  }

  private native void setParent(Widget parent, Widget child) /*-{
   child.@com.google.gwt.user.client.ui.Widget::parent = parent;
   }-*/;

}
