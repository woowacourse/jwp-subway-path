package subway.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.facade.LineFacade;
import subway.line.presentation.dto.LineRequest;
import subway.line.presentation.dto.LineStationResponse;
import subway.line.service.LineService;
import subway.station.presentation.dto.response.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LineFacadeTest {

    @InjectMocks
    LineFacade lineFacade;

    @Mock
    LineService lineService;

    @DisplayName("호선의 아이디를 입력받아 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {"1"})
    void getAllStationByLineIdAsc(Long lineId) {
        // given
        List<StationResponse> stationResponses = List.of(new StationResponse(1L, "station1"), new StationResponse(2L, "station2"));
        LineStationResponse response = new LineStationResponse(lineId, "responseName", "responseColor", stationResponses);
        doReturn(response).when(lineService).findAllByIdAsc(lineId);

        // when
        LineStationResponse result = lineFacade.getAllStationByLineIdAsc(lineId);

        // then
        Assertions.assertAll(
                () -> assertThat(result.getLineId()).isEqualTo(1L),
                () -> assertThat(result.getColor()).isEqualTo("responseColor"),
                () -> assertThat(result.getName()).isEqualTo("responseName"),
                () -> assertThat(result.getStationResponse().size()).isEqualTo(2)
        );
    }

    @DisplayName("호선의 아이디를 입력받아 호선의 정보를 수정한다.")
    @ParameterizedTest
    @CsvSource(value = {"1"})
    void updateLine(Long id) {
        // given
        LineRequest request = new LineRequest("2호선", "초록", 10);

        // when
        lineService.update(id, request);

        // then
        verify(lineService, times(1)).update(id, request);
    }

    @DisplayName("호선의 아이디를 입력받아 호선의 정보를 수정한다.")
    @ParameterizedTest
    @CsvSource(value = {"1"})
    void deleteLineById(Long id) {
        // when
        lineService.deleteById(id);

        // then
        verify(lineService, times(1)).deleteById(id);
    }

}
