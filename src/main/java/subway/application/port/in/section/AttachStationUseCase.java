package subway.application.port.in.section;

import subway.adapter.in.web.section.dto.SectionCreateRequest;

public interface AttachStationUseCase {
    void createSection(final Long lineId, final SectionCreateRequest sectionCreateRequest);
}
