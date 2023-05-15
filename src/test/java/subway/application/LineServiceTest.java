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
import subway.persistence.LineJdbcRepository;
import subway.ui.dto.request.LineRequest;
import subway.ui.dto.response.LineResponse;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

	@Mock
	LineJdbcRepository repository;

	@InjectMocks
	LineService service;

	@DisplayName("노선 생성 서비스 테스트")
	@Test
	void createLine() {
		// given
		given(repository.createLine(any())).willReturn(1L);

		// when
		final LineRequest request = new LineRequest("2호선");
		final LineResponse response = service.createLine(request);

		// then
		Assertions.assertThat(1L).isEqualTo(response.getId());
	}

	@DisplayName("전체 노선 조회 서비스 테스트")
	@Test
	void findAll() {
		// given
		final Line line2 = new Line(1L, "2호선");
		given(repository.findAll()).willReturn(List.of(line2));

		// when
		final List<LineResponse> lines = service.findAll();

		// then
		Assertions.assertThat(lines)
			.usingRecursiveComparison()
			.isEqualTo(List.of(new LineResponse(1L, "2호선")));
	}

	@DisplayName("ID를 사용한 노선 조회 서비스 테스트")
	@Test
	void findById() {
		// given
		final Line line2 = new Line(1L, "2호선");
		given(repository.findById(1L)).willReturn(line2);

		// when
		final LineResponse response = service.findById(1L);

		// then
		Assertions.assertThat(response)
			.hasFieldOrPropertyWithValue("id", 1L)
			.hasFieldOrPropertyWithValue("name", "2호선");
	}

	@DisplayName("노선 갱신 서비스 테스트")
	@Test
	void updateLine() {
		// given
		given(repository.updateLine(anyLong(), any())).willReturn(true);

		// when
		final LineRequest request = new LineRequest("2호선");
		final LineResponse response = service.updateLine(1L, request);

		// then
		Assertions.assertThat(response)
			.hasFieldOrPropertyWithValue("id", 1L)
			.hasFieldOrPropertyWithValue("name", "2호선");
	}

	@DisplayName("노선 삭제 서비스 테스트")
	@Test
	void deleteLine() {
		// given
		given(repository.deleteById(anyLong())).willReturn(true);

		// when
		final long deletedId = service.deleteLine(1L);

		// then
		Assertions.assertThat(1L).isEqualTo(deletedId);
	}
}
