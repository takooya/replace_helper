BizQQWPA.define("wpa.SelectPanel", "lang.browser,util.Style,util.className,util.events,util.offset,util.css,util.proxy,lang.extend", function (c) {
    var b = c("Style"), l = c("className"), d = c("events"), g = c("offset"), i = c("browser"), j = c("css"),
        m = c("proxy"), k = c("extend");
    var h = 1;
    var a = document;
    var f = function (v, p) {
        var r = function (x, z) {
            for (var w = 0, y = z.length; w !== y; x.push(z[w++])) {
            }
            return x
        }, t = r([], v.childNodes), q = t.length, o = 0, u, s = null;
        while (q > o) {
            u = t[o++];
            if (u.nodeType !== h) {
                continue
            }
            if (u.getAttribute("id") === p) {
                s = u;
                break
            }
            r(t, u.childNodes);
            q = t.length
        }
        return s
    };
    var n = {
        container: a.getElementsByTagName("body")[0],
        template: ['<div class="WPA3-SELECT-PANEL">', '<div class="WPA3-SELECT-PANEL-TOP">', '<a id="WPA3-SELECT-PANEL-CLOSE" href="javascript:;" class="WPA3-SELECT-PANEL-CLOSE"></a>', "</div>", '<div class="WPA3-SELECT-PANEL-MAIN">', '<p class="WPA3-SELECT-PANEL-GUIDE">请选择发起聊天的方式：</p>', '<div class="WPA3-SELECT-PANEL-SELECTS">', '<a id="WPA3-SELECT-PANEL-AIO-CHAT" href="javascript:;" class="WPA3-SELECT-PANEL-CHAT WPA3-SELECT-PANEL-AIO-CHAT">', '<span class="WPA3-SELECT-PANEL-QQ WPA3-SELECT-PANEL-QQ-AIO"></span>', '<span class="WPA3-SELECT-PANEL-LABEL">QQ帐号聊天</span>', "</a>", '<a id="WPA3-SELECT-PANEL-ANONY-CHAT" href="javascript:;" class="WPA3-SELECT-PANEL-CHAT WPA3-SELECT-PANEL-ANONY-CHAT">', '<span class="WPA3-SELECT-PANEL-QQ WPA3-SELECT-PANEL-QQ-ANONY"></span>', '<span class="WPA3-SELECT-PANEL-LABEL">匿名聊天</span>', "</a>", "</div>", "</div>", '<div class="WPA3-SELECT-PANEL-BOTTOM">', '<a target="_blank" href="http://im.qq.com" class="WPA3-SELECT-PANEL-INSTALL">安装QQ</a>', "</div>", "</div>"].join(""),
        cssText: [".WPA3-SELECT-PANEL { z-index:2147483647; width:463px; height:292px; margin:0; padding:0; border:1px solid #d4d4d4; background-color:#fff; border-radius:5px; box-shadow:0 0 15px #d4d4d4;}", '.WPA3-SELECT-PANEL * { position:static; z-index:auto; top:auto; left:auto; right:auto; bottom:auto; width:auto; height:auto; max-height:auto; max-width:auto; min-height:0; min-width:0; margin:0; padding:0; border:0; clear:none; clip:auto; background:transparent; color:#333; cursor:auto; direction:ltr; filter:; float:none; font:normal normal normal 12px "Helvetica Neue", Arial, sans-serif; line-height:16px; letter-spacing:normal; list-style:none; marks:none; overflow:visible; page:auto; quotes:none; -o-set-link-source:none; size:auto; text-align:left; text-decoration:none; text-indent:0; text-overflow:clip; text-shadow:none; text-transform:none; vertical-align:baseline; visibility:visible; white-space:normal; word-spacing:normal; word-wrap:normal; -webkit-box-shadow:none; -moz-box-shadow:none; -ms-box-shadow:none; -o-box-shadow:none; box-shadow:none; -webkit-border-radius:0; -moz-border-radius:0; -ms-border-radius:0; -o-border-radius:0; border-radius:0; -webkit-opacity:1; -moz-opacity:1; -ms-opacity:1; -o-opacity:1; opacity:1; -webkit-outline:0; -moz-outline:0; -ms-outline:0; -o-outline:0; outline:0; -webkit-text-size-adjust:none; font-family:Microsoft YaHei,Simsun;}', ".WPA3-SELECT-PANEL a { cursor:auto;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-TOP { height:25px;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-CLOSE { float:right; display:block; width:47px; height:25px; background:url(http://combo.b.qq.com/crm/wpa/release/3.3/wpa/views/SelectPanel-sprites.png) no-repeat;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-CLOSE:hover { background-position:0 -25px;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-MAIN { padding:23px 20px 45px;}", '.WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-GUIDE { margin-bottom:42px; font-family:"Microsoft Yahei"; font-size:16px;}', ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-SELECTS { width:246px; height:111px; margin:0 auto;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-CHAT { float:right; display:block; width:88px; height:111px; background:url(http://combo.b.qq.com/crm/wpa/release/3.3/wpa/views/SelectPanel-sprites.png) no-repeat 0 -80px;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-CHAT:hover { background-position:-88px -80px;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-AIO-CHAT { float:left;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-QQ { display:block; width:76px; height:76px; margin:6px; background:url(http://combo.b.qq.com/crm/wpa/release/3.3/wpa/views/SelectPanel-sprites.png) no-repeat -50px 0;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-QQ-ANONY { background-position:-130px 0;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-LABEL { display:block; padding-top:10px; color:#00a2e6; text-align:center;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-BOTTOM { padding:0 20px; text-align:right;}", ".WPA3-SELECT-PANEL .WPA3-SELECT-PANEL-INSTALL { color:#8e8e8e;}"].join(""),
        modal: true
    };
    b.add("_WPA_SELECT_PANEL_STYLE", n.cssText);
    var e = function (o) {
        this.opts = k({}, o, n);
        this.render()
    };
    e.prototype = {
        render: function () {
            var r = this, s = this.opts, q = this.container = s.container;
            var p = a.createElement("div"), o;
            p.innerHTML = s.template;
            this.$el = o = p.firstChild;
            d.addEvent(f(o, "WPA3-SELECT-PANEL-CLOSE"), "click", function () {
                r.remove();
                s.onClose && s.onClose()
            });
            d.addEvent(f(o, "WPA3-SELECT-PANEL-AIO-CHAT"), "click", function () {
                r.remove();
                s.onAIOChat && s.onAIOChat()
            });
            d.addEvent(f(o, "WPA3-SELECT-PANEL-ANONY-CHAT"), "click", function () {
                r.remove();
                s.onAnonyChat && s.onAnonyChat()
            });
            (function () {
                try {
                    q.appendChild(o)
                } catch (t) {
                    setTimeout(arguments.callee, 1);
                    return
                }
                if (s.modal) {
                    r.renderModal()
                }
                r.setCenter()
            })()
        }, show: function () {
            this.css("display", "block");
            this.modal && j(this.modal, "display", "block");
            return this
        }, hide: function () {
            this.css("display", "none");
            this.modal && j(this.modal, "display", "none");
            return this
        }, remove: function () {
this.\${el.parentNode.removeChild(this.}el);
            this.modal && this.modal.parentNode.removeChild(this.modal);
            return this
        }, css: function () {
            var o = [this.$el].concat(Array.prototype.slice.call(arguments));
            return j.apply(this, o)
        }, setCenter: function () {
            this.css({position: "absolute", top: "50%", left: "50%"});
            var q = {
                position: "fixed",
                marginLeft: "-" + this.outerWidth() / 2 + "px",
                marginTop: "-" + this.outerHeight() / 2 + "px"
            };
            var o = a.compatMode === "BackCompat";
            if ((i.msie && parseInt(i.version, 10) < 7) || o) {
                q.position = "absolute";
                q.marginTop = 0;
                var p = q.top = (g.getClientHeight() - this.outerHeight()) / 2;
                setInterval(m(this.$el, function () {
                    this.style.top = g.getScrollTop() + p + "px"
                }), 128)
            }
            this.css(q)
        }, renderModal: function () {
            var s = this.container, t = j(s, "width"), r = j(s, "height"), p = j(s, "overflow");
            var q = a.createElement("div"), u = {
                position: "fixed",
                top: 0,
                left: 0,
                zIndex: 2147483647,
                width: g.getClientWidth() + "px",
                height: g.getClientHeight() + "px",
                backgroundColor: "white",
                opacity: 0.1,
                filter: "alpha(opacity=10)"
            };
            var o = a.compatMode === "BackCompat";
            if ((i.msie && parseInt(i.version, 10) < 7) || o) {
                u.position = "absolute";
                setInterval(m(q, function () {
                    this.style.top = g.getScrollTop() + "px"
                }), 128)
            }
            j(q, u);
            s.insertBefore(q, this.$el);
            this.modal = q;
            d.addEvent(window, "resize", m(q, function () {
                j(this, {width: g.getClientWidth() + "px", height: g.getClientHeight() + "px"})
            }))
        }, outerWidth: function () {
            return this.$el.offsetWidth
        }, outerHeight: function () {
            return this.$el.offsetHeight
        }
    };
    return e
});
BizQQWPA.define("util.css", "util.contains", function (e) {
    var b = e("contains");
    var d = document.defaultView && document.defaultView.getComputedStyle ? function (g, f) {
        f = f.replace(/([A-Z])/g, "-$1").toLowerCase();
        var h = document.defaultView.getComputedStyle(g, "");
        return h && h.getPropertyValue(f)
    } : function (g, f) {
        return g.currentStyle[f]
    };
    var c = function (l, j) {
        if (!b(l, document)) {
            return j()
        }
        var k = {opacity: 0, filter: "alpha(opacity=0)"}, i = l.parentNode, g = l.nextSibling,
            h = document.createElement("div"), f;
        h.appendChild(l);
        a(h, k);
        a(l, k);
        document.body.appendChild(h);
        f = j();
        g ? i.insertBefore(l, g) : i.appendChild(l);
        h.parentNode.removeChild(h);
        return f
    };
    var a = function (f, j, g) {
        var i;
        if (!g) {
            if (typeof j === "string") {
                return c(f, function () {
                    return d(f, j)
                })
            }
            if (typeof j !== "object") {
                new TypeError("Arg style should be string or object")
            }
            i = [];
            for (var h in j) {
                i.push(h + ":" + j[h])
            }
            i = i.join(";")
        } else {
            i = j + ":" + g
        }
        i = i.replace(/([A-Z])/g, "-$1").toLowerCase();
        f.style.cssText += ";" + i;
        return f
    };
    return a
});
BizQQWPA.define("lang.extend", "lang.each,lang.typeEnhance", function (b) {
    var a = b("each"), c = b("typeEnhance");
    return function () {
        var g = Array.prototype.slice.call(arguments), f = false, d = g.shift(),
            h = c.type(g[g.length - 1]) === "boolean" ? g.pop() : false;
        if (d === true) {
            f = true;
            d = g.shift()
        }
        var e = h ? function (k, j) {
            a(j, function (m, l) {
                k[m] = l
            })
        } : function (k, j) {
            a(j, function (m, l) {
                if (typeof k[m] !== "undefined") {
                    return
                }
                k[m] = l
            })
        };
        var i = h ? function (k, j) {
            a(j, function (m, l) {
                if (c.type(l) !== "object") {
                    k[m] = l;
                    return
                }
                k[m] = arguments.callee({}, l)
            })
        } : function (k, j) {
            a(j, function (m, l) {
                if (typeof k[m] !== "undefined") {
                    return
                }
                if (c.type(l) !== "object") {
                    k[m] = l;
                    return
                }
                k[m] = arguments.callee({}, l)
            })
        };
        a(g, function (j, k) {
            f ? i(d, k) : e(d, k)
        });
        return d
    }
});
BizQQWPA.define("util.contains", function () {
    return document.documentElement.contains ? function (a, d) {
        var b = a.nodeType === 9 ? a.documentElement : a, c = d && d.parentNode;
        return a === c || !!(c && c.nodeType === 1 && b.contains && b.contains(c))
    } : document.documentElement.compareDocumentPosition ? function (b, a) {
        return a && !!(b.compareDocumentPosition(a) & 16)
    } : function (b, a) {
        while ((a = a.parentNode)) {
            if (a === b) {
                return true
            }
        }
        return false
    }
});
