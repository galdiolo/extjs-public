/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.dnd;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

class DNDManager {

  private static DNDManager manager;

  static DNDManager get() {
    if (manager == null) {
      manager = new DNDManager();
    }
    return manager;
  }

  private List<DropTarget> targets = new ArrayList<DropTarget>();
  private DropTarget currentTarget;

  void registerDropTarget(DropTarget target) {
    targets.add(target);
  }

  void unregisterDropTarget(DropTarget target) {
    targets.remove(target);
  }

  void handleDragCancelled(DragSource source, DNDEvent event) {
    source.onDragCancelled(event);
    source.fireEvent(Events.DragCancel, event);
    if (currentTarget != null) {
      currentTarget.onDragCancelled(event);
      currentTarget = null;
    }
  }

  void handleDragMove(DragSource source, DNDEvent event) {
    DropTarget target = getTarget(event.getTarget());

    // no target with current
    if (target == null) {
      if (currentTarget != null) {
        currentTarget.handleDragLeave(event);
        currentTarget.fireEvent(Events.DragLeave, event);
        currentTarget = null;
      }
      return;
    }

    // match move
    if (target == currentTarget) {
      event.doit = false;
      event.target = currentTarget;
      currentTarget.onDragMove(event);
      currentTarget.fireEvent(Events.DragMove, event);
      if (event.doit == true) {
        currentTarget.showFeedback(event);
      } else {
        Insert.get().hide();
      }
      return;
    }

    if (target != currentTarget) {
      if (!source.getGroup().equals(target.getGroup())) {
        return;
      }

      if (currentTarget != null) {
        currentTarget.handleDragLeave(event);
        currentTarget = null;
      }
      if (!target.isEnabled() || !target.component.isEnabled()) {
        return;
      }
      currentTarget = target;
    }

    if (!currentTarget.isAllowSelfAsSource()
        && source.getComponent() == currentTarget.getComponent()) {
      currentTarget = null;
      return;
    }

    // entering
    event.doit = false;
    event.target = currentTarget;
    currentTarget.handleDragEnter(event);
  }

  void handleDragStart(DragSource source, DNDEvent event) {
    source.onDragStart(event);
    if (!source.fireEvent(Events.DragStart, event) || event.data == null) {
      event.doit = false;
      event.dragEvent.doit = false;
      return;
    }
    source.setData(event.data);
    source.draggable.setProxy(source.statusProxy.getElement());
    source.statusProxy.setStatus(false);
  }

  void handleDragEnd(DragSource source, DNDEvent event) {
    if (currentTarget != null && event.status.getStatus()) {
      event.operation = currentTarget.getOperation();
      source.onDragDrop(event);
      source.fireEvent(Events.Drop, event);

      currentTarget.handleDrop(event);
      currentTarget.fireEvent(Events.Drop, event);
    }
    Insert.get().hide();
    currentTarget = null;
  }

  protected DropTarget getTarget(Element elem) {
    DropTarget target = null;
    for (int i = 0, len = targets.size(); i < len; i++) {
      DropTarget t = targets.get(i);
      if (DOM.isOrHasChild(t.component.getElement(), elem)
          && (target == null || (target != null && DOM.isOrHasChild(
              target.getComponent().getElement(), t.component.getElement())))) {
        target = t;
      }
    }
    return target;
  }

}
