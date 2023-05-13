package subway.domain.section;

import subway.domain.section.dto.SectionSaveReq;

public interface SectionRepository {

    Long insert(final SectionSaveReq sectionSaveReq);

    void deleteByLineIdAndStationId(final Long lineId, Long stationId);

    void deleteOldSection(final Long lineId, final Long sourceStationId);
}
