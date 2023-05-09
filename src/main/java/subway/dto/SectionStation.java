package subway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SectionStation {

    private final Long id;

    @JsonCreator
    public SectionStation(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
