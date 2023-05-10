package subway.dao;

public class SectionEntity {

    private Long id;
    private Long nextSectionId;
    private String starterYn;
    private String currentStationName;
    private String nextStationName;
    private int distance;
    private Long lineId;

    public SectionEntity(final Long nextSectionId, final String starterYn, final String currentStationName,
                         final String nextStationName,
                         final int distance, final Long lineId) {
        this.nextSectionId = nextSectionId;
        this.starterYn = starterYn;
        this.currentStationName = currentStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
        this.lineId = lineId;
    }

    public Long getNextSectionId() {
        return nextSectionId;
    }

    public String getStarterYn() {
        return starterYn;
    }

    public String getCurrentStationName() {
        return currentStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public int getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
