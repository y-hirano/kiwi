var kiwi = window.kiwi || {};

kiwi.EditorView = Backbone.View.extend({
    events: {
        'click .emit': '_emit'
    },
    initialize: function(options) {
        'use strict';
        var entry = this.model;
        var el = $.tmpl(options.tmpl, {emitLabel: entry.isNew()? 'New': 'Apply'});
        this.$el.append(el);
        this.render();
    },
    render: function() {
        'use strict';
        var el = this.$el;
        var entry = this.model;
        $('.date', el).val(entry.date);
        $('.authorName', el).val(entry.authorName);
        $('.authorAddress', el).val(entry.authorAddress);
        $('.tag', el).val(entry.tag);
        $('.body', el).val(entry.body);
    },
    
    _emit: function () {
        var entry = this.model;
        var el = this.$el;
        try {
            entry.date = $('.date', el).val();
            entry.authorName = $('.authorName', el).val();
            entry.authorAddress = $('.authorAddress', el).val();
            entry.tag = $('.tag', el).val();
            entry.body = $('.body', el).val();
            entry.save();
        } catch (e) {
            this.render();
        }
    }
});