var kiwi = window.kiwi || {};

kiwi.Settings = Backbone.Model.extend({
    initialize: function (param) {
        'use strict';
        var properties = ['authorName', 'authorAddress', 'loginType', 'searchOnTag', 'searchOnBody', 'searchOnAttachmentName', 'entriesPerPage'];
        var self = this;
        properties.map(function (name) {
            Object.defineProperty(self, name, {
                get: function () {return self.get(name); },
                set: function (v) {self.set(name, v); }
            });
        });
        this._isSet = false;
        Object.defineProperty(this, 'isSet', {
           get: function () {return this._isSet; } 
        });
        var settings;
        // default settings
        this.set({
            authorName: '',
            authorAddress: '',
            loginType: this.LoginType.GUEST,
            searchOnTag: true,
            searchOnBody: true,
            searchOnAttachmentName: false,
            entriesPerPage: 50
        });
        if (typeof param === 'object' || param.storage instanceof Storage) {
            this._storage = param.storage;
            settings = param.storage.getItem('kiwi.settings'); 
            if (typeof settings === 'string') {
                this.set(JSON.parse(settings));
                this._isSet = this._isComplete(JSON.parse(settings));
            }
        }
    },
    _isComplete: function (settings) {
        'use strict';
        return typeof settings.authorName === 'string' &&
               typeof settings.authorAddress === 'string' &&
               (settings.loginType === this.LoginType.GUEST ||
                settings.loginType === this.LoginType.ACCOUNT) &&
               typeof settings.searchOnTag === 'boolean' &&
               typeof settings.searchOnBody === 'boolean' &&
               typeof settings.searchOnAttachmentName === 'boolean' &&
               typeof settings.entriesPerPage === 'number' && !isNaN(settings.entriesPerPage);
    },
    validate: function (attrs) {
        var settings = {
            authorName: this.authorName,
            authorAddress: this.authorAddress,
            loginType: this.loginType,
            searchOnTag: this.searchOnTag,
            searchOnBody: this.searchOnBody,
            searchOnAttachmentName: this.searchOnAttachmentName,
            entriesPerPage: this.entriesPerPage
        };
        for (var a in attrs) {
            if (attrs.hasOwnProperty(a)) {
                settings[a] = attrs[a];
            }
        }
        if (!this._isComplete(settings)) {
            return 'invalid';
        }
    },

    sync: function (settings) {
        if (!this._isComplete(this)) {
            return $.Deferred().reject();
        }
        this._storage.setItem('kiwi.settings', JSON.stringify({
            authorName: this.authorName,
            authorAddress: this.authorAddress,
            loginType: this.loginType,
            searchOnTag: this.searchOnTag,
            searchOnBody: this.searchOnBody,
            searchOnAttachmentName: this.searchOnAttachmentName,
            entriesPerPage: this.entriesPerPage
        }));
        this._isSet = true;
        return $.Deferred().resolve();
    }
});

kiwi.Settings.prototype.LoginType = {
        ACCOUNT: 'account',
        GUEST: 'guest'
};


kiwi.SettingsView = Backbone.View.extend({
   tagName: "div",
   
   className: "settings",
   
   events: {
       "click .save": "emitSave",
       "click .cancel": "cancel"
   },
   
   initialize: function(options) {
       var el = $.tmpl(options.tmpl, kiwi.Settings.prototype.LoginType);
       var settings = this.model;
       this._original = {
           authorName: settings.authorName,
           authorAddress: settings.authorAddress,
           loginType: settings.loginType,
           searchOnTag: settings.searchOnTag,
           searchOnBody: settings.searchOnBody,
           searchOnAttachmentName: settings.searchOnAttachmentName,
           entriesPerPage: settings.entriesPerPage
       };
       
       $('.authorName', el).val(settings.authorName);
       $('.authorAddress', el).val(settings.authorAddress);
       if (settings.loginType === settings.LoginType.ACCOUNT) {
           $('.loginTypeAccount', el).attr('checked', 'checked');
       } else {
           $('.loginTypeGuest', el).attr('checked', 'checked');
       }
       $('.searchOnTag', el).attr('checked', settings.searchOnTag);
       $('.searchOnBody', el).attr('checked', settings.searchOnBody);
       $('.searchOnAttachmentName', el).attr('checked', settings.searchOnAttachmentName);
       $('.entriesPerPage', el).val(settings.entriesPerPage);
       $('.cancel', el).attr('disabled', !settings.isSet);
       this._bindChangeEvents(settings, el);
       this.$el.append(el) ;
   },
   _bindModelChange: function (settings, el) {
       function onChangeAuthorName() {
           $('.authorName', el).val(settings.authorName);
       }
       function onChangeAuthorAddress() {
           $('.authorAddress', el).val(settings.authorAddress);
       }
       function onChangeLoginType() {
           if (settings.loginType === settings.LoginType.ACCOUNT) {
               $('.loginTypeAccount', el).attr('checked', 'checked');
               $('.loginTypeGuest', el).removeAttr('checked');
           } else {
               $('.loginTypeAccount', el).removeAttr('checked');
               $('.loginTypeGuest', el).attr('checked', 'checked');
           }
       }
       function onChangeSearchOnTag() {
           $('.searchOnTag', el).attr('checked', settings.searchOnTag);
       }
       function onChangeSearchOnBody() {
           $('.searchOnBody', el).attr('checked', settings.searchOnBody);
       }
       function onChangeSearchOnAttachmentName() {
           $('.searchOnAttachmentName', el).attr('checked', settings.searchOnAttachmentName);
       }
       function onChangeEntriesPerPage() {
           $('.entriesPerPage', el).val(settings.entriesPerPage);
       }
       settings.on('change:authorName', onChangeAuthorName);
       settings.on('change:authorAddress', onChangeAuthorAddress);
       settings.on('change:loginType', onChangeLoginType);
       settings.on('change:searchOnTag', onChangeSearchOnTag);
       settings.on('change:searchOnBody', onChangeSearchOnBody);
       settings.on('change:searchOnAttachmentName', onChangeSearchOnAttachmentName);
       settings.on('change:entriesPerPage', onChangeEntriesPerPage);

       this._unbindModelChange = function () {
           settings.off('change:authorName', onChangeAuthorName);
           settings.off('change:authorAddress', onChangeAuthorAddress);
           settings.off('change:loginType', onChangeLoginType);
           settings.off('change:searchOnTag', onChangeSearchOnTag);
           settings.off('change:searchOnBody', onChangeSearchOnBody);
           settings.off('change:searchOnAttachmentName', onChangeSearchOnAttachmentName);
           settings.off('change:entriesPerPage', onChangeEntriesPerPage);
       };
   },
   close: function () {
       this.remove();
       if (this._unbindModelChange) {
           this._unbindModelChange();
       }
   },
   
   _bindChangeEvents: function (settings, el) {
       this._bindModelChange(settings, el);
       $('.authorName', el).change(function (e) {
           settings.authorName = $(e.target).val();
           $(e.target).val(settings.authorName);
       });
       $('.authorAddress', el).change(function (e) {
           settings.authorAddress = $(e.target).val();
           $(e.target).val(settings.authorAddress);
       });
       $('.loginTypeAccount', el).change(function (e) {
           if ($(e.target).is(':checked')) {
               settings.loginType = settings.LoginType.ACCOUNT;
           }
           if (settings.loginType === settings.LoginType.ACCOUNT) {
               $('.loginTypeAccount', el).attr('checked', 'checked');
               $('.loginTypeGuest', el).removeAttr('checked');
           } else {
               $('.loginTypeAccount', el).removeAttr('checked');
               $('.loginTypeGuest', el).attr('checked', 'checked');
           }
       });
       $('.loginTypeGuest', el).change(function (e) {
           if ($(e.target).is(':checked')) {
               settings.loginType = settings.LoginType.GUEST;
           }
           if (settings.loginType === settings.LoginType.ACCOUNT) {
               $('.loginTypeAccount', el).attr('checked', 'checked');
               $('.loginTypeGuest', el).removeAttr('checked');
           } else {
               $('.loginTypeAccount', el).removeAttr('checked');
               $('.loginTypeGuest', el).attr('checked', 'checked');
           }
       });
       $('.searchOnTag', el).change(function (e) {
           settings.searchOnTag = $(e.target).is(':checked');
           $(e.target).attr('checked', settings.searchOnTag);
       });
       $('.searchOnBody', el).change(function (e) {
           settings.searchOnBody = $(e.target).is(':checked');
           $(e.target).attr('checked', settings.searchOnBody);
       });
       $('.searchOnAttachmentName', el).change(function (e) {
           settings.searchOnAttachmentName = $(e.target).is(':checked');
           $(e.target).attr('checked', settings.searchOnAttachmentName);
       });
       $('.entriesPerPage', el).change(function (e) {
           settings.entriesPerPage = Number($(e.target).val()); 
           $(e.target).val(settings.entriesPerPage);
       });
   },
   
   emitSave: function () {
       this.model.save().done(this.close.bind(this));
   },
   
   cancel: function () {
       this.close();
       for (var o in this._original) {
           if (this._original.hasOwnProperty(o)) {
               this.model.set(o, this._original[o]);
           }
       }
   }
});