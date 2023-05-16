package subway.domain.line;

public class LineName {

    private static final int MAX_LINE_NAME = 10;
    private final String name;

    public LineName(final String name) {
        validateLineName(name);
        this.name = name;
    }

    private void validateLineName(final String name) {
        validateEmptyName(name);
        validateMaxLength(name);
    }

    private void validateEmptyName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("노선 이름은 공백일 수 없습니다.");
        }
    }

    private void validateMaxLength(final String name) {
        if (name.length() > MAX_LINE_NAME) {
            throw new IllegalArgumentException("노선 이름은 " + MAX_LINE_NAME + "글자 보다 작아야합니다.");
        }
    }

    public String name() {
        return name;
    }
}
