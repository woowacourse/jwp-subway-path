package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final LineService lineService;
    private final StationService stationService;

    public SectionService(final SectionDao sectionDao, final LineService lineService, final StationService stationService) {
        this.sectionDao = sectionDao;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public SectionResponse saveInitialSection(final SectionRequest sectionRequest) {
        return new SectionResponse(null, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
    }

    public SectionResponse saveSection(final SectionRequest sectionRequest) {
        return new SectionResponse(null, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
    }


    public void deleteSectionByStationId(final Long lineId, final Long stationId) {

    }
}
