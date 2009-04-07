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


Ext.Element.VISIBILITY=1;Ext.Element.DISPLAY=2;Ext.Element.addMethods(function(){var VISIBILITY="visibility",DISPLAY="display",HIDDEN="hidden",NONE="none",ELDISPLAY=Ext.Element.DISPLAY;return{originalDisplay:"",visibilityMode:1,setVisibilityMode:function(visMode){this.visibilityMode=visMode;return this;},animate:function(args,duration,onComplete,easing,animType){this.anim(args,{duration:duration,callback:onComplete,easing:easing},animType);return this;},anim:function(args,opt,animType,defaultDur,defaultEase,cb){animType=animType||'run';opt=opt||{};var me=this,anim=Ext.lib.Anim[animType](me.dom,args,(opt.duration||defaultDur)||.35,(opt.easing||defaultEase)||'easeOut',function(){if(cb)cb.call(me);if(opt.callback)opt.callback.call(opt.scope||me,me,opt);},me);opt.anim=anim;return anim;},preanim:function(a,i){return!a[i]?false:(Ext.isObject(a[i])?a[i]:{duration:a[i+1],callback:a[i+2],easing:a[i+3]});},isVisible:function(deep){return!this.isStyle(VISIBILITY,HIDDEN)||!this.isStyle(DISPLAY,NONE);},setVisible:function(visible,animate){var me=this,visMode=me.visibilityMode;if(!animate||!me.anim){if(me.visibilityMode==ELDISPLAY){me.setDisplayed(visible);}else{me.fixDisplay();me.dom.style.visibility=visible?"visible":HIDDEN;}}else{if(visible){me.setOpacity(.01);me.setVisible(true);}
me.anim({opacity:{to:(visible?1:0)}},me.preanim(arguments,1),null,.35,'easeIn',function(){if(!visible){if(visMode==ELDISPLAY){style.display=NONE;}else{style.visibility=HIDDEN;}
Ext.get(me.dom).setOpacity(1);}});}
return me;},toggle:function(animate){var me=this;me.setVisible(!me.isVisible(),me.preanim(arguments,0));return me;},setDisplayed:function(value){if(typeof value=="boolean"){value=value?this.originalDisplay:NONE;}
this.setStyle(DISPLAY,value);return this;},fixDisplay:function(){var me=this;if(me.isStyle(DISPLAY,NONE)){me.setStyle(VISIBILITY,HIDDEN);me.setStyle(DISPLAY,me.originalDisplay);if(me.isStyle(DISPLAY,NONE)){me.setStyle(DISPLAY,"block");}}},hide:function(animate){this.setVisible(false,this.preanim(arguments,0));return this;},show:function(animate){this.setVisible(true,this.preanim(arguments,0));return this;}}}());