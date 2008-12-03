/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

/**
 * Interface for validating a field's value.
 */
public interface Validator<Data, F extends Field<Data>> {

  /**
   * Validates the fields value.
   * 
   * @param field the field
   * @param value the value to validate
   * @return <code>null</code> if validation passes, otherwise the error message
   */
  public String validate(F field, String value);

}
