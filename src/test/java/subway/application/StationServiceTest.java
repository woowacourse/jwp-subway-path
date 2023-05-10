package subway.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StationDao;
import subway.dto.StationSaveRequest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationDao stationDao;

    @Test
    void saveRequest를_받아서_역을_저장한다() {
        // given
        StationSaveRequest request = new StationSaveRequest("잠실역");
        Mockito.when(stationDao.insert(request.toEntity())).thenReturn(1L);

        // when, then
        Assertions.assertThat(stationService.saveStation(request)).isEqualTo(1L);
    }

}
