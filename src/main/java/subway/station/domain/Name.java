package subway.station.domain;

import java.util.Objects;

final class Name {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;
    private static final String KOREAN_REGEX = "^[ㄱ-ㅎ가-힣]*$";

    private final String value;

    private Name(final String value) {
        this.value = value;
    }

    public static Name from(final String value) {
        validate(value);
        return new Name(value);
    }

    private static void validate(final String value) {
        validateLength(value);
        validateLanguage(value);
    }

    private static void validateLength(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("지하철 역 이름은 비어있을 수 없습니다.");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("지하철 역 이름의 길이는 %d이상 %d 이하여야 합니다.", MIN_LENGTH, MIN_LENGTH));
        }
    }

    private static void validateLanguage(final String value) {
        final boolean isKorean = value.matches(KOREAN_REGEX);
        if (!isKorean) {
            throw new IllegalArgumentException("지하철 역 이름은 한글이여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
