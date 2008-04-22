/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Multiline text field. Can be used as a direct replacement for traditional
 * textarea fields.
 */
public class TextArea extends TextField {

  @Override
  protected void onRender(Element target, int index) {
    setElement(DOM.createTextArea(), target, index);
    setSize(100, 60);
    super.onRender(target, index);
  }

}
