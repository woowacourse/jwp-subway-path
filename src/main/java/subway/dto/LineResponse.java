package subway.dto;

import subway.entity.LineEntity;

public class LineResponse {
    
    private final Long id;
    private final String name;
    private final String color;
    
    public LineResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
    
    public static LineResponse of(final LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getColor() {
        return this.color;
    }
}
