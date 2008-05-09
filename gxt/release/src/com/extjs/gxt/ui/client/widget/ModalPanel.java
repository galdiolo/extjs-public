/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A panel that grays out the view port and displays a widget above it.
 */
public class ModalPanel extends BoxComponent {

  /**
   * True to blink the widget being displayed when the use clicks outside of the
   * widgets bounds (defaults to false).
   */
  public boolean blink;

  private Component component;
  private boolean blinking;
  private BaseEventPreview eventPreview;

  /**
   * Creates a new model panel.
   */
  public ModalPanel() {
    baseStyle = "x-modal";
    shim = true;
    setShadow(false);
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv());
    el.insertInto(target, index);
    el.setSize("100%", "100%");

    eventPreview = new BaseEventPreview() {

      @Override
      public boolean onPreview(PreviewEvent pe) {
        return super.onPreview(pe);
      }

      @Override
      protected boolean onAutoHide(PreviewEvent pe) {
        if (blink && !blinking) {
          blinking = true;
          component.el.blink(new Listener<FxEvent>() {
            public void handleEvent(FxEvent fe) {
              blinking = false;
            }
          });
        }
        return false;
      }

    };
  }

  /**
   * Hides the panel.
   */
  public void hide() {
    el.setZIndex(-1);
    eventPreview.remove();
    RootPanel.get().remove(this);
    RootPanel.get().remove(component);

  }

  /**
   * Fowards a event to the underlying event preview instance.
   * 
   * @param event the event
   */
  public void relayEvent(Event event) {
    eventPreview.onEventPreview(event);
  }

  public BaseEventPreview getEventPreview() {
    return eventPreview;
  }

  /**
   * Displays the panel.
   */
  public void show(Component component) {
    this.component = component;
    RootPanel.get().add(this);
    RootPanel.get().add(component);

    el.makePositionable(true);
    el.updateZIndex(0);
    component.el.updateZIndex(0);

    eventPreview.getIgnoreList().removeAll();
    eventPreview.getIgnoreList().add(component.getElement());

    eventPreview.add();
  }

}
