TestCase('EntryModel', {
    'setUp': function () {
        'use strict';
    },
    'tearDown': function () {
        'use strict';
        window.sessionStorage.clear();
        // Some tests replace $.ajax.
        if ($.ajax.restore) {
            $.ajax.restore();
        }
    },
    
    'test: construct': function () {
        var entry = new kiwi.Entry();
        assertSame('', entry.authorName);
        assertSame('', entry.authorAddress);
        assertSame(1970, entry.year);
        assertSame(1, entry.month);
        assertSame(1, entry.mday);
        assertSame('1970-01-01', entry.date);
        assertSame('tag', entry.tag);
        assertSame('body', entry.body);
        assertTrue(entry.isNew());
    },
    
    'test: set invalid value': function () {
        var entry = new kiwi.Entry();
        assertException(function () {entry.tag = ''}, 'Error');
        assertSame('tag', entry.tag);
        assertException(function () {entry.body = ''}, 'Error');
        assertSame('body', entry.body);
        assertException(function () {entry.date = 'hogefuga'}, 'Error');
        assertSame('1970-01-01', entry.date);
        assertException(function () {entry.date = '1900-02-29'}, 'Error');
        assertSame('1970-01-01', entry.date);
    },
    
    'test: set date': function () {
        var entry = new kiwi.Entry();
        entry.date = '2010-04-30';
        assertSame('2010-04-30', entry.date);
        assertSame(2010, entry.year);
        assertSame(4, entry.month);
        assertSame(30, entry.mday);
    },
    
    'test: construct with attrs / New': function () {
        var entry = new kiwi.Entry({
            authorName: 'a',
            authorAddress: 'b',
            year: 2008,
            month: 4,
            mday: 1,
            tag: 'd',
            body: 'e'
        });
        assertSame('a', entry.authorName);
        assertSame('b', entry.authorAddress);
        assertSame(2008, entry.year);
        assertSame(4, entry.month);
        assertSame(1, entry.mday);
        assertSame('d', entry.tag);
        assertSame('e', entry.body);
        assertTrue(entry.isNew());
    },
    
    'test: construct with attrs / NotNew': function () {
        var entry = new kiwi.Entry({
            authorName: 'a',
            authorAddress: 'b',
            year: 2008,
            month: 4,
            mday: 1,
            tag: 'd',
            body: 'e',
            id: 432
        });
        assertSame('a', entry.authorName);
        assertSame('b', entry.authorAddress);
        assertSame(2008, entry.year);
        assertSame(4, entry.month);
        assertSame(1, entry.mday);
        assertSame('d', entry.tag);
        assertSame('e', entry.body);
        assertSame(432, entry.id);
        assertFalse(entry.isNew());
    },

    'test: save / POST': function () {
        var df = $.Deferred();
        sinon.stub($, 'ajax', function (data) {
            assertSame('/kiwi/Entries', data.url);
            assertSame('POST', data.type);
            assertSame(JSON.stringify({
                authorName: 'a',
                authorAddress: 'b',
                year: 2012,
                month: 4,
                mday: 30,
                tag: 'd',
                body: 'e'
            }), data.data);
            return df;
        });
        var entry = new kiwi.Entry({});
        assertSame(df, entry.save({
            authorName: 'a',
            authorAddress: 'b',
            year: 2012,
            month: 4,
            mday: 30,
            tag: 'd',
            body: 'e'
        }));
        assertTrue($.ajax.called);
    },

    'test: save / PUT': function () {
        var df = $.Deferred();
        sinon.stub($, 'ajax', function (data) {
            assertSame('/kiwi/Entries', data.url);
            assertSame('PUT', data.type);
            assertSame(JSON.stringify({
                authorName: 'a',
                authorAddress: 'b',
                year: 2012,
                month: 4,
                mday: 30,
                tag: 'd',
                body: 'e',
                id: 41
            }), data.data);
            return df;
        });
        var entry = new kiwi.Entry({});
        assertSame(df, entry.save({
            authorName: 'a',
            authorAddress: 'b',
            year: 2012,
            month: 4,
            mday: 30,
            tag: 'd',
            body: 'e',
            id: 41
        }));
        assertTrue($.ajax.called);
    },

    'test: validate': function () {
        var entry = new kiwi.Entry({});
        assertUndefined(entry.validate({
            authorName: 'a',
            authorAddress: 'b',
            year: 2012,
            month: 2,
            mday: 29,
            tag: 'd',
            body: 'e'
        }));
        assertUndefined(entry.validate({
        }));
        assertUndefined(entry.validate({year: 2000, month: 2, mday: 29}));

        assertNotUndefined(entry.validate({year: 1900, month: 2, mday: 29}));
        assertNotUndefined(entry.validate({year: 1900, month: 6, mday: 31}));
        assertNotUndefined(entry.validate({tag: ''}));
        assertNotUndefined(entry.validate({body: ''}));
        
    },
    'this is not a test': undefined
});
