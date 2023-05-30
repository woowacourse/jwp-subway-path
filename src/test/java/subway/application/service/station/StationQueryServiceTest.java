package subway.application.service.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.application.port.out.station.LoadStationPort;
import subway.application.service.exception.NoSuchStationException;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationQueryServiceTest {

    private LoadStationPort loadStationPort;
    private StationQueryService stationQueryService;

    @BeforeEach
    void setUp() {
        loadStationPort = mock(LoadStationPort.class);
        stationQueryService = new StationQueryService(loadStationPort);
    }

    @Nested
    class 아이디로_역_조회시_ {

        private final long stationId = 1L;

        @Test
        void 아이디에_해당하는_역이_존재하지_않으면_예외() {
            // given
            given(loadStationPort.findById(stationId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> stationQueryService.findStationById(stationId))
                    .isInstanceOf(NoSuchStationException.class);
        }

        @Test
        void 성공() {
            // given
            Station station = new Station(stationId, "역삼역");
            given(loadStationPort.findById(stationId))
                    .willReturn(Optional.of(station));

            // when
            StationQueryResponse response = stationQueryService.findStationById(stationId);

            // then
            assertThat(response)
                    .usingRecursiveComparison()
                    .isEqualTo(new StationQueryResponse(stationId, "역삼역"));
        }
    }

    @Test
    void 역_전체_조회_테스트() {
        // given
        Station station1 = new Station(1L, "역삼역");
        Station station2 = new Station(2L, "잠실역");
        given(loadStationPort.findAll())
                .willReturn(List.of(station1, station2));

        // when
        List<StationQueryResponse> responses = stationQueryService.findAllStations();

        // then
        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses)
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(List.of(
                                new StationQueryResponse(1L, "역삼역"),
                                new StationQueryResponse(2L, "잠실역")
                        ))
        );
    }
}
