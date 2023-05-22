package subway.fixture;

public enum StationFixture {

    수원(1L, "수원"),
    잠실나루(2L, "잠실나루"),
    의왕(3L, "의왕"),
    선릉(4L, "선릉"),
    여긴못감(5L, "여긴 못감");

    private final Long id;
    private final String name;

    StationFixture(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
