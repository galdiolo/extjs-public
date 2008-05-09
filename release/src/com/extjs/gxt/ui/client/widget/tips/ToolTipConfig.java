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
   * The tool tip text;
   */
  private String text;

  /**
   * Delay in milliseconds before the tooltip displays after the mouse enters
   * the target element (defaults to 500).
   */
  private int showDelay = 500;

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

  /**
   * An XY offset from the mouse position where the tooltip should be shown
   * (defaults to [15,18]).
   */
  private int[] mouseOffset = new int[] {15, 18};

  /**
   * True to have the tooltip follow the mouse as it moves over the target
   * element (defaults to false).
   */
  private boolean trackMouse;

  /**
   * A optional template to be used to render the tool tip. The {@link #params}
   * will be applied to the template. If specified, {@link #title} and
   * {@link #text} will be added to the params before being applied to the
   * template.
   */
  private Template template;

  /**
   * The paramters to be used when a custom a {@link #template} is specified.
   */
  private Params params;

  /**
   * The optional tool tip title (defaults to null).
   */
  private String title;

  /**
   * True to automatically hide the tooltip after the mouse exits the target
   * element or after the {@link #dismissDelay} has expired if set (defaults to
   * true).
   */
  private boolean autoHide = true;

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
    this.setText(text);
  }

  /**
   * Creates a new tool tip config with the given title and text.
   * 
   * @param title the tool tip title
   * @param text the tool tip text
   */
  public ToolTipConfig(String title, String text) {
    this.setTitle(title);
    this.setText(text);
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
   * Returns the hide delay in milliseconds.
   * 
   * @return the delay
   */
  public int getHideDelay() {
    return hideDelay;
  }

  /**
   * Returns the mouse offset.
   * 
   * @return the offset
   */
  public int[] getMouseOffset() {
    return mouseOffset;
  }

  /**
   * Returns the params.
   * 
   * @return the params
   */
  public Params getParams() {
    return params;
  }

  /**
   * Returns the show delay in milliseconds.
   * 
   * @return the delay
   */
  public int getShowDelay() {
    return showDelay;
  }

  /**
   * Returns the template.
   * 
   * @return the template
   */
  public Template getTemplate() {
    return template;
  }

  /**
   * Returns the tool tip text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Returns the tool tip title.
   * 
   * @return the title
   */
  public String getTitle() {
    return title;
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
   * Returns true if mouse tracking is enabled.
   * 
   * @return the mouse track state
   */
  public boolean isTrackMouse() {
    return trackMouse;
  }

  /**
   * True to automatically hide the tooltip after the mouse exits the target
   * element or after the {@link #dismissDelay} has expired if set (defaults to
   * true).
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
   * @param dismissDelay the dismiss delay
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

  /**
   * An XY offset from the mouse position where the tooltip should be shown
   * (defaults to [15,18]).
   * 
   * @param mouseOffset the offset
   */
  public void setMouseOffset(int[] mouseOffset) {
    this.mouseOffset = mouseOffset;
  }

  /**
   * The paramters to be used when a custom a {@link #template} is specified.
   * 
   * @param params the params
   */
  public void setParams(Params params) {
    this.params = params;
  }

  /**
   * Delay in milliseconds before the tooltip displays after the mouse enters
   * the target element (defaults to 500).
   * 
   * @param showDelay the show delay
   */
  public void setShowDelay(int showDelay) {
    this.showDelay = showDelay;
  }

  /**
   * A optional template to be used to render the tool tip. The {@link #params}
   * will be applied to the template. If specified, {@link #title} and
   * {@link #text} will be added to the params before being applied to the
   * template.
   * 
   * @param template the template
   */
  public void setTemplate(Template template) {
    this.template = template;
  }

  /**
   * The tool tip text.
   * 
   * @param text the text
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Sets the tool tip title.
   * 
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
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

}
