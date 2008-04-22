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
 * Provides a convenient wrapper for TextFields that adds a clickable trigger
 * button (looks like a combobox by default).
 */
public class TriggerField extends TextField {

  /**
   * The trigger style name (defaults to null).
   */
  public String triggerStyle;

  protected El wrap;
  protected El trigger;
  protected El input;
  protected EventListener triggerListener;

  @Override
  public Element getElement() {
    if (wrap == null) {
      return super.getElement();
    } else {
      return wrap.dom;
    }
  }

  @Override
  protected void alignErrorIcon() {
    errorIcon.el.alignTo(wrap.dom, "tl-tr", new int[] {2, 1});
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    DOM.setEventListener(trigger.dom, triggerListener);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    DOM.setEventListener(trigger.dom, null);
  }

  @Override
  protected El getFocusEl() {
    return input;
  }

  @Override
  protected El getInputEl() {
    return input;
  }

  @Override
  protected El getStyleEl() {
    return input;
  }

  @Override
  protected void onBlur(ComponentEvent ce) {
    super.onBlur(ce);
    wrap.removeStyleName("x-trigger-wrap-focus");
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    wrap.addStyleName("x-item-disabled");
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    wrap.removeStyleName("x-item-disabled");
  }

  @Override
  protected void onFocus(ComponentEvent ce) {
    super.onFocus(ce);
    wrap.addStyleName("x-trigger-wrap-focus");
  }

  @Override
  protected void onRender(Element target, int index) {
    if (el != null) {
      super.onRender(target, index);
      return;
    }
    input = new El(DOM.createInputText());
    wrap = new El(DOM.createDiv());
    wrap.setStyleName("x-form-field-wrap");

    trigger = new El(DOM.createImg());
    trigger.setStyleName("x-form-trigger " + triggerStyle);
    trigger.setElementAttribute("src", GXT.BLANK_IMAGE_URL);
    wrap.appendChild(input.dom);
    wrap.appendChild(trigger.dom);
    setElement(wrap.dom, target, index);

    super.onRender(target, index);

    final TriggerField ffield = this;
    triggerListener = new EventListener() {
      public void onBrowserEvent(Event event) {
        ComponentEvent ce = new ComponentEvent(ffield);
        ce.event = event;
        ce.type = DOM.eventGetType(event);
        ce.stopEvent();
        onTriggerEvent(ce);
      }
    };
    DOM.sinkEvents(wrap.dom, Event.FOCUSEVENTS);
    DOM.sinkEvents(trigger.dom, Event.ONCLICK | Event.MOUSEEVENTS);
  }

  @Override
  protected void onResize(int width, int height) {
    if (width != Style.DEFAULT) {
      getInputEl().setWidth(this.adjustWidth("input", width - trigger.getWidth()));
      wrap.setWidth(getInputEl().getWidth() + trigger.getWidth(), true);
    }
  }

  protected void onTriggerClick(ComponentEvent ce) {

  }

  protected void onTriggerEvent(ComponentEvent ce) {
    int type = ce.type;
    switch (type) {
      case Event.ONMOUSEOVER:
        trigger.addStyleName("x-form-trigger-over");
        break;
      case Event.ONMOUSEOUT:
        trigger.removeStyleName("x-form-trigger-over");
        break;
      case Event.ONCLICK:
        onTriggerClick(ce);
        break;
    }
  }

}
