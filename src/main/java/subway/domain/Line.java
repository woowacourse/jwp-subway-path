package subway.domain;

public class Line {

    private final Long id;
    private final LineName name;
    private final Sections sections;

    public Line(final Long id, final LineName name, final Sections sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public Line(final Long id, final LineName name) {
        this(id, name, new Sections());
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return name;
    }

    public Sections getSections() {
        return sections;
    }

    public Line addSection(final Section newSection) {
        validateDuplicateSection(newSection);
        if (sections.isHeadStation(newSection.getNextStation())) {
            final Sections addedSections = sections.addHead(newSection);
            return new Line(id, name, addedSections);
        }
        if (sections.isTailStation(newSection.getPrevStation())) {
            final Sections addedSections = sections.addTail(newSection);
            return new Line(id, name, addedSections);
        }
        final Sections addedSections = sections.addCentral(newSection);
        return new Line(id, name, addedSections);
    }

    private void validateDuplicateSection(final Section newSection) {
        if (sections.containSection(newSection)) {
            throw new IllegalArgumentException("이미 등록되어 있는 구간입니다.");
        }
    }

    public Line removeStation(final Station station) {
        validateIsExist(station);
        if (sections.isHeadStation(station)) {
            final Sections removedSections = sections.removeHead();
            return new Line(id, name, removedSections);
        }
        if (sections.isTailStation(station)) {
            final Sections removedSections = sections.removeTail();
            return new Line(id, name, removedSections);
        }
        final Sections removedSections = sections.removeCentral(station);
        return new Line(id, name, removedSections);
    }

    private void validateIsExist(final Station station) {
        if (sections.notContainStation(station)) {
            throw new IllegalArgumentException("삭제하려는 Station은 해당 노선에 존재하지 않습니다.");
        }
    }
}
