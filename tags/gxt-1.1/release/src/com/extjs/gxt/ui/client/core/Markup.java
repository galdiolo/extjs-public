package com.extjs.gxt.ui.client.core;

import com.google.gwt.user.client.Element;

public interface Markup {
	public String getHtml();
	public Element getRootElement();
	public Element select(String selector);
}
