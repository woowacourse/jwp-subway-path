package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dto.request.LineCreateRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.LinesResponse;
import subway.entity.LineEntity;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static subway.fixture.LineEntityFixture.createLineEntity;
import static subway.fixture.LineEntityFixture.createLineEntity2;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

	@InjectMocks
	private LineService lineService;

	@Mock
	private LineRepository lineRepository;


	@Test
	@DisplayName("노선 생성 서비스 테스트")
	void createLine() {
		// given
		LineCreateRequest request = new LineCreateRequest("잠실역");

		// when
		lineService.saveLine(request);

		// then
		verify(lineRepository).insertLine(any(LineEntity.class));
	}

	@Test
	@DisplayName("전체 노선 조회 서비스 테스트")
	void findAllLines() {
		// given
		List<LineEntity> lineEntities = List.of(createLineEntity(), createLineEntity2());
		when(lineRepository.findAll()).thenReturn(lineEntities);

		// when
		LinesResponse result = lineService.findAll();

		// then
		assertAll(
			() -> assertThat(result.getLines().size()).isEqualTo(2),
			() -> assertThat(result.getLines().get(0).getName()).isEqualTo("2호선"),
			() -> assertThat(result.getLines().get(1).getName()).isEqualTo("8호선")
		);
	}

	@Test
	@DisplayName("노선 생신 서비스 테스트")
	void updateLine() {
		// given
		Long id = 1L;
		LineUpdateRequest updateRequest = new LineUpdateRequest("2호선");

		LineEntity lineEntity = createLineEntity();
		given(lineRepository.findById(id)).willReturn(Optional.of(lineEntity));

		// when
		lineService.updateLineById(id, updateRequest);

		// then
		assertThat(lineEntity.getName()).isEqualTo(updateRequest.getName());
	}

	@Test
	@DisplayName("존재하지 않는 노선을 요청하면 예외가 발생한다")
	void exception_whenLineNotFound() {
		// given
		Long id = 1L;

		// then
		assertThatThrownBy(() -> lineService.updateLineById(id, any()))
			.isInstanceOf(LineNotFoundException.class);
	}

	@Test
	@DisplayName("노선 삭제 서비스 테스트")
	void deleteLine() {
		// given
		Long id = 1L;

		// when
		lineService.deleteLineById(id);

		// then
		verify(lineRepository).deleteLineById(id);
	}
}
