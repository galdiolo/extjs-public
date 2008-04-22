/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ext GWT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

/**
 * Tag interface for objects that can return labels for model objects.
 */
public interface BaseLabelProvider<T> {

  /**
   * Returns the cell's text.
   */
  public String getText(T element);

  /**
   * Returns the cell's icon style.
   */
  public String getIconStyle(T element);

  /**
   * Returns the cell's text style.
   */
  public String getTextStyle(T element);
  
}
