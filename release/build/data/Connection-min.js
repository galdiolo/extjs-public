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


(function(){var BEFOREREQUEST="beforerequest",REQUESTCOMPLETE="requestcomplete",REQUESTEXCEPTION="requestexception",LOAD='load',POST='POST',GET='GET',WINDOW=window;Ext.data.Connection=function(config){Ext.apply(this,config);this.addEvents(BEFOREREQUEST,REQUESTCOMPLETE,REQUESTEXCEPTION);Ext.data.Connection.superclass.constructor.call(this);};function handleResponse(response){this.transId=false;var options=response.argument.options;response.argument=options?options.argument:null;this.fireEvent(REQUESTCOMPLETE,this,response,options);if(options.success)options.success.call(options.scope,response,options);if(options.callback)options.callback.call(options.scope,options,true,response);}
function handleFailure(response,e){this.transId=false;var options=response.argument.options;response.argument=options?options.argument:null;this.fireEvent(REQUESTEXCEPTION,this,response,options,e);if(options.failure)options.failure.call(options.scope,response,options);if(options.callback)options.callback.call(options.scope,options,false,response);}
Ext.extend(Ext.data.Connection,Ext.util.Observable,{timeout:30000,autoAbort:false,disableCaching:true,disableCachingParam:'_dc',request:function(o){var me=this;if(me.fireEvent(BEFOREREQUEST,me,o)){if(o.el){if(!Ext.isEmpty(o.indicatorText)){me.indicatorText='<div class="loading-indicator">'+o.indicatorText+"</div>";}
if(me.indicatorText){Ext.getDom(o.el).innerHTML=me.indicatorText;}
o.success=(Ext.isFunction(o.success)?o.success:function(){}).createInterceptor(function(response){Ext.getDom(o.el).innerHTML=response.responseText;});}
var p=o.params,url=o.url||me.url,method,cb={success:handleResponse,failure:handleFailure,scope:me,argument:{options:o},timeout:o.timeout||me.timeout},form,serForm;if(Ext.isFunction(p)){p=p.call(o.scope||WINDOW,o);}
p=Ext.urlEncode(me.extraParams,typeof p=='object'?Ext.urlEncode(p):p);if(Ext.isFunction(url)){url=url.call(o.scope||WINDOW,o);}
if(form=Ext.getDom(o.form)){url=url||form.action;serForm=Ext.lib.Ajax.serializeForm(form);p=p?(p+'&'+serForm):serForm;}
method=o.method||me.method||((p||o.xmlData||o.jsonData)?POST:GET);if(method==GET&&(me.disableCaching||o.disableCaching!==false)){var dcp=o.disableCachingParam||me.disableCachingParam;url+=(url.indexOf('?')!=-1?'&':'?')+dcp+'='+(new Date().getTime());}
o.headers=Ext.apply(o.headers||{},me.defaultHeaders||{});if(o.autoAbort===true||me.autoAbort){me.abort();}
if((method==GET||o.xmlData||o.jsonData)&&p){url+=(/\?/.test(url)?'&':'?')+p;p='';}
return me.transId=Ext.lib.Ajax.request(method,url,cb,p,o);}else{return o.callback?o.callback.apply(o.scope,[o,,]):null;}},isLoading:function(transId){return transId?Ext.lib.Ajax.isCallInProgress(transId):!!this.transId;},abort:function(transId){if(transId||this.isLoading()){Ext.lib.Ajax.abort(transId||this.transId);}}});})();Ext.Ajax=new Ext.data.Connection({autoAbort:false,serializeForm:function(form){return Ext.lib.Ajax.serializeForm(form);}});