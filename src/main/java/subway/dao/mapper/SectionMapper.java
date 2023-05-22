package subway.dao.mapper;

import subway.dao.entity.SectionEntity;
import subway.domain.section.dto.SectionSaveReq;

public class SectionMapper {

    public static SectionEntity convertSectionEntity(final SectionSaveReq sectionSaveReq) {
        return new SectionEntity(sectionSaveReq.getLineId(),
            sectionSaveReq.getSourceStationId(),
            sectionSaveReq.getTargetStationId(), sectionSaveReq.getDistance());
    }
}
