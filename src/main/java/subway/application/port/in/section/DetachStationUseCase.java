package subway.application.port.in.section;

import subway.adapter.in.web.section.dto.SectionDeleteRequest;

public interface DetachStationUseCase {
    void deleteStation(final Long lineId, final SectionDeleteRequest sectionDeleteRequest);
}
