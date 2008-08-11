/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Instances of this class are sent as a result of model changes.
 * 
 * @see ChangeEventSource
 */
public class ChangeEvent {

  /**
   * The change type.
   * 
   * @see ChangeEventSource#Add
   * @see ChangeEventSource#Remove
   * @see ChangeEventSource#Update
   */
  public int type;

  /**
   * The model that fired the event.
   */
  public Model source;

  /**
   * The item being added or removed.
   */
  public Model item;

  /**
   * The location for inserts.
   */
  public int index;

  /**
   * Creates a new instance.
   * 
   * @param type the change type
   * @param source the object that was changed
   */
  public ChangeEvent(int type, Model source) {
    this.type = type;
    this.source = source;
  }

  /**
   * Creates a new instance.
   * 
   * @param type the change type
   * @param source the object that has changed
   * @param item the item that was added or removed
   */
  public ChangeEvent(int type, Model source, Model item) {
    this(type, source);
    this.item = item;
  }

}
