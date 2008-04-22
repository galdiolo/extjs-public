/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.event.WidgetListener;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.util.Observable;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for GXT components. All subclasses of Component can automatically
 * participate in the standard GXT component lifecycle of creation, rendering
 * and destruction. They also have automatic support for basic hide/show and
 * enable/disable behavior. Component allows any subclass to be lazy-rendered
 * into any GXT {@link AbstractContainer}. Components added to a GWT
 * {@link Panel} will be rendered when inserted. All visual widgets that require
 * rendering into a layout should subclass Component (or {@link BoxComponent} if
 * managed box model handling is required).
 * 
 * <p>
 * The following 4 methods inherited from UIObject (setSize, setWidth,
 * setHeight, setPixelSize) have been overridden and do nothing. Any component
 * whose size can change should subclass {@link BoxComponent}.
 * </p>
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Enable</b> : ComponentEvent(component)<br>
 * <div>Fires after the component is enabled.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Disable</b> : ComponentEvent(component)<br>
 * <div>Fires after the component is disabled.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeHide</b> : ComponentEvent(component)<br>
 * <div>Fires before the component is hidden. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeShow</b> : ComponentEvent(component)<br>
 * <div>Fires before the component is shown. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Hide</b> : ComponentEvent(component)<br>
 * <div>Fires after the component is hidden.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Show</b> : ComponentEvent(component)<br>
 * <div>Fires after the component is shown.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Attach</b> : ComponentEvent(component)<br>
 * <div>Fires after the component is attached.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Detach</b> : ComponentEvent(component) <br>
 * <div>Fires after the component is detached.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRender</b> : ComponentEvent(component)<br>
 * <div>Fires before the component is rendered. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Render</b> : ComponentEvent(component)<br>
 * <div>Fires after the component is rendered.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeDestroy</b> : ComponentEvent(component)<br>
 * <div>Fires before the component is destroyed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Destroy</b> : ComponentEvent(component)<br>
 * <div>Fires after the component is destroyed.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BrowserEvent</b> : ComponentEvent(component, event)<br>
 * <div>Fires on any browser event the component receives. Listners will be
 * called prior to any event processing and before
 * {@link #onComponentEvent(ComponentEvent)} is called. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the processing of
 * the event.</div>
 * <ul>
 * <li>component : this</li>
 * <li>event : event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeStateRestore</b> : ComponentEvent(component, state)<br>
 * <div>Fires before the state of the component is restored. Listeners can set
 * the <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
 * <li>state : the state values
 * </ul>
 * </dd>
 * 
 * <dd><b>StateRestore</b> : ComponentEvent(component, state)<br>
 * <div>Fires after the state of the component is restored.</div>
 * <ul>
 * <li>component : this</li>
 * <li>state : map of state key / value pairs</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>StateRestore</b> : ComponentEvent(component, state)<br>
 * <div>Fires before the state of the component is saved to the configured state
 * provider.</div>
 * <ul>
 * <li>component : this</li>
 * <li>state : map of state key / value pairs</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>StateSave</b> : ComponentEvent(component, state)<br>
 * <div>Fires after the state of the component is saved to the configured state
 * provider.</div>
 * <ul>
 * <li>component : this</li>
 * <li>state : map of state key / value pairs</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 */
public abstract class Component extends Widget {

  static {
    GXT.init();
  }

  /**
   * The wrapped el instance.
   */
  public El el;

  /**
   * The base style is typically set as the component's style when rendered. All
   * child styles should be calculated based on the base style when the
   * component is rendered. This allows a component's style to be swapped by
   * simply modifying the base style (defaults to null).
   */
  protected String baseStyle;

  /**
   * The style used when a component is disabled (defaults to
   * 'x-item-disabled').
   */
  protected String disabledStyle = "x-item-disabled";

  /**
   * True if this component has been rendered. Read-only.
   */
  protected boolean rendered;

  /**
   * True if the component is can receive focus (defaults to false). A hidden
   * input field will be created created for Safari.
   */
  protected boolean focusable;

  /**
   * True if this component is hidden. Read only.
   */
  protected boolean hidden;

  /**
   * True if the component is disabled. Read only.
   */
  protected boolean disabled;

  /**
   * True to enable state (defaults to true).
   */
  protected boolean stateful = true;

  /**
   * The state id (defaults to null).
   */
  protected String stateId;

  private Map<String, Object> state;
  private ToolTipConfig toolTipConfig;
  private Menu contextMenu;
  private String id, itemId, cls, title;
  private Object data;
  private HashMap dataMap;
  private Observable observable;
  private String styles = "";
  private ToolTip toolTip;
  private int disableTextSelection = Style.DEFAULT;
  private int disableContextMenu = Style.DEFAULT;
  private int borders = Style.DEFAULT;
  private El focusEl;
  private Map overElements;
  private Element dummy;
  private boolean destroyed;
  private boolean disableEvents;
  private boolean hasBrowserListener;
  private boolean hasListeners;

  /**
   * Creates a new component..
   */
  public Component() {
    observable = new Observable();
  }

  /**
   * Specialized constructor for creating components from existing nodes. The
   * given element should be part of the dom and have a a parent element.
   * 
   * @param element the element
   * @param attach true to attach the component
   */
  protected Component(Element element, boolean attach) {
    this();
    setElement(element);
    render(DOM.getParent(element));
    if (attach) {
      onAttach();
    }
  }

  /**
   * Appends an event handler to this component.
   * 
   * @param eventType the eventType
   * @param listener the listener to be added
   */
  public void addListener(int eventType, Listener listener) {
    hasListeners = true;
    if (eventType == Events.BrowserEvent) {
      hasBrowserListener = true;
    }
    observable.addListener(eventType, listener);
  }

  /**
   * Adds a CSS style name to the component's underlying element.
   * 
   * @param style the CSS style name to add
   */
  public void addStyleName(String style) {
    if (rendered) {
      fly(getStyleElement()).addStyleName(style);
    } else {
      cls = cls == null ? style : cls + " " + style;
    }
  }

  /**
   * Adds a listener to receive widget events.
   * 
   * @param listener the listener to be added
   */
  public void addWidgetListener(WidgetListener listener) {
    TypedListener l = new TypedListener(listener);
    addListener(Events.Attach, l);
    addListener(Events.Detach, l);
    addListener(Events.Resize, l);
  }

  /**
   * Destroys this component by purging any event listeners, removing the
   * component's element from the DOM, removing the component from its parent
   * (if applicable). Subclasses should override as needed.
   */
  public void destroy() {
    if (fireEvent(Events.BeforeDestroy)) {
      Widget p = getParent();
      if (p != null) {
        if (p instanceof HasWidgets) {
          ((HasWidgets) p).remove(this);
        } else if (p instanceof AbstractContainer) {
          ((AbstractContainer) p).remove(this);
        }
      }
      Element el = DOM.getParent(getElement());
      if (el != null) {
        DOM.removeChild(el, getElement());
      }
      if (getElement() != null) {
        clearElemenet();
      }
      overElements = null;
      destroyed = true;
      disabled = true;
      onDestroy();
      fireEvent(Events.Destroy);
      removeAllListeners();
      observable = null;
    }
  }

  /**
   * Disable this component. Fires the <i>Disable</i> event.
   */
  public void disable() {
    if (rendered) {
      onDisable();
    }
    disabled = true;
    fireEvent(Events.Disable);
  }

  /**
   * True to disable event processing.
   * 
   * @param disable true to disable
   */
  public void disableEvents(boolean disable) {
    disableEvents = disable;
  }

  /**
   * Enables and disables text selection for the component.
   * 
   * @param disable <code>true</code> to disable text selection
   */
  public void disableTextSelection(boolean disable) {
    disableTextSelection = disable ? 1 : 0;
    if (isAttached()) {
      el.disableTextSelection(disable);
    }
  }

  /**
   * Enable this component. Fires the <i>Enable</i> event.
   */
  public void enable() {
    if (rendered) {
      onEnable();
    }
    disabled = false;
    fireEvent(Events.Enable);
  }

  /**
   * Enables or disables event processing.
   * 
   * @param enable the enable state
   */
  public void enableEvents(boolean enable) {
    disabled = !enable;
  }

  /**
   * Fires an event with the given event type.
   * 
   * @param type the event type
   * @return <code>false</code> if any listeners return <code>false</code>
   */
  public boolean fireEvent(int type) {
    ComponentEvent be = new ComponentEvent(this);
    return fireEvent(type, be);
  }

  /**
   * Fires the specified event with the given event type.
   * 
   * @param type the event type
   * @param ce the base event
   * @return <code>false</code> if any listeners return <code>false</code>
   */
  public boolean fireEvent(int type, ComponentEvent ce) {
    return observable.fireEvent(type, ce);
  }

  /**
   * Returns the global flyweight instance.
   * 
   * @param elem the new wrapped dom element
   * @return the global flyweight instance
   */
  public El fly(Element elem) {
    return El.fly(elem);
  }

  /**
   * Try to focus this component. Fires the <i>Focus</i> event.
   */
  public void focus() {
    if (rendered) {
      getFocusEl().focus();
    }
    fireEvent(Events.Focus);
  }

  /**
   * Returns the component's base style.
   * 
   * @return the base style
   */
  public String getBaseStyle() {
    return baseStyle;
  }

  /**
   * Returns the application defined data associated with the component, or
   * <code>null</code> if it has not been set.
   */
  public Object getData() {
    return data;
  }

  /**
   * Returns the application defined property for the given name, or
   * <code>null</code> if it has not been set.
   * 
   * @param key the name of the property
   * @return the value or <code>null</code> if it has not been set
   */
  public Object getData(String key) {
    if (dataMap == null) return null;
    return dataMap.get(key);
  }

  @Override
  public Element getElement() {
    // if getElement is called before a component is rendered then the caller is
    // a gwt panel. a proxy element is returned and the component will be
    // rendered when it is attached
    if (!rendered) {
      dummy = DOM.createDiv();
      return dummy;
    }
    return super.getElement();
  }

  /**
   * Returns the id of this component. A new id is generated if an id has not
   * been set.
   * 
   * @return the component's id
   */
  public String getId() {
    if (id == null) {
      id = XDOM.getUniqueId();
      setId(id);
      return id;
    }
    return id;
  }

  /**
   * Returns the item id of this component. Unlike the component's id, the item
   * id does not have to be unique.
   * 
   * @return the component's item id
   */
  public String getItemId() {
    return itemId != null ? itemId : getId();
  }

  /**
   * Returns the component's tool tip.
   * 
   * @return the tool tip
   */
  public ToolTip getToolTip() {
    if (toolTip == null && toolTipConfig != null) {
      toolTip = new ToolTip(this, toolTipConfig);
    }
    return toolTip;
  }

  /**
   * Hide this component. Fires the <i>BeforeHide</i> event before the
   * component is hidden, the fires the <i>Hide</i> event after the component
   * is hidden.
   */
  public void hide() {
    if (fireEvent(Events.BeforeHide)) {
      hidden = true;
      if (rendered) {
        onHide();
      }
      fireEvent(Events.Hide);
    }
  }

  /**
   * Hides the component's tool tip (if one exists).
   */
  public void hideToolTip() {
    if (toolTip != null) {
      toolTip.hide();
    }
  }

  /**
   * Returns <code>true</code> if the component is destroyed.
   * 
   * @return the dispose state
   */
  public boolean isDestroyed() {
    return destroyed;
  }

  /**
   * Returns <code>true</code> if the component is enabled.
   * 
   * @return the enabled state
   */
  public boolean isEnabled() {
    return !disabled;
  }

  /**
   * Returns <code>true</code> if the component is rendered.
   * 
   * @return the rendered state
   */
  public boolean isRendered() {
    return rendered;
  }

  /**
   * Returns <code>true</code> if the component is visible.
   */
  public boolean isVisible() {
    return rendered && el.isVisible();
  }

  public void onAttach() {
    // added to a gwt panel, not rendered
    if (!rendered) {
      // render and swap the proxy element
      Element parent = DOM.getParent(dummy);
      int index = DOM.getChildIndex(parent, dummy);
      DOM.removeChild(parent, dummy);
      render(parent, index);
    }
    if (disableTextSelection > 0) {
      el.disableTextSelection(disableTextSelection == 1);
    }
    if (disableContextMenu > 0) {
      el.disableContextMenu(disableContextMenu == 1);
    }
    super.onAttach();
  }

  /**
   * Components delegate event handling to
   * {@link #onComponentEvent(ComponentEvent)}. Sublcasses should not override.
   * 
   * @param event the dom event
   */
  public void onBrowserEvent(Event event) {
    if (disabled || disableEvents) {
      return;
    }

    ComponentEvent ce = new ComponentEvent(this, event);
    ce.component = this;

    // browser event listeners can cancel event
    if (hasBrowserListener && !fireEvent(Events.BrowserEvent, ce)) {
      return;
    }

    // dom event type
    ce.type = DOM.eventGetType(event);

    // listeners can cancel event
    if (hasListeners && !fireEvent(ce.type, ce)) {
      return;
    }

    if (ce.type == Event.ONMOUSEUP && ce.isRightClick()) {
      onRightClick(ce);
    }

    // specialized support for mouse overs
    int t = ce.type;
    if (overElements != null && (t == Event.ONMOUSEOVER || t == Event.ONMOUSEOUT)) {
      El target = fly(ce.getTarget());
      String id = target.getId();
      if (id != null && overElements.containsKey(id)) {
        String style = (String) overElements.get(id);
        target.setStyleName(style, t == Event.ONMOUSEOVER);
      }
    }

    onComponentEvent(ce);
  }

  /**
   * Any events a component receives will be forwarded to this method.
   * Subclasses should override as needed. The {@link #onBrowserEvent} method
   * should not be overridden or modified.
   * 
   * @param ce the base event
   */
  public void onComponentEvent(ComponentEvent ce) {
  }

  /**
   * Notifies the component that it should recalculate its layout based on its
   * current size if neccessary. Default implementation does nothing. Layouts
   * will call this method if a child component is not sized directly when the
   * layout is executed.
   */
  public void recalculate() {

  }

  /**
   * Removes all listeners.
   */
  public void removeAllListeners() {
    observable.removeAllListeners();
  }

  @Override
  public void removeFromParent() {
    if (getParent() instanceof AbstractContainer) {
      ((AbstractContainer) getParent()).remove(this);
      return;
    }
    super.removeFromParent();
  }

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(int eventType, EventListener listener) {
    observable.removeListener(eventType, listener);
  }

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(int eventType, Listener listener) {
    observable.removeListener(eventType, listener);
  }

  /**
   * Removes a CSS style name from the component's underlying element.
   * 
   * @param style the CSS style name to remove
   */
  public void removeStyleName(String style) {
    if (rendered) {
      fly(getStyleElement()).removeStyleName(style);
    } else if (style != null) {
      String[] s = style.split(" ");
      style = "";
      for (int i = 0; i < s.length; i++) {
        if (!s[i].equals(style)) {
          style += " " + s[i];
        }
      }
    }
  }

  /**
   * Removes a listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeWidgetListener(WidgetListener listener) {
    if (observable.eventTable == null) return;
    observable.eventTable.unhook(Events.Attach, listener);
    observable.eventTable.unhook(Events.Detach, listener);
    observable.eventTable.unhook(Events.Resize, listener);
  }

  /**
   * Renders the element. Typically, this method does not need to be called
   * directly. A component will rendered by its parent if it is a container, or
   * rendered when attached if added to a gwt panel.
   * 
   * @param target the element this component should be rendered into
   */
  public void render(Element target) {
    render(target, -1);
  }

  /**
   * Renders the element. Typically, this method does not need to be called
   * directly. A component will rendered by its parent if it is a container, or
   * rendered when attached if added to a gwt panel.
   * 
   * @param target the element this component should be rendered into
   * @param index the index within the container <b>before</b> which this
   *            component will be inserted (defaults to appending to the end of
   *            the container if value is -1)
   */
  public void render(Element target, int index) {
    if (rendered || !fireEvent(Events.BeforeRender)) {
      return;
    }
    rendered = true;

    initState();

    createStyles(baseStyle);

    if (index == -1) {
      index = DOM.getChildCount(target);
    }
    onRender(target, index);

    if (el == null)
      throw new RuntimeException(getClass().getName()
          + " must call setElement in onRender");

    addStyleName(baseStyle);

    if (cls != null) {
      addStyleName(cls);
      cls = null;
    }

    if (title != null) {
      setTitle(title);
    }

    if (id == null) {
      id = XDOM.getUniqueId();
    }
    setId(id);

    if (styles != null && !styles.equals("")) {
      el.applyStyles(styles);
      styles = null;
    }

    if (toolTipConfig != null) {
      setToolTip(toolTipConfig);
    }

    if (hidden) {
      hide();
    }

    if (disabled) {
      disable();
    }

    if (borders != Style.DEFAULT) {
      setBorders(borders == 1);
    }

    if (focusable && GXT.isSafari) {
      focusEl = new El(createHiddenInput());
      el.appendChild(focusEl.dom);
    }
    afterRender();

    fireEvent(Events.Render);
  }

  /**
   * Adds or removes a border. The style name 'x-border' is added to the widget
   * to display a border.
   * 
   * @param show <code>true</code> to display a border
   */
  public void setBorders(boolean show) {
    borders = show ? 1 : 0;
    if (rendered) {
      fly(getStyleElement()).setBorders(show);
    }
  }

  /**
   * Sets the application defined component data.
   * 
   * @param data the widget data
   */
  public void setData(Object data) {
    this.data = data;
  }

  /**
   * Sets the application defined property with the given name.
   * 
   * @param key the name of the property
   * @param data the new value for the property
   */
  public void setData(String key, Object data) {
    if (dataMap == null) dataMap = new HashMap();
    dataMap.put(key, data);
  }

  // make public
  public void setElement(Element elem) {
    el = new El(elem);
    super.setElement(elem);
  }

  /**
   * Convenience function for setting disabled/enabled by boolean.
   * 
   * @param enabled the enabled state
   */
  public void setEnabled(boolean enabled) {
    if (!enabled) {
      disable();
    } else {
      enable();
    }
  }

  /**
   * Overrides UIObject and does nothing.
   */
  public void setHeight(String height) {
  }

  /**
   * Sets the component's id.
   * 
   * @param id the new id
   */
  public void setId(String id) {
    this.id = id;
    if (el != null) {
      DOM.setElementProperty(getElement(), "id", id);
    }
  }

  /**
   * Sets a style attribute.
   * 
   * @param attr the attribute
   * @param value the attribute value
   */
  public void setIntStyleAttribute(String attr, int value) {
    setStyleAttribute(attr, "" + value);
  }

  /**
   * Sets the component's item id.
   * 
   * @param id the item id
   */
  public void setItemId(String id) {
    this.itemId = id;
  }

  /**
   * Overrides UIObject and does nothing.
   */
  public void setPixelSize(int width, int height) {
  }

  /**
   * Overrides UIObject and does nothing.
   */
  public void setSize(String width, String height) {
  }

  /**
   * Sets a style attribute.
   * 
   * @param attr the attribute
   * @param value the attribute value
   */
  public void setStyleAttribute(String attr, String value) {
    if (rendered) {
      DOM.setStyleAttribute(getStyleElement(), attr, value);
    } else {
      styles += attr + ":" + value + ";";
    }
  }

  public void setStyleName(String style) {
    if (rendered) {
      super.setStyleName(style);
    } else {
      cls = style;
    }
  }

  public void setTitle(String title) {
    this.title = title;
    if (rendered) {
      super.setTitle(title);
    }
  }

  /**
   * Sets the component's tool tip.
   * 
   * @param text the text
   */
  public void setToolTip(String text) {
    if (toolTipConfig == null) {
      toolTipConfig = new ToolTipConfig();
    }
    toolTipConfig.text = text;
    setToolTip(toolTipConfig);
  }

  /**
   * Sets the component's tool tip with the given config.
   * 
   * @param config the tool tip config
   */
  public void setToolTip(ToolTipConfig config) {
    this.toolTipConfig = config;
    if (rendered) {
      if (toolTip == null) {
        toolTip = new ToolTip(this, config);
      } else {
        toolTip.update(config);
      }
    }
  }

  /**
   * Convenience function to hide or show this component by boolean.
   * 
   * @param visible the visible state
   */
  public void setVisible(boolean visible) {
    if (visible) {
      show();
    } else {
      hide();
    }
  }

  /**
   * Overrides UIObject and does nothing.
   */
  public void setWidth(String width) {
  }

  /**
   * Show this component. Fires the <i>BeforeShow</i> event before the
   * component is made visible, then fires the <i>Show</i> event after the
   * component is visible.
   */
  public void show() {
    if (fireEvent(Events.BeforeShow)) {
      hidden = false;
      if (rendered) {
        onShow();
      }
      fireEvent(Events.Show);
    }
  }

  public String toString() {
    return el != null ? el.toString() : super.toString();
  }

  /**
   * Adds a style to the given element on mousever. The component must be
   * sinking mouse events for the over style to function.
   * 
   * @param elem the over element
   * @param style the style to add
   */
  protected void addStyleOnOver(Element elem, String style) {
    if (overElements == null) {
      overElements = new HashMap();
    }
    String id = null;
    if (getElement() == elem) {
      id = getId();
    } else {
      id = fly(elem).getId();
      if (id == null || id.equals("")) {
        id = XDOM.getUniqueId();
        fly(elem).setId(id);
      }
    }
    overElements.put(id, style);
  }

  /**
   * Called after the component has been rendered and is attached for the first
   * time. At this time, the component will be part of the DOM which is required
   * when retrieving location and offsets.
   */
  protected void afterRender() {

  }

  protected void applyState(Map<String, Object> state) {

  }

  /**
   * Tries to remove focus from the component. Fires the <i>Blur</i> event.
   */
  protected void blur() {
    if (rendered) {
      getFocusEl().blur();
    }
    fireEvent(Events.Blur);
  }

  protected void createStyles(String baseStyle) {

  }

  /**
   * Enables and disables the component's context menu.
   * 
   * @param disable <code>true</code> to disable the context menu
   */
  protected void disableContextMenu(boolean disable) {
    disableContextMenu = disable ? 1 : 0;
    if (isAttached()) {
      el.disableContextMenu(disable);
    }
  }

  /**
   * Returns the component's context menu. This method is marked protected,
   * subclasses can change access to public to expose the contenxt menu.
   * 
   * @return the context menu
   */
  protected Menu getContextMenu() {
    return contextMenu;
  }

  protected El getFocusEl() {
    return focusEl == null ? el : focusEl;
  }

  /**
   * Returns the component's state.
   * 
   * @return the state
   */
  public Map<String, Object> getState() {
    if (state == null) {
      state = new HashMap<String, Object>();
    }
    return state;
  }

  protected void initState() {
    String sid = stateId != null ? stateId : getId();
    state = StateManager.getMap(sid);
    if (state != null) {
      ComponentEvent ce = new ComponentEvent(this);
      ce.state = state;
      if (fireEvent(Events.BeforeStateRestore, ce)) {
        applyState(state);
        fireEvent(Events.StateRestore, ce);
      }
    }
  }

  protected void onDestroy() {
  }

  protected void onDetach() {
    super.onDetach();
    if (disableTextSelection > 0) {
      el.disableTextSelection(false);
    }
    if (disableContextMenu > 0) {
      el.disableContextMenu(false);
    }
    fireEvent(Events.Detach);
  }

  protected void onDisable() {
    addStyleName(disabledStyle);
  }

  protected void onEnable() {
    removeStyleName(disabledStyle);
  }

  protected void onHide() {
    el.setVisible(false);
  }

  protected void onHideContextMenu() {
    disabled = false;
  }

  protected void onLoad() {
    super.onLoad();
    fireEvent(Events.Attach);
  }

  /**
   * Subclasses must override and ensure setElement is called for lazy rendered
   * components.
   * 
   * @param target the target element
   * @param index the insert location
   */
  protected void onRender(Element target, int index) {

  }

  protected void onRightClick(final ComponentEvent ce) {
    if (contextMenu != null) {
      ce.stopEvent();
      final int x = ce.getClientX();
      final int y = ce.getClientY();
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          onShowContextMenu(x, y);
        }
      });
    }
  }

  protected void onShow() {
    el.setVisible(true);
  }

  protected void onShowContextMenu(int x, int y) {
    if (fireEvent(Events.ContextMenu)) {
      contextMenu.addListener(Events.Hide, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          contextMenu.removeListener(Events.Hide, this);
          onHideContextMenu();
        }
      });
      contextMenu.showAt(x + 5, y + 5);
      disabled = true;
    }
  }

  protected void removeStyleOnOver(Element elem) {
    if (overElements != null) {
      overElements.remove(fly(elem).getId());
    }
  }

  /**
   * Saves the component's current state.
   */
  public void saveState() {
    if (state != null) {
      ComponentEvent ce = new ComponentEvent(this);
      ce.state = state;
      if (fireEvent(Events.BeforeStateSave, ce)) {
        String sid = stateId != null ? stateId : getId();
        StateManager.set(sid, state);
        fireEvent(Events.StateSave, ce);
      }
    }
  }

  /**
   * Sets the component's context menu.
   * 
   * @param menu the context menu
   */
  protected void setContextMenu(Menu menu) {
    contextMenu = menu;
    disableContextMenu(true);
    if (isAttached()) {
      el.disableContextMenu(true);
    }
  }

  protected void setElement(Element elem, Element parent, int index) {
    setElement(elem);
    DOM.insertChild(parent, elem, index);
  }

  private native void clearElemenet() /*-{
            this.@com.google.gwt.user.client.ui.UIObject::element = null;
          }-*/;

  private native Element createHiddenInput() /*-{
            var input = $doc.createElement('input');
            input.type = 'text';
            input.style.opacity = 0;
            input.style.zIndex = -1;
            input.style.height = '1px';
            input.style.width = '1px';
            input.style.overflow = 'hidden';
            input.style.position = 'absolute';
            return input;
          }-*/;

}
