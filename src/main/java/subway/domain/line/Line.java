package subway.domain.line;

import java.util.LinkedList;
import java.util.List;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.InvalidDistanceException;
import subway.exception.InvalidSectionException;

public final class Line {

    private static final int ADDITIONAL_INDEX = -1;
    private static final int INITIAL_SECTION_SIZE = 2;

    private final Long id;
    private final Name name;
    private final Color color;
    private final Fare fare;
    private final Sections sections;

    public Line(final String name, final String color, final int fare) {
        this(null, name, color, fare);
    }

    public Line(final Long id, final String name, final String color, final int fare) {
        this(id, name, color, fare, new LinkedList<>());
    }

    public Line(final Long id, final String name, final String color, final int fare, final List<Section> sections) {
        this.id = id;
        this.name = new Name(name);
        this.color = new Color(color);
        this.fare = new Fare(fare);
        this.sections = new Sections(sections);
    }

    public void addSection(final Station upward, final Station downward, final int distance) {
        if (sections.isEmpty()) {
            sections.add(new Section(upward, downward, distance));
            sections.add(new Section(downward, Station.TERMINAL, 0));
            return;
        }

        final int upwardPosition = sections.findPosition(upward);
        final int downwardPosition = sections.findPosition(downward);
        validateForAddSection(upwardPosition, downwardPosition);

        if (upwardPosition == ADDITIONAL_INDEX) {
            if (isAddAtFront(downwardPosition)) {
                addSectionEndPoints(true, upward, downward, distance);
                return;
            }
            addUpwardSectionBetweenStations(upward, downward, distance, downwardPosition);
        }

        if (downwardPosition == ADDITIONAL_INDEX) {
            if (isAddAtEnd(upwardPosition)) {
                addSectionEndPoints(false, upward, downward, distance);
                return;
            }
            addDownwardSectionBetweenStations(upward, downward, distance, upwardPosition);
        }
    }

    private void validateForAddSection(final int upwardPosition, final int downwardPosition) {
        if (upwardPosition != Sections.NOT_EXIST_INDEX && downwardPosition != Sections.NOT_EXIST_INDEX) {
            throw new InvalidSectionException("두 역이 이미 노선에 존재합니다.");
        }
        if (upwardPosition == Sections.NOT_EXIST_INDEX && downwardPosition == Sections.NOT_EXIST_INDEX) {
            throw new InvalidSectionException("연결할 역 정보가 없습니다.");
        }
    }

    private boolean isAddAtFront(final int downwardPosition) {
        return downwardPosition == 0;
    }

    private boolean isAddAtEnd(final int upwardPosition) {
        return upwardPosition == sections.size() - 1;
    }

    private void addSectionEndPoints(
            final boolean isFirst,
            final Station upward,
            final Station downward,
            final int distance
    ) {
        sections.deleteByPosition(sections.size() - 1);
        sections.add(getEndPosition(isFirst), new Section(upward, downward, distance));
        final Section lastSection = sections.findSectionByPosition(sections.size() - 1);
        sections.add(sections.size(), new Section(lastSection.getDownward(), Station.TERMINAL, 0));
    }

    public int getEndPosition(final boolean isFirst) {
        if (isFirst) {
            return 0;
        }
        return sections.size();
    }

    private void addUpwardSectionBetweenStations(
            final Station upward,
            final Station downward,
            final int distance,
            final int position
    ) {
        final int targetPosition = position - 1;
        final Section section = sections.findSectionByPosition(targetPosition);
        sections.deleteByPosition(targetPosition);
        validateDistance(section.getDistance(), distance);
        sections.add(targetPosition, new Section(upward, downward, distance));
        sections.add(targetPosition, new Section(section.getUpward(), upward, section.getDistance() - distance));
    }

    private void addDownwardSectionBetweenStations(
            final Station upward,
            final Station downward,
            final int distance,
            final int position
    ) {
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

        sections.add(
                position - 1,
                new Section(
                        previousSection.getUpward(),
                        targetSection.getDownward(),
                        targetSection.getDistance() + previousSection.getDistance()
                )
        );
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

    public int getFare() {
        return fare.getValue();
    }

    public List<Station> getStations() {
        return sections.getUpwards();
    }

    public List<Section> getSections() {
        final List<Section> sections = this.sections.getValue();
        sections.removeIf(section -> section.getDownward() == Station.TERMINAL);
        return sections;
    }
}
