/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.form.Field;

/**
 * Field event type.
 * 
 * @see Field
 */
public class FieldEvent extends ComponentEvent {

  public Field field;

  public Object oldValue;

  public Object value;

  public String message;
  
  public boolean checked;

  public FieldEvent(Field field) {
    super(field);
    this.field = field;
  }

}
