package subway.application;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.repository.SectionRepository;
import subway.persistence.StationJdbcRepository;
import subway.ui.dto.request.StationRequest;
import subway.ui.dto.response.StationResponse;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

	@Mock
	StationJdbcRepository repository;

	@Mock
	SectionRepository sectionRepository;

	@InjectMocks
	StationService service;

	@DisplayName("역 생성 서비스 테스트")
	@Test
	void createStation() {
		// given
		given(repository.createStation(any())).willReturn(1L);

		// when
		final StationRequest request = new StationRequest("잠실");
		final StationResponse response = service.createStation(request);

		// then
		Assertions.assertThat(1L).isEqualTo(response.getId());
	}

	@DisplayName("전체 역 조회 서비스 테스트")
	@Test
	void findAll() {
		// given
		final Station jamsil = new Station(1L, "잠실");
		given(repository.findAll()).willReturn(List.of(jamsil));

		// when
		final List<StationResponse> stations = service.findAll();

		// then
		Assertions.assertThat(stations)
			.usingRecursiveComparison()
			.isEqualTo(List.of(new StationResponse(1L, "잠실")));
	}

	@DisplayName("ID를 사용한 역 조회 서비스 테스트")
	@Test
	void findById() {
		// given
		final Station jamsil = new Station(1L, "잠실");
		given(repository.findById(1L)).willReturn(jamsil);

		// when
		final StationResponse response = service.findById(1L);

		// then
		Assertions.assertThat(response)
			.hasFieldOrPropertyWithValue("id", 1L)
			.hasFieldOrPropertyWithValue("name", "잠실");
	}

	@DisplayName("역 갱신 서비스 테스트")
	@Test
	void updateStation() {
		// given
		given(repository.updateStation(anyLong(), any())).willReturn(true);

		// when
		final StationRequest request = new StationRequest("삼성");
		final StationResponse response = service.updateStation(1L, request);

		// then
		Assertions.assertThat(response)
			.hasFieldOrPropertyWithValue("id", 1L)
			.hasFieldOrPropertyWithValue("name", "삼성");
	}

	@DisplayName("역 삭제 서비스 테스트")
	@Test
	void deleteById() {
		// given
		final Section section1 = new Section(new Line("2호선"), new Station("잠실"), new Station("역삼"), 10L);
		final Section section2 = new Section(new Line("2호선"), new Station("역삼"), new Station("선릉"), 8L);
		given(repository.deleteById(anyLong())).willReturn(true);
		given(sectionRepository.findSectionsContainStation(any())).willReturn(List.of(section1, section2));

		// when
		final long deletedId = service.deleteById(1L);

		// then
		Assertions.assertThat(1L).isEqualTo(deletedId);
	}
}
