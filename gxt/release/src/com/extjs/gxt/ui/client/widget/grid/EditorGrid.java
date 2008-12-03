/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.DomEvent;
import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * Adds editing capabilities to Grid.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeEdit</b> : GridEvent(grid, record, property, value, rowIndex,
 * colIndex)<br>
 * <div>Fires before cell editing is triggered. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>grid : this</li>
 * <li>record : the record being edited</li>
 * <li>property : the property being edited</li>
 * <li>value : the value being edited</li>
 * <li>rowIndex : the current row</li>
 * <li>colIndex : the current column</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>AfterEdit</b> : GridEvent(grid, record, property, value, startValue,
 * rowIndex, colIndex)<br>
 * <div>Fires after a cell is edited.</div>
 * <ul>
 * <li>grid : this</li>
 * <li>record : the record being edited</li>
 * <li>property : the property being edited</li>
 * <li>value : the value being set</li>
 * <li>startValue : the value before the edit</li>
 * <li>rowIndex : the current row</li>
 * <li>colIndex : the current column</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ValidateEdit</b> : GridEvent(grid, record, property, value,
 * startValue, rowIndex, colIndex)<br>
 * <div>Fires after a cell is edited. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>grid : this</li>
 * <li>record : the record being edited</li>
 * <li>property : the property being edited</li>
 * <li>value : the value being set</li>
 * <li>startValue : the value before the edit</li>
 * <li>rowIndex : the current row</li>
 * <li>colIndex : the current column</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 */
public class EditorGrid<M extends ModelData> extends Grid<M> {

  public enum ClicksToEdit {
    ONE, TWO;
  }

  private boolean editing;
  private ClicksToEdit clicksToEdit = ClicksToEdit.ONE;
  private CellEditor activeEditor;
  private Record activeRecord;
  private Listener<DomEvent> editorListener;
  private Listener<GridEvent> gridListener;
  private boolean ignoreScroll;

  /**
   * Creates a new editor grid.
   * 
   * @param store the store
   * @param cm the column model
   */
  public EditorGrid(ListStore store, ColumnModel cm) {
    super(store, cm);
    setSelectionModel(new CellSelectionModel<M>());
    setTrackMouseOver(false);
  }

  /**
   * Returns true if editing is active.
   * 
   * @return the editing state
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * Returns the clicks to edit.
   * 
   * @return the clicks to edit
   */
  public ClicksToEdit getClicksToEdit() {
    return clicksToEdit;
  }

  /**
   * Sets the number of clicks to edit (defaults to ONE).
   * 
   * @param clicksToEdit the clicks to edit
   */
  public void setClicksToEdit(ClicksToEdit clicksToEdit) {
    this.clicksToEdit = clicksToEdit;
  }

  /**
   * Starts editing the specified for the specified row/column.
   * 
   * @param row the row index
   * @param col the column index
   */
  public void startEditing(final int row, final int col) {
    stopEditing();
    if (cm.isCellEditble(col)) {
      getView().ensureVisible(row, col, false);
      if (sm instanceof CellSelectionModel) {
        ((CellSelectionModel) sm).selectCell(row, col);
      }

      final M m = store.getAt(row);
      activeRecord = store.getRecord(m);

      final String field = cm.getDataIndex(col);
      GridEvent e = new GridEvent(this);
      e.model = m;
      e.property = field;
      e.rowIndex = row;
      e.colIndex = col;
      if (fireEvent(Events.BeforeEdit, e)) {
        DeferredCommand.addCommand(new Command() {
        
          public void execute() {
            editing = true;
            CellEditor ed = cm.getEditor(col);
            ed.row = row;
            ed.col = col;

            if (!ed.isRendered()) {
              ed.render((Element) view.getEditorParent());
            }

            if (editorListener == null) {
              editorListener = new Listener<DomEvent>() {
                public void handleEvent(DomEvent e) {
                  if (e.type == Events.Complete) {
                    EditorEvent ee = (EditorEvent) e;
                    onEditComplete((CellEditor) ee.editor, ee.value, ee.startValue);
                  } else if (e.type == Events.SpecialKey) {
                    ((CellSelectionModel) sm).onEditorKey(e);
                  }
                }
              };
            }

            ed.addListener(Events.Complete, editorListener);
            ed.addListener(Events.SpecialKey, editorListener);

            activeEditor = ed;
            // when inserting the editor into the last row, the body is scrolling
            // in ie, and edit is being cancelled
            ignoreScroll = true;
            ed.startEdit((Element) view.getCell(row, col), m.get(field));
            ignoreScroll = false;
          }
        });
        
      }
    }
  }

  /**
   * Stops any active editing.
   */
  public void stopEditing() {
    stopEditing(false);
  }

  public void stopEditing(boolean cancel) {
    if (activeEditor != null) {
      if (cancel) {
        activeEditor.cancelEdit();
      } else {
        activeEditor.completeEdit();
      }
    }
    activeEditor = null;
  }

  protected void onAutoEditClick(GridEvent e) {
    if (e.event.getButton() != Event.BUTTON_LEFT) {
      return;
    }
    int row = view.findRowIndex(e.getTarget());
    int cell = view.findRowIndex(e.getTarget());
    if (row != -1 && cell != -1) {
      stopEditing();
    }
  }

  protected void onEditComplete(CellEditor ed, Object value, Object startValue) {
    editing = false;
    activeEditor = null;
    ed.removeListener(Events.SpecialKey, editorListener);
    Record r = activeRecord;
    String field = cm.getDataIndex(ed.col);
    if ((value == null && startValue != null) || !value.equals(startValue)) {
      GridEvent ge = new GridEvent(this);
      ge.record = r;
      ge.property = field;
      ge.value = value;
      ge.startValue = startValue;
      ge.rowIndex = ed.row;
      ge.colIndex = ed.col;

      if (fireEvent(Events.ValidateEdit, ge)) {
        r.set(ge.property, ge.value);
        fireEvent(Events.AfterEdit, ge);
      }
    }

    getView().focusCell(ed.row, ed.col, false);
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);

    gridListener = new Listener<GridEvent>() {
      public void handleEvent(GridEvent ge) {
        switch (ge.type) {
          case Events.BodyScroll:
            if (!ignoreScroll) {
              stopEditing();
            }
            break;
          case Events.CellClick:
          case Events.CellDoubleClick:
            ge.cancelBubble();
            onCellDoubleClick(ge);
            break;
        }
      }

    };

    addListener(Events.BodyScroll, gridListener);

    if (clicksToEdit == ClicksToEdit.ONE) {
      addListener(Events.CellClick, gridListener);
    } else {
      addListener(Events.CellDoubleClick, gridListener);
    }

    addStyleName("x-edit-grid");
    if (GXT.isSafari) {
      el().setTop(0);
      el().setScrollTop(0);
      el().makePositionable(false);
    }
  }

  protected void onCellDoubleClick(GridEvent e) {
    startEditing(e.rowIndex, e.colIndex);
  }

}
