package subway.domain.lineDetail.dto;

import subway.domain.lineDetail.domain.LineDetail;

import java.util.Objects;

public class LineDetailResponse {

    private Long id;
    private String name;
    private String color;

    private LineDetailResponse() {
    }

    public LineDetailResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineDetailResponse of(final LineDetail lineDetail) {
        return new LineDetailResponse(lineDetail.getId(), lineDetail.getName(), lineDetail.getColor());
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineDetailResponse that = (LineDetailResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
