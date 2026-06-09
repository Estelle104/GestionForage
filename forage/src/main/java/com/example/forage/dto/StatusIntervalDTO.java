package com.example.forage.dto;

public class StatusIntervalDTO {

    private String fromStatus;
    private String toStatus;
    private long durationMinutes;
    private String color;
    private boolean hasAlerte;
    private Integer niveauAlerte;
    
    public String getFromStatus() {
        return fromStatus;
    }
    public void setFromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
    }
    public String getToStatus() {
        return toStatus;
    }
    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
    }
    public long getDurationMinutes() {
        return durationMinutes;
    }
    public void setDurationMinutes(long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public boolean isHasAlerte() {
        return hasAlerte;
    }
    public void setHasAlerte(boolean hasAlerte) {
        this.hasAlerte = hasAlerte;
    }
    public Integer getNiveauAlerte() {
        return niveauAlerte;
    }
    public void setNiveauAlerte(Integer niveauAlerte) {
        this.niveauAlerte = niveauAlerte;
    }

    // getters/setters
}