package subway.application.sectionReader;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

import java.util.List;

public class LowerCase extends SectionReader {
    public LowerCase(AddStationRequest addStationRequest, SectionDao sectionDao) {
        super(addStationRequest, sectionDao);
    }

    @Override
    public List<AddStationResponse> read(long id, List<Section> sections) throws IllegalAccessException {
        return null;
    }

    @Override
    public List<AddStationResponse> addSection(long id, List<Section> departureSections, List<Section> arrivalSections) throws IllegalAccessException {
        return null;
    }

    @Override
    public List<AddStationResponse> addSectionWithDirection(long id, List<Section> departureSections, List<Section> arrivalSections, Section section) throws IllegalAccessException {
        final int upLineDistance = section.getDistance().getDistance();
        final String upLineDeparture = section.getDeparture().getName();
        final String upLineArrival = addStationRequest.getDepartureStation();
        final long upLineSectionId = sectionDao.saveSection(id, upLineDistance, upLineDeparture, upLineArrival);

        final int downLineDistance = addStationRequest.getDistance();
        final String downLineDeparture = addStationRequest.getDepartureStation();
        final String downLineArrival = addStationRequest.getArrivalStation();
        final long downLineSectionId = sectionDao.saveSection(id, downLineDistance, downLineDeparture,
                downLineArrival);
        sectionDao.deleteSection(section.getId());

        return List.of(new AddStationResponse(upLineSectionId, upLineDeparture, upLineArrival, upLineDistance),
                new AddStationResponse(downLineSectionId, downLineDeparture, downLineArrival, downLineDistance));

    }
}
