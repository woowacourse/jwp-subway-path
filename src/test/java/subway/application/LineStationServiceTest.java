package subway.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.request.AddStationToBetweenLineRequest;
import subway.controller.dto.request.AddStationToEndLineRequest;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.vo.Distance;
import subway.exception.BusinessException;
import subway.persistence.LineRepository;
import subway.persistence.StationRepository;

@ExtendWith(MockitoExtension.class)
class LineStationServiceTest {

    private static final Station topStation = new Station(1L, "topStation");
    private static final Station midUpStation = new Station(2L, "midUpStation");
    private static final Station midDownStation = new Station(3L, "midDownStation");
    private static final Station bottomStation = new Station(4L, "bottomStation");
    private static final Distance distance = new Distance(10L);
    private static final Section topSection = new Section(1L, topStation, midUpStation, distance);
    private static final Section midSection = new Section(2L, midUpStation, midDownStation, distance);
    private static final Section bottomSection = new Section(3L, midDownStation, bottomStation, distance);

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private LineStationService lineStationService;

    @Test
    @DisplayName("호선이 없을 시 역을 호선의 맨 위에 추가한다.")
    void testAddStationToTopLineWhenLineNotFound() {
        //given
        given(lineRepository.findByName(anyString()))
            .willReturn(Optional.empty());
        final AddStationToEndLineRequest request = new AddStationToEndLineRequest("lineName", "stationName", 10L);

        //when
        //then
        assertThatThrownBy(() -> lineStationService.addStationToTopLine(request))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("역이 없을 시 역을 호선의 맨 위에 추가한다.")
    void testAddStationToTopLineWhenStationNotFound() {
        //given
        final Sections sections = new Sections(
            new ArrayList<>(List.of(bottomSection, topSection, midSection)));
        final Line line = new Line(1L, "name", "color", 0L, sections);
        given(lineRepository.findByName(anyString()))
            .willReturn(Optional.of(line));
        given(stationRepository.findByName(anyString()))
            .willReturn(Optional.empty());
        final AddStationToEndLineRequest request = new AddStationToEndLineRequest("lineName",
            "stationName", 10L);

        //when
        //then
        assertThatThrownBy(() -> lineStationService.addStationToTopLine(request))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("호선이 없을 시 역을 호선의 맨 아래에 추가한다.")
    void testAddStationToBottomLineWhenLineNotFound() {
        //given
        given(lineRepository.findByName(anyString()))
            .willReturn(Optional.empty());
        final AddStationToEndLineRequest request = new AddStationToEndLineRequest("lineName", "stationName", 10L);

        //when
        //then
        assertThatThrownBy(() -> lineStationService.addStationToBottomLine(request))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("역이 없을 시 역을 호선의 맨 아래에 추가한다.")
    void testAddStationToBottomLineWhenStationNotFound() {
        //given
        final Sections sections = new Sections(
            new ArrayList<>(List.of(bottomSection, topSection, midSection)));
        final Line line = new Line(1L, "name", "color", 0L, sections);
        given(lineRepository.findByName(anyString()))
            .willReturn(Optional.of(line));
        given(stationRepository.findByName(anyString()))
            .willReturn(Optional.empty());
        final AddStationToEndLineRequest request = new AddStationToEndLineRequest("lineName",
            "stationName", 10L);

        //when
        //then
        assertThatThrownBy(() -> lineStationService.addStationToBottomLine(request))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("호선이 없을 시 역을 호선의 사이에 추가한다.")
    void testAddStationToBetweenLineWhenLineNotFound() {
        //given
        given(lineRepository.findByName(anyString()))
            .willReturn(Optional.empty());
        final AddStationToBetweenLineRequest request = new AddStationToBetweenLineRequest("lineName", "stationName",
            "upStationName", "downStationName", 10L);

        //when
        //then
        assertThatThrownBy(() -> lineStationService.addStationToBetweenLine(request))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("역이 없을 시 역을 호선의 사이에 추가한다.")
    void testAddStationToBetweenLineWhenStationNotFound() {
        //given
        final Sections sections = new Sections(
            new ArrayList<>(List.of(bottomSection, topSection, midSection)));
        final Line line = new Line(1L, "name", "color", 0L, sections);
        given(lineRepository.findByName(anyString()))
            .willReturn(Optional.of(line));
        given(stationRepository.findByName(anyString()))
            .willReturn(Optional.empty(), Optional.empty());
        final AddStationToBetweenLineRequest request = new AddStationToBetweenLineRequest("lineName", "stationName",
            "upStationName", "downStationName", 10L);

        //when
        //then
        assertThatThrownBy(() -> lineStationService.addStationToBetweenLine(request))
            .isInstanceOf(BusinessException.class);
    }
}
