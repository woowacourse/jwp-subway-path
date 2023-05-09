package subway.domain;

public class LineName {
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_NAME_LENGTH = 3;

    private final String name;

    public LineName(final String name) {
        validateLength(name);
        this.name = name;
    }

    private void validateLength(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("역 이름 길이는 " + MIN_NAME_LENGTH + "~" + MAX_NAME_LENGTH + "여야 합니다.");
        }
    }

    public String name() {
        return name;
    }
}
