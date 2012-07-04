package kiwi.servlet;

import java.util.Calendar;
import java.util.TimeZone;

import com.google.appengine.api.users.User;

import kiwi.Entry;

class JSONEntry {
    JSONEntry(Entry e) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTime(e.getDate());
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DATE);
        authorName = e.getAuthorName();
        authorAddress = e.getAuthorAddress();
        tag = e.getTag();
        body = e.getBody();
    }
    Entry getEntry(User user) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.set(year, month, mday, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return Entry.create(user, c.getTime(), authorName, authorAddress, tag, body);
    }
    Long id;
    int year;
    int month;
    int mday;
    String authorName;
    String authorAddress;
    String tag;
    String body;
}