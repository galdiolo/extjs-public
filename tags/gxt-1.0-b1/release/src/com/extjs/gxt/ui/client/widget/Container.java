/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A <code>Container</code> that lays out its children using a
 * <code>Layout</code>. Layouts are responsible for connecting the child
 * components to the container. Layouts are very flexible as they can create any
 * internal element structure, inserting its child components at any location.
 * For example, a TableLayout lays out its children using HTML tables.
 * <p>
 * Many layouts support layout data which are configurable objects that provide
 * additional information to the layout. These objects can be passed when adding
 * and inserting child components into the container. Each layout will document
 * if and what layout data it supports.
 * </p>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : ContainerEvent(container, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the component being added</li>
 * <li>index : the index at which the component will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : ContainerEvent(container, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the component being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : ContainerEvent(container, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : ContainerEvent(container, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>AfterLayout</b> : ContainerEvent(container)<br>
 * <div>Fires when the widgets in this container are arranged by the associated
 * layout.</div>
 * <ul>
 * <li>container : this</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @see Layout
 */
public class Container<T extends Component> extends ScrollContainer<T> {

  private static FlowLayout defaultLayout = new FlowLayout();

  /**
   * True to disable the container's layout, stopping it from executing
   * (defaults to false).
   */
  public boolean disableLayout = false;

  /**
   * The size at the last time the layout executed.
   */
  protected Size lastSize;

  /**
   * The container's layout.
   */
  protected Layout layout;

  private boolean monitorResize = true;
  private boolean layoutOnChange;
  private boolean layoutOnAttach;

  /**
   * Adds a widget to this Container. Fires the <i>BeforeAdd</i> event before
   * adding, then fires the <i>Add</i> event after the component has been
   * added.
   * 
   * @param widget the widget to add. If the widget is not a Component instance
   *            it will be wrapped in a WidgetComponent
   */
  public void add(Widget widget) {
    insert(widget, getItemCount());
  }

  /**
   * Adds a widget to this Container. Fires the <i>BeforeAdd</i> event before
   * adding, then fires the <i>Add</i> event after the component has been
   * added.
   * 
   * @param widget the widget to add. If the widget is not a Component instance
   *            it will be wrapped in a WidgetComponent
   * @param layoutData the layout data
   */
  public void add(Widget widget, Object layoutData) {
    insert(widget, layoutData, getItemCount());
  }

  /**
   * Creates a new HTML instance and adds it to the container. Fires the
   * <i>BeforeAdd</i> event before adding, then fires the <i>Add</i> event
   * after the component has been added.
   * 
   * @param text the html text
   * @return the new HTML instance
   */
  public Html addText(String text) {
    Html html = new Html(text);
    add(html);
    return html;
  }

  /**
   * Returns the layout which is associated with the container, or
   * <code>null</code> if one has not been set.
   * 
   * @return the container's layout or <code>null</code>
   */
  public Layout getLayout() {
    return layout;
  }

  /**
   * Returns <code>true</code> if the layout will be executed when widgets are
   * added or removed.
   * 
   * @return the layout on change state
   */
  public boolean getLayoutOnChange() {
    return layoutOnChange;
  }

  /**
   * Override this method to specify the element to be used by the layout as the
   * container. Allows the container to be decorated.
   * 
   * @return the element to be used as the panel's container
   */
  public El getLayoutTarget() {
    return el;
  }

  /**
   * Returns the monitor resize state.
   * 
   * @return <code>true</code> if resizing is being monitored
   */
  public boolean getMonitorResize() {
    return monitorResize;
  }

  /**
   * Returns the index of the component.
   * 
   * @param component the component
   * @return the index
   */
  public int indexOf(Component component) {
    return items.indexOf(component);
  }

  /**
   * Inserts a widget into this Container at a specified index. Fires the
   * <i>BeforeAdd</i> event before inserting, then fires the <i>Add</i> event
   * after the component has been inserted.
   * 
   * @param widget the widget to insert. If the widget is not a Component
   *            instance it will be wrapped in a WidgetComponent
   * @param index the index at which the component will be inserted in
   */
  public void insert(Widget widget, int index) {
    insert(widget, null, index);
  }

  /**
   * Inserts a widget into this Container at a specified index. Fires the
   * <i>BeforeAdd</i> event before inserting, then fires the <i>Add</i> event
   * after the component has been inserted.
   * 
   * @param widget the widget to insert. If the widget is not a Component
   *            instance it will be wrapped in a WidgetComponent
   * @param layoutData the component's layout data
   * @param index the index at which the component will be inserted in
   */
  public void insert(Widget widget, Object layoutData, int index) {
    T c = null;
    if (widget instanceof Component) {
      c = (T) widget;
    } else {
      c = (T) new WidgetComponent(widget);
    }

    ContainerEvent ce = new ContainerEvent(this);
    ce.item = c;
    ce.index = index;
    if (fireEvent(Events.BeforeAdd, ce)) {
      if (layoutData != null) {
        c.setData(layoutData);
      }
      super.insert(c, index);
      if (rendered) {
        if (layoutOnChange) {
          layout(true);
        }
      }
      fireEvent(Events.Add, ce);
    }
  }

  /**
   * Executes the container's layout. If a layout has not been set a
   * <code>FlowLayout</code> will be used. If the size of the container has
   * not changed since the last time layout was called it will not execute. See
   * {@link #layout(boolean)} to force the layout to execute.
   * 
   * @return True if the layout is executed
   */
  public boolean layout() {
    return layout(false);
  }

  /**
   * Force this container's layout to be recalculated. If a layout has not been
   * set a <code>FlowLayout</code> will be used.
   * 
   * @param force <code>true</code> to force the layout to execute
   * @return true if the layout executes
   */
  public boolean layout(boolean force) {
    if (force) lastSize = null;
    if (!isRendered()) {
      layoutOnAttach = true;
      return false;
    }
    return onLayout();
  }

  public void onAttach() {
    super.onAttach();
    if (layoutOnAttach) {
      layoutOnAttach = false;
      layout(true);
    }
  }

  /**
   * Removes a component from this container. Fires the 'BeforeRemove' event
   * before removing, then fires the 'Remove' event after the component has been
   * removed.
   * 
   * @param component the component to remove
   */
  public boolean remove(T component) {
    if (fireEvent(Events.BeforeRemove, component)) {
      boolean result = super.remove(component);

      if (rendered && layoutOnChange) {
        layout(true);
      }
      if (layout != null && layout.activeItem == component) {
        layout.activeItem = null;
      }
      fireEvent(Events.Remove, component);
      return result;
    }
    return false;
  }

  /**
   * Removes all of children from this container.
   */
  public void removeAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
  }

  /**
   * Sets the container's layout.
   * 
   * @param layout the new layout
   */
  public void setLayout(Layout layout) {
    this.layout = layout;
    layout.setContainer(this);
  }

  /**
   * Specifies if the container's layout should be called when widgets are added
   * or removed. Default value is <code>false</code>.
   * 
   * @param layoutOnChange <code>true</code> to enable
   */
  public void setLayoutOnChange(boolean layoutOnChange) {
    this.layoutOnChange = layoutOnChange;
  }
  
  /**
   * Sets the monitor resize state. When <code>true</code> the container's
   * layout will be executed when the container is resized. Default value is
   * <code>true</code>.
   * 
   * @param monitorResize <code>true</code> to monitor resizing
   */
  public void setMonitorResize(boolean monitorResize) {
    this.monitorResize = monitorResize;
  }

  /**
   * Fires an event with the given event type, widget, and item.
   * 
   * @param type the event type
   * @param item the action widget
   * @return <code>false</code> if any listeners return <code>false</code>
   */
  protected boolean fireEvent(int type, Component item) {
    ContainerEvent ce = new ContainerEvent(this);
    ce.item = item;
    return fireEvent(type, ce);
  }

  /**
   * Fires an event with the given event type, widget, item, and index.
   * 
   * @param eventType the event type
   * @param item the action widget
   * @param index the index
   * @return <code>false</code> if any listeners return <code>false</code>
   */
  protected boolean fireEvent(int eventType, Component item, int index) {
    ContainerEvent ce = new ContainerEvent(this);
    ce.item = item;
    ce.index = index;
    return fireEvent(eventType, ce);
  }

  protected boolean onLayout() {
    Size size = getLayoutTarget().getSize();
    if (lastSize != null) {
      if (lastSize.equals(size)) {
        return false;
      }
    }
    lastSize = size;

    if (layout == null) {
      layout = defaultLayout;
    }

    // execute the layout
    layout.layout(this);

    // execution of the layout can result in new component being rendered
    // need to attach newly rendered children if the the container
    // is currently attached
    if (isAttached()) {
      int count = getItemCount();
      for (int i = 0; i < count; i++) {
        Component c = getItem(i);
        if (c.isRendered() & !c.isAttached()) {
          c.onAttach();
        }
      }
    }
    ContainerEvent ce = new ContainerEvent(this);
    fireEvent(Events.AfterLayout, ce);
    return true;
  }

  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    setElement(DOM.createDiv(), parent, index);
    setStyleAttribute("overflow", "hidden");
  }

  protected void onResize(int width, int height) {
    if (monitorResize && !disableLayout) {
      layout();
    }
  }

}
