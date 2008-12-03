package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.Event;

public class ComboQueryEvent extends FieldEvent {

  public String query;

  public boolean forceAll;

  public ComboBox combo;

  public ComboQueryEvent(ComboBox field, Event event) {
    super(field, event);
    this.combo = field;
  }

  public ComboQueryEvent(ComboBox field) {
    super(field);
    this.combo = field;
  }

}
