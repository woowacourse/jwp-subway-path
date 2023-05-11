package subway.application.sectionReader;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

import java.util.List;

public class UpperCase extends SectionReader {
    public UpperCase(AddStationRequest addStationRequest, SectionDao sectionDao) {
        super(addStationRequest, sectionDao);
    }

    @Override
    public List<AddStationResponse> read(long id, List<Section> sections) throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public List<AddStationResponse> addSection(long id, List<Section> departureSections, List<Section> arrivalSections) throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public List<AddStationResponse> addSectionWithDirection(long id, List<Section> departureSections, List<Section> arrivalSections, Section section) throws IllegalAccessException {
        final int upLineDistance = addStationRequest.getDistance();
        final String upLineDeparture = addStationRequest.getDepartureStation();
        final String upLineArrival = addStationRequest.getArrivalStation();
        final long upLineSectionId = sectionDao.saveSection(id, upLineDistance, upLineDeparture, upLineArrival);
        final int downLineDistance = section.getDistance().getDistance();
        final String downLineDeparture = addStationRequest.getArrivalStation();
        final String downLineArrival = section.getArrival().getName();
        final long downLineSectionId = sectionDao.saveSection(id, downLineDistance, downLineDeparture,
                downLineArrival);
        sectionDao.deleteSection(section.getId());

        return List.of(new AddStationResponse(upLineSectionId, upLineDeparture, upLineArrival, upLineDistance),
                new AddStationResponse(downLineSectionId, downLineDeparture, downLineArrival, downLineDistance));
    }
}
