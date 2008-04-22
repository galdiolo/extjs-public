/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;


import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.user.client.Element;

/**
 * A {@link IconButton} that supports a set of predefined styles.
 * 
 * <dt><b>Supported Styles:</b></dt>
 * <dd>.x-tool-close</dd>
 * <dd>.x-tool-minimize</dd>
 * <dd>.x-tool-maximize</dd>
 * <dd>.x-tool-restore</dd>
 * <dd>.x-tool-gear</dd>
 * <dd>.x-tool-pin</dd>
 * <dd>.x-tool-unpin</dd>
 * <dd>.x-tool-right</dd>
 * <dd>.x-tool-left</dd>
 * <dd>.x-tool-up</dd>
 * <dd>.x-tool-down</dd>
 * <dd>.x-tool-refresh</dd>
 * <dd>.x-tool-minus</dd>
 * <dd>.x-tool-plus</dd>
 * <dd>.x-tool-search</dd>
 * <dd>.x-tool-save</dd>
 * <dd>.x-tool-help</dd>
 * </dl>
 */
public class ToolButton extends IconButton {

  /**
   * Creates a new tool button.
   * 
   * @param style the button style
   */
  public ToolButton(String style) {
    super(style);
  }

  /**
   * Creates a new tool button.
   * 
   * @param style the button style
   * @param listener the click listener
   */
  public ToolButton(String style, SelectionListener listener) {
    super(style, listener);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    addStyleName("x-tool");
    // mark element to not start drags
    addStyleName("x-nodrag");
  }

}
