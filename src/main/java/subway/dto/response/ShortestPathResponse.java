package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Path;

public class ShortestPathResponse {

    private List<SectionQueryResponse> sectionQueryResponses;
    private int totalDistance;
    private int fee;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(final List<SectionQueryResponse> sectionQueryResponses, int totalDistance, int fee) {
        this.sectionQueryResponses = sectionQueryResponses;
        this.totalDistance = totalDistance;
        this.fee = fee;
    }

    public static ShortestPathResponse of(final Path path, final int fee) {
        final List<SectionQueryResponse> sectionQueryResponses = path.getSections().stream()
                .map(SectionQueryResponse::from)
                .collect(Collectors.toList());
        return new ShortestPathResponse(sectionQueryResponses, path.getTotalDistance(), fee);
    }

    public List<SectionQueryResponse> getSectionQueryResponses() {
        return sectionQueryResponses;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getFee() {
        return fee;
    }
}
