package subway.presentation.dto.request;

public class CreateLineRequest {

    private String name;
    private String color;

    private CreateLineRequest() {
    }

    private CreateLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public static CreateLineRequest of(final String name, final String color) {
        return new CreateLineRequest(name, color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
