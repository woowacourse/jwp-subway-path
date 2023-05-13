package subway.application.request;

public class CreateLineRequest {

    private String name;
    private String color;

    public CreateLineRequest() {
    }

    public CreateLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
