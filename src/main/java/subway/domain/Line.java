package subway.domain;

import java.util.List;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(final Long id, final String name, final String color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public void add(final Section section) {
        sections.add(section);
    }

    // TODO: 삭제할 세션을 넘겨줄지, 그냥 Station만 넘겨줄지
    public void remove(final Sections deleteSections, final Station deleteStation) {
        if (sections.size() == 3) { // 현재 노선에 역이 2개만 있는 경우
            sections.clear();
            return;
        }

        if (deleteSections.size() == 1) { // 종점을 삭제하는 경우
            sections.removeFinalSection(deleteSections.getSections().get(0), deleteStation);
            return;
        }

        sections.remove(deleteSections.getSections(), deleteStation);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }
}
