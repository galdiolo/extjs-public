/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;


import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.CompositeElement;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;

/**
 * Specialized <code>EventPreview</code>.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Add</b> : PreviewEvent(preview, event, target)<br>
 * <div>Fires after event preview is added.</div>
 * <ul>
 * <li>preview : this</li>
 * <li>event : the dom event</li>
 * <li>target : the target element</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : PreviewEvent(preview)<br>
 * <div>Fires after event preview has been remevoed.</div>
 * <ul>
 * <li>preview : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>KeyPress</b> : PreviewEvent(preview, target, event)<br>
 * <div>Fires when a key is pressed while event preview is active.</div>
 * <ul>
 * <li>preview : this</li>
 * <li>target : the target element</li>
 * <li>event : event</li>
 * </ul>
 * </dd>
 * </dt>
 */
public class BaseEventPreview extends Observable implements EventPreview {

  /**
   * True to remove the event preview when the user clicks on an element not it
   * the ignore list (default to true).
   */
  public boolean autoHide = true;

  /**
   * List of elements to be ignored when autoHide is enabled.
   */
  public CompositeElement ignoreList = new CompositeElement();

  public boolean onEventPreview(Event event) {
    PreviewEvent be = new PreviewEvent(this, event);
    be.type = DOM.eventGetType(event);
    be.event = event;
    if (autoHide && onAutoHidePreview(be)) {
      remove();
    }
    return onPreview(be);
  }

  /**
   * Called when a preview event is received.
   * 
   * @param pe the component event
   * @return true to allow the event
   */
  public boolean onPreview(PreviewEvent pe) {
    switch (pe.type) {
      case Event.ONKEYPRESS:
        onPreviewKeyPress(pe);
        break;
      case Event.ONCLICK:
        onClick(pe);
    }
    return true;
  }
  
  /**
   * Adds this instance to the event preview stack.
   */
  public void add() {
    DOM.addEventPreview(this);
    onAdd();
    fireEvent(Events.Add);
  }

  /**
   * Removes event preview.
   */
  public void remove() {
    DOM.removeEventPreview(this);
    onRemove();
    fireEvent(Events.Remove);
  }

  /**
   * Called right before event preview will be removed from auto hide.
   * 
   * @param ce the component event
   * @return true to allow auto hide, false to cancel
   */
  protected boolean onAutoHide(PreviewEvent ce) {
    return true;
  }
  
  /**
   * Called when a preview event is received and {@link #autoHide} is enabled.
   * 
   * @param ce the component event
   * @return true to remove event preview
   */
  protected boolean onAutoHidePreview(PreviewEvent ce) {
    switch (ce.type) {
      case Event.ONMOUSEDOWN:
      case Event.ONMOUSEUP:
      case Event.ONCLICK:
      case Event.ONDBLCLICK: {
        boolean autoHide = !ignoreList.is(ce.getTarget());
        if (autoHide && onAutoHide(ce)) {
          remove();
        }
      }
    }
    return false;
  }
  
  protected void onClick(PreviewEvent pe) {
    
  }

  protected void onPreviewKeyPress(PreviewEvent pe) {
    fireEvent(Events.KeyPress, pe);
  }
  
  protected void onAdd() {
    
  }

  protected void onRemove() {
    
  }

}
