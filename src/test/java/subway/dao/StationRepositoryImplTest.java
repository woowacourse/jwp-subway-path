package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static subway.exception.ErrorCode.STATION_NOT_FOUND;
import static subway.fixture.StationFixture.역_엔티티들;
import static subway.fixture.StationFixture.잠실역;
import static subway.fixture.StationFixture.잠실역_엔티티;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.domain.station.dto.StationRes;
import subway.exception.DBException;
import subway.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class StationRepositoryImplTest {

    @Mock
    private StationDao stationDao;

    @InjectMocks
    private StationRepositoryImpl stationRepository;

    @Test
    @DisplayName("역 정보를 저장한다")
    void insert() {
        // given
        when(stationDao.insert(any()))
            .thenReturn(1L);

        // when
        final Long 저장된_잠실역_아이디 = stationRepository.insert(잠실역);

        // then
        assertThat(저장된_잠실역_아이디)
            .isSameAs(1L);
    }

    @Test
    @DisplayName("모든 역 정보를 조회한다.")
    void findAll() {
        // given
        when(stationDao.findAll())
            .thenReturn(역_엔티티들());

        // when
        final List<StationRes> stationRes = stationRepository.findAll();

        // then
        assertThat(stationRes)
            .extracting(StationRes::getName)
            .containsExactly("잠실역", "선릉역", "강남역", "신림역", "복정역", "남위례역", "산성역");
    }

    @Test
    @DisplayName("유효한 역 아이디가 주어지면, 역 정보를 조회한다")
    void findById_success() {
        // given
        when(stationDao.findById(anyLong()))
            .thenReturn(Optional.of(잠실역_엔티티));

        // when
        final Station station = stationRepository.findById(1L);

        // then
        assertThat(station)
            .extracting(Station::name)
            .isEqualTo(StationName.create("잠실역"));
    }

    @Test
    @DisplayName("유효하지 않은 역 아이디가 주어지면, 예외가 발생한다.")
    void findById_fail() {
        // given
        when(stationDao.findById(anyLong()))
            .thenReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> stationRepository.findById(1L))
            .isInstanceOf(NotFoundException.class)
            .extracting("errorCode", "errorMessage")
            .containsExactly(STATION_NOT_FOUND, "역 정보가 존재하지 않습니다. id = 1");
    }

    @Test
    @DisplayName("주어진 역 아이디에 해당하는 역 정보를 수정한다.")
    void updateById_success() {
        // given
        when(stationDao.update(any()))
            .thenReturn(1);

        // expected
        assertDoesNotThrow(() -> stationRepository.updateById(1L, 잠실역));
    }

    @ParameterizedTest(name = "역 수정이 발생한 행이 1개가 아니면 예외가 발생한다.")
    @ValueSource(ints = {0, 2})
    void updateById_fail(final int updatedCount) {
        // given
        when(stationDao.update(any()))
            .thenReturn(updatedCount);

        // expected
        assertThatThrownBy(() -> stationRepository.updateById(1L, 잠실역))
            .isInstanceOf(DBException.class)
            .hasMessage("DB 업데이트가 정상적으로 진행되지 않았습니다.");
    }

    @Test
    @DisplayName("주어진 역 아이디에 해당하는 역 정보를 제거한다.")
    void deleteById_success() {
        // given
        when(stationDao.deleteById(anyLong()))
            .thenReturn(1);

        // expected
        assertDoesNotThrow(() -> stationRepository.deleteById(1L));
    }

    @ParameterizedTest(name = "역 제거가 발생한 행이 1개가 아니면 예외가 발생한다.")
    @ValueSource(ints = {0, 2})
    void deleteById_fail(final int deletedCount) {
        // given
        when(stationDao.deleteById(anyLong()))
            .thenReturn(deletedCount);

        // expected
        assertThatThrownBy(() -> stationRepository.deleteById(1L))
            .isInstanceOf(DBException.class)
            .hasMessage("DB 삭제가 정상적으로 진행되지 않았습니다.");
    }

    @ParameterizedTest(name = "주어진 이름을 가진 역이 존재하면 true를, 아니면 false를 반환한다.")
    @CsvSource(value = {"잠실역:true", "선릉역:false"}, delimiter = ':')
    void existByName(final String name, final boolean expected) {
        // given
        when(stationDao.existByName(name))
            .thenReturn(expected);

        // expected
        assertThat(stationRepository.existByName(name))
            .isSameAs(expected);
    }
}
