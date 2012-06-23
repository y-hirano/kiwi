package kiwi;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import kiwi.servlet.EntriesServlet;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class EntryTest {
    public Date date(int year, int month, int date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        c.set(year, month, date, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    @Test
    public void testConstructor() {
        Entry e1 = Entry.create(null, date(2012, 4, 8), "name", "address", "tag", "body");
        assertEquals(date(2012, 4, 8), e1.getDate());
        assertEquals("name", e1.getAuthorName());
        assertEquals("address", e1.getAuthorAddress());
        assertEquals("tag", e1.getTag());
        assertEquals("body", e1.getBody());
    }
    
    
    @Test
    public void testEquals() {
        Entry e = Entry.create(null, date(2012, 4, 8), "name", "address", "tag", "body");
        Entry e1 = Entry.create(null, date(2012, 4, 9), "name", "address", "tag", "body");
        Entry e2 = Entry.create(null, date(2012, 4, 8), "name1", "address", "tag", "body");
        Entry e3 = Entry.create(null, date(2012, 4, 8), "name", "address1", "tag", "body");
        Entry e4 = Entry.create(null, date(2012, 4, 8), "name", "address", "tag1", "body");
        Entry e5 = Entry.create(null, date(2012, 4, 8), "name", "address", "tag", "body1");
        Entry e6 = Entry.create(null, date(2012, 4, 8), "name", "address", "tag", "body");
        
        assertThat(e, is(not(e1)));
        assertThat(e, is(not(e2)));
        assertThat(e, is(not(e3)));
        assertThat(e, is(not(e4)));
        assertThat(e, is(not(e5)));
        assertThat(e, is(e6));
    }
    
    @Test
    public void testLongBodyConstructor() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Entry.STRING_SIZE_MAX + 1; ++i) {
            builder.append('a');
        }
        String body = builder.toString();
        Entry e = Entry.create(null, date(2012, 3, 8), "name", "address", "tag", body);
        assertThat(e.getBody(), is(body));
    }

    @Test
    public void testSetBody() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Entry.STRING_SIZE_MAX + 1; ++i) {
            builder.append('a');
        }
        String longBody = builder.toString();
        String shortBody = "body";
        Entry shortToLong = Entry.create(null, date(2012, 3, 8), "name", "address", "tag", shortBody);
        Entry longToShort = Entry.create(null, date(2012, 3, 8), "name", "address", "tag", longBody);
        assertThat(shortToLong.getBody(), is(shortBody));
        assertThat(longToShort.getBody(), is(longBody));
        
        shortToLong.setBody(longBody);
        longToShort.setBody(shortBody);
        
        assertThat(shortToLong.getBody(), is(longBody));
        assertThat(longToShort.getBody(), is(shortBody));
    }
}
