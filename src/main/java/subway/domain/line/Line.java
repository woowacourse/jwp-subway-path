package subway.domain.line;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.subway.billing_policy.Fare;
import subway.exception.InvalidDistanceException;
import subway.exception.InvalidSectionException;

public final class Line {

    private static final int ADDITIONAL_INDEX = -1;
    private static final int INITIAL_SECTION_SIZE = 2;

    private final Long id;
    private final Name name;
    private final Fare extraFare;
    private final Color color;
    private final Sections sections;

    public Line(final String name, final String color, final int fare) {
        this(null, name, color, fare);
    }

    public Line(final Long id, final String name, final String color, final int extraFare) {
        this(id, name, color, extraFare, Collections.emptyList());
    }

    public Line(
            final Long id,
            final String name,
            final String color,
            final int extraFare,
            final List<Section> sections
    ) {
        this.id = id;
        this.name = new Name(name);
        this.color = new Color(color);
        this.extraFare = new Fare(extraFare);
        this.sections = new Sections();
        loadSections(sections);
    }

    private void loadSections(final List<Section> sections) {
        int numberOfInvalidSections = 0;
        while (!sections.isEmpty()) {
            validateSections(sections, numberOfInvalidSections);
            final Section section = sections.remove(0);
            try {
                addSection(section.getUpward(), section.getDownward(), section.getDistance());
                numberOfInvalidSections = 0;
            } catch (InvalidSectionException e) {
                sections.add(section);
                numberOfInvalidSections++;
            }
        }
    }

    private void validateSections(final List<Section> sections, final int numberOfInvalidSections) {
        if (numberOfInvalidSections == sections.size()) {
            throw new InvalidSectionException("구간 정보가 올바르지 않습니다.");
        }
    }

    public void addSection(final Station upward, final Station downward, final int distance) {
        if (sections.isEmpty()) {
            addInitialSection(upward, downward, distance);
            return;
        }

        final int upwardPosition = sections.findPosition(upward);
        final int downwardPosition = sections.findPosition(downward);
        validateForAddSection(upwardPosition, downwardPosition);

        if (shouldAdd(upwardPosition)) {
            if (isFirstSection(downwardPosition)) {
                sections.add(0, new Section(upward, downward, distance));
                return;
            }
            addUpwardSectionBetweenStations(upward, downward, distance, downwardPosition);
            return;
        }

        if (isLastSection(upwardPosition)) {
            addDownwardSectionAtLast(upward, downward, distance);
            return;
        }
        addDownwardSectionBetweenStations(upward, downward, distance, upwardPosition);
    }

    private void addInitialSection(final Station upward, final Station downward, final int distance) {
        sections.add(new Section(upward, downward, distance));
        sections.add(new Section(downward, Station.TERMINAL, 0));
    }

    private void validateForAddSection(final int upwardPosition, final int downwardPosition) {
        if (upwardPosition != Sections.NOT_EXIST_INDEX
                && downwardPosition != Sections.NOT_EXIST_INDEX) {
            throw new InvalidSectionException("두 역이 이미 노선에 존재합니다.");
        }
        if (upwardPosition == Sections.NOT_EXIST_INDEX
                && downwardPosition == Sections.NOT_EXIST_INDEX) {
            throw new InvalidSectionException("연결할 역 정보가 없습니다.");
        }
    }

    private boolean shouldAdd(final int position) {
        return position == ADDITIONAL_INDEX;
    }

    private boolean isFirstSection(final int position) {
        return position == 0;
    }

    private void addUpwardSectionBetweenStations(final Station upward, final Station downward, final int distance,
                                                 final int downwardPosition) {
        final int targetPosition = downwardPosition - 1;
        final Section section = sections.findSectionByPosition(targetPosition);
        sections.deleteByPosition(targetPosition);
        validateDistance(section.getDistance(), distance);
        sections.add(targetPosition, new Section(upward, downward, distance));
        sections.add(targetPosition, new Section(section.getUpward(), upward, section.getDistance() - distance));
    }

    private boolean isLastSection(final int position) {
        return sections.size() - 1 == position;
    }

    private void addDownwardSectionAtLast(final Station upward, final Station downward, final int distance) {
        sections.deleteByPosition(sections.size() - 1);
        sections.add(sections.size(), new Section(upward, downward, distance));
        sections.add(sections.size(), new Section(downward, Station.TERMINAL, 0));
    }

    private void addDownwardSectionBetweenStations(final Station upward, final Station downward, final int distance,
                                                   final int position) {
        final Section section = sections.findSectionByPosition(position);
        sections.deleteByPosition(position);
        validateDistance(section.getDistance(), distance);
        sections.add(position, new Section(downward, section.getDownward(), section.getDistance() - distance));
        sections.add(position, new Section(upward, downward, distance));
    }

    private void validateDistance(final int oldDistance, final int inputDistance) {
        if (oldDistance <= inputDistance) {
            throw new InvalidDistanceException("추가될 역의 거리는 추가될 위치의 두 역사이의 거리보다 작아야합니다.");
        }
    }

    public void deleteStation(final Station station) {
        final int position = sections.findPosition(station);
        if (position == Sections.NOT_EXIST_INDEX) {
            throw new InvalidSectionException("노선에 해당 역이 존재하지 않습니다.");
        }

        if (sections.size() == INITIAL_SECTION_SIZE) {
            sections.clear();
            return;
        }

        if (position == 0) {
            sections.deleteByPosition(position);
            return;
        }

        final Section targetSection = sections.findSectionByPosition(position);
        final Section previousSection = sections.findSectionByPosition(position - 1);

        sections.deleteByPosition(position - 1);
        sections.deleteByPosition(position - 1);

        sections.add(position - 1,
                new Section(previousSection.getUpward(), targetSection.getDownward(),
                        targetSection.getDistance() + previousSection.getDistance()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color.getValue();
    }

    public int getExtraFare() {
        return extraFare.getValue();
    }

    public List<Station> getStations() {
        return sections.getUpwards();
    }

    public List<Section> getSections() {
        final List<Section> sections = this.sections.getValue();
        sections.removeIf(section -> section.getDownward() == Station.TERMINAL);
        return sections;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
