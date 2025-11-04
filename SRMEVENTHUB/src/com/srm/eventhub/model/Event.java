package com.srm.eventhub.model;

public class Event {
    private int id;
    private String name;
    private String description;
    private String date;
    private String time;
    private String venue;
    private String qrCodePath;
    private String organizerClub;

    // Constructor used when fetching event data
    public Event(int id, String name, String description, String date, String time, String venue, String qrCodePath, String organizerClub) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.qrCodePath = qrCodePath;
        this.organizerClub = organizerClub;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getVenue() {
        return venue;
    }
    public String getQrCodePath() {
        return qrCodePath;
    }
    public String getOrganizerClub() {
        return organizerClub;
    }

    @Override
    public String toString() {
        return name; // This is useful for displaying in JComboBox or JList
    }
}