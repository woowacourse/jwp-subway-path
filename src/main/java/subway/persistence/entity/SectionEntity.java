package subway.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class SectionEntity {
    private final Long id;
    private final Long lineId;
    private final String upStationName;
    private final String downStationName;
    private final Long distance;

    public SectionEntity(final Long lineId, final String upStationName, final String downStationName, final Long distance) {
        this(null, lineId, upStationName, downStationName, distance);
    }
    public static List<SectionEntity> of(final Long lineId, final List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionEntity(
                        lineId,
                        section.getUpStation().getName(),
                        section.getDownStation().getName(),
                        section.getDistance()
                ))
                .collect(Collectors.toList());
    }
}
