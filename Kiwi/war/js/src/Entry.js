var kiwi = window.kiwi || {};

kiwi.Entry = Backbone.Model.extend({
    url: '/kiwi/Entries',
    initialize: function () {
        var props = ['authorName', 'authorAddress', 'date', 'tag', 'body'];
        var self = this;
        props.map(function (name) {
            Object.defineProperty(self, name, {
                get: function () {return self.get(name); },
                set: function (v) {self.set(name, v); }
            })
        });
        this.authorName = this.authorName || '';
        this.authorAddress = this.authorAddress || '';
        this.date = this.date || '1970-01-01';
        this.tag = this.tag || 'tag';
        this.body = this.body || 'body';
    },
    validate: function (attrs) {
        function validateDate(year, month, mday) {
            var numDates = ['31', '29', '31', '30', '31', '30', '31', '31', '30', '31', '30', '31'];
            jstestdriver.console.log('' + year + ', ' + month + ', ' + mday);
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
        if (attrs.hasOwnProperty('date')) {
            var date = attrs.date;
            this._datePattern = new RegExp('^(\\d+)-(\\d+)-(\\d+)$');
            var match = date.match(this._datePattern);
            jstestdriver.console.log('match = ' + match);
            if (match === null) {
                jstestdriver.console.log('return: ' + date + ' does not match ' + this._datePattern);
                return date + ' does not match ' + this._datePattern;
            }
            if (!validateDate(Number(match[1]), Number(match[2]), Number(match[3]))) {
                return date + ': value error';
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
