package subway.domain.subwaymap;

import java.util.Objects;

public final class Line {

    private Long id;
    private String name;
    private String color;
    private Integer additionalFare;

    Line() {
    }

    private Line(Long id, String name, String color, Integer additionalFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public static Line of(final Long id, final String name, final String color, final Integer additionalFare) {
        LineValidator.validate(name, color, additionalFare);
        return new Line(id, name, color, additionalFare);
    }

    public static Line withNullId(final String name, final String color, final Integer additionalFare) {
        LineValidator.validate(name, color, additionalFare);
        return new Line(null, name, color, additionalFare);
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

    public Integer getAdditionalFare() {
        return additionalFare;
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
        if (Objects.isNull(id) || Objects.isNull(line.id)) {
            return false;
        }
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    static class LineValidator {

        private static final int NAME_MAX_LENGTH = 10;
        private static final int MIN_ADDITIONAL_FARE = 0;

        private LineValidator() {

        }

        private static void validate(final String name, final String color, final int additionalFare) {
            validateName(name);
            validateColor(color);
            validateAdditionalFare(additionalFare);
        }

        private static void validateName(final String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("노선의 이름은 비어있을 수 없습니다.");
            }
            if (name.length() > NAME_MAX_LENGTH) {
                throw new IllegalArgumentException("노선의 이름은 10자를 넘을 수 없습니다.");
            }
        }

        private static void validateColor(final String color) {
            if (color == null || color.isBlank()) {
                throw new IllegalArgumentException("노선의 색은 비어있을 수 없습니다.");
            }
        }

        private static void validateAdditionalFare(final int additionalFare) {
            if (additionalFare < MIN_ADDITIONAL_FARE) {
                throw new IllegalArgumentException("추가 요금은 0이상이여야 합니다.");
            }
        }
    }
}
