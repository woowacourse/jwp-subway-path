package subway.station.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.dao.StationDao;
import subway.domain.line.dto.StationCreateRequest;
import subway.domain.line.entity.StationEntity;
import subway.domain.line.service.StationService;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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
        //given
        given(stationDao.findById(anyLong())).willReturn(Optional.of(new StationEntity(1L, null)));

        //when
        stationService.findStationById(anyLong());

        //then
        verify(stationDao).findById(anyLong());
    }

    @Test
    void 모든_역_검색_테스트() {
        //given
        given(stationDao.findAll()).willReturn(Optional.of(new ArrayList<>()));

        //when
        stationService.findAllStation();

        //then
        verify(stationDao).findAll();
    }

    @Test
    void 역_수정_테스트() {
        //given
        given(stationDao.findById(anyLong())).willReturn(Optional.of(new StationEntity(1L, null)));

        //when
        stationService.updateStation(anyLong(), new StationCreateRequest("동대구역"));

        //then
        verify(stationDao).update(any(StationEntity.class));
    }

    @Test
    void 역_삭제_테스트() {
        //given
        given(stationDao.findById(anyLong())).willReturn(Optional.of(new StationEntity(1L, null)));

        //when
        stationService.deleteStationById(anyLong());

        //then
        verify(stationDao).deleteById(anyLong());
    }

    @Test
    void 노선_정보를_추가_테스트() {
        //given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("상인역");
        given(stationDao.findByName(stationCreateRequest.getName())).willReturn(Optional.empty());
        StationEntity stationEntity = new StationEntity("상인역");
        given(stationDao.insert(stationEntity)).willReturn(stationEntity);
        //when
        StationEntity saveStationEntity = stationService.saveStation(stationCreateRequest);

        //then
        verify(stationDao).insert(any());
        Assertions.assertThat(saveStationEntity).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(stationEntity);
    }

    @Test
    void 노선_정보를_추가할_때_중복된_이름의_노선이_존재하면_예외_발생() {
        //given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("상인역");
        given(stationDao.findByName(stationCreateRequest.getName())).willReturn(Optional.of(new StationEntity(stationCreateRequest.getName())));

        //then
        assertThatThrownBy(() -> stationService.saveStation(stationCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름이 이미 존재합니다. 유일한 역 이름을 사용해주세요.");
    }
}
