/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import com.extjs.gxt.ui.client.widget.Editor;
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 * Cell based <code>Editor</code> for Grid.
 */
public class CellEditor extends Editor {

  protected int row;
  protected int col;

  /**
   * Creates a new cell editor.
   * 
   * @param field the editor's field
   */
  public CellEditor(Field field) {
    super(field);
    setAlignment("tl-tl");
    addStyleName("x-small-editor x-grid-editor");
    shim = false;
    setShadow(false);
  }

}
