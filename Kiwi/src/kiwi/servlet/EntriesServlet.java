package kiwi.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.*;

import kiwi.Entry;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("utf-8");
        User user = null;
        EntityManager em = null;
        
        try {
            Account.Type accountType = Account.get((String)req.getParameter("account"));
            switch (accountType) {
            case ACCOUNT:
                user = UserServiceFactory.getUserService().getCurrentUser();
                if (user == null) {
                    // not logged in
                    outputError(resp, ServletResponse.Status.LOGIN_ERROR);
                    return;
                }
                break;
            case GUEST:
                user = null;
                break;
            }
            em = ServiceFactory.getEntityManager().createEntityManager();
            Query q = em.createQuery("select from Entry where user = :user");
            q.setParameter(":user", user);
            q.setMaxResults(NUM_MAX_QUERY_RESULTS);
            List<JSONEntry> es = new ArrayList<JSONEntry>();
            for (Object e : q.getResultList()) {
                es.add(new JSONEntry((Entry) e));
            }
            resp.getWriter().println(new Gson().toJson(es));
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
            em =ServiceFactory.getEntityManager().createEntityManager();
            Date date = new Date(Long.parseLong(req.getParameter("date")));
            String authorName = "Yutaka Hirano";
            String authorAddress = "yuta@luna";
            String tag = req.getParameter("tag");
            Text text = new Text(req.getParameter("body"));
            em.persist(Entry.create(null, date, authorName, authorAddress, tag, text.getValue()));
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
            Account.Type accountType = Account.get((String)req.getParameter("account"));
            User user = null;
            switch (accountType) {
            case ACCOUNT:
                user = UserServiceFactory.getUserService().getCurrentUser();
                if (user == null) {
                    // not logged in
                    outputError(resp, ServletResponse.Status.LOGIN_ERROR);
                    return;
                }
                break;
            case GUEST:
                user = null;
                break;
            }
            em =ServiceFactory.getEntityManager().createEntityManager();
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
