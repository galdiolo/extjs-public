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

import com.google.gwt.user.client.Element;

/**
 * Excutes a set of effects.
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
public class FxStyles extends Fx {

  protected List<Effect> effects;

  /**
   * Creates a new instance.
   */
  public FxStyles() {
    effects = new ArrayList<Effect>();
  }

  /**
   * Creates and adds an effect to the queue.
   * 
   * @param elem the animation element
   * @param style the style being changed
   * @param from the start value
   * @param to the env value
   */
  public void addEffect(Element elem, String style, double from, double to) {
    effects.add(new Effect(elem, style, from, to));
  }

  /**
   * Adds an effect to the queue.
   * 
   * @param effect the effect to be added
   */
  public void addEffect(Effect effect) {
    effects.add(effect);
  }

  /**
   * Runs all effects in the queue.
   */
  public void start() {
    start(effects);
  }

}
