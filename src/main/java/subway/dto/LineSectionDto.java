package subway.dto;

import subway.domain.subwaymap.Line;
import subway.domain.subwaymap.Section;
import subway.domain.subwaymap.Station;

public class LineSectionDto {

    private Long lineId;
    private String lineName;
    private String lineColor;
    private Integer lineAdditionalFare;
    private Long section_id;
    private int distance;
    private Long upStationId;
    private String upStationName;
    private Long downStationId;
    private String downStationName;


    public LineSectionDto(final Long lineId, final String lineName, final String lineColor,
        final Integer lineAdditionalFare, final Long section_id,
        final int distance, final Long upStationId, final String upStationName, final Long downStationId,
        final String downStationName) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.lineAdditionalFare = lineAdditionalFare;
        this.section_id = section_id;
        this.distance = distance;
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineColor() {
        return lineColor;
    }

    public Integer getLineAdditionalFare() {
        return lineAdditionalFare;
    }

    public Long getSection_id() {
        return section_id;
    }

    public int getDistance() {
        return distance;
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

    public Line getLine() {
        return Line.of(lineId, lineName, lineColor, lineAdditionalFare);
    }

    public Section getSection() {
        return Section.of(section_id, Station.of(upStationId, upStationName), Station.of(downStationId,
                downStationName),
            distance);
    }
}
