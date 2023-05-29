package subway.business.service.dto;

import subway.business.domain.Line;

public class LineResponse {
    private Long id;
    private String name;
    private Integer fare;

    public LineResponse(Long id, String name, Integer fare) {
        this.id = id;
        this.name = name;
        this.fare = fare;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getFare());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getFare() {
        return fare;
    }
}
