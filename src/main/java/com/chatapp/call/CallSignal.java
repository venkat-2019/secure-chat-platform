package com.chatapp.call;

public class CallSignal {

    private Long callerId;
    private Long receiverId;
    private String type; // OFFER, ANSWER, ICE

    public Long getCallerId() { return callerId; }
    public void setCallerId(Long callerId) { this.callerId = callerId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
