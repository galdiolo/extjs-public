/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * A 2-trigger TriggerField.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>TwinTriggerClick</b> : FieldEvent(field, event)<br>
 * <div>Fires after the twin trigger is clicked.</div>
 * <ul>
 * <li>field : this</li>
 * <li>event : event</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>Field Focus</dd>
 * <dd>Field Blur</dd>
 * <dd>Field Change</dd>
 * <dd>Field Invalid</dd>
 * <dd>Field Valid</dd>
 * <dd>Field KeyPress</dd>
 * <dd>Field SpecialKey</dd>
 * <dd>TriggerField TriggerClick</dd>
 * </dl>
 */
public class TwinTriggerField extends TriggerField {

  protected El twinTrigger;

  private String twinTriggerStyle;
  private El span;

  /**
   * Returns the twin trigger style.
   * 
   * @return the twin trigger style
   */
  public String getTwinTriggerStyle() {
    return twinTriggerStyle;
  }

  /**
   * Sets the field's twin trigger style
   * 
   * @param twinTriggerStyle the twin trigger style
   */
  public void setTwinTriggerStyle(String twinTriggerStyle) {
    this.twinTriggerStyle = twinTriggerStyle;
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    DOM.setEventListener(twinTrigger.dom, triggerListener);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    DOM.setEventListener(twinTrigger.dom, null);
  }

  @Override
  protected void onRender(Element target, int index) {
    input = new El(DOM.createInputText());
    wrap = new El(DOM.createDiv());
    wrap.dom.setClassName("x-form-field-wrap");

    trigger = new El(DOM.createImg());
    trigger.dom.setClassName("x-form-trigger " + triggerStyle);
    trigger.dom.setPropertyString("src", GXT.BLANK_IMAGE_URL);

    twinTrigger = new El(DOM.createImg());
    twinTrigger.dom.setClassName("x-form-trigger " + twinTriggerStyle);
    twinTrigger.dom.setPropertyString("src", GXT.BLANK_IMAGE_URL);

    span = new El(DOM.createSpan());
    span.dom.setClassName("x-form-twin-triggers");
    DOM.appendChild(span.dom, twinTrigger.dom);
    DOM.appendChild(span.dom, trigger.dom);

    DOM.appendChild(wrap.dom, input.dom);
    DOM.appendChild(wrap.dom, span.dom);

    setElement(wrap.dom, target, index);

    if (isHideTrigger()) {
      span.setVisible(false);
    }
    
    super.onRender(target, index);

    triggerListener = new EventListener() {
      public void onBrowserEvent(Event event) {
        FieldEvent ce = new FieldEvent(TwinTriggerField.this);
        ce.event = event;
        ce.type = DOM.eventGetType(event);
        ce.stopEvent();
        onTriggerEvent(ce);
      }
    };
    DOM.sinkEvents(wrap.dom, Event.FOCUSEVENTS);
    DOM.sinkEvents(trigger.dom, Event.ONCLICK | Event.MOUSEEVENTS);
    DOM.sinkEvents(twinTrigger.dom, Event.ONCLICK | Event.MOUSEEVENTS);
    
    if (width == null) {
      setWidth(150);
    }
  }

  @Override
  protected void onResize(int width, int height) {
    if (width != Style.DEFAULT) {
      int tw = span.getWidth();
      if (!this.isHideTrigger() && tw == 0) { // need to look into why 0 is returned
        tw = 34;
      }
      getInputEl().setWidth(this.adjustWidth("input", width - tw));
      wrap.setWidth(width, true);
    }
  }

  @Override
  protected void onTriggerEvent(ComponentEvent ce) {
    El target = ce.getTargetEl();
    if (target.dom == twinTrigger.dom) {
      onTwinTriggerEvent(ce);
    } else {
      super.onTriggerEvent(ce);
    }
  }

  protected void onTwinTriggerClick(ComponentEvent ce) {
    fireEvent(Events.TwinTriggerClick, ce);
  }

  protected void onTwinTriggerEvent(ComponentEvent ce) {
    int type = ce.getEventType();
    switch (type) {
      case Event.ONMOUSEOVER:
       twinTrigger.addStyleName("x-form-trigger-over");
        break;
      case Event.ONMOUSEOUT:
        twinTrigger.removeStyleName("x-form-trigger-over");
        break;
      case Event.ONCLICK:
        onTwinTriggerClick(ce);
        break;
    }
  }

}
