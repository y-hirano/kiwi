package kiwi.servlet;

public class Account {
    public enum Type {
        ACCOUNT,
        GUEST
    }
    public static Type get(String name) {
        if ("account".equals(name)) {
            return Type.ACCOUNT;
        }
        if ("guest".equals(name)) {
            return Type.GUEST;
        }
        throw new AssertionError(name + " is not a valid Account.Type name.");
    }
}
