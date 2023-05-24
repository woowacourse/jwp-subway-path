package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Sql("classpath:initializeTestDB.sql")
class StationServiceTest {

    @Mock
    StationDao stationDao;

    @InjectMocks
    StationService stationService;

    @DisplayName("역을 저장하면 저장한 정보에 대해 반환한다")
    @Test
    void saveStation() {
        // given
        String testName = "테스트역";
        StationRequest stationRequest = new StationRequest(testName);
        given(stationDao.findByName(testName)).willReturn(Optional.empty());

        // when
        StationResponse stationResponse = stationService.saveStation(stationRequest);

        // then
        assertThat(stationResponse.getName()).isEqualTo(testName);
    }

    @DisplayName("역을 저장 시 이미 존재하는 이름이라면 예외를 반환한다")
    @Test
    void saveStationExistName() {
        // given
        String testName = "서울대입구역";
        StationRequest stationRequest = new StationRequest(testName);
        given(stationDao.findByName(testName)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> stationService.saveStation(stationRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("역을 아이디를 통해 찾을 수 있다")
    @Test
    void findStationResponseById() {
        // given
        Long findId = 1L;
        StationEntity stationEntity = new StationEntity(findId, "서울대입구역");
        given(stationDao.findById(findId)).willReturn(Optional.of(stationEntity));

        // when
        StationResponse stationResponse = stationService.findStationResponseById(findId);

        // then
        assertAll(
                () -> assertThat(stationResponse.getId()).isEqualTo(1L),
                () -> assertThat(stationResponse.getName()).isEqualTo("서울대입구역")
        );
    }

    @DisplayName("모든 역을 조회할 수 있다")
    @Test
    void findAllStationResponses() {
        // given
        List<StationEntity> stationEntities = new ArrayList<>(
                List.of(new StationEntity(1L, "서울대입구역"),
                        new StationEntity(2L, "봉천역"),
                        new StationEntity(3L, "낙성대역"))
        );
        given(stationDao.findAll()).willReturn(stationEntities);

        // when
        List<StationResponse> stationResponses = stationService.findAllStationResponses();

        // then
        List<String> names = stationResponses.stream()
                                             .map(StationResponse::getName)
                                             .collect(Collectors.toList());
        assertAll(
                () -> assertThat(names).contains("서울대입구역", "봉천역", "낙성대역")
        );
    }
}
