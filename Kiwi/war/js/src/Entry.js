var kiwi = window.kiwi || {};

kiwi.Entry = Backbone.Model.extend({
    url: '/kiwi/Entries',
    initialize: function () {
        var props = ['authorName', 'authorAddress', 'year', 'month', 'mday', 'tag', 'body'];
        var self = this;
        props.map(function (name) {
            Object.defineProperty(self, name, {
                get: function () {return self.get(name); },
                set: function (v) {
                    var r = self.set(name, v);
                    if (r === false) {
                        throw new Error('Failed setter: ' + name + ' := ' + v);
                    }
                }
            })
        });
        Object.defineProperty(self, 'date', {
            get: function () {
                var syear = String(this.year);
                var smonth = String(this.month);
                var smday = String(this.mday);
                while (syear.length < 4) {
                    syear = '0' + syear;
                }
                while (smonth.length < 2) {
                    smonth = '0' + smonth;
                }
                while (smday.length < 2) {
                    smday = '0' + smday;
                }
                return syear + '-' + smonth + '-' + smday;
            },
            set: function (date) {
                var pattern = new RegExp('^(\\d+)-(\\d+)-(\\d+)$');
                var match = date.match(pattern);
                if (match === null) {
                    throw new Error(date + ' does not match ' + pattern);
                }
                var o = {year: Number(match[1]), month: Number(match[2]), mday: Number(match[3])};
                if (this.set(o) === false) {
                    throw new Error(date + ': value error');
                }
            }
        });
        
        
        this.authorName = this.authorName || '';
        this.authorAddress = this.authorAddress || '';
        if (this.year === undefined || this.month === undefined || this.mday === undefined) {
            this.set({year: 1970, month: 1, mday: 1});
        }
        this.tag = this.tag || 'tag';
        this.body = this.body || 'body';
    },
    validate: function (attrs) {
        function validateDate(year, month, mday) {
            var numDates = ['31', '29', '31', '30', '31', '30', '31', '31', '30', '31', '30', '31'];
            if (year < 0) {
                return false;
            }
            if (!(1 <= month && month <= 12)) {
                return false;
            }
            if (!(1 <= mday && mday <= numDates[month - 1])) {
                return false;
            }
            if (month == 2 && (year % 4 !== 0 || (year % 100 == 0 && year % 400 !== 0))) {
                return mday <= 28;
            }
            return true;
        }
        if (attrs.hasOwnProperty('year') || attrs.hasOwnProperty('month') || attrs.hasOwnProperty('mday')) {
            var year = this.year;
            var month = this.month;
            var mday = this.mday;
            if (attrs.hasOwnProperty('year')) {
                year = attrs.year;
            }
            if (attrs.hasOwnProperty('month')) {
                month = attrs.month;
            }
            if (attrs.hasOwnProperty('mday')) {
                mday = attrs.mday;
            }
            if (!validateDate(year, month, mday)) {
                return 'Invalid date value: ' + JSON.stringify({year: year, month: month, mday: mday});
            }
        }
        if (attrs.hasOwnProperty('tag')) {
            if (attrs.tag === '') {
                return 'Tag must not be empty';
            }
        }
        if (attrs.hasOwnProperty('body')) {
            if (attrs.body === '') {
                return 'Body must not be empty';
            }
        }
    }
});
