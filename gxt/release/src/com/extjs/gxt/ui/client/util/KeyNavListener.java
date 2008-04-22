/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import java.util.EventListener;

import com.extjs.gxt.ui.client.event.ComponentEvent;

/**
 * Interface for key navs.
 * 
 * @see KeyNav
 */
public interface KeyNavListener extends EventListener {

  public void onDelete(ComponentEvent ce);

  public void onDown(ComponentEvent ce);

  public void onEnd(ComponentEvent ce);

  public void onEnter(ComponentEvent ce);

  public void onEscape(ComponentEvent ce);

  public void onHome(ComponentEvent ce);

  public void onKeyPress(ComponentEvent ce);

  public void onLeft(ComponentEvent ce);

  public void onRight(ComponentEvent ce);

  public void onUp(ComponentEvent ce);

  public void onPageDown(ComponentEvent ce);

  public void onPageUp(ComponentEvent ce);

  public void onTab(ComponentEvent ce);

}
