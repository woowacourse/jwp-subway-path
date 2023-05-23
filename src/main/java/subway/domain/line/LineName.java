package subway.domain.line;

public class LineName {

    private static final int MAX_LENGTH = 20;
    private final String value;

    public LineName(final String value) {
        validateName(value);
        this.value = value;
    }

    private void validateName(final String name) {
        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("노선의 이름의 길이는 20자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
