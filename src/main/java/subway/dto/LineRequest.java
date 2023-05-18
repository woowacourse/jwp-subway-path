package subway.dto;

public class LineRequest {
    
    private String name;
    private String color;
    
    public LineRequest() {
    }
    
    public LineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getColor() {
        return this.color;
    }
    
}
