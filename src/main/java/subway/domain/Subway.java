package subway.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Subway {
    private final List<Sections> sections;

    public Subway() {
        this.sections = new ArrayList<>();
    }
}
