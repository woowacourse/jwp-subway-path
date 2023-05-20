package subway.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import subway.domain.section.PathSection;

@Schema(
        description = "최단 경로 응답 정보",
        example = "{\"transferCount\": 3, \"path\": [{\"lineId\": 1, \"sections\": [{\"upwardStationName\": \"잠실역\", \"downwardStationName\": \"잠실새내역\", \"distance\": 5}]}], \"totalDistance\": 10, \"subwayFare\": 2000}"
)
public class ShortestPathResponse {

    @Schema(description = "환승 횟수")
    private int transferCount;

    @Schema(description = "구간 경로 목록")
    private List<LineSectionResponse> path;

    @Schema(description = "출발역에서 도착역까지의 총 거리")
    private long totalDistance;

    @Schema(description = "출발역에서 도착역까지 운임 요금")
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
        Long currentLineId = sections.get(0).getLineId();
        List<PathSection> currentSections = new ArrayList<>();

        for (final PathSection section : sections) {
            if (section.getLineId() != currentLineId) {
                result.add(LineSectionResponse.from(currentSections));
                currentSections = new ArrayList<>();
                currentLineId = section.getLineId();
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
