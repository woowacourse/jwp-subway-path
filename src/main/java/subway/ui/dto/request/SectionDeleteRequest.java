package subway.ui.dto.request;

import org.springframework.lang.NonNull;

public class SectionDeleteRequest {

    @NonNull
    private String stationName;

    public SectionDeleteRequest() {
    }

    public SectionDeleteRequest(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
