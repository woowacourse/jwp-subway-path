package subway.repository;

import java.util.List;
import subway.domain.Section;

public interface SectionRepository {

    Section insert(final Section section);

    List<Section> findAllByLineId(final Long lineId);

    List<Section> findSectionByLineIdAndStationId(final Long lineId, final Long stationId);

    int countByLineId(final Long lineId);

    void update(Section section);

    void deleteById(Long id);

    void deleteAllByLineId(final Long lineId);

    void deleteByLineIdAndStationId(final Long lineId, final Long stationId);
}
