kiwi.testing = kiwi.testing || {};

TestCase('EditorView', {
    'setUp': function () {
        'use strict';
        /*:DOC tmpl = <script type = "text/x-jquery-tmpl"><div>
          <input type = "text" class = "date">
          <input type = "text" class = "authorName">
          <input type = "text" class = "authorAddress">
          <input type = "text" class = "tag">
          <input type = "text" class = "body">
          <button class = "emit">${emitLabel}</button>
          <button class = "clear">Clear</button>
          </div></script>
         */
        this.entry = new (Backbone.Model.extend({
        }))({
            year: 2008,
            month: 4,
            mday: 8,
            date: '2008-04-08',
            authorName: 'authorName',
            authorAddress: 'authorAddress',
            tag: 'tag1',
            body: 'body1',
        });
        var entry = this.entry;
        var properties = ['date', 'year', 'month', 'mday', 'authorName', 'authorAddress', 'tag', 'body'];
        properties.map(function (name) {
            Object.defineProperty(entry, name, {
                configurable: true,
                get: function () {return entry.get(name); },
                set: function (v) {entry.set(name, v); }
            });
        });
    },

    'test: initialize with a new entry': function () {
        'use strict';
        var view = new kiwi.EditorView({model: this.entry, tmpl: this.tmpl});
        assertSame('2008-04-08', $('.date', view.$el).val());
        assertSame('authorName', $('.authorName', view.$el).val());
        assertSame('authorAddress', $('.authorAddress', view.$el).val());
        assertSame('tag1', $('.tag', view.$el).val());       
        assertSame('body1', $('.body', view.$el).val());
        assertSame('New', $('.emit', view.$el).text());
    },

    'test: initialize with an existing entry': function () {
        'use strict';
        this.entry.id = 48;
        var view = new kiwi.EditorView({model: this.entry, tmpl: this.tmpl});
        assertSame('2008-04-08', $('.date', view.$el).val());
        assertSame('authorName', $('.authorName', view.$el).val());
        assertSame('authorAddress', $('.authorAddress', view.$el).val());
        assertSame('tag1', $('.tag', view.$el).val());       
        assertSame('body1', $('.body', view.$el).val());
        assertSame('Apply', $('.emit', view.$el).text());
    },
    
    'test: save entry': function () {
        'use strict';
        var entry = this.entry;
        var view = new kiwi.EditorView({model: this.entry, tmpl: this.tmpl});
        $('.date', view.$el).val('2012-03-31');
        $('.authorName', view.$el).val('name');
        $('.authorAddress', view.$el).val('address');
        $('.tag', view.$el).val('tag2');       
        $('.body', view.$el).val('body2');
        
        var stub = sinon.stub(entry, 'save');
        $('.emit', view.$el).trigger($.Event('click'));
        
        
        assertTrue(stub.called);
        assertSame('2012-03-31', entry.date);
        assertSame('name', entry.authorName);
        assertSame('address', entry.authorAddress);
        assertSame('tag2', entry.tag);
        assertSame('body2', entry.body);
    },
    
    'test: save entry failed': function () {
        var entry = this.entry;
        var view = new kiwi.EditorView({model: this.entry, tmpl: this.tmpl});
        $('.date', view.$el).val('2012-03-31');
        $('.authorName', view.$el).val('name');
        $('.authorAddress', view.$el).val('address');
        $('.tag', view.$el).val('tag2');       
        $('.body', view.$el).val('body2');
        
        var stub = sinon.stub(entry, 'save');
        Object.defineProperty(entry, 'tag', {
            configurable: true,
            get: function () {return this.get('tag'); },
            set: function () {throw new Error('hogefuga'); }
        });
        
        $('.emit', view.$el).trigger($.Event('click'));
        
        assertFalse(stub.called);
        
        // Below results depend on the order of setter calls.
        assertSame('2012-03-31', entry.date);
        assertSame('name', entry.authorName);
        assertSame('address', entry.authorAddress);
        assertSame('tag1', entry.tag);
        assertSame('body1', entry.body);
        
        assertSame('2012-03-31', $('.date', view.$el).val());
        assertSame('name', $('.authorName', view.$el).val());
        assertSame('address', $('.authorAddress', view.$el).val());
        assertSame('tag1', $('.tag', view.$el).val());       
        assertSame('body1', $('.body', view.$el).val());
    },
    
    
    'this is not a test': null
});