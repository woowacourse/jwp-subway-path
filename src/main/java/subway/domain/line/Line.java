package subway.domain.line;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.exception.InvalidDistanceException;
import subway.exception.InvalidSectionException;

public final class Line {

    private static final int ADDITIONAL_INDEX = -1;
    private static final int INITIAL_SECTION_SIZE = 2;

    private final Long id;
    private final Name name;
    private final Color color;
    private final Sections sections;

    public Line(final String name, final String color) {
        this(null, name, color);
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new LinkedList<>());
    }

    public Line(final Long id, final String name, final String color, final List<Section> sections) {
        this.id = id;
        this.name = new Name(name);
        this.color = new Color(color);
        this.sections = new Sections(sections);
    }

    public static Line from(final LineEntity lineEntity) {
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public static Line of(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        final Line line = new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor()
        );
        loadSections(line, generateSections(sectionEntities));
        return line;
    }

    private static List<Section> generateSections(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(Section::from)
                .collect(Collectors.toList());
    }

    private static void loadSections(final Line line, final List<Section> sections) {
        int numberOfInvalidSections = 0;
        while (!sections.isEmpty()) {
            validateSections(sections, numberOfInvalidSections);
            final Section section = sections.remove(0);
            try {
                line.addSection(section.getUpward(), section.getDownward(), section.getDistance());
                numberOfInvalidSections = 0;
            } catch (InvalidSectionException e) {
                sections.add(section);
                numberOfInvalidSections++;
            }
        }
    }

    private static void validateSections(final List<Section> sections, final int numberOfInvalidSections) {
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
            addDownwardSectionInLast(upward, downward, distance);
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

    private void addDownwardSectionInLast(final Station upward, final Station downward, final int distance) {
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

    public List<Station> getStations() {
        return sections.getUpwards();
    }

    public List<Section> getSections() {
        return sections.getValue();
    }
}
