/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.fx;

/**
 * Effects transitions, to be used with all the effects.
 * 
 * <p>
 * Credits: Code adapted from the MooTool framework, see http://mootools.net.
 * Easing Equations by Robert Penner, see http://www.robertpenner.com/easing.
 * </p>
 * 
 * @see Fx
 */
public interface Transition {

  /**
   * EaseIn transtion.
   */
  public static class EaseIn implements Transition {

    private Transition transition;

    public EaseIn(Transition transition) {
      this.transition = transition;
    }

    public double compute(double value) {
      return transition.compute(value);
    }

  }

  /**
   * EastOut transtion.
   */
  public static class EaseOut implements Transition {

    private Transition transition;

    public EaseOut(Transition transition) {
      this.transition = transition;
    }

    public double compute(double value) {
      return 1 - transition.compute(1 - value);
    }

  }

  /**
   * Computes the adjusted value.
   * 
   * @param value the current value
   * @return the adjusted value
   */
  public double compute(double value);

  /**
   * Displays a sineousidal transition.
   */
  public static Transition SINE = new Transition() {
    public double compute(double value) {
      return 1 - Math.sin((1 - value) * Math.PI / 2);
    }
  };

  /**
   * Displays a exponential transition.
   */
  public static Transition EXPO = new Transition() {
    public double compute(double value) {
      return Math.pow(2, 8 * (value - 1));
    }
  };

  /**
   * Displays a linear transition.
   */
  public static Transition LINEAR = new Transition() {

    public double compute(double value) {
      return value;
    }

  };

}
