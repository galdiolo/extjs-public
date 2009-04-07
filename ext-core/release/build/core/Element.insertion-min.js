/*
 * Ext Core Library 3.0 Beta
 * http://extjs.com/
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * 
 * The MIT License
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */


Ext.Element.addMethods(function(){var GETDOM=Ext.getDom,GET=Ext.get,DH=Ext.DomHelper;return{appendChild:function(el){return GET(el).appendTo(this);},appendTo:function(el){GETDOM(el).appendChild(this.dom);return this;},insertBefore:function(el){(el=GETDOM(el)).parentNode.insertBefore(this.dom,el);return this;},insertAfter:function(el){GETDOM(el).parentNode.insertBefore(this.dom,el.nextSibling);return this;},insertFirst:function(el,returnDom){el=el||{};if(Ext.isObject(el)&&!el.nodeType&&!el.dom){return this.createChild(el,this.dom.firstChild,returnDom);}else{el=GETDOM(el);this.dom.insertBefore(el,this.dom.firstChild);return!returnDom?GET(el):el;}},replace:function(el){el=GET(el);this.insertBefore(el);el.remove();return this;},replaceWith:function(el){var me=this,Element=Ext.Element;if(Ext.isObject(el)&&!el.nodeType&&!el.dom){el=DH.insertBefore(me.dom,el);}else{el=GETDOM(el);me.dom.parentNode.insertBefore(el,me.dom);}
delete El.cache[me.id];Ext.removeNode(me.dom);me.id=Ext.id(me.dom=el);return Element.cache[me.id]=me;},createChild:function(config,insertBefore,returnDom){config=config||{tag:'div'};return insertBefore?DH.insertBefore(insertBefore,config,returnDom!==true):DH[!this.dom.firstChild?'overwrite':'append'](this.dom,config,returnDom!==true);},wrap:function(config,returnDom){var newEl=DH.insertBefore(this.dom,config||{tag:"div"},!returnDom);newEl.dom?newEl.dom.appendChild(this.dom):newEl.appendChild(this.dom);return newEl;},insertHtml:function(where,html,returnEl){var el=DH.insertHtml(where,this.dom,html);return returnEl?Ext.get(el):el;}}}());