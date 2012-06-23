TestCase('SettingsModel', {
    'tearDown': function () {
        window.sessionStorage.clear();
    },
    'test: initial value': function () {
        var settings = new kiwi.Settings({storage: sessionStorage});

        assertFalse(settings.isSet);
        assertSame('', settings.authorName);
        assertSame('', settings.authorAddress);
        assertSame(settings.LoginType.GUEST, settings.loginType);
        assertTrue(settings.searchOnTag);
        assertTrue(settings.searchOnBody);
        assertFalse(settings.searchOnAttachmentName);
        assertSame(50, settings.entriesPerPage);
    }, 
    
    'test: partially set': function () {
        sessionStorage.setItem('kiwi.settings', JSON.stringify({isSet: true, authorName: 'authorName', loginType: 'account', entriesPerPage: 42}));

        var settings = new kiwi.Settings({storage: sessionStorage});
        
        assertFalse(settings.isSet);
        assertSame('authorName', settings.authorName);
        assertSame('', settings.authorAddress);
        assertSame(settings.LoginType.ACCOUNT, settings.loginType);
        assertTrue(settings.searchOnTag);
        assertTrue(settings.searchOnBody);
        assertFalse(settings.searchOnAttachmentName);
        assertSame(42, settings.entriesPerPage);
    },
    
    
    'test: set': function () {
        var data = {
                authorName: 'authorName', authorAddress: 'authorAddress',
                loginType: 'account', searchOnTag: false, searchOnBody: false, searchOnAttachmentName: true, entriesPerPage: 42
        };
        sessionStorage.setItem('kiwi.settings', JSON.stringify(data));
        var settings = new kiwi.Settings({storage: sessionStorage});
        
        assertTrue(settings.isSet);
        assertSame('authorName', settings.authorName);
        assertSame('authorAddress', settings.authorAddress);
        assertSame(settings.LoginType.ACCOUNT, settings.loginType);
        assertFalse(settings.searchOnTag);
        assertFalse(settings.searchOnBody);
        assertTrue(settings.searchOnAttachmentName);
        assertSame(42, settings.entriesPerPage);
    },
    'test: invalid set': function () {
        var settings = new kiwi.Settings({storage: sessionStorage});
        
        settings.authorName = 12;
        settings.authorAddress = 18;
        settings.loginType = 'hello';
        settings.searchOnTag = 42;
        settings.searchOnBody = 'bye';
        settings.searchOnAttachmentName = undefined;
        settings.entriesPerPage = NaN;
        
        assertSame('', settings.authorName);
        assertSame('', settings.authorAddress);
        assertSame(settings.LoginType.GUEST, settings.loginType);
        assertTrue(settings.searchOnTag);
        assertTrue(settings.searchOnBody);
        assertFalse(settings.searchOnAttachmentName);
        assertSame(50, settings.entriesPerPage);
    },    
    'test: save': function () {
        var data = {
                authorName: 'authorName', authorAddress: 'authorAddress',
                loginType: 'account', searchOnTag: false, searchOnBody: false, searchOnAttachmentName: true, entriesPerPage: 42
        };
        var storage = sessionStorage;
        var settings = new kiwi.Settings({storage: storage});
        settings.set(data);
        var df = settings.save();
        
        assertEquals('resolved', df.state());
        data = JSON.parse(storage.getItem('kiwi.settings'));
        assertSame('authorName', data.authorName);
        assertSame('authorAddress', data.authorAddress);
        assertSame(settings.LoginType.ACCOUNT, data.loginType);
        assertFalse(data.searchOnTag);
        assertFalse(data.searchOnBody);
        assertTrue(data.searchOnAttachmentName);
        assertSame(42, data.entriesPerPage);
     },
     
     'test: saved model is set': function () {
         var settings = new kiwi.Settings({storage: sessionStorage});
         
         assertFalse(settings.isSet);
         var df = settings.save();
         assertSame('resolved', df.state());
         assertTrue(settings.isSet);
     },

    'this is not a test': null
});