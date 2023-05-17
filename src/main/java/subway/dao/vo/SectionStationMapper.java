package subway.dao.vo;

public class SectionStationMapper {

    private final Long upStationId;
    private final String upStationName;
    private final Long downStationId;
    private final String downStationName;
    private final Integer distance;

    public SectionStationMapper(Long upStationId, String upStationName,
                                Long downStationId, String downStationName,
                                Integer distance) {
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "SectionStationMapper{" +
                "upStationId=" + upStationId +
                ", upStationName='" + upStationName + '\'' +
                ", downStationId=" + downStationId +
                ", downStationName='" + downStationName + '\'' +
                ", distance=" + distance +
                '}';
    }
}
