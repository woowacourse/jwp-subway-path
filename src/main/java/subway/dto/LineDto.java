package subway.dto;

import subway.domain.line.Line;

import java.util.Objects;

public class LineDto {

    private final Long id;
    private final String name;

    private LineDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LineDto from(Line line) {
        return new LineDto(line.getId(), line.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineDto lineDto = (LineDto) o;
        return Objects.equals(id, lineDto.id) && Objects.equals(name, lineDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "LineDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
