package subway.station.ui.dto.in;

import javax.validation.constraints.NotBlank;

public class StationUpdateInfoRequest {

    @NotBlank
    private String name;

    private StationUpdateInfoRequest() {
    }

    public StationUpdateInfoRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
