/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.core;

import com.google.gwt.user.client.Element;

/**
 * Loads the ext javascript to make avaliable Ext, Element, DomHelper, DomQuery,
 * and Template objects available. Only the parts of the library being used are
 * loaded and were manually moved into the class. No event, listener, or animation code is
 * used. Provides low level dom related functions. A reference to the Ext
 * instance is set $wnd.Ext.
 */
class Ext {

  /**
   * Loads the native exj javascript.
   */
  static void load() {
    loadAdapter();
    loadExt();
    loadElement();
    loadFormat();
    loadDomQuery();
    loadDomHelper();
    loadTemplate();
    loadDate();
  }
  
  native static void call(Element dom, String method, String value) /*-{
    $wnd._el.fly(dom)[method](value);
  }-*/;
  
  native static Element callElement(Element dom, String method, String value) /*-{
    return $wnd._el.fly(dom)[method](value);
  }-*/;
  
  native static Element findParent(Element dom, String selector, int maxDepth) /*-{
    return $wnd._el.fly(dom).findParent(selector, maxDepth);
  }-*/;
  
  native static int callInt(Element dom, String method, String value) /*-{
    return $wnd._el.fly(dom)[method](value);
  }-*/;
  
  native static String callString(Element dom, String method, String value) /*-{
    return $wnd._el.fly(dom)[method](value);
  }-*/;

  private native static void loadExt() /*-{
    var document = $doc;
    var window = $wnd;
    var navigator = window.navigator;
    var Ext = $wnd.Ext;
    var D = $wnd.Ext.lib.Dom;
    window["undefined"] = window["undefined"];
    Ext.apply = function(o, c, defaults){
         if(defaults){
             // no "this" reference for friendly out of scope calls
             Ext.apply(o, defaults);
         }
         if(o && c && typeof c == 'object'){
             for(var p in c){
                 o[p] = c[p];
             }
         }
         return o;
     };
    var initload = function(){
    var idSeed = 0;
    var ua = navigator.userAgent.toLowerCase();
    var isStrict = $doc.compatMode == "CSS1Compat",
      isOpera = ua.indexOf("opera") > -1,
      isSafari = (/webkit|khtml/).test(ua),
      isIE = !isOpera && ua.indexOf("msie") > -1,
      isIE7 = !isOpera && ua.indexOf("msie 7") > -1,
      isGecko = !isSafari && ua.indexOf("gecko") > -1,
      isBorderBox = isIE && !isStrict,
      isWindows = (ua.indexOf("windows") != -1 || ua.indexOf("win32") != -1),
      isMac = (ua.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1),
      isLinux = (ua.indexOf("linux") != -1),
      isSecure = window.location.href.toLowerCase().indexOf("https") === 0;
      // remove css image flicker
      if(isIE && !isIE7){
        try{
          document.execCommand("BackgroundImageCache", false, true);
        }catch(e){}
      }
      Ext.apply(Ext, {
        isStrict : isStrict,
        isSecure : isSecure,
        isReady : false,
        enableGarbageCollector : true,
        enableListenerCollection:false,
        emptyFn : function(){},
        applyIf : function(o, c){
          if(o && c){
            for(var p in c){
              if(typeof o[p] == "undefined"){ o[p] = c[p]; }
              }
            }
          return o;
        },
        id : function(el, prefix){
          prefix = prefix || "ext-gen";
          el = Ext.getDom(el);
             var id = prefix + (++idSeed);
             return el ? (el.id ? el.id : (el.id = id)) : id;
         },
         extend : function(){
             // inline overrides
             var io = function(o){
                 for(var m in o){
                     this[m] = o[m];
                 }
             };
             return function(sb, sp, overrides){
                 if(typeof sp == 'object'){
                     overrides = sp;
                     sp = sb;
                     sb = function(){sp.apply(this, arguments);};
                 }
                 var F = function(){}, sbp, spp = sp.prototype;
                 F.prototype = spp;
                 sbp = sb.prototype = new F();
                 sbp.constructor=sb;
                 sb.superclass=spp;
                 if(spp.constructor == Object.prototype.constructor){
                     spp.constructor=sp;
                 }
                 sb.override = function(o){
                     Ext.override(sb, o);
                 };
                 sbp.override = io;
                 Ext.override(sb, overrides);
                 return sb;
             };
         }(),
         override : function(origclass, overrides){
             if(overrides){
                 var p = origclass.prototype;
                 for(var method in overrides){
                     p[method] = overrides[method];
                 }
             }
         },
         namespace : function(){
             var a=arguments, o=null, i, j, d, rt;
             for (i=0; i<a.length; ++i) {
                 d=a[i].split(".");
                 rt = d[0];
                 eval('if (typeof ' + rt + ' == "undefined"){' + rt + ' = {};} o = ' + rt + ';');
                 for (j=1; j<d.length; ++j) {
                     o[d[j]]=o[d[j]] || {};
                     o=o[d[j]];
                 }
             }
         },
         each : function(array, fn, scope){
             if(typeof array.length == "undefined" || typeof array == "string"){
                 array = [array];
             }
             for(var i = 0, len = array.length; i < len; i++){
                 if(fn.call(scope || array[i], array[i], i, array) === false){ return i; };
             }
         },
         escapeRe : function(s) {
             return s.replace(/([.*+?^${}()|[\]\/\\])/g, "\\$1");
         },
         getDom : function(el){
             if(!el || !document){
                 return null;
             }
             return el.dom ? el.dom : (typeof el == 'string' ? document.getElementById(el) : el);
         },
         isOpera : isOpera,
         isSafari : isSafari,
         isIE : isIE,
         isIE6 : isIE && !isIE7,
         isIE7 : isIE7,
         isGecko : isGecko,
         isBorderBox : isBorderBox,
         isLinux : isLinux,
         isWindows : isWindows,
         isMac : isMac,
       });
     };
     initload();
     $wnd.Ext = Ext;
  }-*/;

  private native static void loadElement() /*-{
    var document = $doc;
    var window = $wnd;
    var Ext = $wnd.Ext;
    var D = $wnd.Ext.lib.Dom;
    var propCache = {};
    var camelRe = /(-[a-z])/gi;
    var camelFn = function(m, a){ return a.charAt(1).toUpperCase(); };
    var view = document.defaultView;
    
    Ext.Element = function(element, forceNew){
       var dom = typeof element == "string" ?
               document.getElementById(element) : element;
       if(!dom){ 
           return null;
       }
       var id = dom.id;
       //if(forceNew !== true && id && Ext.Element.cache[id]){ // element object already exists
           //return Ext.Element.cache[id];
       //}
       this.dom = dom;
       this.id = id || Ext.id(dom);
     };
     

    var El = Ext.Element;
     
    El.prototype = {
        originalDisplay : "",
        visibilityMode : 1,
        defaultUnit : "px",
        classReCache: {},
                 


    
        // private
        adjustForConstraints : function(xy, parent, offsets){
            return this.getConstrainToXY(parent || document, false, offsets, xy) ||  xy;
        },

         
         
         
         addClass : function(className){
            if(className instanceof Array){
                for(var i = 0, len = className.length; i < len; i++) {
                    this.addClass(className[i]);
                }
            }else{
                if(className && !this.hasClass(className)){
                    this.dom.className = this.dom.className + " " + className;
                }
            }
            return this;
        },
        addStyles : function(sides, styles){
             var val = 0, v, w;
             for(var i = 0, len = sides.length; i < len; i++){
                 v = this.getStyle(styles[sides.charAt(i)]);
                 if(v){
                      w = parseInt(v, 10);
                      if(w){ val += (w >= 0 ? w : -1 * w); }
                 }
             }
             return val;
        },
        alignTo : function(element, position, offsets){
            var xy = this.getAlignToXY(element, position, offsets);
            this.setXY(xy);
            return this;
        },
        
        applyStyles : function(style){
             Ext.DomHelper.applyStyles(this.dom, style);
            // return this;
        },

        
        enableDisplayMode : function(display){
            this.setVisibilityMode(El.DISPLAY);
            if(typeof display != "undefined") this.originalDisplay = display;
            return this;
        },
         

         

         
        findParent : function(simpleSelector, maxDepth){
            var p = this.dom, b = document.body, depth = 0, dq = Ext.DomQuery, stopEl;
            maxDepth = maxDepth || 10;
            if(typeof maxDepth != "number"){
                stopEl = Ext.getDom(maxDepth);
                maxDepth = 10;
            }
            while(p && p.nodeType == 1 && depth < maxDepth && p != b && p != stopEl){
                if(dq.is(p, simpleSelector)){
                    return p;
                }
                depth++;
                p = p.parentNode;
            }
            return null;
        },
        getAnchorXY : function(anchor, local, s){
            //Passing a different size is useful for pre-calculating anchors,
            //especially for anchored animations that change the el size.
    
            var w, h, vp = false;
            if(!s){
                var d = this.dom;
                if(d == document.body || d == document){
                    vp = true;
                    w = D.getViewWidth(); h = D.getViewHeight();
                }else{
                    w = this.getWidth(); h = this.getHeight();
                }
            }else{
                w = s.width;  h = s.height;
            }
            
      
            var x = 0, y = 0, r = Math.round;
            switch((anchor || "tl").toLowerCase()){
                case "c":
                    x = r(w*.5);
                    y = r(h*.5);
                break;
                case "t":
                    x = r(w*.5);
                    y = 0;
                break;
                case "l":
                    x = 0;
                    y = r(h*.5);
                break;
                case "r":
                    x = w;
                    y = r(h*.5);
                break;
                case "b":
                    x = r(w*.5);
                    y = h;
                break;
                case "tl":
                    x = 0;
                    y = 0;
                break;
                case "bl":
                    x = 0;
                    y = h;
                break;
                case "br":
                    x = w;
                    y = h;
                break;
                case "tr":
                    x = w;
                    y = 0;
                break;
            }
            if(local === true){
                return [x, y];
            }
            if(vp){
                var sc = this.getScroll();
                return [x + sc.left, y + sc.top];
            }
            //Add the element's offset xy
       
            var o = this.getXY();
            return [x+o[0], y+o[1]];
        },
        
        getAlignToXY : function(el, p, ox, oy){
            el = new Ext.Element(el);
            if(!el || !el.dom){
                throw "Element.alignToXY with an element that doesn't exist";
            }
            
            
            
            var d = this.dom;
            var c = false; //constrain to viewport
            var p1 = "", p2 = "";

    
            if(!p){
                p = "tl-bl";
            }else if(p == "?"){
                p = "tl-bl?";
            }else if(p.indexOf("-") == -1){
                p = "tl-" + p;
            }
            p = p.toLowerCase();
            var m = p.match(/^([a-z]+)-([a-z]+)(\?)?$/);
            if(!m){
               throw "Element.alignTo with an invalid alignment " + p;
            }
            p1 = m[1]; p2 = m[2]; c = !!m[3];
    
            //Subtract the aligned el's internal xy from the target's offset xy
            //plus custom offset to get the aligned el's new offset xy
            var a1 = this.getAnchorXY(p1, true);
            var a2 = el.getAnchorXY(p2, false);
      
     
            var x = a2[0] - a1[0] + ox;
            var y = a2[1] - a1[1] + oy;
            
            if(c){
                //constrain the aligned el to viewport if necessary
                var w = this.getWidth(), h = this.getHeight(), r = el.getRegion();
                // 5px of margin for ie
                var dw = D.getViewWidth()-5, dh = D.getViewHeight()-5;

                //If we are at a viewport boundary and the aligned el is anchored on a target border that is
                //perpendicular to the vp border, allow the aligned el to slide on that border,
                //otherwise swap the aligned el to the opposite border of the target.
                var p1y = p1.charAt(0), p1x = p1.charAt(p1.length-1);
               var p2y = p2.charAt(0), p2x = p2.charAt(p2.length-1);
               var swapY = ((p1y=="t" && p2y=="b") || (p1y=="b" && p2y=="t"));
               var swapX = ((p1x=="r" && p2x=="l") || (p1x=="l" && p2x=="r"));
    
               var doc = $doc;
               var scrollX = (doc.documentElement.scrollLeft || doc.body.scrollLeft || 0)+5;
               var scrollY = (doc.documentElement.scrollTop || doc.body.scrollTop || 0)+5;
               
              
               if((x+w) > dw + scrollX){
                    x = swapX ? r.left-w : dw+scrollX-w;
                }
               if(x < scrollX){
                   x = swapX ? r.right : scrollX;
               }
               if((y+h) > dh + scrollY){
                    y = swapY ? r.top-h : dh+scrollY-h;
                }
               if (y < scrollY){
                   y = swapY ? r.bottom : scrollY;
               }
            }
            return [x,y];
        },
        

        getComputedHeight : function(){
             var h = Math.max(this.dom.offsetHeight, this.dom.clientHeight);
             if(!h){
                 h = parseInt(this.getStyle('height'), 10) || 0;
                 if(!this.isBorderBox()){
                     h += this.getFrameWidth('tb');
                 }
             }
             return h;
         },




          getBottom : function(local){
              if(!local){
                  return this.getY() + this.getHeight();
              }else{
                  return (this.getTop(true) + this.getHeight()) || 0;
              }
          },
         getBorderWidth : function(side){
             return this.addStyles(side, El.borders);
         }, 
         
         getComputedWidth : function(){
             var w = Math.max(this.dom.offsetWidth, this.dom.clientWidth);
             if(!w){
                 w = parseInt(this.getStyle('width'), 10) || 0;
                 if(!this.isBorderBox()){
                     w += this.getFrameWidth('lr');
                 }
             }
             return w;
         },
        getConstrainToXY : function(){
            var os = {top:0, left:0, bottom:0, right: 0};
    
            return function(el, local, offsets, proposedXY){
                offsets = offsets ? Ext.applyIf(offsets, os) : os;
    
                var vw, vh, vx = 0, vy = 0;
                if(el.dom == document.body || el.dom == document){
                    vw = D.getViewWidth();
                    vh = D.getViewHeight();
                }else{
                    vw = el.dom.clientWidth;
                    vh = el.dom.clientHeight;
                    if(!local){
                        var vxy = el.getXY();
                        vx = vxy[0];
                        vy = vxy[1];
                    }
                }
    
                var s = el.getScroll();
    
                vx += offsets.left + s.left;
                vy += offsets.top + s.top;
    
                vw -= offsets.right;
                vh -= offsets.bottom;
    
                var vr = vx+vw;
                var vb = vy+vh;
    
                var xy = proposedXY || (!local ? this.getXY() : [this.getLeft(true), this.getTop(true)]);
                var x = xy[0], y = xy[1];
                var w = this.dom.offsetWidth, h = this.dom.offsetHeight;
    
                // only move it if it needs it
                var moved = false;
    
                // first validate right/bottom
                if((x + w) > vr){
                    x = vr - w;
                    moved = true;
                }
                if((y + h) > vb){
                    y = vb - h;
                    moved = true;
                }
                // then make sure top/left isn't negative
                if(x < vx){
                    x = vx;
                    moved = true;
                }
                if(y < vy){
                    y = vy;
                    moved = true;
                }
                return moved ? [x, y] : false;
            };
        }(),
        getHeight : function(contentHeight){
             var h = this.dom.offsetHeight || 0;
             h = contentHeight !== true ? h : h-this.getBorderWidth("tb")-this.getPadding("tb");
             return h < 0 ? 0 : h;
        },
         
          getMargins : function(side){
              if(!side){
                  return {
                      top: parseInt(this.getStyle("margin-top"), 10) || 0,
                      left: parseInt(this.getStyle("margin-left"), 10) || 0,
                      bottom: parseInt(this.getStyle("margin-bottom"), 10) || 0,
                      right: parseInt(this.getStyle("margin-right"), 10) || 0
                  };
              }else{
                  return this.addStyles(side, El.margins);
               }
          },
         
         
          getLeft : function(local){
              if(!local){
                  return this.getX();
              }else{
                  return parseInt(this.getStyle("left"), 10) || 0;
              }
          },
          getRegion : function(){
              return D.getRegion(this.dom);
          },
          
          getRight : function(local){
              if(!local){
                  return this.getX() + this.getWidth();
              }else{
                  return (this.getLeft(true) + this.getWidth()) || 0;
              }
          },
        getScroll : function(){
            var d = this.dom, doc = document;
            if(d == doc || d == doc.body){
                var l, t;
                if(Ext.isIE && Ext.isStrict){
                    l = doc.documentElement.scrollLeft || (doc.body.scrollLeft || 0);
                    t = doc.documentElement.scrollTop || (doc.body.scrollTop || 0);
                }else{
                    l = window.pageXOffset || (doc.body.scrollLeft || 0);
                    t = window.pageYOffset || (doc.body.scrollTop || 0);
                }
                return {left: l, top: t};
            }else{
                return {left: d.scrollLeft, top: d.scrollTop};
            }
        },
               
         getSize : function(contentSize){
             return {width: this.getWidth(contentSize), height: this.getHeight(contentSize)};
         },
         

         getStyle : function(){
             return view && view.getComputedStyle ?
                 function(prop){
                     var el = this.dom, v, cs, camel;
                     if(prop == 'float'){
                         prop = "cssFloat";
                     }
                     if(v = el.style[prop]){
                         return v;
                     }
                     if(cs = view.getComputedStyle(el, "")){
                         if(!(camel = propCache[prop])){
                             camel = propCache[prop] = prop.replace(camelRe, camelFn);
                         }
                         return cs[camel];
                     }
                     return null;
                 } :
                 function(prop){
                     if (!this.dom.style) {

                     }
                     var el = this.dom, v, cs, camel;
                     if(prop == 'opacity'){
                         if(typeof el.style.filter == 'string'){
                             var m = el.style.filter.match(/alpha\(opacity=(.*)\)/i);
                             if(m){
                                 var fv = parseFloat(m[1]);
                                 if(!isNaN(fv)){
                                     return fv ? fv / 100 : 0;
                                 }
                             }
                         }
                         return 1;
                     }else if(prop == 'float'){
                         prop = "styleFloat";
                     }
                     if(!(camel = propCache[prop])){
                         camel = propCache[prop] = prop.replace(camelRe, camelFn);
                     }
                     if(v = this.dom.style[camel]){
                         return v;
                     }
                     if(cs = this.dom.currentStyle){
                         return cs[camel];
                     }
                     return null;
                 };
         }(),  
          getTop : function(local) {
              if(!local){
                  return this.getY();
              }else{
                  return parseInt(this.getStyle("top"), 10) || 0;
              }
          },
         
         getWidth : function(contentWidth){
             var w = this.dom.offsetWidth || 0;
             w = contentWidth !== true ? w : w-this.getBorderWidth("lr")-this.getPadding("lr");
             return w < 0 ? 0 : w;
         },

         getViewSize : function(){
             var d = this.dom, doc = document, aw = 0, ah = 0;
             if(d == doc || d == doc.body){
                 return {width : D.getViewWidth(), height: D.getViewHeight()};
             }else{
                 return {
                     width : d.clientWidth,
                     height: d.clientHeight
                 };
             }
         },
         
         getStyleSize : function(){
             var w, h, d = this.dom, s = d.style;
             if(s.width && s.width != 'auto'){
                 w = parseInt(s.width, 10);
                 if(Ext.isBorderBox){
                    w -= this.getFrameWidth('lr');
                 }
             }
             if(s.height && s.height != 'auto'){
                 h = parseInt(s.height, 10);
                 if(Ext.isBorderBox){
                    h -= this.getFrameWidth('tb');
                 }
             }
             return {width: w || this.getWidth(true), height: h || this.getHeight(true)};
         },
         
         
        getFrameWidth : function(sides, onlyContentBox){
             return onlyContentBox && Ext.isBorderBox ? 0 : (this.getPadding(sides) + this.getBorderWidth(sides));
        },
         
        getPadding : function(side){
             return this.addStyles(side, El.paddings);
        },
        getX : function(){
            return D.getX(this.dom);
        },
        getXY : function(){
            return D.getXY(this.dom);
        },
        getY : function(){
            return D.getY(this.dom);
        },
        hasClass : function(className){
            return className && (' '+this.dom.className+' ').indexOf(' '+className+' ') != -1;
        },
         is : function(simpleSelector){
             return Ext.DomQuery.is(this.dom, simpleSelector);
         },
         isVisible : function(deep) {
             var vis = !(this.getStyle("visibility") == "hidden" || this.getStyle("display") == "none");
             if(deep !== true || !vis){
                 return vis;
             }
             var p = this.dom.parentNode;
             while(p && p.tagName.toLowerCase() != "body"){
                 if(!Ext.fly(p, '_isVisible').isVisible()){
                     return false;
                 }
                 p = p.parentNode;
             }
             return true;
         },
         
        position : function(pos, zIndex, x, y){
            if(!pos){
               if(this.getStyle('position') == 'static'){
                   this.setStyle('position', 'relative');
               }
            }else{
                this.setStyle("position", pos);
            }
            if(zIndex){
                this.setStyle("z-index", zIndex);
            }
            if(x !== undefined && y !== undefined){
                this.setXY([x, y]);
            }else if(x !== undefined){
                this.setX(x);
            }else if(y !== undefined){
                this.setY(y);
            }
        },

         
        removeClass : function(className){
              if(!className || !this.dom.className){
                  return this;
              }
              if(className instanceof Array){
                  for(var i = 0, len = className.length; i < len; i++) {
                      this.removeClass(className[i]);
                  }
              }else{
                  if(this.hasClass(className)){
                      var re = this.classReCache[className];
                      if (!re) {
                         re = new RegExp('(?:^|\\s+)' + className + '(?:\\s+|$)', "g");
                         this.classReCache[className] = re;
                      }
                      this.dom.className =
                          this.dom.className.replace(re, " ");
                  }
              }
              return this;
          },
          
         setVisibilityMode : function(visMode){
             this.visibilityMode = visMode;
             return this;
         },
        setY : function(y){
            D.setY(this.dom, y);
            return this;
        },
        setX : function(x){
            D.setX(this.dom, x);
            return this;
        },
        setXY : function(pos){
           D.setXY(this.dom, pos);
           return this;
        },
        
        
         setStyle : function(prop, value){
             if(typeof prop == "string"){
                 var camel;
                 if(!(camel = propCache[prop])){
                     camel = propCache[prop] = prop.replace(camelRe, camelFn);
                 }
                 if(camel == 'opacity') {
                     this.setOpacity(value);
                 }else{
                     this.dom.style[camel] = value;
                 }
             }else{
                 for(var style in prop){
                     if(typeof prop[style] != "function"){
                        this.setStyle(style, prop[style]);
                     }
                 }
             }
             return this;
         },
        
        translatePoints : function(el, x, y){
            var dom = el.dom;
            if(typeof x == 'object' || x instanceof Array){
                y = x[1]; x = x[0];
            }
            var p = el.getStyle('position');
            var o = el.getXY();
   
   
            var l = parseInt(dom.style.left, 10);
            var t = parseInt(dom.style.top, 10);
            
            if(isNaN(l)){
                l = (p == "relative") ? 0 : dom.offsetLeft;
            }
            if(isNaN(t)){
                t = (p == "relative") ? 0 : dom.offsetTop;
            }
            return {left: (x - o[0] + l), top: (y - o[1] + t)};
        },
       
     };
     
     var flyFn = function(){};
     flyFn.prototype = El.prototype;
     var _cls = new flyFn();
     
     // dom is optional
     El.Flyweight = function(dom){
         this.dom = dom;
     };
     
     El.Flyweight.prototype = _cls;
     El.Flyweight.prototype.isFlyweight = true;
     
     El._flyweights = {};
     
     El.fly = function(el, named){
         named = named || '_global';
         el = Ext.getDom(el);
         if(!el){
             return null;
         }
         if(!El._flyweights[named]){
             El._flyweights[named] = new El.Flyweight();
         }
         El._flyweights[named].dom = el;
         return El._flyweights[named];
     };
     
     
     Ext.get = El.get;
     Ext.fly = El.fly;
     
     El.borders = {l: "border-left-width", r: "border-right-width", t: "border-top-width", b: "border-bottom-width"};
     El.paddings = {l: "padding-left", r: "padding-right", t: "padding-top", b: "padding-bottom"};
     El.margins = {l: "margin-left", r: "margin-right", t: "margin-top", b: "margin-bottom"};
     $wnd._el = El;
   }-*/;

  private native static void loadFormat() /*-{
     var Ext = $wnd.Ext;
     Ext.util = {};
     Ext.util.Format = function(){
      var trimRe = /^\s+|\s+$/g;
      return {
          ellipsis : function(value, len){
              if(value && value.length > len){
                  return value.substr(0, len-3)+"...";
              }
              return value;
          },
          undef : function(value){
              return value !== undefined ? value : "";
          },
          defaultValue : function(value, defaultValue){
              return value !== undefined && value !== '' ? value : defaultValue;
          },
          htmlEncode : function(value){
              return !value ? value : String(value).replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
          },
          htmlDecode : function(value){
              return !value ? value : String(value).replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"');
          },
          trim : function(value){
              return String(value).replace(trimRe, "");
          },
          substr : function(value, start, length){
              return String(value).substr(start, length);
          },
          lowercase : function(value){
              return String(value).toLowerCase();
          },
          uppercase : function(value){
              return String(value).toUpperCase();
          },
          capitalize : function(value){
              return !value ? value : value.charAt(0).toUpperCase() + value.substr(1).toLowerCase();
          },
          call : function(value, fn){
              if(arguments.length > 2){
                  var args = Array.prototype.slice.call(arguments, 2);
                  args.unshift(value);
                  return eval(fn).apply(window, args);
              }else{
                  return eval(fn).call(window, value);
              }
          },
          usMoney : function(v){
              v = (Math.round((v-0)*100))/100;
              v = (v == Math.floor(v)) ? v + ".00" : ((v*10 == Math.floor(v*10)) ? v + "0" : v);
              v = String(v);
              var ps = v.split('.');
              var whole = ps[0];
              var sub = ps[1] ? '.'+ ps[1] : '.00';
              var r = /(\d+)(\d{3})/;
              while (r.test(whole)) {
                  whole = whole.replace(r, '$1' + ',' + '$2');
              }
              v = whole + sub;
              if(v.charAt(0) == '-'){
                  return '-$' + v.substr(1);
              }
              return "$" +  v;
          },


          date : function(v, format){
              if(!v){
                  return "";
              }
              if(!(v instanceof Date)){
                  v = new Date(Date.parse(v));
              }
              return v.dateFormat(format || "m/d/Y");
          },


          dateRenderer : function(format){
              return function(v){
                  return Ext.util.Format.date(v, format);
              };
          },

          // private
          stripTagsRE : /<\/?[^>]+>/gi,
          

          stripTags : function(v){
              return !v ? v : String(v).replace(this.stripTagsRE, "");
          },

          stripScriptsRe : /(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)/ig,


          stripScripts : function(v){
              return !v ? v : String(v).replace(this.stripScriptsRe, "");
          },


          fileSize : function(size){
              if(size < 1024) {
                  return size + " bytes";
              } else if(size < 1048576) {
                  return (Math.round(((size*10) / 1024))/10) + " KB";
              } else {
                  return (Math.round(((size*10) / 1048576))/10) + " MB";
              }
          },

          math : function(){
              var fns = {};
              return function(v, a){
                  if(!fns[a]){
                      fns[a] = new Function('v', 'return v ' + a + ';');
                  }
                  return fns[a](v);
              }
          }()
      };
     }();
   }-*/;

  private native static void loadDomQuery() /*-{
    var Ext = $wnd.Ext;
    Ext.DomQuery = function() {
      var cache      = {
      }, simpleCache = {
      }, valueCache  = {
      };
      var nonSpace = /\S/;
      var trimRe = /^\s+|\s+$/g;
      var tplRe = /\{(\d+)\}/g;
      var modeRe = /^(\s?[\/>+~]\s?|\s|$)/;
      var tagTokenRe = /^(#)?([\w-\*]+)/;
      var nthRe = /(\d*)n\+?(\d*)/, nthRe2 = /\D/;
      var document = $doc;
      function child(p, index) {
        var i = 0;
        var n = p.firstChild;
        while (n) {
          if (n.nodeType == 1) {
            if (++ i == index) {
              return n;
            }
          }
          n = n.nextSibling;
        }
        return null;
      }
      ;
      function next(n) {
        while ((n = n.nextSibling) && n.nodeType != 1);
        return n;
      }
      ;
      function prev(n) {
        while ((n = n.previousSibling) && n.nodeType != 1);
        return n;
      }
      ;
      function children(d) {
        var n = d.firstChild, ni = - 1;
        while (n) {
          var nx = n.nextSibling;
          if (n.nodeType == 3 && ! nonSpace.test(n.nodeValue)) {
            d.removeChild(n);
          } else {
            n.nodeIndex = ++ ni;
          }
          n = nx;
        }
        return this;
      }
      ;
      $wnd.__byClassName = function(c, a, v) {
        if (! v) {
          return c;
        }
        var r = [], ri = - 1, cn;
        for (var i = 0, ci; ci = c[i]; i++) {
          if ((' ' + ci.className + ' ').indexOf(v) != - 1) {
            r[++ ri] = ci;
          }
        }
        return r;
      };
      function attrValue(n, attr) {
        if (! n.tagName && typeof n.length != "undefined") {
          n = n[0];
        }
        if (! n) {
          return null;
        }
        if (attr == "for") {
          return n.htmlFor;
        }
        if (attr == "class" || attr == "className") {
          return n.className;
        }
        return n.getAttribute(attr) || n[attr];
      }
      ;
      $wnd.__getNodes = function(ns, mode, tagName) {
        var result = [], ri = - 1, cs;
        if (! ns) {
          return result;
        }
        tagName = tagName || "*";
        if (typeof ns.getElementsByTagName != "undefined") {
          ns = [ns];
        }
        if (! mode) {
          for (var i = 0, ni; ni = ns[i]; i++) {
            cs = ni.getElementsByTagName(tagName);
            for (var j = 0, ci; ci = cs[j]; j++) {
              result[++ ri] = ci;
            }
          }
        } else if (mode == "/" || mode == ">") {
          var utag = tagName.toUpperCase();
          for (var i = 0, ni, cn; ni = ns[i]; i++) {
            cn = ni.children || ni.childNodes;
            for (var j = 0, cj; cj = cn[j]; j++) {
              if (cj.nodeName == utag || cj.nodeName == tagName || tagName == '*') {
                result[++ ri] = cj;
              }
            }
          }
        } else if (mode == "+") {
          var utag = tagName.toUpperCase();
          for (var i = 0, n; n = ns[i]; i++) {
            while ((n = n.nextSibling) && n.nodeType != 1);
            if (n && (n.nodeName == utag || n.nodeName == tagName || tagName == '*')) {
              result[++ ri] = n;
            }
          }
        } else if (mode == "~") {
          for (var i = 0, n; n = ns[i]; i++) {
            while ((n = n.nextSibling)
             && (n.nodeType != 1 || (tagName == '*' || n.tagName.toLowerCase() != tagName)));
            if (n) {
              result[++ ri] = n;
            }
          }
        }
        return result;
      };
      function concat(a, b) {
        if (b.slice) {
          return a.concat(b);
        }
        for (var i = 0, l = b.length; i < l; i++) {
          a[a.length] = b[i];
        }
        return a;
      }
      $wnd.__byTag = function(cs, tagName) {
        if (cs.tagName || cs == document) {
          cs = [cs];
        }
        if (! tagName) {
          return cs;
        }
        var r = [], ri = - 1;
        tagName = tagName.toLowerCase();
        for (var i = 0, ci; ci = cs[i]; i++) {
          if (ci.nodeType == 1 && ci.tagName.toLowerCase() == tagName) {
            r[++ ri] = ci;
          }
        }
        return r;
      };
      $wnd.__byId = function(cs, attr, id) {
        if (cs.tagName || cs == document) {
          cs = [cs];
        }
        if (! id) {
          return cs;
        }
        var r = [], ri = - 1;
        for (var i = 0, ci; ci = cs[i]; i++) {
          if (ci && ci.id == id) {
            r[++ ri] = ci;
            return r;
          }
        }
        return r;
      };
      $wnd.__byAttribute = function(cs, attr, value, op, custom) {
        var r = [], ri = - 1, st = custom == "{";
        var f = Ext.DomQuery.operators[op];
        for (var i = 0, ci; ci = cs[i]; i++) {
          var a;
          if (st) {
            a = Ext.DomQuery.getStyle(ci, attr);
          } else if (attr == "class" || attr == "className") {
            a = ci.className;
          } else if (attr == "for") {
            a = ci.htmlFor;
          } else if (attr == "href") {
            a = ci.getAttribute("href", 2);
          } else {
            a = ci.getAttribute(attr);
          }
          if ((f && f(a, value)) || (! f && a)) {
            r[++ ri] = ci;
          }
        }
        return r;
      };
      $wnd.__byPseudo = function(cs, name, value) {
        return Ext.DomQuery.pseudos[name](cs, value);
      } ;
      // This is for IE MSXML which does not support expandos.
      // IE runs the same speed using setAttribute, however FF slows way down
      // and Safari completely fails so they need to continue to use expandos.
      var isIE = window.ActiveXObject ? true : false;
      // this eval is stop the compressor from
      // renaming the variable to something shorter
      eval("var batch = 30803;");
      var key = 30803;
      function nodupIEXml(cs) {
        var d = ++ key;
        cs[0].setAttribute("_nodup", d);
        var r = [cs[0]];
        for (var i = 1, len = cs.length; i < len; i++) {
          var c = cs[i];
          if (! c.getAttribute("_nodup") != d) {
            c.setAttribute("_nodup", d);
            r[r.length] = c;
          }
        }
        for (var i = 0, len = cs.length; i < len; i++) {
          cs[i].removeAttribute("_nodup");
        }
        return r;
      }
      $wnd.GXTnodup = function(cs) {
        if (! cs) {
          return [];
        }
        var len = cs.length, c, i, r = cs, cj, ri = - 1;
        if (! len || typeof cs.nodeType != "undefined" || len == 1) {
          return cs;
        }
        if (isIE && typeof cs[0].selectSingleNode != "undefined") {
          return nodupIEXml(cs);
        }
        var d = ++ key;
        cs[0]._nodup = d;
        for (i = 1; c = cs[i]; i++) {
          if (c._nodup != d) {
            c._nodup = d;
          } else {
            r = [];
            for (var j = 0; j < i; j++) {
              r[++ ri] = cs[j];
            }
            for (j = i + 1; cj = cs[j]; j++) {
              if (cj._nodup != d) {
                cj._nodup = d;
                r[++ ri] = cj;
              }
            }
            return r;
          }
        }
        return r;
      }
      function quickDiffIEXml(c1, c2) {
        var d = ++ key;
        for (var i = 0, len = c1.length; i < len; i++) {
          c1[i].setAttribute("_qdiff", d);
        }
        var r = [];
        for (var i = 0, len = c2.length; i < len; i++) {
          if (c2[i].getAttribute("_qdiff") != d) {
            r[r.length] = c2[i];
          }
        }
        for (var i = 0, len = c1.length; i < len; i++) {
          c1[i].removeAttribute("_qdiff");
        }
        return r;
      }
      function quickDiff(c1, c2) {
        var len1 = c1.length;
        if (! len1) {
          return c2;
        }
        if (isIE && c1[0].selectSingleNode) {
          return quickDiffIEXml(c1, c2);
        }
        var d = ++ key;
        for (var i = 0; i < len1; i++) {
          c1[i]._qdiff = d;
        }
        var r = [];
        for (var i = 0, len = c2.length; i < len; i++) {
          if (c2[i]._qdiff != d) {
            r[r.length] = c2[i];
          }
        }
        return r;
      }
      function quickId(ns, mode, root, id) {
        if (ns == root) {
          var d = root.ownerDocument || root;
          return d.getElementById(id);
        }
        ns = $wnd.__getNodes(ns, mode, "*");
        return $wnd.__byId(ns, null, id);
      }
      return {
        getStyle: function(el, name) {
          return Ext.fly(el).getStyle(name);
        },
        compile: function(path, type) {
          type = type || "select";
          var fn = ["var f = function(root){\n var mode; ++batch; var n = root || document;\n"];
          var q = path, mode, lq;
          var tk = Ext.DomQuery.matchers;
          var tklen = tk.length;
          var mm;
          // accept leading mode switch
          var lmode = q.match(modeRe);
          if (lmode && lmode[1]) {
            fn[fn.length] = 'mode="' + lmode[1].replace(trimRe, "") + '";';
            q = q.replace(lmode[1], "");
          }
          // strip leading slashes
          while (path.substr(0, 1) == "/") {
            path = path.substr(1);
          }
          while (q && lq != q) {
            lq = q;
            var tm = q.match(tagTokenRe);
            if (type == "select") {
              if (tm) {
                if (tm[1] == "#") {
                  fn[fn.length] = 'n = quickId(n, mode, root, "' + tm[2] + '");';
                } else {
                  fn[fn.length] = 'n = $wnd.__getNodes(n, mode, "' + tm[2] + '");';
                }
                q = q.replace(tm[0], "");
              } else if (q.substr(0, 1) != '@') {
                fn[fn.length] = 'n = $wnd.__getNodes(n, mode, "*");';
              }
            } else {
              if (tm) {
                if (tm[1] == "#") {
                  fn[fn.length] = 'n = $wnd.__byId(n, null, "' + tm[2] + '");';
                } else {
                  fn[fn.length] = 'n = $wnd.__byTag(n, "' + tm[2] + '");';
                }
                q = q.replace(tm[0], "");
              }
            }
            while (!(mm = q.match(modeRe))) {
              var matched = false;
              for (var j = 0; j < tklen; j++) {
                var t = tk[j];
                var m = q.match(t.re);
                if (m) {
                  fn[fn.length] = t.select.replace(tplRe, function(x, i) {
                    return m[i];
                  } );
                  q = q.replace(m[0], "");
                  matched = true;
                  break;
                }
              }
              // prevent infinite loop on bad selector
              if (! matched) {
                throw 'Error parsing selector, parsing failed at "' + q + '"';
              }
            }
            if (mm[1]) {
              fn[fn.length] = 'mode="' + mm[1].replace(trimRe, "") + '";';
              q = q.replace(mm[1], "");
            }
          }
          fn[fn.length] = "return $wnd.GXTnodup(n);\n}";
          eval(fn.join(""));
          return f;
        },
        select: function(path, root, type) {
          if (! root || root == document) {
            root = document;
          }
          if (typeof root == "string") {
            root = document.getElementById(root);
          }
          var paths = path.split(",");
          var results = [];
          for (var i = 0, len = paths.length; i < len; i++) {
            var p = paths[i].replace(trimRe, "");
            if (! cache[p]) {
              cache[p] = Ext.DomQuery.compile(p);
              if (! cache[p]) {
                throw p + " is not a valid selector";
              }
            }
            var result = cache[p](root);
            if (result && result != document) {
              results = results.concat(result);
            }
          }
          if (paths.length > 1) {
            return $wnd.GXTnodup(results);
          }
          return results;
        },
        selectNode: function(path, root) {
          return Ext.DomQuery.select(path, root)[0];
        },
        selectValue: function(path, root, defaultValue) {
          path = path.replace(trimRe, "");
          if (! valueCache[path]) {
            valueCache[path] = Ext.DomQuery.compile(path, "select");
          }
          var n = valueCache[path](root);
          n = n[0] ? n[0] : n;
          var v = (n && n.firstChild ? n.firstChild.nodeValue : null);
          return ((v === null || v === undefined || v === '') ? defaultValue : v);
        },
        selectNumber: function(path, root, defaultValue) {
          var v = Ext.DomQuery.selectValue(path, root, defaultValue || 0);
          return parseFloat(v);
        },
        is: function(el, ss) {
          if (typeof el == "string") {
            el = document.getElementById(el);
          }
          var isArray = (el instanceof Array);
          var result = Ext.DomQuery.filter(isArray ? el : [el], ss);
          return isArray ? (result.length == el.length) : (result.length > 0);
        },
        filter: function(els, ss, nonMatches) {
          ss = ss.replace(trimRe, "");
          if (! simpleCache[ss]) {
            simpleCache[ss] = Ext.DomQuery.compile(ss, "simple");
          }
          var result = simpleCache[ss](els);
          return nonMatches ? quickDiff(result, els) : result;
        },
        matchers: [
          {
            re: /^\.([\w-]+)/,
            select: 'n = $wnd.__byClassName(n, null, " {1} ");'
          },
          {
            re: /^\:([\w-]+)(?:\(((?:[^\s>\/]*|.*?))\))?/,
            select: 'n = $wnd.__byPseudo(n, "{1}", "{2}");'
          },
          {
            re: /^(?:([\[\{])(?:@)?([\w-]+)\s?(?:(=|.=)\s?['"]?(.*?)["']?)?[\]\}])/,
            select: 'n = $wnd.__byAttribute(n, "{2}", "{4}", "{3}", "{1}");'
          },
          {
            re: /^#([\w-]+)/,
            select: 'n = byId(n, null, "{1}");'
          },
          {
            re: /^@([\w-]+)/,
            select: 'return {firstChild:{nodeValue:attrValue(n, "{1}")}};'
          }
        ],
        operators: {
          "=": function(a, v) {
            return a == v;
          },
          "!=": function(a, v) {
            return a != v;
          },
          "^=": function(a, v) {
            return a && a.substr(0, v.length) == v;
          },
          "$=": function(a, v) {
            return a && a.substr(a.length - v.length) == v;
          },
          "*=": function(a, v) {
            return a && a.indexOf(v) !== - 1;
          },
          "%=": function(a, v) {
            return (a % v) == 0;
          },
          "|=": function(a, v) {
            return a && (a == v || a.substr(0, v.length + 1) == v + '-');
          },
          "~=": function(a, v) {
            return a && (' ' + a + ' ').indexOf(' ' + v + ' ') != - 1;
          }
        },
        pseudos: {
          "first-child": function(c) {
            var r = [], ri = - 1, n;
            for (var i = 0, ci; ci = n = c[i]; i++) {
              while ((n = n.previousSibling) && n.nodeType != 1);
              if (! n) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "last-child": function(c) {
            var r = [], ri = - 1, n;
            for (var i = 0, ci; ci = n = c[i]; i++) {
              while ((n = n.nextSibling) && n.nodeType != 1);
              if (! n) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "nth-child": function(c, a) {
            var r = [], ri = - 1;
            var m = nthRe.exec(a == "even" && "2n" || a == "odd" && "2n+1" || ! nthRe2.test(a) && "n+" + a || a);
            var f = (m[1] || 1) - 0, l = m[2] - 0;
            for (var i = 0, n; n = c[i]; i++) {
              var pn = n.parentNode;
              if (batch != pn._batch) {
                var j = 0;
                for (var cn = pn.firstChild; cn; cn = cn.nextSibling) {
                  if (cn.nodeType == 1) {
                    cn.nodeIndex = ++ j;
                  }
                }
                pn._batch = batch;
              }
              if (f == 1) {
                if (l == 0 || n.nodeIndex == l) {
                  r[++ ri] = n;
                }
              } else if ((n.nodeIndex + l) % f == 0) {
                r[++ ri] = n;
              }
            }
            return r;
          },
          "only-child": function(c) {
            var r = [], ri = - 1;
            ;
            for (var i = 0, ci; ci = c[i]; i++) {
              if (! prev(ci) && ! next(ci)) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "empty": function(c) {
            var r = [], ri = - 1;
            for (var i = 0, ci; ci = c[i]; i++) {
              var cns = ci.childNodes, j = 0, cn, empty = true;
              while (cn = cns[j]) {
                ++ j;
                if (cn.nodeType == 1 || cn.nodeType == 3) {
                  empty = false;
                  break;
                }
              }
              if (empty) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "contains": function(c, v) {
            var r = [], ri = - 1;
            for (var i = 0, ci; ci = c[i]; i++) {
              if ((ci.textContent || ci.innerText || '').indexOf(v) != - 1) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "nodeValue": function(c, v) {
            var r = [], ri = - 1;
            for (var i = 0, ci; ci = c[i]; i++) {
              if (ci.firstChild && ci.firstChild.nodeValue == v) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "checked": function(c) {
            var r = [], ri = - 1;
            for (var i = 0, ci; ci = c[i]; i++) {
              if (ci.checked == true) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "not": function(c, ss) {
            return Ext.DomQuery.filter(c, ss, true);
          },
          "any": function(c, selectors) {
            var ss = selectors.split('|');
            var r = [], ri = - 1, s;
            for (var i = 0, ci; ci = c[i]; i++) {
              for (var j = 0; s = ss[j]; j++) {
                if (Ext.DomQuery.is(ci, s)) {
                  r[++ ri] = ci;
                  break;
                }
              }
            }
            return r;
          },
          "odd": function(c) {
            return this["nth-child"](c, "odd");
          },
          "even": function(c) {
            return this["nth-child"](c, "even");
          },
          "nth": function(c, a) {
            return c[a - 1] || [];
          },
          "first": function(c) {
            return c[0] || [];
          },
          "last": function(c) {
            return c[c.length - 1] || [];
          },
          "has": function(c, ss) {
            var s = Ext.DomQuery.select;
            var r = [], ri = - 1;
            for (var i = 0, ci; ci = c[i]; i++) {
              if (s(ss, ci).length > 0) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "next": function(c, ss) {
            var is = Ext.DomQuery.is;
            var r = [], ri = - 1;
            for (var i = 0, ci; ci = c[i]; i++) {
              var n = next(ci);
              if (n && is(n, ss)) {
                r[++ ri] = ci;
              }
            }
            return r;
          },
          "prev": function(c, ss) {
            var is = Ext.DomQuery.is;
            var r = [], ri = - 1;
            for (var i = 0, ci; ci = c[i]; i++) {
              var n = prev(ci);
              if (n && is(n, ss)) {
                r[++ ri] = ci;
              }
            }
            return r;
          }
        }
      };
    }();
   }-*/;

  private native static void loadDomHelper() /*-{
       var Ext = $wnd.Ext;
       Ext.DomHelper = function(){
          var tempTableEl = null;
          var emptyTags = /^(?:br|frame|hr|img|input|link|meta|range|spacer|wbr|area|param|col)$/i;
          var tableRe = /^table|tbody|tr|td$/i;
          
     
          var createHtml = function(o){
              if(typeof o == 'string'){
                  return o;
              }
              var b = "";
              if(!o.tag){
                  o.tag = "div";
              }
              b += "<" + o.tag;
              for(var attr in o){
                  if(attr == "tag" || attr == "children" || attr == "cn" || attr == "html" || typeof o[attr] == "function") continue;
                  if(attr == "style"){
                      var s = o["style"];
                      if(typeof s == "function"){
                          s = s.call();
                      }
                      if(typeof s == "string"){
                          b += ' style="' + s + '"';
                      }else if(typeof s == "object"){
                          b += ' style="';
                          for(var key in s){
                              if(typeof s[key] != "function"){
                                  b += key + ":" + s[key] + ";";
                              }
                          }
                          b += '"';
                      }
                  }else{
                      if(attr == "cls"){
                          b += ' class="' + o["cls"] + '"';
                      }else if(attr == "htmlFor"){
                          b += ' for="' + o["htmlFor"] + '"';
                      }else{
                          b += " " + attr + '="' + o[attr] + '"';
                      }
                  }
              }
              if(emptyTags.test(o.tag)){
                  b += "/>";
              }else{
                  b += ">";
                  var cn = o.children || o.cn;
                  if(cn){
                      if(cn instanceof Array){
                          for(var i = 0, len = cn.length; i < len; i++) {
                              b += createHtml(cn[i], b);
                          }
                      }else{
                          b += createHtml(cn, b);
                      }
                  }
                  if(o.html){
                      b += o.html;
                  }
                  b += "</" + o.tag + ">";
              }
              return b;
          };
     
     
          var createDom = function(o, parentNode){
              var el = document.createElement(o.tag||'div');
              var useSet = el.setAttribute ? true : false; // In IE some elements don't have setAttribute
              for(var attr in o){
                  if(attr == "tag" || attr == "children" || attr == "cn" || attr == "html" || attr == "style" || typeof o[attr] == "function") continue;
                  if(attr=="cls"){
                      el.className = o["cls"];
                  }else{
                      if(useSet) el.setAttribute(attr, o[attr]);
                      else el[attr] = o[attr];
                  }
              }
              Ext.DomHelper.applyStyles(el, o.style);
              var cn = o.children || o.cn;
              if(cn){
                  if(cn instanceof Array){
                      for(var i = 0, len = cn.length; i < len; i++) {
                          createDom(cn[i], el);
                      }
                  }else{
                      createDom(cn, el);
                  }
              }
              if(o.html){
                  el.innerHTML = o.html;
              }
              if(parentNode){
                 parentNode.appendChild(el);
              }
              return el;
          };
     
          var ieTable = function(depth, s, h, e){
              tempTableEl.innerHTML = [s, h, e].join('');
              var i = -1, el = tempTableEl;
              while(++i < depth){
                  el = el.firstChild;
              }
              return el;
          };
     
          // kill repeat to save bytes
          var ts = '<table>',
              te = '</table>',
              tbs = ts+'<tbody>',
              tbe = '</tbody>'+te,
              trs = tbs + '<tr>',
              tre = '</tr>'+tbe;
     
     
          var insertIntoTable = function(tag, where, el, html){
              if(!tempTableEl){
                  tempTableEl = document.createElement('div');
              }
              var node;
              var before = null;
              if(tag == 'td'){
                  if(where == 'afterbegin' || where == 'beforeend'){ // INTO a TD
                      return;
                  }
                  if(where == 'beforebegin'){
                      before = el;
                      el = el.parentNode;
                  } else{
                      before = el.nextSibling;
                      el = el.parentNode;
                  }
                  node = ieTable(4, trs, html, tre);
              }
              else if(tag == 'tr'){
                  if(where == 'beforebegin'){
                      before = el;
                      el = el.parentNode;
                      node = ieTable(3, tbs, html, tbe);
                  } else if(where == 'afterend'){
                      before = el.nextSibling;
                      el = el.parentNode;
                      node = ieTable(3, tbs, html, tbe);
                  } else{ // INTO a TR
                      if(where == 'afterbegin'){
                          before = el.firstChild;
                      }
                      node = ieTable(4, trs, html, tre);
                  }
              } else if(tag == 'tbody'){
                  if(where == 'beforebegin'){
                      before = el;
                      el = el.parentNode;
                      node = ieTable(2, ts, html, te);
                  } else if(where == 'afterend'){
                      before = el.nextSibling;
                      el = el.parentNode;
                      node = ieTable(2, ts, html, te);
                  } else{
                      if(where == 'afterbegin'){
                          before = el.firstChild;
                      }
                      node = ieTable(3, tbs, html, tbe);
                  }
              } else{ // TABLE
                  if(where == 'beforebegin' || where == 'afterend'){ // OUTSIDE the table
                      return;
                  }
                  if(where == 'afterbegin'){
                      before = el.firstChild;
                  }
                  node = ieTable(2, ts, html, te);
              }
              el.insertBefore(node, before);
              return node;
          };
     
     
          return {
     
          useDom : false,
     
          markup : function(o){
              return createHtml(o);
          },
          applyStyles : function(el, styles){
              if(styles){
                 if(typeof styles == "string"){
                     var re = /\s?([a-z\-]*)\:\s?([^;]*);?/gi;
                     var matches;
                     while ((matches = re.exec(styles)) != null){
                         Ext.fly(el).setStyle(matches[1], matches[2]);
                     }
                 }else if (typeof styles == "object"){
                     for (var style in styles){
                        el.setStyle(style, styles[style]);
                     }
                 }else if (typeof styles == "function"){
                      Ext.DomHelper.applyStyles(el, styles.call());
                 }
              }
          },
     
     
          insertHtml : function(where, el, html){
              where = where.toLowerCase();
              if(el.insertAdjacentHTML){
                  if(tableRe.test(el.tagName)){
                      var rs;
                      if(rs = insertIntoTable(el.tagName.toLowerCase(), where, el, html)){
                          return rs;
                      }
                  }
                  switch(where){
                      case "beforebegin":
                          el.insertAdjacentHTML('BeforeBegin', html);
                          return el.previousSibling;
                      case "afterbegin":
                          el.insertAdjacentHTML('AfterBegin', html);
                          return el.firstChild;
                      case "beforeend":
                          el.insertAdjacentHTML('BeforeEnd', html);
                          return el.lastChild;
                      case "afterend":
                          el.insertAdjacentHTML('AfterEnd', html);
                          return el.nextSibling;
                  }
                  throw 'Illegal insertion point -> "' + where + '"';
              }
              var range = el.ownerDocument.createRange();
              var frag;
              switch(where){
                   case "beforebegin":
                      range.setStartBefore(el);
                      frag = range.createContextualFragment(html);
                      el.parentNode.insertBefore(frag, el);
                      return el.previousSibling;
                   case "afterbegin":
                      if(el.firstChild){
                          range.setStartBefore(el.firstChild);
                          frag = range.createContextualFragment(html);
                          el.insertBefore(frag, el.firstChild);
                          return el.firstChild;
                      }else{
                          el.innerHTML = html;
                          return el.firstChild;
                      }
                  case "beforeend":
                      if(el.lastChild){
                          range.setStartAfter(el.lastChild);
                          frag = range.createContextualFragment(html);
                          el.appendChild(frag);
                          return el.lastChild;
                      }else{
                          el.innerHTML = html;
                          return el.lastChild;
                      }
                  case "afterend":
                      range.setStartAfter(el);
                      frag = range.createContextualFragment(html);
                      el.parentNode.insertBefore(frag, el.nextSibling);
                      return el.nextSibling;
                  }
                  throw 'Illegal insertion point -> "' + where + '"';
          },
     
     
          insertBefore : function(el, o, returnElement){
              return this.doInsert(el, o, returnElement, "beforeBegin");
          },
     
     
          insertAfter : function(el, o, returnElement){
              return this.doInsert(el, o, returnElement, "afterEnd", "nextSibling");
          },
     
     
          insertFirst : function(el, o, returnElement){
              return this.doInsert(el, o, returnElement, "afterBegin", "firstChild");
          },
     
          // private
          doInsert : function(el, o, returnElement, pos, sibling){
              var newNode;
              if(this.useDom){
                  newNode = createDom(o, null);
                  (sibling === "firstChild" ? el : el.parentNode).insertBefore(newNode, sibling ? el[sibling] : el);
              }else{
                  var html = createHtml(o);
                  newNode = this.insertHtml(pos, el, html);
              }
              return newNode;
          },
     
     
          append : function(el, o, returnElement){
     
              var newNode;
              if(this.useDom){
                  newNode = createDom(o, null);
                  el.appendChild(newNode);
              }else{
                  var html = createHtml(o);
                  newNode = this.insertHtml("beforeEnd", el, html);
              }
              return newNode;
          },
     
     
          overwrite : function(el, o, returnElement){
              el.innerHTML = createHtml(o);
              return el.firstChild;
          },
     
     
          createTemplate : function(o){
              var html = createHtml(o);
              return new Ext.Template(html);
          }
          };
       }();
   }-*/;

  private native static void loadTemplate() /*-{
       var Ext = $wnd.Ext;
      Ext.Template = function(html){
        var a = arguments;
        if(html instanceof Array){
            html = html.join("");
        }else if(a.length > 1){
            var buf = [];
            for(var i = 0, len = a.length; i < len; i++){
                if(typeof a[i] == 'object'){
                    Ext.apply(this, a[i]);
                }else{
                    buf[buf.length] = a[i];
                }
            }
            html = buf.join('');
        }
   
        this.html = html;
        if(this.compiled){
            this.compile();   
        }
     };
     Ext.Template.prototype = {
   
        applyTemplate : function(values){
            if(this.compiled){
                return this.compiled(values);
            }
            var useF = this.disableFormats !== true;
            var fm = Ext.util.Format, tpl = this;
            var fn = function(m, name, format, args){
                if(format && useF){
                    if(format.substr(0, 5) == "this."){
                        return tpl.call(format.substr(5), values[name], values);
                    }else{
                        if(args){
                            // quoted values are required for strings in compiled templates, 
                            // but for non compiled we need to strip them
                            // quoted reversed for jsmin
                            var re = /^\s*['"](.*)["']\s*$/;
                            args = args.split(',');
                            for(var i = 0, len = args.length; i < len; i++){
                                args[i] = args[i].replace(re, "$1");
                            }
                            args = [values[name]].concat(args);
                        }else{
                            args = [values[name]];
                        }
                        return fm[format].apply(fm, args);
                    }
                }else{
                    return values[name] !== undefined ? values[name] : "";
                }
            };
            return this.html.replace(this.re, fn);
        },
        
   
        set : function(html, compile){
            this.html = html;
            this.compiled = null;
            if(compile){
                this.compile();
            }
            return this;
        },
   
        disableFormats : true,
        
   
        re : /\{([\w-]+)(?:\:([\w\.]*)(?:\((.*?)?\))?)?\}/g,
        
   
        compile : function(){
            var fm = Ext.util.Format;
            var useF = this.disableFormats !== true;
            var sep = Ext.isGecko ? "+" : ",";
            var fn = function(m, name, format, args){
                if(format && useF){
                    args = args ? ',' + args : "";
                    if(format.substr(0, 5) != "this."){
                        format = "fm." + format + '(';
                    }else{
                        format = 'this.call("'+ format.substr(5) + '", ';
                        args = ", values";
                    }
                }else{
                    args= ''; format = "(values['" + name + "'] == undefined ? '' : ";
                }
                return "'"+ sep + format + "values['" + name + "']" + args + ")"+sep+"'";
            };
            var body;
            // branched to use + in gecko and [].join() in others
            if(Ext.isGecko){
                body = "this.compiled = function(values){ return '" +
                       this.html.replace(/\\/g, '\\\\').replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn) +
                        "';};";
            }else{
                body = ["this.compiled = function(values){ return ['"];
                body.push(this.html.replace(/\\/g, '\\\\').replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn));
                body.push("'].join('');};");
                body = body.join('');
            }
            eval(body);
            return this;
        },
        
        // private function used to call members
        call : function(fnName, value, allValues){
            return this[fnName](value, allValues);
        },
        
   
        insertFirst: function(el, values, returnElement){
            return this.doInsert('afterBegin', el, values, returnElement);
        },
   
   
        insertBefore: function(el, values, returnElement){
            return this.doInsert('beforeBegin', el, values, returnElement);
        },
   
   
        insertAfter : function(el, values, returnElement){
            return this.doInsert('afterEnd', el, values, returnElement);
        },
        
   
        append : function(el, values, returnElement){
            return this.doInsert('beforeEnd', el, values, returnElement);
        },
   
        doInsert : function(where, el, values, returnEl){
            //el = Ext.getDom(el);
            var newNode = Ext.DomHelper.insertHtml(where, el, this.applyTemplate(values));
            return returnEl ? Ext.get(newNode, true) : newNode;
        },
   
   
        overwrite : function(el, values, returnElement){
            el = Ext.getDom(el);
            el.innerHTML = this.applyTemplate(values);
            return returnElement ? Ext.get(el.firstChild, true) : el.firstChild;
        }
     };
   
     Ext.Template.prototype.apply = Ext.Template.prototype.applyTemplate;
   
     // backwards compat
     Ext.DomHelper.Template = Ext.Template;
   
   
     Ext.Template.from = function(el, config){
        el = Ext.getDom(el);
        return new Ext.Template(el.value || el.innerHTML, config || '');
     };
   }-*/;
  
  private static native void loadAdapter() /*-{
    var document = $doc;
    var Ext = {}
    Ext.lib = {};
    
    
    Ext.lib.Dom = {
      getViewWidth : function(full) {
          return full ? this.getDocumentWidth() : this.getViewportWidth();
      },

      getViewHeight : function(full) {
          return full ? this.getDocumentHeight() : this.getViewportHeight();
      },
      
      getRegion : function(el) {
           var r = {};
           r.left = this.getX(el);
           r.top = this.getY(el);
           r.right = r.left + $wnd._el.fly(el).getWidth();
           r.bottom = r.top + $wnd._el.fly(el).getHeight(); 
          return r;
      },

      getDocumentHeight: function() {
          var scrollHeight = (document.compatMode != "CSS1Compat") ? $doc.body.scrollHeight : $doc.documentElement.scrollHeight;
          return Math.max(scrollHeight, this.getViewportHeight());
      },

      getDocumentWidth: function() {
          var scrollWidth = (document.compatMode != "CSS1Compat") ? $doc.body.scrollWidth : $doc.documentElement.scrollWidth;
          return Math.max(scrollWidth, this.getViewportWidth());
      },
      getViewportHeight: function(){
          if($wnd.Ext.isIE){
              return Ext.isStrict ? document.documentElement.clientHeight :
                       document.body.clientHeight;
          }else{
              return $wnd.self.innerHeight;
          }
      },

      getViewportWidth: function() {
          if(Ext.isIE){
              return Ext.isStrict ? document.documentElement.clientWidth :
                       document.body.clientWidth;
          }else{
              return $wnd.self.innerWidth;
          }
      },
      
      setX : function(el, x) {
          this.setXY(el, [x, false]);
      },

      setY : function(el, y) {
          this.setXY(el, [false, y]);
      },
      setXY : function(el, xy) {
          var dom = el;
          el = new $wnd.Ext.Element(el);
          
          el.position();
          var pts = el.translatePoints(el, xy);
          if (xy[0] !== false) {
              dom.style.left = pts.left + "px";
          }
          if (xy[1] !== false) {
              dom.style.top = pts.top + "px";
          }
      },
      getX : function(el) {
          return this.getXY(el)[0];
      },
      getY : function(el) {
          this.getXY(el)[1];
          return this.getXY(el)[1];
      },
      getXY : function(el) {
            var fly = $wnd.Ext.fly;
            var p, pe, b, scroll, bd = ($doc.body || $doc.documentElement);
            if(el == bd){
                return [0, 0];
            }
            if (el.getBoundingClientRect) {
                b = el.getBoundingClientRect();
                scroll = fly(document).getScroll();
                return [b.left + scroll.left, b.top + scroll.top];
            }
            var x = 0, y = 0;
            
            p = el;


            var hasAbsolute = el.style.position == "absolute";

            while (p) {

                x += p.offsetLeft;
                y += p.offsetTop;

                if (!hasAbsolute && fly(p).getStyle("position") == "absolute") {
                    hasAbsolute = true;
                }

                if (Ext.isGecko) {
                    pe = fly(p);
                    
                    var bt = parseInt(pe.getStyle("borderTopWidth"), 10) || 0;
                    var bl = parseInt(pe.getStyle("borderLeftWidth"), 10) || 0;


                    x += bl;
                    y += bt;


                    if (p != el && pe.getStyle('overflow') != 'visible') {
                        x += bl;
                        y += bt;
                    }
                }
                p = p.offsetParent;
            }

            if (Ext.isSafari && hasAbsolute) {
                x -= bd.offsetLeft;
                y -= bd.offsetTop;
            }

            if (Ext.isGecko && !hasAbsolute) {
                var dbd = fly(bd);
                x += parseInt(dbd.getStyle("borderLeftWidth"), 10) || 0;
                y += parseInt(dbd.getStyle("borderTopWidth"), 10) || 0;
            }

            p = el.parentNode;
            while (p && p != bd) {
                if (!Ext.isOpera || (p.tagName != 'TR' && fly(p).getStyle("display") != "inline")) {
                    x -= p.scrollLeft;
                    y -= p.scrollTop;
                }
                p = p.parentNode;
            }
            return [x, y];
      }
    };
    $wnd.Ext = Ext;
  }-*/;

  private static native void loadDate() /*-{
  
var Ext = $wnd.Ext;  
  
Date.prototype.getFirstDateOfMonth = function() {
    return new Date(this.getFullYear(), this.getMonth(), 1);
};
Date.prototype.getLastDateOfMonth = function() {
    return new Date(this.getFullYear(), this.getMonth(), this.getDaysInMonth());
};
Date.prototype.getDaysInMonth = function() {
    Date.daysInMonth[1] = this.isLeapYear() ? 29 : 28;
    return Date.daysInMonth[this.getMonth()];
};
Date.prototype.isLeapYear = function() {
    var year = this.getFullYear();
    return ((year & 3) == 0 && (year % 100 || (year % 400 == 0 && year)));
};
Date.prototype.getWeekOfYear = function() {
    // adapted from http://www.merlyn.demon.co.uk/weekcalc.htm
    var ms1d = 864e5; // milliseconds in a day
    var ms7d = 7 * ms1d; // milliseconds in a week
    var DC3 = Date.UTC(this.getFullYear(), this.getMonth(), this.getDate() + 3) / ms1d; // an Absolute Day Number
    var AWN = Math.floor(DC3 / 7); // an Absolute Week Number
    var Wyr = new Date(AWN * ms7d).getUTCFullYear();
    return AWN - Math.floor(Date.UTC(Wyr, 0, 7) / ms7d) + 1;
};
Date.prototype.getDayOfYear = function() {
    var num = 0;
    Date.daysInMonth[1] = this.isLeapYear() ? 29 : 28;
    for (var i = 0; i < this.getMonth(); ++i) {
        num += Date.daysInMonth[i];
    }
    return num + this.getDate() - 1;
};

Date.daysInMonth = [31,28,31,30,31,30,31,31,30,31,30,31];

Date.prototype.clone = function() {
  return new Date(this.getTime());
};

Date.prototype.clearTime = function(clone){
    if(clone){
        return this.clone().clearTime();
    }
    this.setHours(0);
    this.setMinutes(0);
    this.setSeconds(0);
    this.setMilliseconds(0);
    return this;
};

if(Ext.isSafari){
    Date.brokenSetMonth = Date.prototype.setMonth;
    Date.prototype.setMonth = function(num){
    if(num <= -1){
      var n = Math.ceil(-num);
      var back_year = Math.ceil(n/12);
      var month = (n % 12) ? 12 - n % 12 : 0 ;
      this.setFullYear(this.getFullYear() - back_year);
      return Date.brokenSetMonth.call(this, month);
    } else {
      return Date.brokenSetMonth.apply(this, arguments);
    }
  };
}


Date.MILLI = "ms";

Date.SECOND = "s";

Date.MINUTE = "mi";

Date.HOUR = "h";

Date.DAY = "d";

Date.MONTH = "mo";

Date.YEAR = "y";
Date.prototype.add = function(interval, value){
  var d = this.clone();
  if (!interval || value === 0) return d;
  switch(interval.toLowerCase()){
    case Date.MILLI:
      d.setMilliseconds(this.getMilliseconds() + value);
      break;
    case Date.SECOND:
      d.setSeconds(this.getSeconds() + value);
      break;
    case Date.MINUTE:
      d.setMinutes(this.getMinutes() + value);
      break;
    case Date.HOUR:
      d.setHours(this.getHours() + value);
      break;
    case Date.DAY:
      d.setDate(this.getDate() + value);
      break;
    case Date.MONTH:
      var day = this.getDate();
      if(day > 28){
          day = Math.min(day, this.getFirstDateOfMonth().add('mo', value).getLastDateOfMonth().getDate());
      }
      d.setDate(day);
      d.setMonth(this.getMonth() + value);
      break;
    case Date.YEAR:
      d.setFullYear(this.getFullYear() + value);
      break;
  }
  return d;
};

$wnd.Date = Date;
  }-*/;
}
