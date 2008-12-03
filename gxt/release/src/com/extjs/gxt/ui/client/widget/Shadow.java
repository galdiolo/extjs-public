/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.Stack;

import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Markup;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A transparent shadow.
 */
public class Shadow extends BoxComponent {

  private static Stack shadowStack = new Stack();

  /**
   * Returns a Shadow from the stack.
   * 
   * @return the shadow
   */
  public static Shadow pop() {
    Shadow shadow = shadowStack.size() > 0 ? (Shadow) shadowStack.pop() : null;
    if (shadow == null) {
      shadow = new Shadow(ShadowPosition.SIDES);
    }
    return shadow;
  }

  /**
   * Pushes a shadow back onto the stack.
   * 
   * @param shadow the shadow
   */
  public static void push(Shadow shadow) {
    shadowStack.push(shadow);
  }

  public enum ShadowPosition {
    DROP, SIDES, FRAME;
  }

  private ShadowPosition shadowPosition;
  private Element target;
  public Rectangle adjusts;
  private int offset = 4;

  /**
   * Creates a new shadow widget.
   * 
   * @param position the shadow position
   */
  public Shadow(ShadowPosition position) {
    this.shadowPosition = position;
  }

  /**
   * Removes the shadow.
   */
  public void remove() {
    el().removeFromParent();
  }

  public void show(Element target) {
    this.target = target;
    if (!rendered) {
      render(XDOM.getBody());
    }
    el().insertBefore(target);
    el().setVisible(true);
    sync(fly(target).getBounds());
  }

  @Override
  protected void onHide() {
    super.onHide();
    el().removeFromParent();
  }

  public void show(Widget widget) {
    show(widget.getElement());
  }

  public void sync(int l, int t, int width, int height) {
    if (target == null) return;
    el().setLeft(l + adjusts.x);
    el().setTop(t + adjusts.y);

    int sw = width + adjusts.width;
    int sh = height + adjusts.height;
    if (getWidth() != sw || getOffsetHeight() != sh) {
      el().setSize(sw, sh);
      if (!GXT.isIE) {
        int w = Math.max(0, sw - 12);
        fly(getChild(0, 1)).setWidth(w);
        fly(getChild(1, 1)).setWidth(w);
        fly(getChild(2, 1)).setWidth(w);
        int h = Math.max(0, sh - 12);
        Element middle = DOM.getChild(getElement(), 1);
        fly(middle).setHeight(h);
      }
    }
  }

  public void sync(Rectangle rect) {
    sync(rect.x, rect.y, rect.width, rect.height);
  }

  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    if (GXT.isIE) {
      setElement(DOM.createDiv());
      setStyleName("x-ie-shadow");
    } else {
      setElement(XDOM.create(Markup.SHADOW));
    }

    el().enableDisplayMode("block");

    DOM.appendChild(parent, getElement());

    if (GXT.isIE) {
      setStyleAttribute("filter", "progid:DXImageTransform.Microsoft.alpha("
          + "opacity=50) progid:DXImageTransform.Microsoft.Blur(pixelradius=" + offset + ")");
    }

    adjusts = new Rectangle();
    int radius = offset / 2;

    switch (shadowPosition) {
      case SIDES:
        adjusts.width = offset * 2;
        adjusts.x = -offset;
        adjusts.y = offset - 1;
        if (GXT.isIE) {
          adjusts.x -= (offset - radius);
          adjusts.y -= (offset + radius);
          adjusts.x += 1;
          adjusts.width -= (offset - radius) * 2;
          adjusts.width -= radius + 1;
          adjusts.height -= 1;
        }
        break;
      case FRAME:
        adjusts.width = adjusts.height = (offset * 2);
        adjusts.x = adjusts.y = -offset;
        adjusts.y += 1;
        adjusts.height -= 2;
        if (GXT.isIE) {
          adjusts.x -= (offset - radius);
          adjusts.y -= (offset - radius);
          adjusts.width -= (offset + radius);
          adjusts.width += 1;
          adjusts.height -= (offset + radius);
          adjusts.height += 3;
        }
        break;
      default:
        adjusts.width = 0;
        adjusts.x = adjusts.y = offset;
        adjusts.y -= 1;
        if (GXT.isIE) {
          adjusts.x -= offset + radius;
          adjusts.y -= offset + radius;
          adjusts.width -= radius;
          adjusts.height -= radius;
          adjusts.y += 1;
        }
        break;
    }
  }

  private Element getChild(int index, int subindex) {
    Element e = DOM.getChild(getElement(), index);
    return DOM.getChild(e, subindex);
  }
}
