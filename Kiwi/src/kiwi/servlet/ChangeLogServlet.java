package kiwi.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import kiwi.ChangeLogReader;
import kiwi.Entry;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
@SuppressWarnings("serial")
public class ChangeLogServlet extends HttpServlet {
    private void outputError(HttpServletResponse resp, ServletResponse.Status status, Object errorBody) throws IOException {
        resp.setStatus(500);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServletResponse.Status.class, ServletResponse.Status.gsonAdapter);
        resp.getWriter().println(builder.create().toJson(new ServletResponse(status, errorBody)));
    }
    private void outputError(HttpServletResponse resp, ServletResponse.Status status) throws IOException {
        outputError(resp, status, null);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = null;
        User user = null;
        System.out.println(new Gson().toJson(req.getParameterMap()));
        try {
            Account.Type accountType = Account.get(req.getParameter("account"));
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
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator it = upload.getItemIterator(req);
            ChangeLogReader reader = new ChangeLogReader(user);
            while (it.hasNext()) {
                FileItemStream stream = it.next();
                if (stream.getFieldName().equals("ChangeLog")) {
                    InputStream is = null;
                    InputStreamReader isr = null;
                    try {
                        is = stream.openStream();
                        isr = new InputStreamReader(stream.openStream(), Charset.forName("utf-8"));
                        reader.read(isr);
                    } finally {
                        if (isr != null) {
                            isr.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }
            em = ServiceFactory.getEntityManager().createEntityManager();
            System.out.println("reader.getEntries().size() = " + reader.getEntries().size());
            EntityTransaction transaction = em.getTransaction();
            for (Entry e: reader.getEntries()) {
                transaction.begin();
                em.persist(e);
                transaction.commit();
            }
            resp.setContentType("text/plain");
            resp.getWriter().println("{\"result\": \"done\"}");
        } catch (FileUploadException e) {
            e.printStackTrace();
            resp.setContentType("text/plain");
            resp.setStatus(503);
            resp.getWriter().println("{\"result\": \"error\", \"cause\": \"UploadError\"}");
        } catch (ParseException e) {
            e.printStackTrace();
            resp.setContentType("text/plain");
            Map<String, Object> responseData = new HashMap<String, Object>();
            responseData.put("result", "error");
            responseData.put("cause", "ParseError");
            responseData.put("lineNumber", e.getErrorOffset());
            responseData.put("message", e.getMessage());
            resp.getWriter().println(new Gson().toJson(responseData));
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
