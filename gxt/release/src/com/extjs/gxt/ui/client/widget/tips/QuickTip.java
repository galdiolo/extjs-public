/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tips;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A specialized tooltip class for tooltips that can be specified in markup.
 * 
 * <p /> Quicktips can be configured via tag attributes directly in markup.
 * Below is the summary of the configuration properties which can be used.
 * 
 * <ul>
 * <li>text (required)</li>
 * <li>title</li>
 * <li>width</li>
 * </ul>
 * 
 * <p>
 * To register a quick tip in markup, you simply add one or more of the valid
 * QuickTip attributes prefixed with the <b>ext:</b> namespace. The HTML element
 * itself is automatically set as the quick tip target. Here is the summary of
 * supported attributes (optional unless otherwise noted):
 * </p>
 * <ul>
 * <li><b>qtip (required)</b>: The quick tip text (equivalent to the 'text'
 * target element config).</li>
 * <li><b>qtitle</b>: The quick tip title (equivalent to the 'title' target
 * element config).</li>
 * <li><b>qwidth</b>: The quick tip width (equivalent to the 'width' target
 * element config).</li>
 * </ul>
 */
public class QuickTip extends ToolTip {

  private Element targetElem;
  private boolean interceptTitles;
  private boolean initialized;

  /**
   * Creates a new quick tip instance.
   * 
   * @param component the source component
   */
  public QuickTip(Component component) {
    super(component);
  }

  public void handleEvent(ComponentEvent ce) {
    switch (ce.type) {
      case Event.ONMOUSEOVER:
        onTargetOver(ce);
        break;
      case Event.ONMOUSEOUT:
        onTargetOut(ce);
        break;
      case Event.ONMOUSEMOVE:
        onMouseMove(ce);
        break;
      case Events.Hide:
      case Events.Detach:
        hide();
        break;
    }
  }

  /**
   * Returns true if intercept titles is enabled.
   * 
   * @return the intercept title state
   */
  public boolean isInterceptTitles() {
    return interceptTitles;
  }

  /**
   * True to automatically use the element's DOM title value if available
   * (defaults to false).
   * 
   * @param intercepTitiles true to to intercept titles
   */
  public void setInterceptTitles(boolean intercepTitiles) {
    this.interceptTitles = intercepTitiles;
  }

  @Override
  protected void onTargetOver(ComponentEvent ce) {
    if (disabled) {
      return;
    }

    Element target = ce.getTarget();
    String tip = target.getAttribute("qtip");
    String title = target.getAttribute("title");
    boolean hasTip = hasAttributeValue(tip) || (interceptTitles && hasAttributeValue(title));

    if (!initialized && !hasTip) {
      return;
    }

    initialized = true;

    if ((targetElem == null && hasTip)) {
      hide();
      updateTargetElement(target);
    } else if (targetElem != null && !ce.within(targetElem)) {
      if (!ce.within(targetElem)) {
        if (hasTip) {
          updateTargetElement(target);
        } else {
          clearTimers();
          hide();
          targetElem = null;
          text = null;
          title = null;
          return;
        }
      }

    } else if (targetElem != null && !ce.within(targetElem)) {
      return;
    }

    clearTimer("hide");
    targetXY = ce.getXY();
    delayShow();
  }

  private String getAttributeValue(Element target, String attr) {
    String v = target.getAttribute(attr);
    return v != null && v.equals("") ? null : v;

  }

  private boolean hasAttributeValue(String v) {
    return v != null && !v.equals("");
  }

  private void updateTargetElement(Element target) {
    targetElem = target;
    text = interceptTitles ? getAttributeValue(target, "title") : getAttributeValue(target, "qtip");
    title = getAttributeValue(target, "qtitle");

    String width = getAttributeValue(target, "qwidth");
    if (width != null) {
      setWidth(Integer.parseInt(width));
    }
  }

}
