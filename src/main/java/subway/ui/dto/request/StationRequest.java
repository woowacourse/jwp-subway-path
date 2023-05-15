package subway.ui.dto.request;

import org.springframework.lang.NonNull;

public class StationRequest {

    @NonNull
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
