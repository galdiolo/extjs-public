/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Primary interface for GXT model objects with event support.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Update</b> : ChangeEvent(source)<br>
 * <div>Fires after the the model is updated.</div>
 * <ul>
 * <li>source : this</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 * 
 * <p/>For a default implementation see {@link BaseModel} or
 * {@link BaseTreeModel}.
 */
public interface Model extends ModelData, ChangeEventSource {
}
