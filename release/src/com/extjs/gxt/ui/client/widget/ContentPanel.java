/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;


/**
 * ContentPanel is a component container that has specific functionality and
 * structural components that make it the perfect building block for
 * application-oriented user interfaces. The Panel contains bottom and top
 * toolbars, along with separate header, footer and body sections. It also
 * provides built-in expandable and collapsible behavior, along with a variety
 * of prebuilt tool buttons that can be wired up to provide other customized
 * behavior.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeExpand</b> : ComponentEvent(component)<br>
 * <div>Fires before the panel is expanded. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : ComponentEvent(component)<br>
 * <div>Fires after the panel is expanded</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : ComponentEvent(component)<br>
 * <div>Fires before the panel is collpased. Listeners can set the
 * <code>doit</code> field <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : ComponentEvent(component)<br>
 * <div>Fires after the panel is collapsed.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeClose</b> : ComponentEvent(component)<br>
 * <div>Fires before a content panel is closed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the operation.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : ComponentEvent(component)<br>
 * <div>Fires after a content panel is closed.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 */
public class ContentPanel extends GenericContentPanel<Component> {
}
