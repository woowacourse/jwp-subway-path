package subway.dto.line;

public class LineResponse {

    private final Long id;
    private final Long lineNumber;
    private final String name;
    private final String color;
    private final int additionalFare;

    public LineResponse(final Long id, final Long lineNumber, final String name, final String color, final int additionalFare) {
        this.id = id;
        this.lineNumber = lineNumber;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public Long getId() {
        return id;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }
}
