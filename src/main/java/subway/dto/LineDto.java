package subway.dto;

public class LineDto {
    private Long id;
    private final String name;

    public LineDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
