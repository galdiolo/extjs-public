/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;

public class MessageBoxEvent extends WindowEvent {

  /**
   * The source message box.
   */
  public MessageBox messageBox;
  /**
   * The dialog intance.
   */
  public Dialog dialog;

  /**
   * The field value. Only applies to prompt and multi-prompt message boxes.
   */
  public String value;

  public MessageBoxEvent(MessageBox messageBox, Dialog window, Button buttonClicked) {
    super(window, buttonClicked);
    this.messageBox = messageBox;
    this.dialog = window;
  }

}
