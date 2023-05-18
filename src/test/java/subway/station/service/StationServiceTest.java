package subway.station.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.station.dao.StationDao;
import subway.domain.station.entity.StationEntity;
import subway.domain.station.dto.StationRequest;
import subway.domain.station.service.StationService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StationServiceTest {

    @Mock
    private StationDao stationDao;
    @InjectMocks
    private StationService stationService;

    @Test
    void 단일_역_검색_테스트() {
        //when
        stationService.findStationById(anyLong());

        //then
        verify(stationDao).findById(anyLong());
    }

    @Test
    void 모든_역_검색_테스트() {
        //when
        stationService.findAllStation();

        //then
        verify(stationDao).findAll();
    }

    @Test
    void 역_수정_테스트() {
        //when
        stationService.updateStation(anyLong(), new StationRequest("동대구역"));

        //then
        verify(stationDao).update(any(StationEntity.class));
    }

    @Test
    void 역_삭제_테스트() {
        //when
        stationService.deleteStationById(anyLong());

        //then
        verify(stationDao).deleteById(anyLong());
    }

    @Test
    void 노선_정보를_추가_테스트() {
        //given
        StationRequest stationRequest = new StationRequest("상인역");
        given(stationDao.findByName(stationRequest.getName())).willReturn(Optional.empty());
        StationEntity stationEntity = new StationEntity("상인역");
        given(stationDao.insert(stationEntity)).willReturn(stationEntity);
        //when
        StationEntity saveStationEntity = stationService.saveStation(stationRequest);

        //then
        verify(stationDao).insert(any());
        Assertions.assertThat(saveStationEntity).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(stationEntity);
    }

    @Test
    void 노선_정보를_추가할_때_중복된_이름의_노선이_존재하면_예외_발생() {
        //given
        StationRequest stationRequest = new StationRequest("상인역");
        given(stationDao.findByName(stationRequest.getName())).willReturn(Optional.of(new StationEntity(stationRequest.getName())));

        //then
        assertThatThrownBy(() -> stationService.saveStation(stationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름이 이미 존재합니다. 유일한 역 이름을 사용해주세요.");
    }
}
