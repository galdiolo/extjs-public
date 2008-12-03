/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.dnd;

import java.util.List;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;

/**
 * A <code>DropTarget</code> implementation for the ListView component.
 */
public class ListViewDropTarget extends DropTarget {

  protected ListView listView;
  protected int insertIndex;

  /**
   * Creates a new list view drop target instance.
   * 
   * @param listView the target list view
   */
  public ListViewDropTarget(ListView listView) {
    super(listView);
    this.listView = listView;
  }

  /**
   * Returns the target's list view component.
   * 
   * @return the list view
   */
  public ListView getListView() {
    return listView;
  }

  @Override
  protected void onDragDrop(DNDEvent e) {
    super.onDragDrop(e);
    final Object data = e.data;
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        if (feedback == Feedback.APPEND) {
          if (data instanceof ModelData) {
            listView.getStore().add((ModelData) data);
          } else if (data instanceof List) {
            listView.getStore().add((List) data);
          }
        } else {
          if (data instanceof ModelData) {
            listView.getStore().insert((ModelData) data, insertIndex);
          } else if (data instanceof List) {
            listView.getStore().insert((List) data, insertIndex);
          }
        }
      }
    });
  }

  @Override
  protected void onDragEnter(DNDEvent e) {
    super.onDragEnter(e);
    e.doit = true;
    e.status.setStatus(true);
  }

  @Override
  protected void onDragLeave(DNDEvent e) {
    super.onDragLeave(e);
    Insert insert = Insert.get();
    insert.setVisible(false);
  }

  @Override
  protected void onDragMove(DNDEvent event) {
    if (feedback == Feedback.APPEND) {
      event.doit = true;
    } else {
      event.doit = true;
    }
  }

  @Override
  protected void showFeedback(DNDEvent event) {
    if (feedback == Feedback.INSERT) {
      event.status.setStatus(true);
      Element row = listView.findElement(event.getTarget()).cast();

      if (row == null && listView.getStore().getCount() > 0) {
        row = listView.getElement(listView.getStore().getCount() - 1).cast();
      }

      if (row != null) {
        int height = row.getOffsetHeight();
        int mid = height / 2;
        mid += row.getAbsoluteTop();
        int y = event.getClientY();
        boolean before = y < mid;
        int idx = listView.indexOf(row);
        insertIndex = before ? idx : idx + 1;
        if (before) {
          showInsert(event, row, true);
        } else {
          showInsert(event, row, false);
        }
      }
    }
  }

  private void showInsert(DNDEvent event, Element row, boolean before) {
    Insert insert = Insert.get();
    insert.setVisible(true);
    Rectangle rect = El.fly(row).getBounds();
    int y = !before ? (rect.y + rect.height - 4) : rect.y - 2;
    insert.el().setBounds(rect.x, y, rect.width, 6);
  }

}
