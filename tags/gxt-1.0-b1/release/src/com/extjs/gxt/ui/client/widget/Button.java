/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.util.TextMetrics;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A button component.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeSelect</b> : ButtonEvent(button, event)<br>
 * <div>Fires before this button is selected.</div>
 * <ul>
 * <li>button : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : ButtonEvent(button, event)<br>
 * <div>Fires when this button is selected.</div>
 * <ul>
 * <li>button : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>MouseOver</b> : ButtonEvent(button, event)<br>
 * <div>Fires when the mouse hovers over the button.</div>
 * <ul>
 * <li>button : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>MouseOut</b> : ButtonEvent(button, event)<br>
 * <div>Fires when the mouse exits the button.</div>
 * <ul>
 * <li>button : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>MenuShow</b> : ButtonEvent(button, item)<br>
 * <div>If this button has a menu, this event fires when it is shown.</div>
 * <ul>
 * <li>button : this</li>
 * <li>menu : the menu</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>MenuHide</b> : ButtonEvent(button, item)<br>
 * <div>If this button has a menu, this event fires when it is hidden.</div>
 * <ul>
 * <li>button : this</li>
 * <li>menu : the menu</li>
 * </ul>
 * </dd>
 * 
 * </dt>
 */
public class Button extends Component {

  /**
   * Template used by buttons if a template is not provided.
   */
  public static Template buttonTemplate;

  /**
   * False to disable visual cues on mouseover, mouseout and mousedown (defaults
   * to true).
   */
  public boolean handleMouseEvents = true;

  /**
   * Submit, reset or button (defaults to 'button').
   */
  public String type = "button";

  /**
   * The position to align the menu to (see {@link El#alignTo} for more details,
   * defaults to 'tl-bl?').
   */
  public String menuAlign = "tl-bl?";

  /**
   * tabIndex Set a DOM tabIndex for this button (defaults to undefined)
   */
  public int tabIndex = -1;

  /**
   * The minimum width for this button (used to give a set of buttons a common
   * width)
   */
  public int minWidth;

  /**
   * The optional button template (defaults to null). If a template is not
   * specified, the {@link #buttonTemplate} will be used.
   */
  protected Template template;

  protected String text;
  protected String buttonSelector = "button";
  protected Menu menu;
  protected El buttonEl;

  private String iconStyle;

  /**
   * Creates a new button.
   */
  public Button() {
    baseStyle = "x-btn";
  }

  /**
   * Creates a new button with the given text.
   * 
   * @param text the button text
   */
  public Button(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new button with the given text and specified selection listener.
   * 
   * @param text the button's text
   * @param listener the selection listener
   */
  public Button(String text, SelectionListener listener) {
    this(text);
    addSelectionListener(listener);
  }

  /**
   * Adds a selection listener.
   * 
   * @param listener the listener to add
   */
  public void addSelectionListener(SelectionListener listener) {
    TypedListener tl = new TypedListener(listener);
    addListener(Events.Select, tl);
  }

  /**
   * Returns the button's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return iconStyle;
  }

  /**
   * Returns the button's menu (if it has one).
   * 
   * @return the menu
   */
  public Menu getMenu() {
    return menu;
  }

  /**
   * Returns the button's text.
   * 
   * @return the button text
   */
  public String getText() {
    return text;
  }

  /**
   * Hide this button's menu (if it has one).
   */
  public void hideMenu() {
    if (menu != null) {
      menu.hide();
      ButtonEvent be = new ButtonEvent(this);
      be.menu = menu;
      fireEvent(Events.MenuHide, be);
    }
  }

  @Override
  public void onAttach() {
    super.onAttach();
    DOM.setEventListener(getFocusEl().dom, this);
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {
    Element source = ce.component.getElement();
    ce.stopEvent();
    super.onComponentEvent(ce);
    switch (ce.type) {
      case Event.ONMOUSEOVER:
        Element from = DOM.eventGetFromElement(ce.event);
        if (from != null && !DOM.isOrHasChild(source, from)) {
          onMouseEnter(ce);
        }
        onMouseOver(ce);
        break;
      case Event.ONMOUSEOUT:
        Element to = DOM.eventGetToElement(ce.event);
        if (!DOM.isOrHasChild(source, to)) {
          onMouseLeave(ce);
        }
        onMouseOut(ce);
        break;
      case Event.ONMOUSEDOWN:
        onMouseDown(ce);
        break;
      case Event.ONMOUSEUP:
        onMouseUp(ce);
        break;
      case Event.ONCLICK:
        onClick(ce);
        break;
      case Event.ONFOCUS:
        onFocus(ce);
        break;
      case Event.ONBLUR:
        onBlur(ce);
    }
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   * @return this
   */
  public Button removeSelectionListener(SelectionListener listener) {
    removeListener(Events.Select, listener);
    return this;
  }

  /**
   * A CSS style which sets a background image to be used as the icon.
   * 
   * @param iconStyle the CSS class name
   */
  public void setIconStyle(String iconStyle) {
    if (rendered) {
      buttonEl.replaceStyleName(this.iconStyle, iconStyle);
      el.addStyleName(text != null ? baseStyle + "-text-icon" : baseStyle + "-icon");
    }
    this.iconStyle = iconStyle;
  }

  /**
   * Sets the button's menu.
   * 
   * @param menu the menu
   */
  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  /**
   * Sets the button's tab index.
   * 
   * @param index the tab index
   */
  public void setTabIndex(int index) {
    this.tabIndex = index;
    if (rendered) {
      buttonEl.setElementAttribute("tabIndex", index);
    }
  }

  /**
   * Sets the button's text.
   * 
   * @param text the new text
   */
  public void setText(String text) {
    this.text = text;
    if (rendered) {
      el.child("td.x-btn-center " + buttonSelector).update(text);
      autoWidth();
    }
  }

  /**
   * Show this button's menu (if it has one).
   */
  public void showMenu() {
    if (menu != null) {
      menu.show(getElement(), menuAlign);
      ButtonEvent be = new ButtonEvent(this);
      be.menu = menu;
      fireEvent(Events.MenuShow, be);
    }
  }

  @Override
  protected void afterRender() {
    super.afterRender();

    if (iconStyle != null) {
      buttonEl.addStyleName(iconStyle);
      el.addStyleName(text != null ? baseStyle + "-text-icon" : baseStyle + "-icon");
    }

    if (tabIndex > -1) {
      setTabIndex(tabIndex);
    }

    if (GXT.isIE6) {
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          autoWidth();
        }
      });
    } else {
      this.autoWidth();
    }

    el.addEventsSunk(Event.FOCUSEVENTS);
  }

  protected void autoWidth() {
    if (rendered) {
      el.setWidth("auto");
      if (GXT.isIE7 && GXT.isStrict) {
        if (buttonEl.getWidth() > 20) {
          buttonEl.clip();
          TextMetrics.get().bind(buttonEl.dom);
          int w = TextMetrics.get().getWidth(text) + buttonEl.getFrameWidth("lr");
          buttonEl.setWidth(w);
        }
      }
      if (minWidth != Style.DEFAULT) {
        if (el.getWidth() < minWidth) {
          el.setWidth(minWidth);
        }
      }
    }
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    if (buttonEl != null) {
      DOM.setEventListener(getFocusEl().dom, this);
    }
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    DOM.setEventListener(getFocusEl().dom, null);
  }

  @Override
  protected El getFocusEl() {
    return buttonEl;
  }

  protected void onBlur(ComponentEvent ce) {
    removeStyleName(baseStyle + "-focus");
  }

  protected void onClick(ComponentEvent ce) {
    ce.preventDefault();
    if (!disabled) {
      ButtonEvent be = new ButtonEvent(this);
      if (!fireEvent(Events.BeforeSelect, be)) {
        return;
      }
      if (menu != null && !menu.isVisible()) {
        showMenu();
      }

      fireEvent(Events.Select, be);
    }
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    DOM.setEventListener(getFocusEl().dom, null);
  }

  @Override
  protected void onDisable() {
    if (!GXT.isIE6 || text == null) {
      addStyleName(disabledStyle);
    }
    removeStyleName(baseStyle + "-over");
    buttonEl.disable();
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    buttonEl.enable();
  }

  protected void onFocus(ComponentEvent ce) {
    if (!disabled) {
      addStyleName(baseStyle + "-focus");
    }
  }

  protected void onMenuHide(ComponentEvent ce) {
    removeStyleName(baseStyle + "-menu-active");
  }

  protected void onMenuShow(ComponentEvent ce) {
    addStyleName(baseStyle + "-menu-active");
  }

  protected void onMouseDown(ComponentEvent ce) {
    addStyleName(baseStyle + "-click");
  }

  protected void onMouseEnter(ComponentEvent ce) {

  }

  protected void onMouseLeave(ComponentEvent ce) {
    removeStyleName(baseStyle + "-click");
  }

  protected void onMouseOut(ComponentEvent ce) {
    removeStyleName(baseStyle + "-over");
    fireEvent(Events.MouseOut, ce);
  }

  protected void onMouseOver(ComponentEvent ce) {
    if (!disabled && handleMouseEvents) {
      addStyleName(baseStyle + "-over");
    }
    fireEvent(Events.MouseOver, ce);
  }

  protected void onMouseUp(ComponentEvent ce) {
    removeStyleName(baseStyle + "-click");
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    if (template == null) {
      if (buttonTemplate == null) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border=0 cellpadding=0 cellspacing=0 class='{2}-wrap'><tbody><tr>");
        sb.append("<td class={2}-left><i>&#160;</i></td><td class='x-btn-center'><em unselectable=on><button class={2}-text type={1}>{0}</button></em></td><td class={2}-right><i>&#160;</i></td>");
        sb.append("</tr></tbody></table>");
        buttonTemplate = new Template(sb.toString());
      }
      template = buttonTemplate;
    }

    setElement(template.create(text != null ? text : "", type, baseStyle), target, index);

    buttonEl = el.selectNode(buttonSelector);

    if (menu != null) {
      this.el.child("tr").addStyleName("x-btn-with-menu");
    }
    
    getFocusEl().addEventsSunk(Event.FOCUSEVENTS);

    el.addEventsSunk(Event.ONCLICK | Event.MOUSEEVENTS);
  }

}
