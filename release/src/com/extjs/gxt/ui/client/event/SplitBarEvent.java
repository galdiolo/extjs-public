/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.SplitBar;

/**
 * SplitBar event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see SplitBar
 */
public class SplitBarEvent extends ComponentEvent {

  /**
   * The source split bar.
   */
  public SplitBar splitBar;

  /**
   * The drag event.
   */
  public DragEvent dragEvent;

  /**
   * The current size.
   */
  public int size;

  public SplitBarEvent(SplitBar splitBar) {
    super(splitBar);
    this.splitBar = splitBar;
  }

}
