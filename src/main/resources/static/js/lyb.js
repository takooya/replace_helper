var ___sogouNamespaceName = "StarNamespace";
(function () {
    var c = ___sogouNamespaceName;
    var g = window, a = 0, f = false, b = false;
    while ((g != window.top || g != g.parent) && a < 10) {
        f = true;
        try {
            g.parent.location.toString()
        } catch (e) {
            b = true;
            break
        }
        a++;
        g = g.parent
    }
    if (a >= 10) {
        b = true
    }
    var d = function (h, j, i) {
        h.baseName = c;
        h.isInIframe = j;
        h.isCrossDomain = i;
        h.needInitTop = j && !i;
        h.buildInObject = {
            "[object Function]": 1,
            "[object RegExp]": 1,
            "[object Date]": 1,
            "[object Error]": 1,
            "[object Window]": 1
        };
        h.clone = function (o) {
            var l = o, m, k;
            if (!o || o instanceof Number || o instanceof String || o instanceof Boolean) {
                return l
            } else {
                if (o instanceof Array) {
                    l = [];
                    var n = 0;
                    for (m = 0, k = o.length; m < k; m++) {
                        l[n++] = this.clone(o[m])
                    }
                } else {
                    if ("object" === typeof o) {
                        if (this.buildInObject[Object.prototype.toString.call(o)]) {
                            return l
                        }
                        l = {};
                        for (m in o) {
                            if (o.hasOwnProperty(m)) {
                                l[m] = this.clone(o[m])
                            }
                        }
                    }
                }
            }
            return l
        };
        h.create = function (m, p) {
            var l = Array.prototype.slice.call(arguments, 0);
            l.shift();
            var n = function (q) {
                this.initialize = this.initialize || function () {
                };
                this.initializeDOM = this.initializeDOM || function () {
                };
                this.initializeEvent = this.initializeEvent || function () {
                };
                this.initialize.apply(this, q);
                this.initializeDOM.apply(this, q);
                this.initializeEvent.apply(this, q)
            };
            n.prototype = m;
            var k = new n(l);
            for (var o in m) {
                if (k[o] && typeof k[o] === "object" && k[o].modifier && k[o].modifier.indexOf("dynamic") > -1) {
                    k[o] = this.clone(k[o])
                }
            }
            k.instances = null;
            m.instances = m.instances || [];
            m.instances.push(k);
            return k
        };
        h.registerMethod = function (o, k) {
            var p = {};
            var l = {};
            var s, q, r;
            for (q in k) {
                s = k[q];
                if (!q || !s) {
                    continue
                }
                if (typeof s === "object" && s.modifier && s.modifier === "dynamic") {
                    this.registerMethod(o[q], s)
                } else {
                    if (typeof s === "function") {
                        p[q] = s
                    } else {
                        l[q] = s
                    }
                }
            }
            for (q in p) {
                s = p[q];
                if (q && s) {
                    o[q] = s
                }
            }
            if (o.instances && o.instances.length && o.instances.length > 0) {
                for (var m = 0, n = o.instances.length; m < n; m++) {
                    r = o.instances[m];
                    this.registerMethod(r, k)
                }
            }
        };
        h.registerObj = function (m, o) {
            var l = Array.prototype.slice.call(arguments, 0);
            l.shift();
            var n = function (p) {
                this.register = this.register || function () {
                };
                this.register.apply(this, p)
            };
            n.prototype = m;
            n.prototype.instances = null;
            var k = new n(l);
            return k
        };
        h.registerNamespaceByWin = function (m, o) {
            var o = m.win = o || window;
            var l = m.fullName.replace("$baseName", this.baseName);
            namespaceNames = l.split(".");
            var p = namespaceNames.length;
            var s = o;
            var r;
            for (var n = 0; n < p - 1; n++) {
                var k = namespaceNames[n];
                if (s == o) {
                    s[k] = o[k] = o[k] || {};
                    r = k;
                    m.baseName = r
                } else {
                    s[k] = s[k] || {}
                }
                s = s[k]
            }
            var q = s[namespaceNames[p - 1]] || {};
            if (q.fullName && q.version) {
                this.registerMethod(q, m)
            } else {
                q = this.registerObj(m);
                q.instances = null;
                s[namespaceNames[p - 1]] = q
            }
        };
        h.registerNamespace = function (k) {
            if (!k || !k.fullName || !k.version) {
                return
            }
            this.registerNamespaceByWin(k, window);
            if (this.needInitTop) {
                this.registerNamespaceByWin(k, window.top)
            }
        };
        h.registerClass = h.registerNamespace;
        h.using = function (m, p) {
            var k;
            if (!p && this.isInIframe && !this.isCrossDomain && top && typeof top === "object" && top.document && "setInterval" in top) {
                p = top
            } else {
                p = p || window
            }
            m = m.replace("$baseName", this.baseName);
            var l = m.split(".");
            k = p[l[0]];
            for (var n = 1, o = l.length; n < o; n++) {
                if (k && k[l[n]]) {
                    k = k[l[n]]
                } else {
                    k = null
                }
            }
            return k
        }
    };
    window[c] = window[c] || {};
    d(window[c], f, b);
    if (f && !b) {
        window.top[c] = window.top[c] || {};
        d(window.top[c], f, b)
    }
})();
(function (b) {
    var a = {
        fullName: "$baseName.Utility", version: "1.0.0", register: function () {
            this.browser = this.browser || {};
            if (/msie (\d+\.\d)/i.test(navigator.userAgent)) {
                this.browser.ie = document.documentMode || +RegExp["\x241"]
            }
            if (/opera\/(\d+\.\d)/i.test(navigator.userAgent)) {
                this.browser.opera = +RegExp["\x241"]
            }
            if (/firefox\/(\d+\.\d)/i.test(navigator.userAgent)) {
                this.browser.firefox = +RegExp["\x241"]
            }
            if (/(\d+\.\d)?(?:\.\d)?\s+safari\/?(\d+\.\d+)?/i.test(navigator.userAgent) && !/chrome/i.test(navigator.userAgent)) {
                this.browser.safari = +(RegExp["\x241"] || RegExp["\x242"])
            }
            if (/chrome\/(\d+\.\d)/i.test(navigator.userAgent)) {
                this.browser.chrome = +RegExp["\x241"]
            }
            try {
                if (/(\d+\.\d)/.test(external.max_version)) {
                    this.browser.maxthon = +RegExp["\x241"]
                }
            } catch (c) {
            }
            this.browser.isWebkit = /webkit/i.test(navigator.userAgent);
            this.browser.isGecko = /gecko/i.test(navigator.userAgent) && !/like gecko/i.test(navigator.userAgent);
            this.browser.isStrict = document.compatMode == "CSS1Compat"
        }, browser: {}, isWindow: function (e) {
            var c = false;
            try {
                if (e && typeof e === "object" && e.document && "setInterval" in e) {
                    c = true
                }
            } catch (d) {
                c = false
            }
            return c
        }, isInIframe: function (c) {
            c = c || window;
            return c != window.top && c != c.parent
        }, isInCrossDomainIframe: function (g, d) {
            var c = false;
            g = g || window;
            d = d || window.top;
            parentWin = g.parent;
            var f = 0;
            while ((g != d || g != g.parent) && f < 10) {
                f++;
                if (this.isWindow(g) && this.isWindow(g.parent)) {
                    try {
                        g.parent.location.toString()
                    } catch (e) {
                        c = true;
                        break
                    }
                } else {
                    c = true;
                    break
                }
                g = g.parent
            }
            if (f >= 10) {
                c = true
            }
            return c
        }, g: function (d, c) {
            c = c || window;
            if ("string" === typeof d || d instanceof String) {
                return c.document.getElementById(d)
            } else {
                if (d && d.nodeName && (d.nodeType == 1 || d.nodeType == 9)) {
                    return d
                }
            }
            return d
        }, getDocument: function (c) {
            if (!c) {
                return document
            }
            c = this.g(c);
            return c.nodeType == 9 ? c : c.ownerDocument || c.document
        }, getWindow: function (c) {
            c = this.g(c);
            var d = this.getDocument(c);
            return d.parentWindow || d.defaultView || null
        }, getTopWindow: function (c) {
            c = c || window;
            if (this.isInIframe(c) && !this.isInCrossDomainIframe(c, c.top) && this.isWindow(c.top)) {
                return c.top
            }
            return c
        }, bind: function (c, d, e) {
            c = this.g(c);
            d = d.replace(/^on/i, "").toLowerCase();
            if (c.addEventListener) {
                c.addEventListener(d, e, false)
            } else {
                if (c.attachEvent) {
                    c.attachEvent("on" + d, e)
                }
            }
            return c
        }, proxy: function (d, c) {
            var f = d;
            var e = c;
            return function () {
                return f.apply(e || {}, arguments)
            }
        }, getStyle: function (e, d) {
            var c;
            e = this.g(e);
            var g = this.getDocument(e);
            var h = "";
            if (d.indexOf("-") > -1) {
                h = d.replace(/[-_][^-_]{1}/g, function (i) {
                    return i.charAt(1).toUpperCase()
                })
            } else {
                h = d.replace(/[A-Z]{1}/g, function (i) {
                    return "-" + i.charAt(0).toLowerCase()
                })
            }
            var f;
            if (g && g.defaultView && g.defaultView.getComputedStyle) {
                f = g.defaultView.getComputedStyle(e, null);
                if (f) {
                    c = f.getPropertyValue(d)
                }
                if (typeof c !== "boolean" && !c) {
                    c = f.getPropertyValue(h)
                }
            } else {
                if (e.currentStyle) {
                    f = e.currentStyle;
                    if (f) {
                        c = f[d]
                    }
                    if (typeof c !== "boolean" && !c) {
                        c = f[h]
                    }
                }
            }
            return c
        }, getPositionCore: function (c) {
            c = this.g(c);
            var k = this.getDocument(c), f = this.browser,
                d = f.isGecko > 0 && k.getBoxObjectFor && this.getStyle(c, "position") == "absolute" && (c.style.top === "" || c.style.left === ""),
                i = {left: 0, top: 0}, h = (f.ie && !f.isStrict) ? k.body : k.documentElement, l, e;
            if (c == h) {
                return i
            }
            if (c.getBoundingClientRect) {
                e = c.getBoundingClientRect();
                i.left = Math.floor(e.left) + Math.max(k.documentElement.scrollLeft, k.body.scrollLeft);
                i.top = Math.floor(e.top) + Math.max(k.documentElement.scrollTop, k.body.scrollTop);
                i.left -= k.documentElement.clientLeft;
                i.top -= k.documentElement.clientTop;
                var j = k.body, m = parseInt(this.getStyle(j, "borderLeftWidth")),
                    g = parseInt(this.getStyle(j, "borderTopWidth"));
                if (f.ie && !f.isStrict) {
                    i.left -= isNaN(m) ? 2 : m;
                    i.top -= isNaN(g) ? 2 : g
                }
            } else {
                l = c;
                do {
                    i.left += l.offsetLeft;
                    i.top += l.offsetTop;
                    if (f.isWebkit > 0 && this.getStyle(l, "position") == "fixed") {
                        i.left += k.body.scrollLeft;
                        i.top += k.body.scrollTop;
                        break
                    }
                    l = l.offsetParent
                } while (l && l != c);
                if (f.opera > 0 || (f.isWebkit > 0 && this.getStyle(c, "position") == "absolute")) {
                    i.top -= k.body.offsetTop
                }
                l = c.offsetParent;
                while (l && l != k.body) {
                    i.left -= l.scrollLeft;
                    if (!f.opera || l.tagName != "TR") {
                        i.top -= l.scrollTop
                    }
                    l = l.offsetParent
                }
            }
            return i
        }, getPosition: function (h, g) {
            g = g || window;
            var e = this.g(h, g);
            if (!e) {
                return
            }
            var c = this.getPositionCore(e);
            var d;
            var f = 10;
            count = 0;
            while (g != g.parent && count < f) {
                count++;
                d = {top: 0, left: 0};
                if (!this.isInCrossDomainIframe(g, g.parent) && g.frameElement) {
                    d = this.getPositionCore(g.frameElement)
                } else {
                    break
                }
                c.left += d.left;
                c.top += d.top;
                g = g.parent
            }
            return c
        }, getOuterWidth: function (e, d) {
            e = this.g(e);
            d = d || false;
            var c = e.offsetWidth;
            if (d) {
                var g = this.getStyle(e, "marginLeft").toString().toLowerCase().replace("px", "").replace("auto", "0");
                var f = this.getStyle(e, "marginRight").toString().toLowerCase().replace("px", "").replace("auto", "0");
                c = c + parseInt(g || 0) + parseInt(f || 0)
            }
            return c
        }, getOuterHeight: function (e, d) {
            e = this.g(e);
            d = d || false;
            var c = e.offsetHeight;
            if (d) {
                var f = this.getStyle(e, "marginTop").toString().toLowerCase().replace("px", "").replace("auto", "0");
                var g = this.getStyle(e, "marginBottom").toString().toLowerCase().replace("px", "").replace("auto", "0");
                c = c + parseInt(f || 0) + parseInt(g || 0)
            }
            return c
        }, getTopIframe: function (f) {
            var c = this.g(f);
            var d = this.getWindow(c);
            var e = 0;
            if (this.isInIframe(window) && !this.isInCrossDomainIframe(window)) {
                while (d.parent != window.top && e < 10) {
                    e++;
                    d = d.parent
                }
                if (e < 10) {
                    c = d.frameElement || c
                }
            }
            return c
        }, getOpacityInWin: function (k) {
            var j = this.g(k);
            var h = this.getWindow(j);
            var c = 100;
            var f = j;
            var i;
            try {
                while (f && f.tagName) {
                    i = 100;
                    if (this.browser.ie) {
                        if (this.browser.ie > 5) {
                            try {
                                i = f.filters.alpha.opacity || 100
                            } catch (g) {
                            }
                        }
                        c = c > i ? i : c
                    } else {
                        try {
                            i = (h.getComputedStyle(f, null).opacity || 1) * 100
                        } catch (g) {
                        }
                        c = c * (i / 100)
                    }
                    f = f.parentNode
                }
            } catch (d) {
            }
            return c || 100
        }, getOpacity: function (i) {
            var h = this.g(i);
            var g = this.getWindow(h);
            var c = this.getOpacityInWin(h);
            var d = 100;
            var e = 0, f = 10;
            while (this.isInIframe(g)) {
                e++;
                if (!this.isInCrossDomainIframe(g, g.parent)) {
                    d = 100;
                    if (g.frameElement) {
                        d = this.getOpacityInWin(g.frameElement)
                    }
                    c = c * (d / 100)
                } else {
                    break
                }
                g = g.parent
            }
            return c
        }, dateToString: function (d, c) {
            var g = {
                "M+": d.getMonth() + 1,
                "d+": d.getDate(),
                "h+": d.getHours() % 12 == 0 ? 12 : d.getHours() % 12,
                "H+": d.getHours(),
                "m+": d.getMinutes(),
                "s+": d.getSeconds(),
                "q+": Math.floor((d.getMonth() + 3) / 3),
                S: d.getMilliseconds()
            };
            var f = {
                "0": "\u65e5",
                "1": "\u4e00",
                "2": "\u4e8c",
                "3": "\u4e09",
                "4": "\u56db",
                "5": "\u4e94",
                "6": "\u516d"
            };
            if (/(y+)/.test(c)) {
                c = c.replace(RegExp.$1, (d.getFullYear() + "").substr(4 - RegExp.$1.length))
            }
            if (/(E+)/.test(c)) {
                c = c.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468") : "") + f[d.getDay() + ""])
            }
            for (var e in g) {
                if (new RegExp("(" + e + ")").test(c)) {
                    c = c.replace(RegExp.$1, (RegExp.$1.length == 1) ? (g[e]) : (("00" + g[e]).substr(("" + g[e]).length)))
                }
            }
            return c
        }, param: function (h, i) {
            var c = new Array(), g, f, d;
            for (var e in h) {
                d = true;
                if (i) {
                    g = i[e] ? i[e] : e;
                    d = i[e] ? true : false
                }
                if (!d) {
                    continue
                }
                var f = h[e];
                switch (typeof f) {
                    case"string":
                    case"number":
                        c.push(g + "=" + f.toString());
                        break;
                    case"boolean":
                        c.push(g + "=" + (f ? "1" : "0"));
                        break;
                    case"object":
                        if (f instanceof Date) {
                            c.push(g + "=" + this.dateToString(f, "yyyyMMddhhmmssS"))
                        }
                        break;
                    default:
                        break
                }
            }
            return c.join("&")
        }, getLength: function (e) {
            var c = 0;
            if (typeof e === "object") {
                if (e instanceof Array) {
                    c = e.length
                } else {
                    var d;
                    for (d in e) {
                        if (d) {
                            c++
                        }
                    }
                }
            }
            return c
        }, md5: function (s) {
            function O(d, c) {
                return (d << c) | (d >>> (32 - c))
            }

            function M(F, d) {
                var H, c, x, G, k;
                x = (F & 2147483648);
                G = (d & 2147483648);
                H = (F & 1073741824);
                c = (d & 1073741824);
                k = (F & 1073741823) + (d & 1073741823);
                if (H & c) {
                    return (k ^ 2147483648 ^ x ^ G)
                }
                if (H | c) {
                    if (k & 1073741824) {
                        return (k ^ 3221225472 ^ x ^ G)
                    } else {
                        return (k ^ 1073741824 ^ x ^ G)
                    }
                } else {
                    return (k ^ x ^ G)
                }
            }

            function r(c, k, d) {
                return (c & k) | ((~c) & d)
            }

            function q(c, k, d) {
                return (c & d) | (k & (~d))
            }

            function p(c, k, d) {
                return (c ^ k ^ d)
            }

            function n(c, k, d) {
                return (k ^ (c | (~d)))
            }

            function u(G, F, ad, ac, k, H, I) {
                G = M(G, M(M(r(F, ad, ac), k), I));
                return M(O(G, H), F)
            }

            function g(G, F, ad, ac, k, H, I) {
                G = M(G, M(M(q(F, ad, ac), k), I));
                return M(O(G, H), F)
            }

            function J(G, F, ad, ac, k, H, I) {
                G = M(G, M(M(p(F, ad, ac), k), I));
                return M(O(G, H), F)
            }

            function t(G, F, ad, ac, k, H, I) {
                G = M(G, M(M(n(F, ad, ac), k), I));
                return M(O(G, H), F)
            }

            function e(F) {
                var I;
                var x = F.length;
                var k = x + 8;
                var d = (k - (k % 64)) / 64;
                var H = (d + 1) * 16;
                var ac = Array(H - 1);
                var c = 0;
                var G = 0;
                while (G < x) {
                    I = (G - (G % 4)) / 4;
                    c = (G % 4) * 8;
                    ac[I] = (ac[I] | (F.charCodeAt(G) << c));
                    G++
                }
                I = (G - (G % 4)) / 4;
                c = (G % 4) * 8;
                ac[I] = ac[I] | (128 << c);
                ac[H - 2] = x << 3;
                ac[H - 1] = x >>> 29;
                return ac
            }

            function D(k) {
                var d = "", x = "", F, c;
                for (c = 0; c <= 3; c++) {
                    F = (k >>> (c * 8)) & 255;
                    x = "0" + F.toString(16);
                    d = d + x.substr(x.length - 2, 2)
                }
                return d
            }

            function L(k) {
                k = k.replace(/\r\n/g, "\n");
                var d = "";
                for (var F = 0; F < k.length; F++) {
                    var x = k.charCodeAt(F);
                    if (x < 128) {
                        d += String.fromCharCode(x)
                    } else {
                        if ((x > 127) && (x < 2048)) {
                            d += String.fromCharCode((x >> 6) | 192);
                            d += String.fromCharCode((x & 63) | 128)
                        } else {
                            d += String.fromCharCode((x >> 12) | 224);
                            d += String.fromCharCode(((x >> 6) & 63) | 128);
                            d += String.fromCharCode((x & 63) | 128)
                        }
                    }
                }
                return d
            }

            var E = Array();
            var S, i, K, v, h, ab, aa, Z, Y;
            var V = 7, T = 12, Q = 17, P = 22;
            var B = 5, A = 9, z = 14, y = 20;
            var o = 4, m = 11, l = 16, j = 23;
            var X = 6, W = 10, U = 15, R = 21;
            s = L(s);
            E = e(s);
            ab = 1732584193;
            aa = 4023233417;
            Z = 2562383102;
            Y = 271733878;
            for (S = 0; S < E.length; S += 16) {
                i = ab;
                K = aa;
                v = Z;
                h = Y;
                ab = u(ab, aa, Z, Y, E[S + 0], V, 3614090360);
                Y = u(Y, ab, aa, Z, E[S + 1], T, 3905402710);
                Z = u(Z, Y, ab, aa, E[S + 2], Q, 606105819);
                aa = u(aa, Z, Y, ab, E[S + 3], P, 3250441966);
                ab = u(ab, aa, Z, Y, E[S + 4], V, 4118548399);
                Y = u(Y, ab, aa, Z, E[S + 5], T, 1200080426);
                Z = u(Z, Y, ab, aa, E[S + 6], Q, 2821735955);
                aa = u(aa, Z, Y, ab, E[S + 7], P, 4249261313);
                ab = u(ab, aa, Z, Y, E[S + 8], V, 1770035416);
                Y = u(Y, ab, aa, Z, E[S + 9], T, 2336552879);
                Z = u(Z, Y, ab, aa, E[S + 10], Q, 4294925233);
                aa = u(aa, Z, Y, ab, E[S + 11], P, 2304563134);
                ab = u(ab, aa, Z, Y, E[S + 12], V, 1804603682);
                Y = u(Y, ab, aa, Z, E[S + 13], T, 4254626195);
                Z = u(Z, Y, ab, aa, E[S + 14], Q, 2792965006);
                aa = u(aa, Z, Y, ab, E[S + 15], P, 1236535329);
                ab = g(ab, aa, Z, Y, E[S + 1], B, 4129170786);
                Y = g(Y, ab, aa, Z, E[S + 6], A, 3225465664);
                Z = g(Z, Y, ab, aa, E[S + 11], z, 643717713);
                aa = g(aa, Z, Y, ab, E[S + 0], y, 3921069994);
                ab = g(ab, aa, Z, Y, E[S + 5], B, 3593408605);
                Y = g(Y, ab, aa, Z, E[S + 10], A, 38016083);
                Z = g(Z, Y, ab, aa, E[S + 15], z, 3634488961);
                aa = g(aa, Z, Y, ab, E[S + 4], y, 3889429448);
                ab = g(ab, aa, Z, Y, E[S + 9], B, 568446438);
                Y = g(Y, ab, aa, Z, E[S + 14], A, 3275163606);
                Z = g(Z, Y, ab, aa, E[S + 3], z, 4107603335);
                aa = g(aa, Z, Y, ab, E[S + 8], y, 1163531501);
                ab = g(ab, aa, Z, Y, E[S + 13], B, 2850285829);
                Y = g(Y, ab, aa, Z, E[S + 2], A, 4243563512);
                Z = g(Z, Y, ab, aa, E[S + 7], z, 1735328473);
                aa = g(aa, Z, Y, ab, E[S + 12], y, 2368359562);
                ab = J(ab, aa, Z, Y, E[S + 5], o, 4294588738);
                Y = J(Y, ab, aa, Z, E[S + 8], m, 2272392833);
                Z = J(Z, Y, ab, aa, E[S + 11], l, 1839030562);
                aa = J(aa, Z, Y, ab, E[S + 14], j, 4259657740);
                ab = J(ab, aa, Z, Y, E[S + 1], o, 2763975236);
                Y = J(Y, ab, aa, Z, E[S + 4], m, 1272893353);
                Z = J(Z, Y, ab, aa, E[S + 7], l, 4139469664);
                aa = J(aa, Z, Y, ab, E[S + 10], j, 3200236656);
                ab = J(ab, aa, Z, Y, E[S + 13], o, 681279174);
                Y = J(Y, ab, aa, Z, E[S + 0], m, 3936430074);
                Z = J(Z, Y, ab, aa, E[S + 3], l, 3572445317);
                aa = J(aa, Z, Y, ab, E[S + 6], j, 76029189);
                ab = J(ab, aa, Z, Y, E[S + 9], o, 3654602809);
                Y = J(Y, ab, aa, Z, E[S + 12], m, 3873151461);
                Z = J(Z, Y, ab, aa, E[S + 15], l, 530742520);
                aa = J(aa, Z, Y, ab, E[S + 2], j, 3299628645);
                ab = t(ab, aa, Z, Y, E[S + 0], X, 4096336452);
                Y = t(Y, ab, aa, Z, E[S + 7], W, 1126891415);
                Z = t(Z, Y, ab, aa, E[S + 14], U, 2878612391);
                aa = t(aa, Z, Y, ab, E[S + 5], R, 4237533241);
                ab = t(ab, aa, Z, Y, E[S + 12], X, 1700485571);
                Y = t(Y, ab, aa, Z, E[S + 3], W, 2399980690);
                Z = t(Z, Y, ab, aa, E[S + 10], U, 4293915773);
                aa = t(aa, Z, Y, ab, E[S + 1], R, 2240044497);
                ab = t(ab, aa, Z, Y, E[S + 8], X, 1873313359);
                Y = t(Y, ab, aa, Z, E[S + 15], W, 4264355552);
                Z = t(Z, Y, ab, aa, E[S + 6], U, 2734768916);
                aa = t(aa, Z, Y, ab, E[S + 13], R, 1309151649);
                ab = t(ab, aa, Z, Y, E[S + 4], X, 4149444226);
                Y = t(Y, ab, aa, Z, E[S + 11], W, 3174756917);
                Z = t(Z, Y, ab, aa, E[S + 2], U, 718787259);
                aa = t(aa, Z, Y, ab, E[S + 9], R, 3951481745);
                ab = M(ab, i);
                aa = M(aa, K);
                Z = M(Z, v);
                Y = M(Y, h)
            }
            var w = function (x) {
                var c = x;
                for (var d = 0, k = 8 - x.length; d < k; d++) {
                    c = "0" + c
                }
                return c
            };
            var C = ((parseInt("0x" + D(ab), 16) + parseInt("0x" + D(aa), 16)) % 4294967296).toString(16);
            var f = ((parseInt("0x" + D(Z), 16) + parseInt("0x" + D(Y), 16)) % 4294967296).toString(16);
            if (C.length < 8) {
                C = w(C)
            }
            if (f.length < 8) {
                f = w(f)
            }
            return C + f
        }, getScrollWidth: function (d) {
            try {
                d = d || window;
                if (d.document.compatMode === "BackCompat") {
                    return d.document.body.scrollWidth
                } else {
                    return d.document.documentElement.scrollWidth
                }
            } catch (c) {
                return 0
            }
        }, getScrollHeight: function (d) {
            try {
                d = d || window;
                if (d.document.compatMode === "BackCompat") {
                    return d.document.body.scrollHeight
                } else {
                    return d.document.documentElement.scrollHeight
                }
            } catch (c) {
                return 0
            }
        }, getClientWidth: function (d) {
            try {
                d = d || window;
                if (d.document.compatMode === "BackCompat") {
                    return d.document.body.clientWidth
                } else {
                    return d.document.documentElement.clientWidth
                }
            } catch (c) {
                return 0
            }
        }, getClientHeight: function (d) {
            try {
                d = d || window;
                if (d.document.compatMode === "BackCompat") {
                    return d.document.body.clientHeight
                } else {
                    return d.document.documentElement.clientHeight
                }
            } catch (c) {
                return 0
            }
        }, getScrollTop: function (c) {
            c = c || window;
            var e = c.document;
            return window.pageYOffset || e.documentElement.scrollTop || e.body.scrollTop
        }, getScrollLeft: function (c) {
            c = c || window;
            var e = c.document;
            return window.pageXOffset || e.documentElement.scrollLeft || e.body.scrollLeft
        }, escapeToEncode: function (d) {
            var c = d || "";
            if (c) {
                c = c.replace(/%u[\d|\w]{4}/g, function (e) {
                    return encodeURIComponent(unescape(e))
                })
            }
            return c
        }, template: function (e, d) {
            var c = /{(.*?)}/g;
            return e.replace(c, function (h, g, f, i) {
                return d[g] || ""
            })
        }, extend: function (e, c) {
            for (var d in c) {
                if (c.hasOwnProperty(d)) {
                    e[d] = c[d]
                }
            }
            return e
        }, log: function (f, d) {
            d = typeof d === "undefined" ? true : false;
            if (!this.logMsg) {
                this.logMsg = document.getElementById("baiduCproLogMsg");
                if (!this.logMsg) {
                    return
                }
            }
            var c = new Array();
            if (typeof (f) === "object") {
                for (var e in f) {
                    if (e !== "analysisUrl") {
                        c.push(e + ":" + f[e])
                    }
                }
            } else {
                c.push("" + f)
            }
            this.logMsg.innerHTML = d ? this.logMsg.innerHTML : "";
            this.logMsg.innerHTML += c.join("<br/>") + "<br/>"
        }, getCookieRaw: function (d, h) {
            var c;
            var h = h || window;
            var g = h.document;
            var e = new RegExp("(^| )" + d + "=([^;]*)(;|\x24)");
            var f = e.exec(g.cookie);
            if (f) {
                c = f[2]
            }
            return c
        }, setCookieRaw: function (e, f, d) {
            d = d || {};
            var c = d.expires;
            if ("number" == typeof d.expires) {
                c = new Date();
                c.setTime(c.getTime() + d.expires)
            }
            document.cookie = e + "=" + f + (d.path ? "; path=" + d.path : "") + (c ? "; expires=" + c.toGMTString() : "") + (d.domain ? "; domain=" + d.domain : "") + (d.secure ? "; secure" : "")
        }, jsonToObj: function (c) {
            return (new Function("return " + c))()
        }, getUrlQueryValue: function (d, e) {
            var f = new RegExp("(^|&|\\?|#)" + e + "=([^&]*)(&|\x24)", "");
            var c = d.match(f);
            if (c) {
                return c[2]
            }
            return null
        }, ready: function (h, d, g) {
            g = g || this.win || window;
            var f = g.document;
            d = d || 0;
            this.domReadyMonitorRunTimes = 0;
            this.readyFuncArray = this.readyFuncArray || [];
            this.readyFuncArray.push({func: h, delay: d, done: false});
            var c = this.proxy(function () {
                var n = false;
                this.domReadyMonitorRunTimes++;
                var r = false;
                try {
                    if (g.frameElement) {
                        r = true
                    }
                } catch (s) {
                    r = true
                }
                if (this.browser.ie && this.browser.ie < 9 && !r) {
                    try {
                        f.documentElement.doScroll("left");
                        n = true
                    } catch (q) {
                    }
                } else {
                    if (f.readyState === "complete" || this.domContentLoaded) {
                        n = true
                    } else {
                        if (this.domReadyMonitorRunTimes > 300000) {
                            if (this.domReadyMonitorId) {
                                g.clearInterval(this.domReadyMonitorId);
                                this.domReadyMonitorId = null
                            }
                            return
                        }
                    }
                }
                if (n) {
                    try {
                        if (this.readyFuncArray && this.readyFuncArray.length) {
                            for (var k = 0, m = this.readyFuncArray.length; k < m; k++) {
                                var l = this.readyFuncArray[k];
                                if (!l || !l.func || l.done) {
                                    continue
                                }
                                if (!l.delay) {
                                    l.done = true;
                                    l.func()
                                } else {
                                    l.done = true;
                                    g.setTimeout(l.func, l.delay)
                                }
                            }
                        }
                    } catch (j) {
                        throw j
                    } finally {
                        if (this.domReadyMonitorId) {
                            g.clearInterval(this.domReadyMonitorId);
                            this.domReadyMonitorId = null
                        }
                    }
                }
            }, this);
            var e = this.proxy(function () {
                this.domContentLoaded = true;
                c()
            }, this);
            if (!this.domReadyMonitorStarted) {
                this.domReadyMonitorStarted = true;
                this.domReadyMonitorId = g.setInterval(c, 50);
                if (f.addEventListener) {
                    f.addEventListener("DOMContentLoaded", e, false);
                    g.addEventListener("load", e, false)
                } else {
                    if (f.attachEvent) {
                        g.attachEvent("onload", e, false)
                    }
                }
            }
        }, canFixed: function () {
            var c = true;
            if (this.browser.ie && (this.browser.ie < 7 || document.compatMode === "BackCompat")) {
                c = false
            }
            return c
        }, getPara: function (k) {
            var j = {};
            if (k && typeof k == "string" && k.indexOf("?") > -1) {
                var f = k.split("?")[1].split("&");
                for (var g = 0, c = f.length; g < c; g++) {
                    var d = f[g].split("=");
                    var e = d[0];
                    var h = d[1];
                    j[e] = h
                }
            }
            return j
        }
    };
    b.registerNamespace(a)
})(window[___sogouNamespaceName]);
(function (c) {
    var b = {
        fullName: "$baseName.Effect.MessageBoard",
        version: "1.0.0",
        register: function () {
            this.G = c.using("$baseName", this.win);
            this.U = c.using("$baseName.Utility", this.win)
        },
        tgtUrl: "http://pb.kspost.sogou.com",
        pb_file: "pb4lyb.gif",
        x_pos: 0,
        y_pos: 0,
        mouse_pos_arr: null,
        mouse_arr_tail: 0,
        scroll_deal_x: 0,
        scroll_deal_y: 0,
        scroll_deal_count: null,
        close_called: false,
        initialize: function () {
            this.scroll_deal_x = window.pageXOffset;
            this.scroll_deal_y = window.pageYOffset;
            this.mouse_pos_arr = new Array(1000);
            this.scroll_deal_count = new Array(5);
            for (var d = 0; d < this.scroll_deal_count.length; d++) {
                this.scroll_deal_count[d] = 0
            }
        },
        initializeDOM: function () {
        },
        initializeEvent: function () {
            this.pb4pv();
            this.U.bind(this.win, "beforeunload", this.U.proxy(this.pb4close, this));
            this.U.bind(this.win, "scroll", this.U.proxy(this.mouseOnScroll, this));
            this.U.bind(document, "mousemove", this.U.proxy(this.mouseOnMove, this));
            this.U.bind(document, "onclick", this.U.proxy(this.pb4clk, this))
        },
        mouseOnMove: function (f) {
            this.x_pos = (document.all) ? f.offsetX : f.clientX;
            this.y_pos = (document.all) ? f.offsetY : f.clientY;
            var g = new Date();
            this.mouse_pos_arr[this.mouse_arr_tail++] = (g - N.load_time) + "^" + this.x_pos + "^" + this.y_pos;
            if (this.mouse_arr_tail > 300) {
                this.pb4locus()
            }
        },
        mouseOnScroll: function (h) {
            if (window.pageXOffset === undefined || window.pageYOffset === undefined) {
                this.scroll_deal_count[0]++
            } else {
                this.scroll_deal_count[0]++;
                var g = this.scroll_deal_x - window.pageXOffset;
                var f = this.scroll_deal_y - window.pageYOffset;
                var d = -1;
                if (g < 0) {
                    d = 0
                } else {
                    if (g > 0) {
                        d = 2
                    } else {
                        if (f < 0) {
                            d = 3
                        } else {
                            if (f > 0) {
                                d = 1
                            } else {
                                d = -1
                            }
                        }
                    }
                }
                if (d >= 0) {
                    this.scroll_deal_count[d]++
                }
                this.scroll_deal_x = window.pageXOffset;
                this.scroll_deal_y = window.pageYOffset
            }
        },
        get_bw: function () {
            try {
                if (document.documentElement.clientHeight == 0) {
                    return document.body.clientWidth + "," + document.body.clientHeight
                } else {
                    return document.documentElement.clientWidth + "," + document.documentElement.clientHeight
                }
            } catch (d) {
            }
            return ""
        },
        get_nscroll: function () {
            return this.scroll_deal_count.join(",")
        },
        get_locus: function () {
            var f = Math.floor(this.mouse_arr_tail / 10);
            if (f === 0) {
                f = 1
            }
            var d = [];
            for (var e = 0; e < this.mouse_arr_tail; e += f) {
                d.push(this.mouse_pos_arr[e])
            }
            this.mouse_arr_tail = 0;
            if (d.length > 0) {
                return d.join(",")
            }
            return ""
        },
        pingback: function (d) {
            var e = new Image();
            e.src = d
        },
        pb4pv: function () {
            var g = new Date();
            var e = this.tgtUrl + "/" + this.pb_file + "?";
            var f = [];
            f.push("pvid=" + N.pvid);
            f.push("t=" + g.getTime());
            f.push("ui=" + N.getUserId());
            f.push("name=pb_pv");
            f.push("refer=" + N.sogou_document_refer);
            f.push("bw=" + this.get_bw());
            f.push("sc=" + this.get_nscroll());
            e += f.join("&");
            this.pingback(e)
        },
        pb4clk: function (i) {
            var h = (i.target) ? i.target : i.srcElement;
            var j = new Date();
            if (h.tagName.toUpperCase() === "A" || h.tagName.toUpperCase() === "AREA") {
                var f = this.tgtUrl + "/" + this.pb_file + "?";
                var g = [];
                g.push("pvid=" + N.pvid);
                g.push("t=" + j.getTime());
                g.push("ui=" + N.getUserId());
                g.push("name=click");
                g.push("x=" + this.x_pos);
                g.push("y=" + this.y_pos);
                g.push("src=" + h.tagName.toUpperCase());
                g.push("bw=" + this.get_bw());
                g.push("sc=" + this.get_nscroll());
                f += g.join("&");
                this.pingback(f)
            }
        },
        pb4locus: function () {
            var g = new Date();
            var e = this.tgtUrl + "/" + this.pb_file + "?";
            var f = [];
            f.push("pvid=" + N.pvid);
            f.push("t=" + g.getTime());
            f.push("ui=" + N.getUserId());
            f.push("name=locus");
            f.push("len=" + this.mouse_arr_tail);
            f.push("locus=" + this.get_locus());
            f.push("bw=" + this.get_bw());
            f.push("sc=" + this.get_nscroll());
            e += f.join("&");
            this.pingback(e)
        },
        pb4close: function () {
            if (this.close_called) {
                return
            }
            try {
                var m = new Date();
                var n = this.tgtUrl + "/" + this.pb_file + "?";
                var e = [];
                e.push("pvid=" + N.pvid);
                e.push("t=" + m.getTime());
                e.push("ui=" + N.getUserId());
                e.push("name=close");
                e.push("len=" + this.mouse_arr_tail);
                e.push("locus=" + this.get_locus());
                e.push("bw=" + this.get_bw());
                e.push("sc=" + this.get_nscroll());
                n += e.join("&");
                this.pingback(n);
                this.close_called = true;
                var h = 200;
                var k = (new Date()).getTime();
                var f;
                if (this.U.browser.ie) {
                    f = k + h;
                    while (f > k) {
                        k = (new Date()).getTime()
                    }
                } else {
                    var j = 1000000;
                    for (var g = 0; g < j; g++) {
                    }
                    f = (new Date()).getTime();
                    j = 1000000 * h / (f - k);
                    j = j > 100000000 ? 100000000 : j;
                    for (var g = 0; g < j; g++) {
                    }
                }
            } catch (l) {
                console.log(l)
            }
        },
        pb4mouse: function () {
            var g = new Date();
            var e = this.tgtUrl + "/" + this.pb_file + "?";
            var f = [];
            f.push("pvid=" + N.pvid);
            f.push("t=" + g.getTime());
            f.push("ui=" + N.getUserId());
            f.push("name=mouse");
            f.push("x=" + this.x_pos);
            f.push("y=" + this.y_pos);
            f.push("len=" + this.mouse_arr_tail);
            f.push("locus=" + this.get_locus());
            f.push("bw=" + this.get_bw());
            f.push("sc=" + this.get_nscroll());
            e += f.join("&");
            this.pingback(e)
        },
        getInstance: function () {
            if (!this.instances || this.instances.length < 1) {
                this.instances = [];
                var d = this.G.create(this);
                this.instances.push(d)
            }
        }
    };
    c.registerClass(b);
    var a = c.using("$baseName.Effect");
    a.MessageBoard.getInstance()
})(window[___sogouNamespaceName]);
