/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Tom Schindl <tom.schindl@bestsolution.at> - port to GXT (issue 39)
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

/**
 * A listener which is notified of changes to the checked state of items in
 * checkbox viewers.
 * 
 * @see CheckStateChangedEvent
 */
public interface CheckStateListener {

  /**
   * Notifies of a change to the checked state of an element.
   * 
   * @param event event object describing the change
   */
  void checkStateChanged(CheckStateChangedEvent event);
}
