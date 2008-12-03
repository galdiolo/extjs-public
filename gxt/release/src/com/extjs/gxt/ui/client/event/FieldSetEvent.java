/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.google.gwt.user.client.Event;

public class FieldSetEvent extends ContainerEvent {

  public FieldSet fieldSet;

  public FieldSetEvent(FieldSet fieldSet) {
    super(fieldSet);
    this.fieldSet = fieldSet;
  }

  public FieldSetEvent(FieldSet fieldSet, Event event) {
    this(fieldSet);
    this.event = event;

  }

}