package subway.line;

public class LineCreateDto {

    private final String name;

    public LineCreateDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
