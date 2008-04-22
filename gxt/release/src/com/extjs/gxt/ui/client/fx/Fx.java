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
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.EffectListener;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.util.Observable;
import com.google.gwt.user.client.Timer;

/**
 * Base class for animation effects.
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
 * 
 * This class is partially based on the Fx package in the MooTools framework, see
 * http://mootools.net.
 * </p>
 */
public class Fx extends Observable {

  /**
   * The duration of the effect in ms. Default value is 500.
   */
  public int duration = 500;

  /**
   * The frames per second for the transition. Default is 50.
   */
  public int fps = 50;

  /**
   * To wait or not to wait for a current transition to end before running
   * another of the same instance. Default value is <code>true</code>.
   */
  public boolean wait = true;

  /**
   * The effect transition. Default values is Transition.LINEAR;
   */
  public Transition transition = Transition.LINEAR;

  /**
   * <code>true</code> to hide the element after the effect using the css
   * 'display' attribute.
   */
  public boolean hideOnComplete = false;

  protected List<Effect> effects;
  protected Timer timer;
  protected boolean running;

  private long time;
  private double delta;

  /**
   * Adds a listener to be notified of effect events.
   * 
   * @param listener the listener to be added
   */
  public void addEffectListener(EffectListener listener) {
    TypedListener tl = new TypedListener(listener);
    addListener(Events.EffectStart, tl);
    addListener(Events.EffectCancel, tl);
    addListener(Events.EffectComplete, tl);
  }

  /**
   * Cancels the animation.
   */
  public void cancel() {
    if (!running) return;
    timer.cancel();
    timer = null;
    running = false;
    FxEvent fe = new FxEvent(this);
    fireEvent(Events.EffectCancel, fe);
  }

  /**
   * Returns the current effects.
   * 
   * @return the effect array
   */
  public List<Effect> getEffects() {
    return effects;
  }

  /**
   * Returns <code>true</code> if any effects are running.
   * 
   * @return the running state
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Removes a previously added effect listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeEffectListener(EffectListener listener) {
    if (eventTable == null) return;
    eventTable.unhook(Events.EffectStart, listener);
    eventTable.unhook(Events.EffectCancel, listener);
    eventTable.unhook(Events.EffectComplete, listener);
  }

  /**
   * Runs the effect.
   * 
   * @param effect the effect to be run
   */
  public void start(Effect effect) {
    List<Effect> effects = new ArrayList<Effect>();
    effects.add(effect);
    start(effects);
  }

  /**
   * Runs the list of effects.
   * 
   * @param effects the effects to be run
   */
  public void start(List<Effect> effects) {
    if (!wait) {
      stop();
    } else if (running) {
      return;
    }
    running = true;
    this.effects = effects;
    this.time = new Date().getTime();

    for (Effect effect : effects) {
      effect.onStart();
    }

    timer = new Timer() {
      public void run() {
        step();
      }
    };
    timer.scheduleRepeating(Math.round(1000 / fps));

    FxEvent fx = new FxEvent(this);
    fireEvent(Events.EffectStart, fx);
  }

  /**
   * Stops the animation.
   */
  public void stop() {
    if (!running) return;
    timer.cancel();
    timer = null;
    running = false;
    for (Effect effect : effects) {
      effect.increase(effect.to);
      effect.onComplete();
    }
    FxEvent fe = new FxEvent(this);
    fireEvent(Events.EffectComplete, fe);
  }

  protected double compute(double from, double to) {
    return (to - from) * delta + from;
  }

  protected double getNow(Effect effect) {
    return compute(effect.from, effect.to);
  }

  protected void onComplete() {

  }

  protected void onStart() {

  }

  protected void step() {
    long time = new Date().getTime();
    if (time < this.time + duration) {
      double diff = time - this.time;
      delta = transition.compute(diff / duration);
      for (Effect effect : effects) {
        effect.increase(getNow(effect));
      }
    } else {
      stop();
      onComplete();
    }
  }

}
