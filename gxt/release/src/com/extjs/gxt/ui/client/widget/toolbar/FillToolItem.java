/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.toolbar;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class FillToolItem extends ToolItem {
  
  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv());
    el.insertInto(target, index);
    setWidth("100%");
  }

  @Override
  protected void doAttachChildren() {
    // do nothing
  }

  @Override
  protected void doDetachChildren() {
    // do nothing
  }
}
