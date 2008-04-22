/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tips;

import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.util.Params;

public class ToolTipConfig {

  /**
   * The optional tool tip title (defaults to null).
   */
  public String title;

  /**
   * The tool tip text;
   */
  public String text;

  /**
   * True to automatically hide the tooltip after the mouse exits the target
   * element or after the {@link #dismissDelay} has expired if set (defaults to
   * true).
   */
  public boolean autoHide = true;

  /**
   * Delay in milliseconds before the tooltip displays after the mouse enters
   * the target element (defaults to 500).
   */
  public int showDelay = 500;

  /**
   * Delay in milliseconds after the mouse exits the target element but before
   * the tooltip actually hides (defaults to 200). Set to 0 for the tooltip to
   * hide immediately.
   */
  public int hideDelay = 200;

  /**
   * Delay in milliseconds before the tooltip automatically hides (defaults to
   * 5000). To disable automatic hiding, set dismissDelay = 0.
   */
  public int dismissDelay = 5000;

  /**
   * An XY offset from the mouse position where the tooltip should be shown
   * (defaults to [15,18]).
   */
  public int[] mouseOffset = new int[] {15, 18};

  /**
   * True to have the tooltip follow the mouse as it moves over the target
   * element (defaults to false).
   */
  public boolean trackMouse;

  /**
   * A optional template to be used to render the tool tip. The {@link #params}
   * will be applied to the template. If specified, {@link #title} and
   * {@link #text} will be added to the params before being applied to the
   * template.
   */
  public Template template;

  /**
   * The paramters to be used when a custom a {@link #template} is specified.
   */
  public Params params;

  /**
   * Creates a new tool tip config.
   */
  public ToolTipConfig() {

  }

  /**
   * Creates a new tool tip config with the given text.
   * 
   * @param text the tool tip text
   */
  public ToolTipConfig(String text) {
    this.text = text;
  }

  /**
   * Creates a new tool tip config with the given title and text.
   * 
   * @param title the tool tip title
   * @param text the tool tip text
   */
  public ToolTipConfig(String title, String text) {
    this.title = title;
    this.text = text;
  }

}
