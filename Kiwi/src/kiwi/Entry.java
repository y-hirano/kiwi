package kiwi;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

/**
 * An Entry holds properties of a Kiwi entry.
 * This class is a JPA Entity class.
 * @author Yutaka Hirano
 */
@Entity
public class Entry {
    public static Entry create(User user, Date date, String authorName, String authorAddress, String tag, String body) {
        return new Entry(user, date, authorName, authorAddress, tag, body);
    }
    
    private Entry(User user, Date date, String name, String address, String tag, String body) {
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
}
