package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.StationCreateRequest;
import subway.dao.StationDao;
import subway.entity.StationEntity;
import subway.exception.InvalidStationNameException;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationDao stationDao;

    @Nested
    @DisplayName("역 생성 시 ")
    class CreateStation {

        @Test
        @DisplayName("정보가 유효하면 역을 생성한다.")
        void createStation() {
            final StationCreateRequest request = new StationCreateRequest("잠실역");
            final StationEntity entity = new StationEntity(1L, "잠실역");
            given(stationDao.save(any(StationEntity.class))).willReturn(entity);

            final Long stationId = stationService.createStation(request);

            assertThat(stationId).isEqualTo(1L);
        }

        @Test
        @DisplayName("역 이름이 유효하지 않으면 예외를 던진다.")
        void createStationWithInvalidName() {
            final StationCreateRequest request = new StationCreateRequest("잠실");

            assertThatThrownBy(() -> stationService.createStation(request))
                    .isInstanceOf(InvalidStationNameException.class);
        }
    }
}
