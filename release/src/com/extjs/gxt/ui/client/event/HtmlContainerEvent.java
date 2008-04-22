/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.google.gwt.http.client.Response;

/**
 * Html Container event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see HtmlContainer
 */
public class HtmlContainerEvent extends ComponentEvent {

  /**
   * The source container.
   */
  public HtmlContainer container;

  /**
   * The child item.
   */
  public Component item;

  /**
   * The insert index.
   */
  public int index;

  /**
   * The exception for remote requests.
   */
  public Throwable exception;

  /**
   * The remote html.
   */
  public String html;

  /**
   * The remote response.
   */
  public Response response;

  /**
   * Creates a new event.
   * 
   * @param container the source container
   */
  public HtmlContainerEvent(HtmlContainer container) {
    super(container);
    this.container = container;
  }

}
