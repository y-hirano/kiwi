package kiwi.servlet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.*;

import kiwi.Entry;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class InvalidUserException extends GeneralSecurityException {
    InvalidUserException(String message) {
        super(message);
    }
    InvalidUserException() {
        super();
    }
}
@SuppressWarnings("serial")
public class EntriesServlet extends HttpServlet {
    private void outputError(HttpServletResponse resp, ServletResponse.Status status, Object errorBody) throws IOException {
        resp.setStatus(500);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServletResponse.Status.class, ServletResponse.Status.gsonAdapter);
        resp.getWriter().println(builder.create().toJson(new ServletResponse(status, errorBody)));
    }
    private void outputError(HttpServletResponse resp, ServletResponse.Status status) throws IOException {
        outputError(resp, status, null);
    }

    public static final int NUM_MAX_QUERY_RESULTS = 50;
    
    static User selectUser(HttpServletRequest req) throws InvalidParameterException, InvalidUserException {
        User user = null;
        Account.Type accountType = Account.get((String)req.getParameter("account"));
        switch (accountType) {
        case ACCOUNT:
            user = UserServiceFactory.getUserService().getCurrentUser();
            if (user == null) {
                throw new InvalidUserException();
            }
            break;
        case GUEST:
            user = null;
            break;
        default:
            throw new InvalidParameterException();
        }
        return user;
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("utf-8");
        EntityManager em = null;
        
        try {
            User user = selectUser(req);
            em = ServiceFactory.getEntityManager().createEntityManager();
            Query q = em.createQuery("select from :kind where user = :user");
            q.setParameter(":kind", Entry.KIND);
            q.setParameter(":user", user);
            q.setMaxResults(NUM_MAX_QUERY_RESULTS);
            List<JSONEntry> es = new ArrayList<JSONEntry>();
            for (Object e : q.getResultList()) {
                es.add(new JSONEntry((Entry) e));
            }
            resp.getWriter().println(new Gson().toJson(es));
        } catch (InvalidUserException e) {
            e.printStackTrace();
            outputError(resp, ServletResponse.Status.LOGIN_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            outputError(resp, ServletResponse.Status.UNKNOWN_ERROR);
        } catch (AssertionError e) {
            e.printStackTrace();
            outputError(resp, ServletResponse.Status.UNKNOWN_ERROR);
        } finally {
            if (em != null) {
                em.close();
            }
        }
     }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = null;
        
        try {
            em = ServiceFactory.getEntityManager().createEntityManager();
            User user = selectUser(req);
            em.persist(Entry.create(req, user));
        } catch (InvalidUserException e) {
            e.printStackTrace();
            outputError(resp, ServletResponse.Status.LOGIN_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            outputError(resp, ServletResponse.Status.UNKNOWN_ERROR);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("DELETE");
        EntityManager em = null;
        System.out.println(new Gson().toJson(req.getParameterMap()));
        System.out.println(req.getAttribute("account") + ", " + req.getParameter("account"));
        try {
            User user = selectUser(req);
            em = ServiceFactory.getEntityManager().createEntityManager();
            if ("true".equals(req.getParameter("deleteAll"))) {
                Query q = em.createQuery("delete from Entry where user = :user");
                q.setParameter("user", user);
                while (q.executeUpdate() > 0) {
                    // do nothing
                }
                resp.getWriter().println("{\"status\": \"ok\", \"body\": null}");
            } else {
                outputError(resp, ServletResponse.Status.UNKNOWN_ERROR);
            }
        } catch (InvalidUserException e) {
            e.printStackTrace();
            outputError(resp, ServletResponse.Status.LOGIN_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            outputError(resp, ServletResponse.Status.UNKNOWN_ERROR);
        } catch (AssertionError e) {
            e.printStackTrace();
            outputError(resp, ServletResponse.Status.UNKNOWN_ERROR);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
