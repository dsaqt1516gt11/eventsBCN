package edu.upc.eetac.dsa.eventsBCN.entity;

/**
 * Created by Aitor on 29/11/15.
 */
public class EventBCNError {
    private int status;
    private String reason;
    public EventBCNError() {
    }

    public EventBCNError(int status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
