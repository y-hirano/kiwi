package kiwi;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;


class KeyFactory {
    Key createKey(String kind, long id) {
        return com.google.appengine.api.datastore.KeyFactory.createKey(kind, id);
    }
}

/**
 * An Entry holds properties of a Kiwi entry.
 * This class is a JPA Entity class.
 * @author Yutaka Hirano
 */
@Entity
public class Entry {
    public static Entry create(User user, Date date, String authorName, String authorAddress, String tag, String body) {
        return new Entry(null, user, date, authorName, authorAddress, tag, body);
    }
    
    public static Entry create(Key key, User user, Date date, String authorName, String authorAddress, String tag, String body) {
        return new Entry(key, user, date, authorName, authorAddress, tag, body);
    }
    public static Entry create(Long id, User user, Date date, String authorName, String authorAddress, String tag, String body) {
        Key key = id == null? null: com.google.appengine.api.datastore.KeyFactory.createKey(KIND, id);
        return new Entry(key, user, date, authorName, authorAddress, tag, body);
    }
    
    
    public static Entry create(HttpServletRequest req, User user, KeyFactory factory) throws InvalidParameterException {
        String syear = req.getParameter("year");
        String smonth = req.getParameter("month");
        String smday = req.getParameter("mday");
        String authorName = req.getParameter("authorName");
        String authorAddress = req.getParameter("authorAddress");
        String tag = req.getParameter("tag");
        String body = req.getParameter("body");
        
        if (syear == null || smonth == null || smday == null || authorName == null || authorAddress == null ||
                tag == null || body == null) {
            throw new InvalidParameterException();
        }
        
        int year = Integer.parseInt(syear);
        int month = Integer.parseInt(smonth);
        int mday = Integer.parseInt(smday);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        c.set(year, month, mday, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        String id = req.getParameter("id");
        return new Entry(id != null? factory.createKey(KIND, Long.parseLong(id)): null, user, c.getTime(), authorName, authorAddress, tag, body);
    }
    
    public static Entry create(HttpServletRequest req, User user) {
        return create(req, user, new KeyFactory());
    }
    
    private Entry(Key key, User user, Date date, String name, String address, String tag, String body) {
        this.key = key;
        this.user = user;
        this.date = date;
        this.authorName = name;
        this.authorAddress = address;
        this.tag = tag;
        
        setBody(body);
    }
    
    public Key getKey() {
        return key;
    }
    
    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorAddress() {
        return authorAddress;
    }

    public String getTag() {
        return tag;
    }
    public String getBody() {
        return longBody != null? longBody.getValue(): body;
    }
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorAddress(String authorAddress) {
        this.authorAddress = authorAddress;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setBody(String body) {
        this.body = null;
        this.longBody = null;
        if (body.length() <= STRING_SIZE_MAX) {
            this.body = body;
        } else {
            this.longBody = new Text(body);
        }
    }
    
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Entry)) {
            return false;
        }
        Entry e = (Entry)o;
        return  ((key == null && e.key == null) || (key != null && key.equals(e.key))) &&
                ((user == null && e.user == null) || (user != null && user.equals(e.user))) &&
                date.equals(e.date) && 
                authorName.equals(e.authorName) && authorAddress.equals(e.authorAddress) &&
                tag.equals(e.tag) && body.equals(e.body);
    }
    public String toString() {
        return String.format("%s %s <%s>\n %s: %s", date, authorName, authorAddress, tag, body);
    }
    final static public int STRING_SIZE_MAX = 500;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Key key;
    private User user;
    private Date date;
    private String authorName;
    private String authorAddress;
    private String tag; 
    private String body;
    private Text longBody;

    public static String KIND = "Entry";
}
