package com.spring.otmanagement.dto;

import com.spring.otmanagement.entity.OtStatus;

public class OtStatusUpdate {
    private String managerNote;
    private OtStatus otStatus;

    public OtStatusUpdate () {}

    public OtStatus getOtStatus() {
        return otStatus;
    }

    public void setOtStatus(OtStatus otStatus) {
        this.otStatus = otStatus;
    }

    public String getManagerNote() {
        return managerNote;
    }

    public void setManagerNote(String managerNote) {
        this.managerNote = managerNote;
    }
}
