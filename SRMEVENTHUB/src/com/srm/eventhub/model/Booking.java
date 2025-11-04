package com.srm.eventhub.model;

public class Booking {
    private int bookingId;
    private int eventId;
    private int userId;
    private String userFullName;
    private String userRegNo;
    private String status;

    public Booking(int bookingId, int eventId, int userId, String userFullName, String userRegNo, String status) {
        this.bookingId = bookingId;
        this.eventId = eventId;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userRegNo = userRegNo;
        this.status = status;
    }

    // Getters
    public int getBookingId() { return bookingId; }
    public int getEventId() { return eventId; }
    public int getUserId() { return userId; }
    public String getUserFullName() { return userFullName; }
    public String getUserRegNo() { return userRegNo; }
    public String getStatus() { return status; }
}