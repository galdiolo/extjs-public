/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.button;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A split button that provides a built-in dropdown arrow that can fire an event
 * separately from the default click event of the button.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>ArrowClick</b> : ButtonEvent(button, item, event)<br>
 * <div>Fires when this button's arrow is clicked.</div>
 * <ul>
 * <li>button : this</li>
 * <li>item : the menu</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * </dt>
 */
public class SplitButton extends Button {

  protected String arrowSelector = "button:last";

  private Element arrowBtnTable;

  /**
   * Creates a new split button.
   */
  public SplitButton() {
    super();
  }

  /**
   * Creates a new split button.
   * 
   * @param text the button's text
   * @param listener the selection listener
   */
  public SplitButton(String text, SelectionListener listener) {
    super(text, listener);
  }

  /**
   * Creates a new split button.
   * 
   * @param text the button's text
   */
  public SplitButton(String text) {
    super(text);
  }

  @Override
  protected void onRender(Element target, int index) {
    if (template == null) {
      StringBuffer sb = new StringBuffer();
      sb.append("<table cellspacing=0 class='x-btn-menu-wrap x-btn'><tr><td>");
      sb.append("<table cellspacing=0 class='x-btn-wrap x-btn-menu-text-wrap'><tbody>");
      sb.append("<tr><td class=x-btn-left><i>&#160;</i></td><td class=x-btn-center><button class=x-btn-text type={1}>{0}</button></td></tr>");
      sb.append("</tbody></table></td><td>");
      sb.append("<table cellspacing=0 class='x-btn-wrap x-btn-menu-arrow-wrap'><tbody>");
      sb.append("<tr><td class=x-btn-center><button class=x-btn-menu-arrow-el type=button>&#160;</button></td><td class=x-btn-right><i>&#160;</i></td></tr>");
      sb.append("</tbody></table></td></tr></table>");
      template = new Template(sb.toString());
    }

    setElement(template.create(text, getType(), baseStyle), target, index);

    buttonEl = el().selectNode(buttonSelector);
    arrowBtnTable = el().child(arrowSelector).dom;

    el().addEventsSunk(Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS);
  }

  @Override
  protected void onClick(ComponentEvent ce) {
    ce.preventDefault();
    hideToolTip();
    if (!disabled) {
      ButtonEvent be = new ButtonEvent(this);
      be.event = ce.event;
      if (ce.getTarget(".x-btn-menu-arrow-wrap", 5) != null) {
        if (menu != null && !menu.isVisible()) {
          showMenu();
        }
        be.menu = menu;
        fireEvent(Events.ArrowClick, be);
      } else {
        fireEvent(Events.Select, be);
      }
    }
  }

  protected void autoWidth() {
    if (rendered) {
      El tbl = el().child("table:first");
      Element tbl2 = el().child("table:last").dom;
      el().setWidth("auto");
      tbl.setWidth("auto");

      if (getMinWidth() != Style.DEFAULT) {
        if ((tbl.getWidth() + fly(tbl2).getWidth()) < getMinWidth()) {
          tbl.setWidth(getMinWidth() - fly(tbl2).getWidth());
        }
      }
      el().setWidth(tbl.getWidth() + fly(tbl2).getWidth());
    }
  }

  @Override
  protected void onDisable() {
    if (rendered) {
      if (!GXT.isIE6) {
        el().addStyleName(disabledStyle);
      }
      el().child(buttonSelector).disable();
      el().child(arrowSelector).disable();
    }
    disabled = true;
  }

  @Override
  protected void onEnable() {
    if (rendered) {
      if (!GXT.isIE6) {
        el().removeStyleName(disabledStyle);
      }
      el().child(buttonSelector).enable();
      el().child(arrowSelector).enable();
    }
    disabled = true;
  }

  protected boolean isMenuTriggerOver(ComponentEvent ce) {
    if (menu != null) {
      if (ce.within(arrowBtnTable)) {
        return true;
      }
    }
    return false;
  }

  protected boolean isMenuTriggerOut(ComponentEvent ce) {
    return menu != null && ce.within(arrowBtnTable);
  }

}
