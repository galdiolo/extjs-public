/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.user.client.Event;

/**
 * Field event type.
 * 
 * @see Field
 */
public class FieldEvent extends BoxComponentEvent {

  /**
   * The source field.
   */
  public Field field;

  /**
   * The old value.
   */
  public Object oldValue;

  /**
   * The new value.
   */
  public Object value;

  /**
   * The message.
   */
  public String message;

  public FieldEvent(Field field) {
    super(field);
    this.field = field;
  }
  
  public FieldEvent(Field field, Event event) {
    this(field);
    this.event = event;
  }

}
