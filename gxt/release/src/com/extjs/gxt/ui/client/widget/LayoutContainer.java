/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A <code>Container</code> that lays out its children using a
 * <code>Layout</code>. Layouts are responsible for connecting the child
 * components to the container. Layouts are very flexible as they can create any
 * internal element structure, inserting its child components at any location.
 * For example, a TableLayout lays out its children using HTML tables.
 * 
 * <p/>Many layouts support layout data which are configurable objects that
 * provide additional information to the layout. These objects can be passed
 * when adding and inserting child components into the container. Each layout
 * will document if and what layout data it supports.
 * 
 * <p/>{@link FlowLayout} is the the default layout and will be used if not a
 * layout is not specified.
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
public class LayoutContainer extends ScrollContainer<Component> {

  /**
   * Creates a new layout container.
   */
  public LayoutContainer() {
    enableLayout = true;
  }

  /**
   * Creates a new layout container.
   * 
   * @param layout the layout
   */
  public LayoutContainer(Layout layout) {
    this();
    setLayout(layout);
  }

  /**
   * Adds a widget to this Container. Fires the <i>BeforeAdd</i> event before
   * adding, then fires the <i>Add</i> event after the component has been
   * added.
   * 
   * @param widget the widget to add. If the widget is not a Component instance
   *          it will be wrapped in a WidgetComponent
   */
  public boolean add(Widget widget) {
    return insert(widget, getItemCount());
  }

  /**
   * Adds a widget to this Container. Fires the <i>BeforeAdd</i> event before
   * adding, then fires the <i>Add</i> event after the component has been
   * added.
   * 
   * @param widget the widget to add. If the widget is not a Component instance
   *          it will be wrapped in a WidgetComponent
   * @param layoutData the layout data
   */
  public boolean add(Widget widget, LayoutData layoutData) {
    return insert(widget, getItemCount(), layoutData);
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
    if (add(html)) {
      return html;
    } else {
      return null;
    }
  }

  /**
   * Returns the widget component that wraps the given widget.
   * 
   * @param widget the wrapped widget
   * @return the component or null
   */
  public WidgetComponent findComponent(Widget widget) {
    for (Component c : getItems()) {
      if (c instanceof WidgetComponent) {
        WidgetComponent wc = (WidgetComponent) c;
        if (widget == wc.getWidget()) {
          return wc;
        }
      }
    }
    return null;
  }

  /**
   * Returns the layout which is associated with the container, or
   * <code>null</code> if one has not been set.
   * 
   * @return the container's layout or <code>null</code>
   */
  public Layout getLayout() {
    return super.getLayout();
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
    return el();
  }

  /**
   * Inserts a widget into this Container at a specified index. Fires the
   * <i>BeforeAdd</i> event before inserting, then fires the <i>Add</i> event
   * after the component has been inserted.
   * 
   * @param widget the widget to insert. If the widget is not a Component
   *          instance it will be wrapped in a WidgetComponent
   * @param index the index at which the component will be inserted in
   */
  public boolean insert(Widget widget, int index) {
    return super.insert(wrapWidget(widget), index);
  }

  /**
   * Inserts a widget into this Container at a specified index. Fires the
   * <i>BeforeAdd</i> event before inserting, then fires the <i>Add</i> event
   * after the component has been inserted.
   * 
   * @param widget the widget to insert. If the widget is not a Component
   *          instance it will be wrapped in a WidgetComponent
   * @param index the index at which the component will be inserted in
   * @param layoutData the component's layout data
   */
  public boolean insert(Widget widget, int index, LayoutData layoutData) {
    Component component = wrapWidget(widget);
    if (layoutData != null) {
      component.setLayoutData(layoutData);
    }
    boolean added = super.insert(component, index);
    return added;
  }

  @Override
  public boolean isMonitorResize() {
    return monitorResize;
  }

  @Override
  public boolean isMonitorWindowResize() {
    return super.isMonitorWindowResize();
  }

  /**
   * Executes the container's layout.
   */
  public boolean layout() {
    return super.layout();
  }

  /**
   * Removes a component from this container. Fires the 'BeforeRemove' event
   * before removing, then fires the 'Remove' event after the component has been
   * removed.
   * 
   * @param widget the widget to remove
   */
  public boolean remove(Widget widget) {
    Component c = null;
    if (!(widget instanceof Component)) {
      c = findComponent(widget);
    } else {
      c = (Component) widget;
    }
    if (c == null) {
      return false;
    }
    return super.remove(c);
  }

  /**
   * Removes all of children from this container.
   */
  public boolean removeAll() {
    return super.removeAll();
  }

  /**
   * Sets the container's layout.
   * 
   * @param layout the new layout
   */
  public void setLayout(Layout layout) {
    super.setLayout(layout);
  }
  
  /**
   * Sets the component's layout data.
   * 
   * @param component the component
   * @param layoutData the layou data
   */
  public void setLayoutData(Component component, LayoutData layoutData) {
    ComponentHelper.setLayoutData(component, layoutData);
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

  @Override
  public void setMonitorWindowResize(boolean monitorWindowResize) {
    super.setMonitorWindowResize(monitorWindowResize);
  }

  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    if (el() == null) {
      setElement(DOM.createDiv(), parent, index);
    }
    setStyleAttribute("overflow", "hidden");
  }

}
