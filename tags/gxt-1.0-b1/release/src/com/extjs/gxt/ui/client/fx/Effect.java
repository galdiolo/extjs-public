/*
 * Copyright (c) 2006-2007 Valerio Proietti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * Contributors:
 *     Valerio Proietti - initial API and implementation
 *     GXT - derived implementation
 */
package com.extjs.gxt.ui.client.fx;


import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.Style.ScrollDir;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Effects are a single fx operation involving an element, property, and to /
 * from values.
 * 
 * <p>
 * This code is partially based on code from the MooTools Project by Valerio Proietti.
 * </p>
 * 
 * @see Fx
 */
public class Effect {

  public static class Scroll extends Effect {

    private ScrollDir dir;

    public Scroll(Element element, ScrollDir dir, int value) {
      super(element);
      this.dir = dir;
      if (dir == ScrollDir.HORIZONTAL) {
        from = el.getScrollLeft();
        to = value;
      } else if (dir == ScrollDir.VERTICAL) {
        from = el.getScrollTop();
        to = value;
      }

    }

    protected void increase(double now) {
      if (dir == ScrollDir.HORIZONTAL) {
        el.setScrollLeft((int) now);
      } else if (dir == ScrollDir.VERTICAL) {
        el.setScrollTop((int) now);
      }
    }
  }

  /**
   * A blink effect.
   */
  public static class Blink extends Effect {

    public Blink(El el) {
      super(el);
      from = 0;
      to = 20;
    }

    public void increase(double now) {
      if (to == now) {
        el.setVisibility(true);
      } else {
        el.setVisibility(!el.isVisibility());
      }
    }
  }

  /**
   * Fades an element in by adjusting its opacity from 0 to 1.
   */
  public static class FadeIn extends Effect {
    public FadeIn(Element element) {
      super(element);
      style = "opacity";
      from = 0;
      to = 1;
    }

    protected void onStart() {
      el.setStyleAttribute("opacity", 0);
      el.setVisibility(true);
    }

    protected void onComplete() {
      el.setStyleAttribute("filter", "");
    }

  }

  /**
   * Fades the element out by adjusting its opacity from 1 to 0.
   */
  public static class FadeOut extends Effect {
    public FadeOut(Element element) {
      super(element);
      style = "opacity";
      from = 1;
      to = 0;
    }

    protected void onComplete() {
      el.setVisibility(false);
      super.onComplete();
    }

  }

  /**
   * Slides an element into view.
   * 
   */
  public static class SlideIn extends Slide {

    /**
     * Creates a new slide in effect.
     * 
     * @param dir the direction of travel.
     * @param element the animation effect
     */
    public SlideIn(Direction dir, Element element) {
      super(dir, element);
    }

    public SlideIn(Direction dir, El el) {
      super(dir, el);
    }

    protected void onComplete() {
      wrapEl.unwrap(el.dom, oBounds);
      el.setStyleAttribute("overflow", overflow);
      super.onComplete();
    }

    protected void onStart() {
      overflow = el.getStyleAttribute("overflow");
      wrapEl = new El(DOM.createDiv());
      oBounds = el.wrap(wrapEl.dom);

      int h = oBounds.height;
      int w = oBounds.width;

      wrapEl.setSize(w, h);

      el.setVisible(true);
      wrapEl.setVisible(true);

      switch (dir) {
        case DOWN:
          wrapEl.setHeight(1);
          style = "height";
          from = 1;
          to = oBounds.height;
          break;
        case RIGHT:
          style = "width";
          from = 1;
          to = oBounds.width;
          break;
        case LEFT:
          wrapEl.setWidth(1);
          style = "width";
          from = 1;
          to = oBounds.width;
          break;
        case UP:
          wrapEl.setHeight(1);
          style = "height";
          from = 1;
          to = oBounds.height;
      }
    }

    protected void increase(double now) {
      int v = (int) now;
      switch (dir) {
        case LEFT:
          wrapEl.setLeft(oBounds.width - v);
          wrapEl.setStyleAttribute(style, v);
          break;
        case UP:
          wrapEl.setTop((oBounds.height - v));
          wrapEl.setStyleAttribute(style, v);
          break;
        case DOWN:
          el.setStyleAttribute("marginTop", -(oBounds.height - v));
          wrapEl.setStyleAttribute(style, v);
          break;
        case RIGHT:
          el.setStyleAttribute("marginLeft", -(oBounds.width - v));
          wrapEl.setStyleAttribute(style, v);
          break;
      }
    }

  }

  /**
   * Slides an element out of view.
   */
  public static class SlideOut extends Slide {

    /**
     * Creates a new slide effect.
     * 
     * @param dir the direction of travel
     * @param element the effect element
     */
    public SlideOut(Direction dir, Element element) {
      super(dir, element);
    }

    public SlideOut(Direction dir, El el) {
      super(dir, el);
    }

    protected void onComplete() {
      el.setVisible(false);
      wrapEl.unwrap(el.dom, oBounds);
      el.setStyleAttribute("overflow", overflow);
      super.onComplete();
    }

    protected void onStart() {
      overflow = el.getStyleAttribute("overflow");
      wrapEl = new El(DOM.createDiv());
      oBounds = el.wrap(wrapEl.dom);

      int h = oBounds.height;
      int w = oBounds.width;

      wrapEl.setSize(w, h);
      wrapEl.setVisible(true);
      el.setVisible(true);

      switch (dir) {
        case UP:
          style = "height";
          from = oBounds.height;
          to = 1;
          break;
        case LEFT:
          style = "width";
          from = oBounds.width;
          to = 0;
          break;
        case RIGHT:
          style = "left";
          from = wrapEl.getX();
          to = from + wrapEl.getWidth();
          break;

        case DOWN:
          style = "top";
          from = wrapEl.getY();
          to = from + wrapEl.getHeight();
          break;
      }
    }

  }

  private static class Slide extends Effect {

    protected Direction dir;
    protected El wrapEl;
    protected Rectangle oBounds;
    protected String overflow;

    public Slide(Direction dir, Element element) {
      this(dir, new El(element));
    }

    public Slide(Direction dir, El el) {
      super(el);
      this.dir = dir;
    }

    protected void increase(double now) {
      int v = (int) now;

      switch (dir) {
        case LEFT:
          el.setStyleAttribute("marginLeft", -(oBounds.width - v));
          wrapEl.setStyleAttribute(style, v);
          break;
        case UP:
          el.setStyleAttribute("marginTop", -(oBounds.height - v));
          wrapEl.setStyleAttribute(style, v);
          break;
        case DOWN:
          el.setY(v);
          break;
        case RIGHT:
          el.setX(v);
          break;
      }
    }
  }

  /**
   * The effect operation.
   */
  public int operation;

  /**
   * The effect element.
   */
  public El el;

  /**
   * The css style be adjusted.
   */
  public String style;

  /**
   * The start value.
   */
  public double from;

  /**
   * The end value.
   */
  public double to;

  /**
   * Creates a new effect.
   * 
   * @param elem the animation element
   */
  public Effect(Element elem) {
    this.el = new El(elem);
  }

  /**
   * Creates a new effect.
   * 
   * @param el the element
   */
  public Effect(El el) {
    this.el = el;
  }

  /**
   * Creates a new effect.
   * 
   * @param elem the effect element
   * @param style the style
   * @param from the from value
   * @param to the to value
   */
  public Effect(Element elem, String style, double from, double to) {
    this.el = new El(elem);
    this.style = style;
    this.from = from;
    this.to = to;
  }

  protected void increase(double now) {
    if (style.equalsIgnoreCase("x")) {
      el.setX((int) now);
    } else if (style.equalsIgnoreCase("y")) {
      el.setY((int) now);
    } else {
      el.setStyleAttribute(style, "" + now);
    }

  }

  /**
   * Called after the effect has been completed.
   */
  protected void onComplete() {

  }

  /**
   * Called before the effect starts.
   */
  protected void onStart() {

  }

}
