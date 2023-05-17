package subway.adapter.in.web.line.dto;

import org.springframework.lang.NonNull;

public class LineRequest {
    @NonNull
    private String name;


    public LineRequest() {
    }

    public LineRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
