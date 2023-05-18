package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationRepositoryTest {

    @Mock
    StationDao stationDao;
    @InjectMocks
    StationRepository stationRepository;

    @DisplayName("저장한다")
    @Test
    void 저장한다() {
        //given
        Station station = new Station("테스트");
        when(stationDao.save(any())).thenReturn(1L);
        //when
        Station savedStation = stationRepository.save(station);
        //then
        assertThat(savedStation).isEqualTo(new Station(1L, "테스트"));
    }

    @DisplayName("아이디로 찾는다")
    @Test
    void 아이디로_찾는다() {
        //given
        Long id = 1L;
        when(stationDao.findById(id)).thenReturn(Optional.of(new StationEntity(1L, "테스트")));
        //when
        Station findStation = stationRepository.findById(id);
        //then
        assertThat(findStation).isEqualTo(new Station(1L, "테스트"));
    }

    @DisplayName("수정한다")
    @Test
    void 수정한다() {
        //given
        Station beforeStation = new Station(1L, "테스트");
        Station afterStation = new Station(1L, "수정");
        StationEntity stationEntity = new StationEntity(1L, "수정");
        //when
        stationRepository.update(beforeStation, afterStation);
        //then
        verify(stationDao, times(1)).update(stationEntity);
    }

    @DisplayName("수정한다")
    @Test
    void 삭제한다() {
        //given
        Station station = new Station(1L, "테스트");
        //when
        stationRepository.delete(station);
        //then
        verify(stationDao, times(1)).deleteById(1L);
    }
}
