package subway.dto;

public class SectionAddResponse {
    private final Long id;

    private SectionAddResponse(Long id) {
        this.id = id;
    }

    public static SectionAddResponse from(Long id) {
        return new SectionAddResponse(id);
    }

    public Long getId() {
        return id;
    }
}
