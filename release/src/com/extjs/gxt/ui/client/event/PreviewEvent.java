/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.google.gwt.user.client.Event;

/**
 * BaseEventPreview event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see BaseEventPreview
 */
public class PreviewEvent extends DomEvent {

  public BaseEventPreview preview;

  public PreviewEvent(BaseEventPreview preview, Event event) {
    super(event);
    this.preview = preview;
  }

}
