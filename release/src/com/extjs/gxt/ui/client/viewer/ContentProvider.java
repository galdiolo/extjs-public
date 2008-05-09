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
 * A content provider mediates between the viewer's model and the viewer itself.
 */
public interface ContentProvider<I> {

  /**
   * Notifies this content provider that the given viewer's input has been
   * switched to a different element.
   * 
   * @param viewer the viewer
   * @param oldInput the previous input or <code>null</code> if none
   * @param newInput the new input
   */
  public void inputChanged(Viewer viewer, I oldInput, I newInput);

}
