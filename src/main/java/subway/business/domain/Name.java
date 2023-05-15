package subway.business.domain;

public class Name {
    public static final int MIN_LENGTH = 1;
    private final String name;

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    public void validate(String name) {
        if (name.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("이름의 길이는 1보다 작을 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
