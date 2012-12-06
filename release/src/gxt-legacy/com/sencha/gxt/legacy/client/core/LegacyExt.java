/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.core;

/**
 * Loads the ext javascript to make available Template and XTemplate objects
 * available. Only the parts of the library being used are loaded and were
 * manually moved into the class. No event, listener, or animation code is used.
 * Provides low level dom related functions. A reference to the Ext instance is
 * set $wnd.GXT.Ext.
 */
class LegacyExt {
  static {
    ensureExt();
  }

  private native static void ensureExt() /*-{
		@com.sencha.gxt.core.client.dom.Ext::loadExt()();
		@com.sencha.gxt.core.client.dom.Ext::loadDomHelper()();
  }-*/;

  native static void loadFormat() /*-{
	var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
	if (!!Ext.util) {
		return;
	}
	Ext.util = {};
	Ext.util.Format = function() {
		var trimRe = /^\s+|\s+$/g;
		return {
			ellipsis : function(value, len) {
				if (value && value.length > len) {
					return value.substr(0, len - 3) + "...";
				}
				return value;
			},
			undef : function(value) {
				return value !== undefined ? value : "";
			},
			defaultValue : function(value, defaultValue) {
				return value !== undefined && value !== '' ? value
						: defaultValue;
			},
			htmlEncode : function(value) {
				return !value ? value : String(value)
						.replace(/&/g, "&amp;").replace(/>/g, "&gt;")
						.replace(/</g, "&lt;").replace(/"/g, "&quot;");
			},
			htmlDecode : function(value) {
				return !value ? value : String(value)
						.replace(/&amp;/g, "&").replace(/&gt;/g, ">")
						.replace(/&lt;/g, "<").replace(/&quot;/g, '"');
			},
			trim : function(value) {
				return String(value).replace(trimRe, "");
			},
			substr : function(value, start, length) {
				return String(value).substr(start, length);
			},
			lowercase : function(value) {
				return String(value).toLowerCase();
			},
			uppercase : function(value) {
				return String(value).toUpperCase();
			},
			capitalize : function(value) {
				return !value ? value : value.charAt(0).toUpperCase()
						+ value.substr(1).toLowerCase();
			},
			call : function(value, fn) {
				if (arguments.length > 2) {
					var args = Array.prototype.slice.call(arguments, 2);
					args.unshift(value);
					return eval(fn).apply(window, args);
				} else {
					return eval(fn).call(window, value);
				}
			},
			usMoney : function(v) {
				v = (Math.round((v - 0) * 100)) / 100;
				v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math
						.floor(v * 10)) ? v + "0" : v);
				v = String(v);
				var ps = v.split('.');
				var whole = ps[0];
				var sub = ps[1] ? '.' + ps[1] : '.00';
				var r = /(\d+)(\d{3})/;
				while (r.test(whole)) {
					whole = whole.replace(r, '$1' + ',' + '$2');
				}
				v = whole + sub;
				if (v.charAt(0) == '-') {
					return '-$' + v.substr(1);
				}
				return "$" + v;
			},

			date : function(v, format) {
				if (!v) {
					return "";
				}
			},

			currency : function(v) {
				if (!v) {
					return "";
				}
				return @com.sencha.gxt.core.client.util.Format::currency(D)(v);
			},

			number : function(v, format) {
				if (!v) {
					return "";
				}
				try {
					return @com.sencha.gxt.core.client.util.Format::number(DLjava/lang/String;)(v, format || "#");
				} catch (err) {
				}
				return "";
			},

			decimal : function(v) {
				if (!v) {
					return "";
				}
				return @com.sencha.gxt.core.client.util.Format::decimal(D)(v);
			},

			scientific : function(v) {
				if (!v) {
					return "";
				}
				return @com.sencha.gxt.core.client.util.Format::scientific(D)(v);
			},

			// private
			stripTagsRE : /<\/?[^>]+>/gi,

			stripTags : function(v) {
				return !v ? v : String(v).replace(this.stripTagsRE, "");
			},

			stripScriptsRe : /(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)/ig,

			stripScripts : function(v) {
				return !v ? v : String(v).replace(this.stripScriptsRe, "");
			},

			fileSize : function(size) {
				if (size < 1024) {
					return size + " bytes";
				} else if (size < 1048576) {
					return (Math.round(((size * 10) / 1024)) / 10) + " KB";
				} else {
					return (Math.round(((size * 10) / 1048576)) / 10)
							+ " MB";
				}
			},

			math : function() {
				var fns = {};
				return function(v, a) {
					if (!fns[a]) {
						fns[a] = new Function('v', 'return v ' + a + ';');
					}
					return fns[a](v);
				}
			}()
		};
	}();
}-*/;

  native static void loadTemplate() /*-{
		var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
		if (!!Ext.Template) {
			return;
		}
		Ext.Template = function(html) {
			var a = arguments;
			if (Ext.isArray(html)) {
				html = html.join("");
			} else if (a.length > 1) {
				var buf = [];
				for ( var i = 0, len = a.length; i < len; i++) {
					if (typeof a[i] == 'object') {
						Ext.apply(this, a[i]);
					} else {
						buf[buf.length] = a[i];
					}
				}
				html = buf.join('');
			}

			this.html = html;
			if (this.compiled) {
				this.compile();
			}
		};

		Ext.Template.prototype = {

			applyTemplate : function(values) {
				if (this.compiled) {
					return this.compiled(values);
				}
				var useF = this.disableFormats !== true;
				var fm = Ext.util.Format, tpl = this;
				var fn = function(m, name, format, args) {
					if (format && useF) {
						if (format.substr(0, 5) == "this.") {
							return tpl.call(format.substr(5), values[name],
									values);
						} else {
							if (args) {
								// quoted values are required for strings in compiled templates,
								// but for non compiled we need to strip them
								// quoted reversed for jsmin
								var re = /^\s*['"](.*)["']\s*$/;
								args = args.split(',');
								for ( var i = 0, len = args.length; i < len; i++) {
									args[i] = args[i].replace(re, "$1");
								}
								args = [ values[name] ].concat(args);
							} else {
								args = [ values[name] ];
							}
							return fm[format].apply(fm, args);
						}
					} else {
						return values[name] !== undefined ? values[name] : "";
					}
				};
				return this.html.replace(this.re, fn);
			},

			set : function(html, compile) {
				this.html = html;
				this.compiled = null;
				if (compile) {
					this.compile();
				}
				return this;
			},

			disableFormats : false,

			re : /\{([\w-]+)(?:\:([\w\.]*)(?:\((.*?)?\))?)?\}/g,

			compile : function() {
				var fm = Ext.util.Format;
				var useF = this.disableFormats !== true;
				var sep = @com.sencha.gxt.core.client.GXT::isGecko()() ? "+"
						: ",";
				var fn = function(m, name, format, args) {
					if (format && useF) {
						args = args ? ',' + args : "";
						if (format.substr(0, 5) != "this.") {
							format = "fm." + format + '(';
						} else {
							format = 'this.call("' + format.substr(5) + '", ';
							args = ", values";
						}
					} else {
						args = '';
						format = "(values['" + name + "'] == undefined ? '' : ";
					}
					return "'" + sep + format + "values['" + name + "']" + args
							+ ")" + sep + "'";
				};
				var body;
				// branched to use + in gecko and [].join() in others
				if (@com.sencha.gxt.core.client.GXT::isGecko()()) {
					body = "this.compiled = function(values){ return '"
							+ this.html.replace(/\\/g, '\\\\').replace(
									/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'")
									.replace(this.re, fn) + "';};";
				} else {
					body = [ "this.compiled = function(values){ return ['" ];
					body.push(this.html.replace(/\\/g, '\\\\').replace(
							/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(
							this.re, fn));
					body.push("'].join('');};");
					body = body.join('');
				}
				eval(body);
				return this;
			},

			call : function(fnName, value, allValues) {
				return this[fnName](value, allValues);
			},

			insertFirst : function(el, values, returnElement) {
				return this.doInsert('afterBegin', el, values, returnElement);
			},

			insertBefore : function(el, values, returnElement) {
				return this.doInsert('beforeBegin', el, values, returnElement);
			},

			insertAfter : function(el, values, returnElement) {
				return this.doInsert('afterEnd', el, values, returnElement);
			},

			append : function(el, values, returnElement) {
				return this.doInsert('beforeEnd', el, values, returnElement);
			},

			doInsert : function(where, el, values, returnEl) {
				el = Ext.getDom(el);
				var newNode = Ext.DomHelper.insertHtml(where, el, this
						.applyTemplate(values));
				return returnEl ? Ext.get(newNode, true) : newNode;
			},

			overwrite : function(el, values, returnElement) {
				el = Ext.getDom(el);
				el.innerHTML = this.applyTemplate(values);
				return returnElement ? Ext.get(el.firstChild, true)
						: el.firstChild;
			}
		};

		// backwards compat
		Ext.DomHelper.Template = Ext.Template;
  }-*/;

  static native void loadXTemplate() /*-{
		var Ext = @com.sencha.gxt.core.client.dom.Ext::ext;
		if (!!Ext.XTemplate) {
			return;
		}
		Ext.XTemplate = function() {
			Ext.XTemplate.superclass.constructor.apply(this, arguments);
			var s = this.html;

			s = [ '<tpl>', s, '</tpl>' ].join('');

			var re = /<tpl\b[^>]*>((?:(?=([^<]+))\2|<(?!tpl\b[^>]*>))*?)<\/tpl>/;

			var nameRe = /^<tpl\b[^>]*?for="(.*?)"/;
			var ifRe = /^<tpl\b[^>]*?if="(.*?)"/;
			var execRe = /^<tpl\b[^>]*?exec="(.*?)"/;
			var m, id = 0;
			var tpls = [];

			while (m = s.match(re)) {
				var m2 = m[0].match(nameRe);
				var m3 = m[0].match(ifRe);
				var m4 = m[0].match(execRe);
				var exp = null, fn = null, exec = null;
				var name = m2 && m2[1] ? m2[1] : '';
				if (m3) {
					exp = m3 && m3[1] ? m3[1] : null;
					if (exp) {
						fn = new Function('values', 'parent', 'xindex',
								'xcount', 'with(values){ return '
										+ (Ext.util.Format.htmlDecode(exp))
										+ '; }');
					}
				}
				if (m4) {
					exp = m4 && m4[1] ? m4[1] : null;
					if (exp) {
						exec = new Function('values', 'parent', 'xindex',
								'xcount', 'with(values){ '
										+ (Ext.util.Format.htmlDecode(exp))
										+ '; }');
					}
				}
				if (name) {
					switch (name) {
					case '.':
						name = new Function('values', 'parent',
								'with(values){ return values; }');
						break;
					case '..':
						name = new Function('values', 'parent',
								'with(values){ return parent; }');
						break;
					default:
						name = new Function('values', 'parent',
								'with(values){ return ' + name + '; }');
					}
				}
				tpls.push({
					id : id,
					target : name,
					exec : exec,
					test : fn,
					body : m[1] || ''
				});
				s = s.replace(m[0], '{xtpl' + id + '}');
				++id;
			}
			for ( var i = tpls.length - 1; i >= 0; --i) {
				this.compileTpl(tpls[i]);
			}
			this.master = tpls[tpls.length - 1];
			this.tpls = tpls;
		};
		var override = function(origclass, overrides){
			if(overrides){
				var p = origclass.prototype;
				for(var method in overrides){
					p[method] = overrides[method];
				}
			}
		};
		var extend = (function(){
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
					override(sb, o);
				};
				sbp.override = io;
				override(sb, overrides);
				return sb;
			};
		}());

		extend(
						Ext.XTemplate,
						Ext.Template,
						{
							// private
							re : /\{([\w-\.\#]+)(?:\:([\w\.]*)(?:\((.*?)?\))?)?(\s?[\+\-\*\\]\s?[\d\.\+\-\*\\\(\)]+)?\}/g,
							// private
							codeRe : /\{\[((?:\\\]|.|\n)*?)\]\}/g,

							// private
							applySubTemplate : function(id, values, parent,
									xindex, xcount) {
								var t = this.tpls[id];

								if (t.test
										&& !t.test.call(this, values, parent,
												xindex, xcount)) {
									return '';
								}
								if (t.exec
										&& t.exec.call(this, values, parent,
												xindex, xcount)) {
									return '';
								}
								var vs = t.target ? t.target.call(this, values,
										parent) : values;
								parent = t.target ? values : parent;
								if (t.target && Ext.isArray(vs)) {
									var buf = [];
									for ( var i = 0, len = vs.length; i < len; i++) {
										buf[buf.length] = t.compiled.call(this,
												vs[i], parent, i + 1, len, Ext.util.Format);
									}
									return buf.join('');
								}
								return t.compiled.call(this, vs, parent,
										xindex, xcount, Ext.util.Format);
							},

							compileTpl : function(tpl) {
								var fm = Ext.util.Format;
								var useF = this.disableFormats !== true;
								var sep = @com.sencha.gxt.core.client.GXT::isGecko()() ? "+"
										: ",";
								var fn = function(m, name, format, args, math) {
									if (name.substr(0, 4) == 'xtpl') {
										return "'"
												+ sep
												+ 'this.applySubTemplate('
												+ name.substr(4)
												+ ', values, parent, xindex, xcount)'
												+ sep + "'";
									}
									var v;
									if (name === '.') {
										v = 'values';
									} else if (name === '#') {
										v = 'xindex';
									} else if (name.indexOf('.') != -1) {
										v = name;
									} else {
										v = "values['" + name + "']";
									}
									if (math) {
										v = '(' + v + math + ')';
									}
									if (format && useF) {
										args = args ? ',' + args : "";
										if (format.substr(0, 5) != "this.") {
											format = "fm." + format + '(';
										} else {
											format = 'this.call("'
													+ format.substr(5) + '", ';
											args = ", values";
										}
									} else {
										args = '';
										format = "(" + v
												+ " === undefined ? '' : ";
									}
									return "'" + sep + format + v + args + ")"
											+ sep + "'";
								};
								var codeFn = function(m, code) {
									return "'" + sep + '(' + code + ')' + sep
											+ "'";
								};

								var tempBody = tpl.body;
								var tempTpl = tpl;
								var body;
								// branched to use + in gecko and [].join() in others
								if (@com.sencha.gxt.core.client.GXT::isGecko()()) {
									body = "var temp = function(values, parent, xindex, xcount, fm){ return '"
											+ tempBody.replace(/(\r\n|\n)/g,
													'\\n').replace(/'/g, "\\'")
													.replace(this.re, fn)
													.replace(this.codeRe,
															codeFn) + "';};";
								} else {
									body = [ "var temp = function(values, parent, xindex, xcount, fm){ return ['" ];
									body.push(tempBody.replace(/(\r\n|\n)/g,
											'\\n').replace(/'/g, "\\'")
											.replace(this.re, fn).replace(
													this.codeRe, codeFn));
									body.push("'].join('');};");
									body = body.join('');
								}
								eval(body);
								tempTpl.compiled = temp;
								return this;
							},

							applyTemplate : function(values) {
								return this.master.compiled.call(this, values,
										{}, 1, 1, Ext.util.Format);
							},

							compile : function() {
								return this;
							}

						});

		Ext.XTemplate.prototype.apply = Ext.XTemplate.prototype.applyTemplate;
  }-*/;
}
