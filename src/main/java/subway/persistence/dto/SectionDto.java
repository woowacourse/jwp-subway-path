package subway.persistence.dto;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Section;

public class SectionDto {

    private Long lineId;
    private Long stationId;
    private Long nextStationId;
    private Integer distance;

    public SectionDto(Long lineId, Long stationId, Long nextStationId, Integer distance) {
        this.lineId = lineId;
        this.stationId = stationId;
        this.nextStationId = nextStationId;
        this.distance = distance;
    }

    public static SectionDto of(Long lineId, Section section) {
        return new SectionDto(
                lineId,
                section.getUpperStation().getId(),
                section.getLowerStation().getId(),
                section.getDistance().getValue()
        );
    }

    public static List<SectionDto> of(Long lineId, List<Section> sections) {
        return sections.stream()
                .map(section -> SectionDto.of(lineId, section))
                .collect(Collectors.toList());
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
