package subway.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.persistence.dao.LineSectionStationJoinDto;

class LineConverterTest {

    private final Station topStation = new Station(1L, "topStation");
    private final Station midUpStation = new Station(2L, "midUpStation");
    private final Station midDownStation = new Station(3L, "midDownStation");
    private final Station bottomStation = new Station(4L, "bottomStation");
    private final long distance = 10L;
    private final Section topSection = new Section(1L, topStation, midUpStation, distance);
    private final Section midSection = new Section(2L, midUpStation, midDownStation, distance);
    private final Section bottomSection = new Section(3L, midDownStation, bottomStation, distance);

    private final LineConverter lineConverter = new LineConverter();


    @Test
    @DisplayName("join dto를 Line으로 변환한다.")
    void testConvertToLine() {
        //given
        final long lineId = 1L;
        final String lineName = "lineName";
        final String lineColor = "lineColor";
        final LineSectionStationJoinDto dto1 = new LineSectionStationJoinDto(lineId, lineName, lineColor,
            topStation.getId(),
            topStation.getName(), midUpStation.getId(), midUpStation.getName(),
            topSection.getId(), topSection.getDistance());
        final LineSectionStationJoinDto dto2 = new LineSectionStationJoinDto(lineId, lineName, lineColor,
            midUpStation.getId(), midUpStation.getName(), midDownStation.getId(), midDownStation.getName(),
            midSection.getId(), midSection.getDistance());
        final LineSectionStationJoinDto dto3 = new LineSectionStationJoinDto(lineId, lineName, lineColor,
            midDownStation.getId(), midDownStation.getName(), bottomStation.getId(), bottomStation.getName(),
            bottomSection.getId(), bottomSection.getDistance());
        final List<LineSectionStationJoinDto> lineSectionStationJoinDtos = new ArrayList<>(List.of(dto1, dto2, dto3));

        //when
        final Line line = lineConverter.convertToLine(lineSectionStationJoinDtos);

        //then
        assertThat(line.getId()).isEqualTo(lineId);
        assertThat(line.getName()).isEqualTo(lineName);
        assertThat(line.getColor()).isEqualTo(lineColor);
        final Sections sections = line.getSections();
        assertThat(sections.findStation(0)).isEqualTo(topStation);
        assertThat(sections.findStation(1)).isEqualTo(midUpStation);
        assertThat(sections.findStation(2)).isEqualTo(midDownStation);
        assertThat(sections.findStation(3)).isEqualTo(bottomStation);
    }
}
