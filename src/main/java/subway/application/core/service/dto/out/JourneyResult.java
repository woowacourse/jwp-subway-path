package subway.application.core.service.dto.out;

import subway.application.core.domain.Station;

import java.util.List;

public class JourneyResult {

    private final List<Station> path;
    private final Integer fee;

    public JourneyResult(List<Station> path, Integer fee) {
        this.path = path;
        this.fee = fee;
    }

    public List<Station> getPath() {
        return path;
    }

    public Integer getFee() {
        return fee;
    }
}
