package com.lester.carrentalsystem.Model;

public class ClientSession {
    private static ClientSession instance;

    private int clientId;
    private String fullName;
    private String username;
    private String contactNumber;

    private ClientSession() {}

    public static ClientSession getInstance() {
        if (instance == null) {
            instance = new ClientSession();
        }
        return instance;
    }

    public void setClientData(int clientId, String fullName, String username, String contactNumber) {
        this.clientId = clientId;
        this.fullName = fullName;
        this.username = username;
        this.contactNumber = contactNumber;
    }

    public int getClientId() {
        return clientId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void clearSession() {
        clientId = 0;
        fullName = null;
        username = null;
        contactNumber = null;
    }
}
