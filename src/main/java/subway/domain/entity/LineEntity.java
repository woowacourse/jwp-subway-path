package subway.domain.entity;

import subway.domain.vo.Color;
import subway.domain.vo.Name;

public class LineEntity {
    private Long id;
    private Name name;
    private Color color;

    private LineEntity(final Long id, final Name name, final Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineEntity of(String name, String color) {
        return new LineEntity(null, Name.from(name), Color.from(color));
    }

    public static LineEntity of(Long id, String name, String color) {
        return new LineEntity(id, Name.from(name), Color.from(color));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public void updateInfo(final String name, final String color) {
        this.name = Name.from(name);
        this.color = Color.from(color);
    }

}
