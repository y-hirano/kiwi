package kiwi;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kiwi.servlet.EntriesServlet;

import org.junit.Test;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import static org.hamcrest.CoreMatchers.*;

class ServletRequest implements HttpServletRequest {
    ServletRequest(Map<String, String[]> map) {
        this.parameterMap = map;
    }
    Map<String, String[]> parameterMap;
    @Override
    public Object getAttribute(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getContentLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getContentType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLocalAddr() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLocalName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLocalPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration getLocales() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getParameter(String arg0) {
        String []ss = parameterMap.get(arg0);
        return ss == null || ss.length == 0? null: ss[0];
    }

    @Override
    public Map getParameterMap() {
        return parameterMap;
    }

    @Override
    public Enumeration getParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getParameterValues(String arg0) {
        return parameterMap.get(arg0);
    }

    @Override
    public String getProtocol() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRealPath(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemoteAddr() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemoteHost() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRemotePort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getScheme() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getServerName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getServerPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isSecure() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeAttribute(String arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setAttribute(String arg0, Object arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCharacterEncoding(String arg0)
            throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getAuthType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getContextPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getDateHeader(String arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getHeader(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration getHeaderNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration getHeaders(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getIntHeader(String arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getMethod() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPathInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPathTranslated() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getQueryString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemoteUser() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getServletPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpSession getSession() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpSession getSession(boolean arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Principal getUserPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUserInRole(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }
    
}

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
    
    @Test
    public void testCreate() {
        Map<String, String[]> map = new HashMap<String, String[]>();
        HttpServletRequest req = new ServletRequest(map);
        
        map.put("id", new String[] {"1234"});
        map.put("authorName", new String[] {"a"});
        map.put("authorAddress", new String[] {"b"});
        map.put("year", new String[]{"2008"});
        map.put("month", new String[]{"02"});
        map.put("mday", new String[]{"14"});
        map.put("tag", new String[] {"c"});
        map.put("body", new String[] {"d"});
        
        User user = null;
        Key key = null;
        Entry entry = Entry.create(req, user, new KeyFactory() {
            @Override
            Key createKey(String kind, long id) {
                return null;
            }
        });
        assertThat(entry, is(Entry.create(key, user, date(2008, 2, 14), "a", "b", "c", "d")));
    }

    void tryCreateEntry(Map<String, String[]> map) {
        User user = null;
        KeyFactory factory = new KeyFactory() {
            @Override
            Key createKey(String kind, long id) {
                return null;
            }
        };
        try {
            Entry.create(new ServletRequest(map), user, factory);
            assertTrue(false);
        } catch (InvalidParameterException e) {
            
        }
    }
    
    @Test
    public void testCreateFail() {
        Map<String, String[]> map_ = new HashMap<String, String[]>();
        map_.put("id", new String[] {"1234"});
        map_.put("authorName", new String[] {"a"});
        map_.put("authorAddress", new String[] {"b"});
        map_.put("year", new String[]{"2008"});
        map_.put("month", new String[]{"02"});
        map_.put("mday", new String[]{"14"});
        map_.put("tag", new String[] {"c"});
        map_.put("body", new String[] {"d"});
        
        {
            Map<String, String[]> map = new HashMap<String, String[]>(map_);
            map.remove("authorName");
            tryCreateEntry(map);
        }
        {
            Map<String, String[]> map = new HashMap<String, String[]>(map_);
            map.remove("authorAddress");
            tryCreateEntry(map);
        }
        {
            Map<String, String[]> map = new HashMap<String, String[]>(map_);
            map.remove("year");
            tryCreateEntry(map);
        }
        {
            Map<String, String[]> map = new HashMap<String, String[]>(map_);
            map.remove("month");
            tryCreateEntry(map);
        }
        {
            Map<String, String[]> map = new HashMap<String, String[]>(map_);
            map.remove("mday");
            tryCreateEntry(map);
        }
        {
            Map<String, String[]> map = new HashMap<String, String[]>(map_);
            map.remove("tag");
            tryCreateEntry(map);
        }
        {
            Map<String, String[]> map = new HashMap<String, String[]>(map_);
            map.remove("body");
            tryCreateEntry(map);
        }
    }
    @Test
    public void testCreateWithoutID() {
        Map<String, String[]> map = new HashMap<String, String[]>();
        HttpServletRequest req = new ServletRequest(map);
        
        map.put("authorName", new String[] {"a"});
        map.put("authorAddress", new String[] {"b"});
        map.put("year", new String[]{"2008"});
        map.put("month", new String[]{"02"});
        map.put("mday", new String[]{"14"});
        map.put("tag", new String[] {"c"});
        map.put("body", new String[] {"d"});
        
        User user = null;
        Key key = null;
        Entry entry = Entry.create(req, user, new KeyFactory() {
            @Override
            Key createKey(String kind, long id) {
                assertTrue(false);
                return null;
            }
        });
        assertThat(entry, is(Entry.create(key, user, date(2008, 2, 14), "a", "b", "c", "d")));
    }
}
