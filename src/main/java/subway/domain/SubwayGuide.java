package subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubwayGuide {

    private final Map<Section, Line> sectionLines;
    private final Path path;

    private SubwayGuide(final Map<Section, Line> sectionLines, final Path path) {
        this.sectionLines = sectionLines;
        this.path = path;
    }

    public static SubwayGuide from(final List<Line> lines) {
        Map<Section, Line> sectionLines = new HashMap<>();
        for (Line line : lines) {
            for (Section section : line.getSectionsExceptEmpty()) {
                sectionLines.put(section, line);
            }
        }
        return new SubwayGuide(sectionLines, Path.from(lines));
    }

    public List<Station> findPath(final Station from, final Station to) {
        return path.getShortestPathStations(from, to);
    }

    public int calculateFare(final Station from, final Station to, final AgeGroup ageGroup) {
        int maxExtraFare = path.getShortestPathSections(from, to)
                .stream()
                .mapToInt(section -> sectionLines.get(section).getExtraFare())
                .max()
                .orElseThrow(() -> new IllegalStateException("[ERROR] 노선별 추가요금의 최대값을 찾을 수 없습니다."));
        return Fare.from(path.getShortestPathDistance(from, to))
                .applyExtraFare(maxExtraFare)
                .applyDiscountRateOfAge(ageGroup)
                .getFare();
    }
}
