package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import subway.exception.DuplicatedStationNameException;
import subway.exception.StationNotFoundException;
import subway.repository.StationDao;
import subway.entity.StationEntity;
import subway.controller.dto.request.StationRequest;
import subway.controller.dto.response.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @Mock
    private StationDao stationDao;

    @InjectMocks
    private StationService stationService;

    @Test
    @DisplayName("저장 성공")
    void save_success() {
        // given
        final long id = 20L;
        final String name = "오리";
        final StationRequest stationRequest = new StationRequest(name);
        final StationEntity stationEntity = new StationEntity(id, name);
        given(stationDao.insert(any())).willReturn(stationEntity);

        // when
        final StationResponse response = stationService.save(stationRequest);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(id),
                () -> assertThat(response.getName()).isEqualTo(name)
        );
    }

    @Test
    @DisplayName("저장 실패 - 중복된 이름")
    void save_fail_duplicated_name() {
        // given
        final String name = "잠실";
        final StationRequest stationRequest = new StationRequest(name);
        given(stationDao.insert(any())).willThrow(DataIntegrityViolationException.class);

        // when, then
        assertThatThrownBy(() -> stationService.save(stationRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 조회 성공")
    void findById_success() {
        // given
        final long id = 1L;
        final String name = "잠실";
        final StationEntity stationEntity = new StationEntity(id, name);
        given(stationDao.findById(id)).willReturn(stationEntity);

        // when
        final StationResponse response = stationService.findById(id);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(id),
                () -> assertThat(response.getName()).isEqualTo(name)
        );
    }

    @Test
    @DisplayName("id로 조회 실패 - 존재하지 않는 id인 경우")
    void findById_fail_id_not_found() {
        // given
        final long id = 10L;
        given(stationDao.findById(id)).willThrow(EmptyResultDataAccessException.class);

        // when, then
        assertThatThrownBy(() -> stationService.findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("전체 조회 성공")
    void findByName_success() {
        // given
        final List<StationEntity> stationEntities = List.of(
                new StationEntity(1L, "잠실"),
                new StationEntity(2L, "잠실새내"),
                new StationEntity(3L, "종합운동장")
        );
        given(stationDao.findAll()).willReturn(stationEntities);

        // when
        final List<StationResponse> responses = stationService.findAll();

        // then
        assertAll(
                () -> assertThat(responses).hasSize(3),
                () -> assertThat(responses.get(0).getId()).isEqualTo(1L),
                () -> assertThat(responses.get(0).getName()).isEqualTo("잠실")
        );
    }

    @Test
    @DisplayName("수정 성공")
    void update_success() {
        // given
        final long id = 1L;
        final String name = "실잠";
        final StationRequest stationRequest = new StationRequest(name);

        // when, then
        assertThatCode(() -> stationService.update(id, stationRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("수정 실패 - 존재하지 않는 id")
    void update_fail_id_not_found() {
        // given
        final long id = 11L;
        final String name = "실잠";
        final StationRequest stationRequest = new StationRequest(name);

        // when
        doThrow(StationNotFoundException.class).when(stationDao).update(any());

        // then
        assertThatThrownBy(() -> stationService.update(id, stationRequest))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("수정 실패 - 겹치는 역 이름")
    void update_fail_duplicated_station_name() {
        // given
        final long id = 1L;
        final String name = "종합운동장";
        final StationRequest stationRequest = new StationRequest(name);

        // when
        doThrow(DuplicatedStationNameException.class).when(stationDao).update(any());

        // then
        assertThatThrownBy(() -> stationService.update(id, stationRequest))
                .isInstanceOf(DuplicatedStationNameException.class);
    }

    @Test
    @DisplayName("삭제 성공")
    void delete_success() {
        // given
        final long id = 1L;

        // when, then
        assertThatCode(() -> stationService.delete(id))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제 실패 - 존재하지 않는 id")
    void delete_fail_station_not_found() {
        // given
        final long id = 11L;

        // when
        doThrow(StationNotFoundException.class).when(stationDao).deleteById(id);

        // then
        assertThatThrownBy(() -> stationService.delete(id))
                .isInstanceOf(StationNotFoundException.class);
    }
}
