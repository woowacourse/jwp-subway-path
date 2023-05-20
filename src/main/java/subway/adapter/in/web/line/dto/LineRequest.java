package subway.adapter.in.web.line.dto;

import org.springframework.lang.NonNull;

public class LineRequest {
    @NonNull
    private String name;
    @NonNull
    private Integer surcharge;

    public LineRequest() {
    }

    public LineRequest(@NonNull final String name, @NonNull final Integer surcharge) {
        this.name = name;
        this.surcharge = surcharge;
    }

    public String getName() {
        return name;
    }

    public Integer getSurcharge() {
        return surcharge;
    }
}
