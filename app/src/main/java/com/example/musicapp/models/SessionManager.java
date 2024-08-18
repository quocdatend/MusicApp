package com.example.musicapp.models;

public class SessionManager {
    private static SessionManager instance;
    private Session session;

    private SessionManager() {
        // Khởi tạo đối tượng SessionManager nếu chưa có
        if (session == null) {
            session = new Session();
        }
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void clearSession() {
        session = null;
    }
}
