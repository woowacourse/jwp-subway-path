package subway.application.sectionReader;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

import java.util.List;
import java.util.Optional;

public class SelectDirection extends SectionReader {
    public SelectDirection(AddStationRequest addStationRequest, SectionDao sectionDao) {
        super(addStationRequest, sectionDao);
    }

    @Override
    public List<AddStationResponse> read(long id, List<Section> sections) throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public List<AddStationResponse> addSection(long id, List<Section> departureSections, List<Section> arrivalSections) throws IllegalAccessException {
        Optional<Section> optionalDeparture = arrivalSections.stream()
                .filter(section -> section.getArrival().getName().equals(addStationRequest.getArrivalStation()))
                .findFirst();

        Optional<Section> optionalArrival = departureSections.stream()
                .filter(section -> section.getDeparture().getName().equals(addStationRequest.getDepartureStation()))
                .findFirst();

        Section section = optionalDeparture.orElseGet(optionalArrival::get);
        if (section.getDistance().getDistance() <= addStationRequest.getDistance()) {
            throw new IllegalArgumentException("기존의 거리보다 작아야 합니다.");
        }

        if (findConnectedStation(addStationRequest, section).equals(addStationRequest.getDepartureStation())) {
            return new UpperCase(addStationRequest, sectionDao).addSectionWithDirection(id, departureSections, arrivalSections, section);
        }
        return new LowerCase(addStationRequest, sectionDao).addSectionWithDirection(id, departureSections, arrivalSections, section);
    }

    @Override
    public List<AddStationResponse> addSectionWithDirection(long id, List<Section> departureSections, List<Section> arrivalSections, Section section) throws IllegalAccessException {
        return null;
    }

    private String findConnectedStation(final AddStationRequest addStationRequest, final Section section) {
        if (section.getArrival().getName().equals(addStationRequest.getArrivalStation())) {
            return section.getArrival().getName();
        }
        return section.getDeparture().getName();
    }
}
