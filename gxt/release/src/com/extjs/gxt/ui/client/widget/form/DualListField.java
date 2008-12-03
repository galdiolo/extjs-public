/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.Element;

/**
 * Combines two list fields and allows selections to be moved between fields
 * either using buttons or by dragging and dropping selections.
 * 
 * @param <D> the model type
 */
public class DualListField<D extends ModelData> extends MultiField<Field> {

  public enum Mode {
    APPEND, INSERT;
  }

  protected ListField<D> fromField;
  protected ListField<D> toField;
  protected AdapterField buttonAdapter;
  protected VerticalPanel buttonBar;
  protected Mode mode = Mode.APPEND;

  private String dndGroup;
  private boolean enableDND = true;

  public DualListField() {
    fromField = new ListField<D>();
    toField = new ListField<D>();

    buttonBar = new VerticalPanel();
    buttonBar.setStyleAttribute("margin", "7px");
    buttonBar.setHorizontalAlign(HorizontalAlignment.CENTER);
    buttonAdapter = new AdapterField(buttonBar);

    add(fromField);
    add(buttonAdapter);
    add(toField);
  }

  public String getDNDGroup() {
    return dndGroup;
  }

  /**
   * Returns the from list field.
   * 
   * @return the field
   */
  public ListField<D> getFromList() {
    return fromField;
  }

  public Mode getMode() {
    return mode;
  }

  /**
   * Returns the to list field.
   * 
   * @return the field
   */
  public ListField<D> getToList() {
    return toField;
  }

  /**
   * Returns true if drag and drop is enabled.
   * 
   * @return true of drag and rop
   */
  public boolean isEnableDND() {
    return enableDND;
  }

  /**
   * Sets the drag and drop grup name. A group name will be generated if none is
   * specified.
   * 
   * @param group the group name
   */
  public void setDNDGroup(String group) {
    this.dndGroup = group;
  }

  /**
   * True to allow selections to be dragged and dropped between lists (defaults
   * to true).
   * 
   * @param enableDND true to enable drag and drop
   */
  public void setEnableDND(boolean enableDND) {
    this.enableDND = enableDND;
  }

  /**
   * Specifies if selections are either inserted or appended when moving between
   * lists.
   * 
   * @param mode the mode
   */
  public void setMode(Mode mode) {
    this.mode = mode;
  }

  protected void initButtons() {
    if (mode == Mode.INSERT) {
      IconButton top = new IconButton("arrow-top");
      IconButton up = new IconButton("arrow-up");
      up.addListener(Events.Select, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent be) {
          toField.getListView().moveSelectedUp();
        }
      });
      buttonBar.add(top);
      buttonBar.add(up);
    }

    IconButton right = new IconButton("arrow-right");
    right.addListener(Events.Select, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent be) {
        List<D> sel = fromField.getSelection();
        for (D model : sel) {
          fromField.getStore().remove(model);
        }
        toField.getStore().add(sel);
      }
    });

    IconButton left = new IconButton("arrow-left");
    left.addListener(Events.Select, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent be) {
        List<D> sel = toField.getSelection();
        for (D model : sel) {
          toField.getStore().remove(model);
        }
        fromField.getStore().add(sel);
      }
    });

    buttonBar.add(right);
    buttonBar.add(left);

    if (mode == Mode.INSERT) {
      IconButton down = new IconButton("arrow-down");
      down.addListener(Events.Select, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent be) {
          toField.getListView().moveSelectedDown();
        }
      });
      IconButton bottom = new IconButton("arrow-bottom");
      buttonBar.add(down);
      buttonBar.add(bottom);
    }
  }

  protected void initDND() {
    if (dndGroup == null) {
      dndGroup = getId() + "-group";
    }

    ListViewDragSource source1 = new ListViewDragSource(fromField.getListView());
    ListViewDragSource source2 = new ListViewDragSource(toField.getListView());

    source1.setGroup(dndGroup);
    source2.setGroup(dndGroup);

    ListViewDropTarget target1 = new ListViewDropTarget(fromField.getListView());
    ListViewDropTarget target2 = new ListViewDropTarget(toField.getListView());

    target1.setGroup(dndGroup);
    target2.setGroup(dndGroup);
    
    if (mode == Mode.INSERT) {
      target1.setFeedback(Feedback.INSERT);
      target2.setFeedback(Feedback.INSERT);
    }
  }

  protected void initLists() {
    fromField.setHeight(125);
    toField.setHeight(125);
  }

  @Override
  protected void onRender(Element target, int index) {
    initLists();
    initButtons();

    boolean vertical = orientation == Orientation.VERTICAL;
    if (vertical) {
      lc = new VerticalPanel();
    } else {
      lc = new HorizontalPanel();
    }
    if (GXT.isIE) lc.setStyleAttribute("position", "relative");

    for (int i = 0, len = fields.size(); i < len; i++) {
      Field f = fields.get(i);
      TableData data = (TableData) ComponentHelper.getLayoutData(f);
      if (data == null) {
        data = new TableData();
      }
      String style = "position: static;";
      style += "paddingRight:" + spacing + ";";
      data.setStyle(style);
      lc.add(f, data);
    }

    lc.render(target, index);
    setElement(lc.getElement());

    if (enableDND) {
      initDND();
    }
  }

}
