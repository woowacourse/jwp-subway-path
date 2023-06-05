package subway.dto;

import javax.validation.constraints.NotBlank;

public class StationCreateReqeust {

    @NotBlank
    private String name;

    public StationCreateReqeust(String name) {
        this.name = name;
    }

    public StationCreateReqeust() {

    }

    public String getName() {
        return name;
    }
}
