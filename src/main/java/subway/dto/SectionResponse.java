package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.entity.SectionEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {

    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public static SectionResponse of(final SectionEntity sectionEntity) {
        return new SectionResponse(
                sectionEntity.getId(),
                sectionEntity.getLineId(),
                sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(),
                sectionEntity.getDistance()
        );
    }
}
