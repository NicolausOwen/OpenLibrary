package com.kelompok5.openlibrary.data.model;

import com.google.gson.annotations.SerializedName;

public class Availability {

    @SerializedName("status")
    private String status;

    @SerializedName("available_to_borrow")
    private boolean availableToBorrow;

    public String getStatus() { return status; }
    public boolean isAvailableToBorrow() { return availableToBorrow; }

    public void setStatus(String status) { this.status = status; }
    public void setAvailableToBorrow(boolean availableToBorrow) {
        this.availableToBorrow = availableToBorrow;
    }
}
