package org.pixle.util;

public enum Side {
    CLIENT,
    SERVER;

    public static Side get() {
        return Thread.currentThread().getName().equals("Server") ? SERVER : CLIENT;
    }

    public boolean isClient() {
        return this == CLIENT;
    }

    public boolean isServer() {
        return !isClient();
    }
}
