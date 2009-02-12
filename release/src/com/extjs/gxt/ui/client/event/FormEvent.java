/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.form.FormPanel;

public class FormEvent extends ComponentEvent {

  public FormPanel formPanel;

  public String resultHtml;

  public FormEvent(FormPanel formPanel) {
    this(formPanel, null);
  }

  public FormEvent(FormPanel formPanel, String resultHtml) {
    super(formPanel);
    this.resultHtml = resultHtml;
  }

}
