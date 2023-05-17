package subway.ui.dto.request;

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
