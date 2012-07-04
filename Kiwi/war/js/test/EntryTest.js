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
        assertSame('1970-01-01', entry.date);
        assertSame('tag', entry.tag);
        assertSame('body', entry.body);
        assertTrue(entry.isNew());
    },
    
    'test: construct with attrs / New': function () {
        var entry = new kiwi.Entry({
            authorName: 'a',
            authorAddress: 'b',
            date: '2008-04-01',
            tag: 'd',
            body: 'e'
        });
        assertSame('a', entry.authorName);
        assertSame('b', entry.authorAddress);
        assertSame('2008-04-01', entry.date);
        assertSame('d', entry.tag);
        assertSame('e', entry.body);
        assertTrue(entry.isNew());
    },
    
    'test: construct with attrs / NotNew': function () {
        var entry = new kiwi.Entry({
            authorName: 'a',
            authorAddress: 'b',
            date: '2008-04-01',
            tag: 'd',
            body: 'e',
            id: 432
        });
        assertSame('a', entry.authorName);
        assertSame('b', entry.authorAddress);
        assertSame('2008-04-01', entry.date);
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
                date: '2012-04-30',
                tag: 'd',
                body: 'e'
            }), data.data);
            return df;
        });
        var entry = new kiwi.Entry({});
        assertSame(df, entry.save({
            authorName: 'a',
            authorAddress: 'b',
            date: '2012-04-30',
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
                date: '2012-04-30',
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
            date: '2012-04-30',
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
            date: '2012-02-29',
            tag: 'd',
            body: 'e',
        }));
        assertUndefined(entry.validate({
        }));
        assertUndefined(entry.validate({date: '2000-02-29'}));

        assertNotUndefined(entry.validate({date: '1900-02-29'}));
        assertNotUndefined(entry.validate({date: '1900-06-31'}));
        assertNotUndefined(entry.validate({date: 'hogefuga'}));
        assertNotUndefined(entry.validate({tag: ''}));
        assertNotUndefined(entry.validate({body: ''}));
        
    },

    'this is not a test': undefined
});
