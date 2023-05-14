package subway.application.sectionreader;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

import java.util.List;

public class FirstSaveCase extends SectionReader {
    public FirstSaveCase(AddStationRequest addStationRequest, SectionDao sectionDao) {
        super(addStationRequest, sectionDao);
    }


    @Override
    public List<AddStationResponse> read(long id, List<Section> allSections) throws IllegalAccessException {
        if (allSections.size() == 0) {
            final long sectionId = sectionDao.saveSection(id, addStationRequest.getDistance(),
                    addStationRequest.getDepartureStation(),
                    addStationRequest.getArrivalStation());
            return List.of(new AddStationResponse(sectionId, addStationRequest.getDepartureStation(),
                    addStationRequest.getArrivalStation(), addStationRequest.getDistance()));
        }

        return new ValidateCase(addStationRequest, sectionDao).read(id, allSections);
    }

    @Override
    public List<AddStationResponse> addSection(long id, List<Section> departureSections, List<Section> arrivalSections) throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public List<AddStationResponse> addSectionWithDirection(long id, List<Section> departureSections, List<Section> arrivalSections, Section section) throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
