package subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static subway.line.exception.station.StationExceptionType.DUPLICATE_STATION_NAME;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.common.exception.BaseExceptionType;
import subway.line.application.dto.StationCreateCommand;
import subway.line.domain.Station;
import subway.line.domain.StationRepository;
import subway.line.exception.station.StationException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("StationService 은(는)")
class StationServiceTest {

    private final StationRepository stationRepository = mock(StationRepository.class);
    private final StationService stationService = new StationService(stationRepository);

    @Test
    void 역을_저장한다() {
        // given
        willDoNothing().given(stationRepository).save(any());

        // when
        final UUID uuid = stationService.create(new StationCreateCommand("잠실역"));

        // then
        assertThat(uuid).isNotNull();
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
