package subway.dto;

import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionDto {

    private final Long lineId;
    private final Long upperStation;
    private final Long lowerStation;
    private final Integer distance;

    public SectionDto(final Long lineId, final Long upperStation, final Long lowerStation, final Integer distance) {
        this.lineId = lineId;
        this.upperStation = upperStation;
        this.lowerStation = lowerStation;
        this.distance = distance;
    }

    public static List<SectionDto> makeList(final Long lineId, List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionDto(
                        lineId,
                        section.getUpper().getId(),
                        section.getLower().getId(),
                        section.getDistance().getValue()))
                .collect(Collectors.toList());
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpperStation() {
        return upperStation;
    }

    public Long getLowerStation() {
        return lowerStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
