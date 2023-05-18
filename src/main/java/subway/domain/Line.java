package subway.domain;

import java.util.List;

public class Line {

    private final Long id;
    private final String name;
    private final Sections sections;
    private int additionalFee;

    public Line(final String name, final Sections sections) {
        this(null, name, sections);
    }

    public Line(final Long id, final String name, final Sections sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public Line(String name, Sections sections, int additionalFee) {
        this(null, name, sections, additionalFee);
    }

    public Line(Long id, String name, Sections sections, int additionalFee) {
        this.id = id;
        this.name = name;
        this.sections = sections;
        this.additionalFee = additionalFee;
    }

    public void addSection(final Section section) {
        sections.addSection(section);
    }

    public void removeStation(final Station station) {
        sections.removeStation(station);
    }

    public boolean containSection(final Section section) {
        return sections.contains(section);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int getAdditionalFee() {
        return additionalFee;
    }
}
