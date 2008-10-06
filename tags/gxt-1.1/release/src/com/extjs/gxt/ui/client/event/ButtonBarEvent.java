/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;

/**
 * ButtonBar event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see ButtonBar
 */
public class ButtonBarEvent extends ContainerEvent<ButtonBar, Button> {

  /**
   * Creates a new event.
   * 
   * @param buttonBar the source button bar
   */
  public ButtonBarEvent(ButtonBar buttonBar, Button item) {
    super(buttonBar, item);
  }

}
