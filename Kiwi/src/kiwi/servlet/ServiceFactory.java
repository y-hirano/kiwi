package kiwi.servlet;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.appengine.api.users.UserServiceFactory;

public class ServiceFactory {
    private ServiceFactory() {}
    private static final EntityManagerFactory emfInstance =
            Persistence.createEntityManagerFactory("transactions-optional");
    public static EntityManagerFactory getEntityManager() {
        return emfInstance;
    }
}
