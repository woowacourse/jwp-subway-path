package subway.adapter.in.web.section.dto;

import org.springframework.lang.NonNull;

import javax.validation.constraints.Positive;

public class SectionCreateRequest {
    @NonNull
    private String upStationName;
    @NonNull
    private String downStationName;
    @Positive
    private Long distance;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(final String upStationName, final String downStationName, final Long distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Long getDistance() {
        return distance;
    }
}
