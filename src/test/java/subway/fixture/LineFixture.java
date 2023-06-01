package subway.fixture;

import java.util.List;

import static subway.fixture.StationFixture.선릉;
import static subway.fixture.StationFixture.수원;
import static subway.fixture.StationFixture.의왕;
import static subway.fixture.StationFixture.잠실나루;

public enum LineFixture {

    일호선(1L, "1호선", "파랑", List.of(수원, 잠실나루)),
    이호선(2L, "2호선", "초록", List.of(의왕, 수원, 선릉)),
    빈호선(3L, "empty", "none", List.of());

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationFixture> stationFixtures;

    LineFixture(final Long id, final String name, final String color, final List<StationFixture> stationFixtures) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationFixtures = stationFixtures;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationFixture> getStationFixtures() {
        return stationFixtures;
    }

    public int getSize() {
        return stationFixtures.size();
    }
}
