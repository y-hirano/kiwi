kiwi.testing = kiwi.testing || {};

TestCase('SettingsView', {
    'setUp': function () {
        'use strict';
        /*:DOC tmpl = <script type = "text/x-jquery-tmpl"><div>
          <input type = "text" class = "authorName">
          <input type = "text" class = "authorAddress">
          <input type = "radio" name = "loginType" class = "loginTypeAccount" value = ${ACCOUNT}>
          <input type = "radio" name = "loginType" class = "loginTypeGuest" value = ${GUEST}>
          <input type = "checkbox" class = "searchOnTag" value = "searchOnTag">
          <input type = "checkbox" class = "searchOnBody" value = "searchOnBody">
          <input type = "checkbox" class = "searchOnAttachmentName" value = "searchOnAttachmentName">
          <input type = "text" class = "entriesPerPage">
          <button class = "save">Save</button>
          <button class = "cancel">Cancel</button>
          </div></script>
         */
        this.settings = new (Backbone.Model.extend({
        }))({
            authorName: 'authorName',
            authorAddress: 'authorAddress',
            loginType: 'account',
            searchOnTag: false,
            searchOnBody: false,
            searchOnAttachmentName: true,
            entriesPerPage: 42,
        });
        var settings = this.settings;
        settings.isSet = false;
        settings.LoginType = kiwi.Settings.prototype.LoginType;
        
        var properties = ['authorName', 'authorAddress', 'loginType', 'searchOnTag', 'searchOnBody', 'searchOnAttachmentName', 'entriesPerPage'];
        properties.map(function (name) {
            Object.defineProperty(settings, name, {
                get: function () {return settings.get(name); },
                set: function (v) {settings.set(name, v); }
            });
        });
    },

    'test: initialize': function () {
        'use strict';
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.tmpl});
        assertSame('authorName', $('.authorName', view.$el).val());
        assertSame('authorAddress', $('.authorAddress', view.$el).val());
        assertTrue($('.loginTypeAccount', view.$el).is(':checked'));
        assertFalse($('.loginTypeGuest', view.$el).is(':checked'));
        assertFalse($('.searchOnTag', view.$el).is(':checked'));
        assertFalse($('.searchOnBody', view.$el).is(':checked'));
        assertTrue($('.searchOnAttachmentName', view.$el).is(':checked'));
        assertSame('42', $('.entriesPerPage', view.$el).val());
    },
    
    'test: cannot cancel': function () {
        'use strict';
        this.settings.isSet = false;
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.tmpl});
        
        assertTrue($('.cancel', view.$el).is(':disabled'));
    },

    'test: can cancel': function () {
        'use strict';
        this.settings.isSet = true;
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.tmpl});
        
        assertFalse($('.cancel', view.$el).is(':disabled'));
    },
    
    'test: model change => view': function () {
        'use strict';
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.tmpl});
        var settings = this.settings;
        
        settings.authorName = 'foo';
        settings.authorAddress = 'bar';
        settings.loginType = 'guest';
        settings.searchOnTag = true;
        settings.searchOnBody = true;
        settings.searchOnAttachmentName = false;
        settings.entriesPerPage = 188;
        
        assertSame('foo', $('.authorName', view.$el).val());
        assertSame('bar', $('.authorAddress', view.$el).val());
        assertFalse($('.loginTypeAccount', view.$el).is(':checked'));
        assertTrue($('.loginTypeGuest', view.$el).is(':checked'));
        assertTrue($('.searchOnTag', view.$el).is(':checked'));
        assertTrue($('.searchOnBody', view.$el).is(':checked'));
        assertFalse($('.searchOnAttachmentName', view.$el).is(':checked'));
        assertSame('188', $('.entriesPerPage', view.$el).val());
    },

    'test: view change => model': function () {
        'use strict';
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.tmpl});
        var settings = this.settings;
        
        $('.authorName', view.$el).val('foo');
        $('.authorAddress', view.$el).val('bar');
        $('.loginTypeGuest', view.$el).attr('checked', true);
        $('.searchOnTag', view.$el).attr('checked', true);
        $('.searchOnBody', view.$el).attr('checked', true);
        $('.searchOnAttachmentName', view.$el).attr('checked', false);
        $('.entriesPerPage', view.$el).val('188');

        $('.authorName', view.$el).trigger($.Event('change'));
        $('.authorAddress', view.$el).trigger($.Event('change'));
        $('.loginTypeGuest', view.$el).trigger($.Event('change'));
        $('.searchOnTag', view.$el).trigger($.Event('change'));
        $('.searchOnBody', view.$el).trigger($.Event('change'));
        $('.searchOnAttachmentName', view.$el).trigger($.Event('change'));
        $('.entriesPerPage', view.$el).trigger($.Event('change'));
        
        assertSame('foo', settings.authorName);
        assertSame('bar', settings.authorAddress);
        assertSame('guest', settings.loginType);
        assertTrue(settings.searchOnTag);
        assertTrue(settings.searchOnBody);
        assertFalse(settings.searchOnAttachmentName);
        assertSame(188, settings.entriesPerPage);
    },

    'test: invalid view change': function () {
        'use strict';
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.tmpl});
        var settings = this.settings;
        
        settings.validate = function () {
            return 'hogefuga';
        }
        
        $('.authorName', view.$el).val('foo');
        $('.authorAddress', view.$el).val('bar');
        $('.loginTypeGuest', view.$el).attr('checked', true);
        $('.searchOnTag', view.$el).attr('checked', true);
        $('.searchOnBody', view.$el).attr('checked', true);
        $('.searchOnAttachmentName', view.$el).attr('checked', false);
        $('.entriesPerPage', view.$el).val('188');

        $('.authorName', view.$el).trigger($.Event('change'));
        $('.authorAddress', view.$el).trigger($.Event('change'));
        $('.loginTypeGuest', view.$el).trigger($.Event('change'));
        $('.searchOnTag', view.$el).trigger($.Event('change'));
        $('.searchOnBody', view.$el).trigger($.Event('change'));
        $('.searchOnAttachmentName', view.$el).trigger($.Event('change'));
        $('.entriesPerPage', view.$el).trigger($.Event('change'));
        
        assertSame('authorName', settings.authorName);
        assertSame('authorAddress', settings.authorAddress);
        assertSame('account', settings.loginType);
        assertFalse(settings.searchOnTag);
        assertFalse(settings.searchOnBody);
        assertTrue(settings.searchOnAttachmentName);
        assertSame(42, settings.entriesPerPage);
        
        assertSame('authorName', $('.authorName', view.$el).val());
        assertSame('authorAddress', $('.authorAddress', view.$el).val());
        assertTrue($('.loginTypeAccount', view.$el).is(':checked'));
        assertFalse($('.loginTypeGuest', view.$el).is(':checked'));
        assertFalse($('.searchOnTag', view.$el).is(':checked'));
        assertFalse($('.searchOnBody', view.$el).is(':checked'));
        assertTrue($('.searchOnAttachmentName', view.$el).is(':checked'));
        assertSame('42', $('.entriesPerPage', view.$el).val());
    },
    
    'test: unbind events on close': function () {
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.tmpl});
        var settings = this.settings;
        var isCalled = false;
        view.remove = function () {
            isCalled = true;
        };
        settings.authorName = 'name1';
        assertSame('name1', $('.authorName', view.$el).val());
        view.close();
        assertTrue(isCalled);
        settings.authorName = 'name2';
        assertSame('name1', $('.authorName', view.$el).val());
    },
    
    'test: emit save': function () {
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.impl});
        var settings = this.settings;
        settings.sync = sinon.stub().returns($.Deferred().resolve());
        
        sinon.spy(settings, 'save');
        view.close = sinon.spy();
        view.emitSave();
        assertTrue(settings.save.called);
        assertTrue(view.close.called);
    },
    
    'test: emit save failed': function () {
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.impl});
        var settings = this.settings;
        settings.sync = sinon.stub().returns($.Deferred().reject());
        sinon.spy(settings, 'save');
        view.close = sinon.spy();
        view.emitSave();
        assertTrue(settings.save.called);
        assertFalse(view.close.called);
    },
    
    'test: cancel': function () {
        var view = new kiwi.SettingsView({model: this.settings, tmpl: this.impl});
        var settings = this.settings;
        sinon.spy(view, 'close');
        
        settings.authorName = 'foo';
        settings.authorAddress = 'bar';
        settings.loginType = 'guest';
        settings.searchOnTag = true;
        settings.searchOnBody = true;
        settings.searchOnAttachmentName = false;
        settings.entriesPerPage = 188;
        
        view.cancel();
        assertTrue(view.close.called);
        assertSame('authorName', settings.authorName);
        assertSame('authorAddress', settings.authorAddress);
        assertSame('account', settings.loginType);
        assertFalse(settings.searchOnTag);
        assertFalse(settings.searchOnBody);
        assertTrue(settings.searchOnAttachmentName);
        assertSame(42, settings.entriesPerPage);
    },

    'this is not a test': null
});