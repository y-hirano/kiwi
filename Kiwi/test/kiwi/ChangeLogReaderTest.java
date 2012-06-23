package kiwi;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.users.User;

public class ChangeLogReaderTest {
    public Date date(int year, int month, int date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        c.set(year, month, date, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    private ChangeLogReader reader;
    private User user;
    @Before
    public void setup() {
        reader = new ChangeLogReader(user);
    }
    
    @Test
    public void testConstruct() throws Exception {
        assertEquals(new ArrayList<Entry>(), reader.getEntries());
    }

    @Test
    public void testEmpty() throws Exception {
        reader.read(new StringReader(""));
        assertEquals(new ArrayList<Entry>(), reader.getEntries());
    }

    @Test
    public void testSimpleEntry() throws Exception {
        Entry expected = Entry.create(user, date(2001, 8, 11), "name", "address", "tag", "body");
        String input = String.format("%04d-%02d-%02d %s <%s>\n\t%s: %s\n", 
                       2001, 8, 11, "name", "address", "tag", "body");
        
        reader.read(new StringReader(input));
        List<Entry> entries = reader.getEntries();
        assertThat(entries, is(Arrays.asList(expected)));
    }
    
    @Test
    public void testMultilineBodyEntry() throws Exception {
        Entry expected = Entry.create(user, date(2001, 8, 11), 
                "name", "address", "tag", "body\n\n \nbody2");
        String input = String.format("%04d-%02d-%02d %s <%s>\n\t%s: %s\n", 
                2001, 8, 11, "name", "address", "tag", "body\n\t\n\t \n\tbody2");
        
        reader.read(new StringReader(input));
        assertThat(reader.getEntries(), is(Arrays.asList(expected)));
    }
    
    @Test
    public void testMultipleEntries() throws Exception {
        List<Entry> expected = Arrays.asList(
                Entry.create(user, date(2004, 8, 11), "name 1", "address 1", "tag1", "hello\nworld"),
                Entry.create(user, date(2004, 8, 11), "name 1", "address 1", "tag2", "foobar\nhoge: fuga"),
                Entry.create(user, date(2004, 8, 11), "name3", "address3", "tag3", "hello world"),
                Entry.create(user, date(2004, 12, 3), "name4", "address4", "tag4", "hogefuga")
                );
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%04d-%02d-%02d %s <%s>\n", 2004, 8, 11, "name 1", "address 1"));
        builder.append(String.format("\t%s: %s\n", "tag1", "hello\n\tworld"));
        builder.append("\n");
        builder.append(String.format("\t%s: %s\n", "tag2", "foobar\n\thoge: fuga"));
        builder.append("\n");
        builder.append(String.format("%04d-%02d-%02d %s <%s>\n", 2004, 8, 11, "name3", "address3"));
        builder.append(String.format("\t%s: %s\n", "tag3", "hello world"));
        builder.append("\n");
        builder.append("\n");
        builder.append(String.format("%04d-%02d-%02d %s <%s>\n", 2004, 12, 3, "name4", "address4"));
        builder.append(String.format("\t%s: %s\n", "tag4", "hogefuga"));
        builder.append("\n");
        builder.append("\n");
        builder.append("\n");
        
        reader.read(new StringReader(builder.toString()));
        assertThat(reader.getEntries(), is(expected));
    }
    @Test
    public void testClearExistingEntries() throws Exception {
        reader.read(new StringReader("2012-03-21 x <y>\n\ttag: body\n"));
        assertThat(reader.getEntries().size(), is(1));
        reader.read(new StringReader(""));
        assertThat(reader.getEntries().size(), is(0));
    }
    
    @Test
    public void testRetainExistingEntriesOnError() throws Exception {
        reader.read(new StringReader("2012-03-21 x <y>\n\ttag: body\n"));
        List<Entry> entries = reader.getEntries();
        assertThat(entries.size(), is(1));
        try {
            reader.read(new StringReader("hogefuga"));
            assertTrue(false);
        } catch (ParseException e) {
            assertThat(e.getErrorOffset(), is(1));
        }
        assertThat(reader.getEntries(), is(entries));
    }
    
    @Test
    public void testErrorNoDateHeader() throws Exception {
        try {
            reader.read(new StringReader("\ttag: hoge\n"));
            assertTrue(false);
        } catch (ParseException e) {
            assertThat(e.getErrorOffset(), is(1));
        }
    }

    @Test
    public void testErrorNoEntryStart() throws Exception {
        try {
            reader.read(new StringReader("2011-04-05 hoge <fuga>\n\nfoobar\n"));
            assertTrue(false);
        } catch (ParseException e) {
            assertThat(e.getErrorOffset(), is(3));
        }
    }

}
