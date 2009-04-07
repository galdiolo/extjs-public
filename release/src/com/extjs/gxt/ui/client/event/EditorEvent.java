/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Editor;

public class EditorEvent extends DomEvent {

  /**
   * The source editor.
   */
  public Editor editor;

  /**
   * The underlying element bound to the editor.
   */
  public El boundEl;

  /**
   * The current field value.
   */
  public Object value;

  /**
   * The start value.
   */
  public Object startValue;

  public EditorEvent(Editor editor) {
    this.editor = editor;
  }

}
