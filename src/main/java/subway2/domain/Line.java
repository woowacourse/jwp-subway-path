package subway2.domain;

import java.util.List;

//todo 생각해볼것1: 도메인 객체를 값객체로 감싸야 하는지?
//todo 생각해볼것2: addStation같은 역할을 부여해야 하는지?
//todo 생각해볼것3: 비즈니스로직에서 id가 필요한가?
public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(String name, String color) {
        this.id = null;
        this.name = name;
        this.color = color;
        this.sections = Sections.EMPTY_SECTION;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = Sections.EMPTY_SECTION;
    }

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public List<Station> findStations() {
        return sections.findStations();
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

    public Sections getSections() {
        return sections;
    }
}
