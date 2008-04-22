/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

public class SourceSelectionChangedListener implements SelectionChangedListener {

  private SelectionProvider provider;

  public SourceSelectionChangedListener(SelectionProvider provider) {
    this.provider = provider;
  }

  public void selectionChanged(SelectionChangedEvent event) {
    SelectionProvider eventProvider = event.getSelectionProvider();
    if (eventProvider != provider) {
      if (provider.getSelection().getFirstElement() != eventProvider.getSelection().getFirstElement()) {
        provider.setSelection(event.getSelection());
      }
    }
  }

}
