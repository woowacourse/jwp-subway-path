package subway.application.sectionReader;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.domain.SectionSorter;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

import java.util.List;

public class TerminalCase extends SectionReader{
    public TerminalCase(AddStationRequest addStationRequest, SectionDao sectionDao) {
        super(addStationRequest, sectionDao);
    }

    @Override
    public List<AddStationResponse> read(long id, List<Section> allSections) throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public List<AddStationResponse> addSection(long id, List<Section> departureSections, List<Section> arrivalSections) throws IllegalAccessException {
        final long sectionId = sectionDao.saveSection(id, addStationRequest.getDistance(),
                addStationRequest.getDepartureStation(),
                addStationRequest.getArrivalStation());
        return List.of(new AddStationResponse(sectionId, addStationRequest.getDepartureStation(),
                addStationRequest.getArrivalStation(), addStationRequest.getDistance()));
    }

    @Override
    public List<AddStationResponse> addSectionWithDirection(long id, List<Section> departureSections, List<Section> arrivalSections, Section section) throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
