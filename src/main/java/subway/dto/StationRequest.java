package subway.dto;

import org.jetbrains.annotations.NotNull;

public class StationRequest {

    @NotNull("이름은 비어있을 수 없습니다.")
    private String name;

    public StationRequest() {
    }

    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
