$(function () {
    'use strict';
    var settings = new kiwi.Settings();
    settings.fetch();
    var settingsView = new kiwi.SettingsView({model: settings, tmpl: $('#settingsTemplate')});
    settingsView.$el.appendTo($('#settings'));
    if (settings.isSet) {
        settingsView.hide();
    } else {
        settingsView.show();
    }
});



$(function () {
	'use strict';
    $('#get').click(function () {
        $.ajax({
            url: '/kiwi/entries',
            type: 'GET',
            datatype: 'json',
            data: {
            	account: 'guest'
            }
        }).done(function (resp) {
            $('#console').text(resp);
        });
    });

    $('#post').click(function () {
        var body = 'a';
        for (var i = 0; i < 10; ++i) {
            body += body
       	}
        $.ajax({
            url: '/kiwi/entries',
            type: 'POST',
            data: {
                date: $('#date').val(),
                authorName: 'Yutaka Hirano',
                authorAddress: 'yuta@luna',
                tag: $('#tag').val(),
                // body: $('#body').val()
                body: body
            }
        }).done(function (resp) {
            $('#console').text('post done!');
        });
    });
    $('#delete').click(function () {
        $.ajax({
            url: '/kiwi/entries?account=guest',
            type: 'DELETE',
            data: {
            	hello: 'world'
            }
        }).done(function (resp) {
            $('#console').text('post done!');
        });
    })

});   
    
