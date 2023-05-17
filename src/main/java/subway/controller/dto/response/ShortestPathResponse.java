package subway.controller.dto.response;

import java.util.ArrayList;
import java.util.List;
import subway.domain.section.PathSection;

public class ShortestPathResponse {

    private int transferCount;
    private List<LineSectionResponse> path;
    private long totalDistance;
    private long subwayFare;

    public ShortestPathResponse(
            final int transferCount,
            final List<LineSectionResponse> path,
            final long totalDistance,
            final long subwayFare
    ) {
        this.transferCount = transferCount;
        this.path = path;
        this.totalDistance = totalDistance;
        this.subwayFare = subwayFare;
    }

    public static ShortestPathResponse of(
            final List<PathSection> sections,
            final long totalDistance,
            final long subwayFare
    ) {
        final List<LineSectionResponse> path = generateLineSections(sections);
        return new ShortestPathResponse(path.size() - 1, path, totalDistance, subwayFare);
    }

    private static List<LineSectionResponse> generateLineSections(final List<PathSection> sections) {
        final List<LineSectionResponse> result = new ArrayList<>();
        final Long currentLineId = sections.get(0).getLineId();

        List<PathSection> currentSections = new ArrayList<>();
        for (final PathSection section : sections) {
            if (section.getLineId() != currentLineId) {
                result.add(LineSectionResponse.from(currentSections));
                currentSections = new ArrayList<>();
            }
            currentSections.add(section);
        }

        if (!currentSections.isEmpty()) {
            result.add(LineSectionResponse.from(currentSections));
        }
        return result;
    }

    public int getTransferCount() {
        return transferCount;
    }

    public List<LineSectionResponse> getPath() {
        return path;
    }

    public long getTotalDistance() {
        return totalDistance;
    }

    public long getSubwayFare() {
        return subwayFare;
    }
}
