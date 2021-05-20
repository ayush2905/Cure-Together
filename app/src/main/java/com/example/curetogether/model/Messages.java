package com.example.curetogether.model;

public class Messages {
    private String SENDER;
    private String MESSAGE;
    private Long TYPE; // 0 --> TEXT 1 --> MEDIA
    private Long TIME; // in millis

    public Messages(String SENDER, String MESSAGE, Long TYPE, Long TIME) {
        this.SENDER = SENDER;
        this.MESSAGE = MESSAGE;
        this.TYPE = TYPE;
        this.TIME = TIME;
    }

    public Long getTIME() {
        return TIME;
    }

    public void setTIME(Long TIME) {
        this.TIME = TIME;
    }

    public String getSENDER() {
        return SENDER;
    }

    public void setSENDER(String SENDER) {
        this.SENDER = SENDER;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public Long getTYPE() {
        return TYPE;
    }

    public void setTYPE(Long TYPE) {
        this.TYPE = TYPE;
    }
}
