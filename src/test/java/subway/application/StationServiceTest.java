package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static subway.exception.station.StationExceptionType.DUPLICATE_STATION_NAME;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.application.dto.StationCreateCommand;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.exception.BaseExceptionType;
import subway.exception.station.StationException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("StationService 은(는)")
class StationServiceTest {

    private final StationRepository stationRepository = mock(StationRepository.class);
    private final StationService stationService = new StationService(stationRepository);

    @Test
    void 역을_저장한다() {
        // given
        given(stationRepository.save(any()))
                .willReturn(1L);

        // when
        final Long id = stationService.create(new StationCreateCommand("잠실역"));

        // then
        assertThat(id).isEqualTo(1);
    }

    @Test
    void 중복된_역을_저장할수_없다() {
        // given
        given(stationRepository.findByName("잠실역"))
                .willReturn(Optional.of(new Station("잠실역")));

        // when & then
        final BaseExceptionType exceptionType = assertThrows(StationException.class, () ->
                stationService.create(new StationCreateCommand("잠실역"))
        ).exceptionType();
        assertThat(exceptionType).isEqualTo(DUPLICATE_STATION_NAME);
    }
}
