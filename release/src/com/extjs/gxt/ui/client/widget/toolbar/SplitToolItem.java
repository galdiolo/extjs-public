/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.toolbar;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.button.SplitButton;

/**
 * A split button tool item.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>ArrowClick</b> : ToolBarEvent(container, item, event)<br>
 * <div>Fires when this button's arrow is clicked.</div>
 * <ul>
 * <li>container : the parent toolbar</li>
 * <li>item : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd> </dt>
 */
public class SplitToolItem extends TextToolItem {

  protected SplitButton splitButton;

  /**
   * Creates a new split button item.
   */
  public SplitToolItem() {
    splitButton = new SplitButton();
    initComponent();
  }

  /**
   * Creates a new split tool item.
   * 
   * @param text the item's text
   */
  public SplitToolItem(String text) {
    splitButton = new SplitButton(text);
    initComponent();
  }

  protected void initComponent() {
    button = splitButton;
    splitButton.addListener(Events.ArrowClick, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        onArrowClick(ce);
      }
    });
  }

  protected void onArrowClick(ComponentEvent ce) {
    ToolBarEvent evt = new ToolBarEvent(toolBar, this);
    evt.event = ce.event;
    fireEvent(Events.ArrowClick, evt);
  }

}
