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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * Provides a convenient wrapper for TextFields that adds a clickable trigger
 * button (looks like a combobox by default).
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>TriggerClick</b> : FieldEvent(field, event)<br>
 * <div>Fires after the trigger is clicked.</div>
 * <ul>
 * <li>field : this</li>
 * <li>event : event</li>
 * </ul>
 * </dd>
 */
public class TriggerField<Data> extends TextField<Data> {

  protected El wrap;
  protected El trigger;
  protected El input;
  protected EventListener triggerListener;
  protected String triggerStyle = "x-form-trigger-arrow";

  private boolean hideTrigger;

  /**
   * Returns true if the trigger is hidden.
   * 
   * @return the hide trigger state
   */
  public boolean isHideTrigger() {
    return hideTrigger;
  }

  /**
   * True to hide the trigger (defaults to false, pre-render).
   * 
   * @param hideTrigger true to hide the trigger
   */
  public void setHideTrigger(boolean hideTrigger) {
    this.hideTrigger = hideTrigger;
  }

  @Override
  public Element getElement() {
    if (wrap == null) {
      return super.getElement();
    } else {
      return wrap.dom;
    }
  }

  /**
   * Returns the trigger style.
   * 
   * @return the trigger style
   */
  public String getTriggerStyle() {
    return triggerStyle;
  }

  /**
   * Sets the trigger style name.
   * 
   * @param triggerStyle
   */
  public void setTriggerStyle(String triggerStyle) {
    this.triggerStyle = triggerStyle;
  }

  @Override
  protected void alignErrorIcon() {
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        errorIcon.el().alignTo(wrap.dom, "tl-tr", new int[] {1, 1});
      }
    });
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
    if (el() != null) {
      super.onRender(target, index);
      return;
    }
    input = new El(DOM.createInputText());
    wrap = new El(DOM.createDiv());
    wrap.dom.setClassName("x-form-field-wrap");

    input.addStyleName(fieldStyle);

    trigger = new El(DOM.createImg());
    trigger.dom.setClassName("x-form-trigger " + triggerStyle);
    trigger.dom.setPropertyString("src", GXT.BLANK_IMAGE_URL);
    wrap.dom.appendChild(input.dom);
    wrap.dom.appendChild(trigger.dom);
    setElement(wrap.dom, target, index);

    if (hideTrigger) {
      trigger.setVisible(false);
    }

    super.onRender(target, index);

    triggerListener = new EventListener() {
      public void onBrowserEvent(Event event) {
        FieldEvent ce = new FieldEvent(TriggerField.this);
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
      int tw = trigger.getWidth();
      if (!hideTrigger && tw == 0) { // need to look into why 0 is returned
        tw = 17;
      }
      getInputEl().setWidth(this.adjustWidth("input", width - tw));
      wrap.setWidth(getInputEl().getWidth() + trigger.getWidth(), true);
    }
  }

  protected void onTriggerClick(ComponentEvent ce) {
    fireEvent(Events.TriggerClick, ce);
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
