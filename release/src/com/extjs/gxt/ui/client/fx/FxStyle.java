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


import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.Style.ScrollDir;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.google.gwt.user.client.Element;

/**
 * Effect for changing a single css style property for a given element.
 * 
 * <p>
 * This code is based on code from the MooTools Project by Valerio Proietti.
 * </p>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>EffectStart</b> : FxEvent(fx)<br>
 * <div>Fires after an effect is started.</div>
 * <ul>
 * <li>fx : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>EffecCancel</b> : FxEvent(fx)<br>
 * <div>Fires after an effect has been cancelled.</div>
 * <ul>
 * <li>fx : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>EffecComplete</b> : FxEvent(fx)<br>
 * <div>Fires after an effect has been completed</div>
 * <ul>
 * <li>fx : this</li>
 * </ul>
 * </dd>
 * </dl>
 * <dl>
 */
public class FxStyle extends Fx {

  protected String style;
  protected El elem;

  /**
   * Creates a new instance.
   * 
   * @param element the animation element
   */
  public FxStyle(Element element) {
    this.elem = new El(element);
  }
  
  /**
   * Creates a new instance.
   * 
   * @param el the element
   */
  public FxStyle(El el) {
    this.elem = el;
  }
  
  public void scroll(ScrollDir dir, int value) {
    if (running) return;
    duration = 150;
    start(new Effect.Scroll(elem.dom, dir, value));
  }

  /**
   * Blinks the element.
   */
  public void blink() {
    if (running) return;
    fps = 20;
    start(new Effect.Blink(elem));
  }

  /**
   * Fades the element in.
   */
  public void fadeIn() {
    if (running) return;
    Effect e = new Effect.FadeIn(elem.dom);
    start(e);
  }

  /**
   * Changes the size of the element.
   * 
   * @param width the new width
   * @param height the new height
   */
  public void size(int width, int height) {
    if (running) return;
    ArrayList<Effect> effects = new ArrayList<Effect>();
    effects.add(new Effect(elem.dom, "width", elem.getWidth(), width));
    effects.add(new Effect(elem.dom, "height", elem.getHeight(), height));
    start(effects);
  }

  /**
   * Fades out the element.
   */
  public void fadeOut() {
    if (running) return;
    Effect e = new Effect.FadeOut(elem.dom);
    start(e);
  }

  /**
   * Fades the element in or out.
   */
  public void fadeToggle() {
    if (running) return;
    if (elem.isVisibility()) {
      fadeOut();
    } else {
      fadeIn();
    }
  }

  /**
   * Moves a element to a new location.
   * 
   * @param x the end x coordinate
   * @param y the end y coordinate
   */
  public void move(int x, int y) {
    if (running) return;
    Rectangle r = elem.getBounds();
    List<Effect> effects = new ArrayList<Effect>();
    effects.add(new Effect(elem.dom, "x", r.x, x));
    effects.add(new Effect(elem.dom, "y", r.y, y));
    start(effects);
  }

  /**
   * Moves and sizes the element.
   * 
   * @param x the new x coordinate
   * @param y the new y coordinate
   * @param width the new width
   * @param height the new height
   */
  public void zoom(int x, int y, int width, int height) {
    if (running) return;
    List<Effect> effects = new ArrayList<Effect>();
    effects.add(new Effect(elem.dom, "left", elem.getX(), x));
    effects.add(new Effect(elem.dom, "top", elem.getY(), y));
    effects.add(new Effect(elem.dom, "width", elem.getWidth(), width));
    effects.add(new Effect(elem.dom, "height", elem.getHeight(), height));
    start(effects);
  }

  /**
   * Slides the element in.
   * 
   * @param dir the direction
   */
  public void slideIn(Direction dir) {
    if (running) return;
    start(new Effect.SlideIn(dir, elem));
  }
  
  /**
   * Slides the element in.
   * 
   * @param dir the direction
   */
  public void slideIn(Direction dir, int duration, Listener callback) {
    if (running) return;
    this.duration = duration;
    addListener(Events.EffectComplete, callback);
    start(new Effect.SlideIn(dir, elem.dom));
  }

  /**
   * Slides the element out.
   * 
   * @param dir the direction
   */
  public void slideOut(Direction dir) {
    if (running) return;
    start(new Effect.SlideOut(dir, elem));
  }
  
  /**
   * Slides the element out.
   * 
   * @param dir the direction
   */
  public void slideOut(Direction dir, int duration, Listener callback) {
    if (running) return;
    this.duration = duration;
    addListener(Events.EffectComplete, callback);
    start(new Effect.SlideOut(dir, elem.dom));
  }

  /**
   * Creates and starts a new effect.
   * 
   * @param style the css style being modified
   * @param from the start value
   * @param to the end value
   */
  public void start(String style, double from, double to) {
    Effect e = new Effect(elem.dom, style, from, to);
    start(e);
  }

}
