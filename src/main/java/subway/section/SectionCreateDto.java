package subway.section;

public class SectionCreateDto {

    private final Long lineId;
    private final String up;
    private final String down;
    private final int distance;

    public SectionCreateDto(final Long lineId, final String up, final String down, final int distance) {
        this.lineId = lineId;
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getUp() {
        return up;
    }

    public String getDown() {
        return down;
    }

    public int getDistance() {
        return distance;
    }
}
