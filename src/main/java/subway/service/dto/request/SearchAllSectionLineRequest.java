package subway.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchAllSectionLineRequest {

    private final String lineName;

    public SearchAllSectionLineRequest(@JsonProperty(value = "lineName") String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }
}
