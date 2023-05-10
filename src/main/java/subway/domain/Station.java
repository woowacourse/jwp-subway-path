package subway.domain;

import lombok.ToString;

@ToString
public class Station {
    private String name;

    public Station(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
