/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.button;


import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A simple css styled button with 3 states: normal, over, and disabled.
 * 
 * <p>
 * Note: To change the icon style after construction use
 * {@link #changeStyle(String)}.
 * </p>
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Select</b> : (widget, event)<br>
 * <div>Fires after the item is selected.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 */
public class IconButton extends BoxComponent {

  protected String style;
  protected boolean cancelBubble = true;

  /**
   * Creates a new icon button. When using the default constructor,
   * {@link #changeStyle(String)} must be called to initialize the button.
   */
  public IconButton() {
    this("");
  }

  /**
   * Creates a new icon button. The 'over' style and 'disabled' style names
   * determined by adding '-over' and '-disabled' to the base style name.
   * 
   * @param style the base style
   */
  public IconButton(String style) {
    this.style = style;
  }

  /**
   * Creates a new icon button. The 'over' style and 'disabled' style names
   * determined by adding '-over' and '-disabled' to the base style name.
   * 
   * @param style the base style
   * @param listener the click listener
   */
  public IconButton(String style, SelectionListener listener) {
    this(style);
    addSelectionListener(listener);
  }


  /**
   * @param listener
   */
  public void addSelectionListener(SelectionListener listener) {
    addListener(Events.Select, listener);
  }

  /**
   * Changes the icon style.
   * 
   * @param style the new icon style
   */
  public void changeStyle(String style) {
    removeStyleName(this.style);
    removeStyleName(this.style + "-over");
    removeStyleName(this.style + "-disabled");
    addStyleName(style);
    this.style = style;
  }

  public void onComponentEvent(ComponentEvent ce) {
    switch (ce.type) {
      case Event.ONMOUSEOVER:
        addStyleName(style + "-over");
        break;
      case Event.ONMOUSEOUT:
        removeStyleName(style + "-over");
        break;
      case Event.ONCLICK:
        onClick(ce);
        break;
    }
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeSelectionListener(SelectionListener listener) {
    removeListener(Events.Select, listener);
  }

  protected void onClick(ComponentEvent ce) {
    if (cancelBubble) {
      ce.cancelBubble();
    }
    removeStyleName(style + "-over");
    fireEvent(Events.Select, ce);
  }

  protected void onDisable() {
    addStyleName(style + "-disabled");
  }

  protected void onEnable() {
    removeStyleName(style + "-disabled");
  }

  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);
    addStyleName("x-icon-btn");
    addStyleName("x-nodrag");
    addStyleName(style);
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
    super.onRender(target, index);
  }

}
