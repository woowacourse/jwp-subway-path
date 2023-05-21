package subway.domain;

import subway.domain.strategy.AddSectionStrategy;
import subway.domain.strategy.DirectionStrategy;
import subway.domain.strategy.FirstAddStrategy;
import subway.domain.strategy.SecondaryAddStrategy;
import subway.dto.SectionResponse;
import subway.exception.InvalidInputException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Line {
    private final Long id;

    private final String name;

    private final String color;

    private final List<Section> sections;

    public Line(Long id, String name, String color, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }


    public long getId() {
        return id;
    }

    public AddSectionStrategy preprocess(
            Station baseStation,
            Station newStation,
            DirectionStrategy direction,
            int distance) {
        if (isBlank()) {
            Section newSection = new Section(baseStation, newStation, new Distance(distance), id);
            return new FirstAddStrategy(newSection);
        }
        validateNotExist(newStation.getId());
        baseStation = getStation(baseStation.getId());
        return add(direction, baseStation, newStation, new Distance(distance));
    }

    public Station getStation(long baseStationId) {
        return sections.stream()
                .map(section -> section.getUpOrDownStation(baseStationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("기준역이 라인에 존재하지 않습니다."));
    }

    private void validateNotExist(Long newStationId) {
        boolean isExist = sections.stream()
                .map(section -> section.getUpOrDownStation(newStationId))
                .anyMatch(Optional::isPresent);

        if (isExist) {
            throw new InvalidInputException("새로운역이 라인에 이미 존재합니다.");
        }
    }

    public boolean isBlank() {
        return sections.size() == 0;
    }


    private AddSectionStrategy add(DirectionStrategy direction, Station baseStation, Station newStation, Distance distance) {
        Section newSection = direction.createSectionWith(baseStation, newStation, distance, id);
        Optional<Section> findSection = direction.findSection(baseStation, sections);

        if (findSection.isEmpty()) {
            return new FirstAddStrategy(newSection);
        }

        Section originalSection = findSection.get();
        Section newSectionBasedOnOriginal = direction.createSectionBasedOn(originalSection, newSection);

        sections.remove(originalSection);
        sections.add(newSection);
        sections.add(newSectionBasedOnOriginal);

        return new SecondaryAddStrategy(this);
    }

    public List<Section> getSections() {
        return sections;
    }

    // TODO: 지우기
    public List<SectionResponse> getSectionResponse() {
        return sections.stream().map(section -> new SectionResponse(
                section.getId(),
                section.getLineId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance())
        ).collect(Collectors.toUnmodifiableList());
    }
}
