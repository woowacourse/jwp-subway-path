package subway.presentation.dto.request;

public class CreationLineRequest {

    private String name;
    private String color;

    private CreationLineRequest() {
    }

    private CreationLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public static CreationLineRequest of(final String name, final String color) {
        return new CreationLineRequest(name, color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
