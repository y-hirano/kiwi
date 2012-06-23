package kiwi.servlet;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.users.User;

public class EntriesServletTest {
    private EntriesServlet servlet;
    private User user;
    @SuppressWarnings("serial")
    @Before
    public void setup() {
        servlet = new EntriesServlet() {
            User getCurrentUser() {
                return user;
            }
        };
        user = null;
    }
}
