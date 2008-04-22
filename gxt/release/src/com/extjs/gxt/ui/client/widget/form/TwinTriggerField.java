/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * A 2-trigger TriggerField.
 */
public class TwinTriggerField extends TriggerField {

  /**
   * The field's twin trigger style (defaults to null).
   */
  public String twinTriggerStyle;

  protected Element twinTrigger;
  private Element span;

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    DOM.setEventListener(twinTrigger, triggerListener);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    DOM.setEventListener(twinTrigger, null);
  }

  @Override
  protected void onRender(Element target, int index) {
    input = new El(DOM.createInputText());
    wrap = new El(DOM.createDiv());
    wrap.setStyleName("x-form-field-wrap");

    trigger = new El(DOM.createImg());
    trigger.setStyleName("x-form-trigger");
    DOM.setElementProperty(trigger.dom, "src", GXT.BLANK_IMAGE_URL);

    twinTrigger = DOM.createImg();
    fly(twinTrigger).setStyleName("x-form-trigger " + twinTriggerStyle);
    DOM.setElementProperty(twinTrigger, "src", GXT.BLANK_IMAGE_URL);

    span = DOM.createSpan();
    fly(span).setStyleName("x-form-twin-triggers");
    DOM.appendChild(span, twinTrigger);
    DOM.appendChild(span, trigger.dom);

    DOM.appendChild(wrap.dom, input.dom);
    DOM.appendChild(wrap.dom, span);

    setElement(wrap.dom, target, index);

    super.onRender(target, index);

    triggerListener = new EventListener() {
      public void onBrowserEvent(Event event) {
        ComponentEvent ce = new ComponentEvent(TwinTriggerField.this);
        ce.event = event;
        ce.type = DOM.eventGetType(event);
        ce.stopEvent();
        onTriggerEvent(ce);
      }
    };
    DOM.sinkEvents(wrap.dom, Event.FOCUSEVENTS);
    DOM.sinkEvents(trigger.dom, Event.ONCLICK | Event.MOUSEEVENTS);
    DOM.sinkEvents(twinTrigger, Event.ONCLICK | Event.MOUSEEVENTS);
  }

  @Override
  protected void onResize(int width, int height) {
    if (width != Style.DEFAULT) {
      int tw = fly(span).getWidth();
      input.setWidth(adjustWidth("input", width - tw), true);
    }
  }

  @Override
  protected void onTriggerEvent(ComponentEvent ce) {
    El target = ce.getTargetEl();
    if (target.dom == twinTrigger) {
      onTwinTriggerEvent(ce);
    } else {
      super.onTriggerEvent(ce);
    }
  }

  protected void onTwinTriggerClick(ComponentEvent ce) {

  }

  protected void onTwinTriggerEvent(ComponentEvent ce) {
    int type = ce.getEventType();
    switch (type) {
      case Event.ONMOUSEOVER:
        fly(twinTrigger).addStyleName("x-form-trigger-over");
        break;
      case Event.ONMOUSEOUT:
        fly(twinTrigger).removeStyleName("x-form-trigger-over");
        break;
      case Event.ONCLICK:
        onTwinTriggerClick(ce);
        break;
    }
  }

}
