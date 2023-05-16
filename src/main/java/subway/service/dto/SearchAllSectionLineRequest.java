package subway.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchAllSectionLineRequest {

    private final String lineName;

    @JsonCreator
    public SearchAllSectionLineRequest(@JsonProperty(value = "lineName") String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }
}
