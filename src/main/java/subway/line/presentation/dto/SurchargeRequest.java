package subway.line.presentation.dto;

public class SurchargeRequest {
    private String surcharge;

    public SurchargeRequest() {
    }

    public SurchargeRequest(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getSurcharge() {
        return surcharge;
    }
}
