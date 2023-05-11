package subway.station.domain;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Station {

    private Long id;
    private String name;

    public Station(final String name) {
        this.name = name;
    }
}
