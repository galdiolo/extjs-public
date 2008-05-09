/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tips;

import java.util.Date;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;

/**
 * A standard tooltip implementation for providing additional information when
 * hovering over a target element.
 */
public class ToolTip extends Tip {

  /**
   * Delay in milliseconds before the tooltip displays after the mouse enters
   * the target element (defaults to 500).
   */
  private int showDelay = 500;

  /**
   * An XY offset from the mouse position where the tooltip should be shown
   * (defaults to [10,10]).
   */
  private int[] mouseOffset = new int[] {10, 10};

  /**
   * True to have the tooltip follow the mouse as it moves over the target
   * element (defaults to false).
   */
  private boolean trackMouse;

  /**
   * True to automatically hide the tooltip after the mouse exits the target
   * element or after the {@link #dismissDelay} has expired if set (defaults to
   * true). If {@link #closable} = true a close tool button will be rendered
   * into the tooltip header.
   */
  private boolean autoHide = true;

  /**
   * Delay in milliseconds after the mouse exits the target element but before
   * the tooltip actually hides (defaults to 200). Set to 0 for the tooltip to
   * hide immediately.
   */
  private int hideDelay = 200;

  /**
   * Delay in milliseconds before the tooltip automatically hides (defaults to
   * 5000). To disable automatic hiding, set dismissDelay = 0.
   */
  private int dismissDelay = 5000;

  private Date lastActive;
  private Timer dismissTimer;
  private Timer showTimer;
  private Timer hideTimer;
  private Component target;
  private Point targetXY;
  private ToolTipConfig config;

  /**
   * Creates a new tool tip.
   */
  public ToolTip() {
    hidden = true;
    lastActive = new Date();
  }

  /**
   * Creates a new tool tip for the given target.
   * 
   * @param target the target widget
   */
  public ToolTip(Component target, ToolTipConfig config) {
    this();
    this.config = config;
    initTarget(target);
  }

  /**
   * Returns the dismiss delay.
   * 
   * @return the dismiss delay
   */
  public int getDismissDelay() {
    return dismissDelay;
  }

  /**
   * Returns the hide delay.
   * 
   * @return the hide delay
   */
  public int getHideDelay() {
    return hideDelay;
  }

  public void hide() {
    clearTimer("dismiss");
    lastActive = new Date();
    super.hide();
  }

  /**
   * Returns true if auto hide is enabled.
   * 
   * @return the auto hide state
   */
  public boolean isAutoHide() {
    return autoHide;
  }

  /**
   * True to automatically hide the tooltip after the mouse exits the target
   * element or after the {@link #dismissDelay} has expired if set (defaults to
   * true). If {@link #closable} = true a close tool button will be rendered
   * into the tooltip header.
   * 
   * @param autoHide the auto hide state
   */
  public void setAutoHide(boolean autoHide) {
    this.autoHide = autoHide;
  }

  /**
   * Delay in milliseconds before the tooltip automatically hides (defaults to
   * 5000). To disable automatic hiding, set dismissDelay = 0.
   * 
   * @param dismissDelay the dismiss delay in milliseconds
   */
  public void setDismissDelay(int dismissDelay) {
    this.dismissDelay = dismissDelay;
  }

  /**
   * Delay in milliseconds after the mouse exits the target element but before
   * the tooltip actually hides (defaults to 200). Set to 0 for the tooltip to
   * hide immediately.
   * 
   * @param hideDelay the hide delay
   */
  public void setHideDelay(int hideDelay) {
    this.hideDelay = hideDelay;
  }

  public void show() {
    super.show();
    showAt(getTargetXY());
  }

  public void showAt(int x, int y) {
    lastActive = new Date();
    clearTimers();
    super.showAt(x, y);
    if (getDismissDelay() != -1 && isAutoHide()) {
      dismissTimer = new Timer() {
        public void run() {
          hide();
        }
      };
      dismissTimer.schedule(getDismissDelay());
    }
  }

  /**
   * Updates the tool tip with the given config.
   * 
   * @param config the tool tip config
   * @return this
   */
  public ToolTip update(ToolTipConfig config) {
    this.config = config;
    if (!hidden) {
      updateContent();
    }
    return this;
  }

  protected void updateContent() {
    getHeader().setText(config.getTitle() == null ? "" : config.getTitle());
    if (config.getText() != null) {
      fly(getElement("body")).update(config.getText());
    }
  }

  private void clearTimer(String timer) {
    if (timer.equals("hide")) {
      if (hideTimer != null) {
        hideTimer.cancel();
        hideTimer = null;
      }
    } else if (timer.equals("dismiss")) {
      if (dismissTimer != null) {
        dismissTimer.cancel();
        dismissTimer = null;
      }
    } else if (timer.equals("show")) {
      if (showTimer != null) {
        showTimer.cancel();
        showTimer = null;
      }
    }
  }

  private void clearTimers() {
    clearTimer("show");
    clearTimer("dismiss");
    clearTimer("hide");
  }

  private void delayHide() {
    if (!hidden && hideTimer == null) {
      hideTimer = new Timer() {
        public void run() {
          hide();
        }
      };
      hideTimer.schedule(getHideDelay());
    }
  }

  private void delayShow() {
    if (hidden && showTimer == null) {
      if ((new Date().getTime() - lastActive.getTime()) < quickShowInterval) {
        show();
      } else {
        showTimer = new Timer() {
          public void run() {
            show();
          }
        };
        showTimer.schedule(getShowDelay());
      }

    } else if (!hidden && !isAutoHide()) {
      show();
    }
  }

  private Point getTargetXY() {
    int x = targetXY.x + getMouseOffset()[0];
    int y = targetXY.y + getMouseOffset()[1];
    return new Point(x, y);
  }

  private void initTarget(final Component target) {
    this.target = target;
    Listener<ComponentEvent> l = new Listener<ComponentEvent>() {

      public void handleEvent(ComponentEvent ce) {
        Element source = target.getElement();
        switch (ce.getEventType()) {
          case Event.ONMOUSEOVER:
            Element from = DOM.eventGetFromElement(ce.event);
            if (!DOM.isOrHasChild(source, from)) {
              onTargetOver(ce);
            }
            break;
          case Event.ONMOUSEOUT:
            Element to = DOM.eventGetToElement(ce.event);
            if (!DOM.isOrHasChild(source, to)) {
              onTargetOut(ce);
            }
            break;
          case Events.MouseMove:
            onMouseMove(ce);
            break;
        }
      }

    };
    target.addListener(Event.ONMOUSEOVER, l);
    target.addListener(Event.ONMOUSEOUT, l);
    target.addListener(Event.ONMOUSEMOVE, l);
  }

  private void onMouseMove(ComponentEvent ce) {
    targetXY = ce.getXY();
    if (!hidden && isTrackMouse()) {
      setPagePosition(getTargetXY());
    }
  }

  private void onTargetOut(ComponentEvent ce) {
    if (disabled) {
      return;
    }
    clearTimer("show");
    if (isAutoHide()) {
      delayHide();
    }
  }

  private void onTargetOver(ComponentEvent ce) {
    if (disabled || !ce.within(target.getElement())) {
      return;
    }
    clearTimer("hide");
    targetXY = ce.getXY();
    delayShow();
  }

  /**
   * True to have the tooltip follow the mouse as it moves over the target
   * element (defaults to false).
   * 
   * @param trackMouse the track mouse state
   */
  public void setTrackMouse(boolean trackMouse) {
    this.trackMouse = trackMouse;
  }

  /**
   * Returns true if mouse tracking is enabled.
   * 
   * @return the track mouse state
   */
  public boolean isTrackMouse() {
    return trackMouse;
  }

  /**
   * Delay in milliseconds before the tooltip displays after the mouse enters
   * the target element (defaults to 500).
   * 
   * @param showDelay the show delay in milliseconds
   */
  public void setShowDelay(int showDelay) {
    this.showDelay = showDelay;
  }

  /**
   * Returns the show delay.
   * 
   * @return the show delay
   */
  public int getShowDelay() {
    return showDelay;
  }

  /**
   * An XY offset from the mouse position where the tooltip should be shown
   * (defaults to [10,10]).
   * 
   * @param mouseOffset the mouse offsets
   */
  public void setMouseOffset(int[] mouseOffset) {
    this.mouseOffset = mouseOffset;
  }

  /**
   * Returns the mouse offsets.
   * 
   * @return the mouse offsets
   */
  public int[] getMouseOffset() {
    return mouseOffset;
  }

}
