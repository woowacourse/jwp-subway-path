package subway.adapter.in.web.station.dto;

import org.springframework.lang.NonNull;


public class StationCreateRequest {

    @NonNull
    private String name;

    public StationCreateRequest() {
    }

    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
