package subway.application.sectionReader;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.domain.SectionSorter;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ValidateCase extends SectionReader {
    public ValidateCase(AddStationRequest addStationRequest, SectionDao sectionDao) {
        super(addStationRequest, sectionDao);
    }

    @Override
    public List<AddStationResponse> read(long id, List<Section> allSections) throws IllegalAccessException {
        final SectionSorter sectionSorter = SectionSorter.getInstance();
        final List<Section> sortedSections = sectionSorter.sortSections(allSections);


        List<Section> departureSections = allSections.stream()
                .filter(section -> hasSection(section,addStationRequest.getDepartureStation()))
                .collect(toList());

        List<Section> arrivalSections = allSections.stream()
                .filter(section -> hasSection(section, addStationRequest.getArrivalStation()))
                .collect(toList());

        validate(departureSections,arrivalSections);
        if (isTerminalAdding(addStationRequest, sortedSections)){
            return new TerminalCase(addStationRequest,sectionDao).addSection(id,departureSections,arrivalSections);
        }
        return new SelectDirection(addStationRequest,sectionDao).addSection(id,departureSections,arrivalSections);
    }

    @Override
    public List<AddStationResponse> addSection(long id, List<Section> departureSections, List<Section> arrivalSections) throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public List<AddStationResponse> addSectionWithDirection(long id, List<Section> departureSections, List<Section> arrivalSections, Section section) throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    private boolean hasSection(Section section, String requestStation) {
        return section.getDeparture().getName().equals(requestStation)||
                section.getArrival().getName().equals(requestStation);
    }

    private void validate(List<Section> departureSections, List<Section> arrivalSections) {
        int departureMatchStationCount = departureSections.size();
        int arrivalMatchStationCount = arrivalSections.size();

        if (departureMatchStationCount == 0 && arrivalMatchStationCount == 0) {
            throw new IllegalArgumentException("노선에 연결될 수 없는 역입니다.");
        }

        if (isAbnormalCase(departureMatchStationCount, arrivalMatchStationCount)) {
            throw new IllegalArgumentException("순환 노선입니다");
        }
    }
    private boolean isAbnormalCase(final long departureMatchCount, final long arrivalMatchCount) {
        return !List.of(departureMatchCount, arrivalMatchCount).contains(0l);
    }
    private boolean isTerminalAdding(final AddStationRequest addStationRequest,
                                     final List<Section> sortedSections) {
        final String upLineTerminal = sortedSections.get(0).getDeparture().getName();
        final String downLineTerminal = sortedSections.get(sortedSections.size() - 1).getArrival().getName();

        return addStationRequest.getDepartureStation().equals(downLineTerminal)
                || addStationRequest.getArrivalStation().equals(upLineTerminal);
    }
}
