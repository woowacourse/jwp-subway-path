package subway.application;

import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.persistence.LineJdbcRepository;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

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

	@Test
	void findAll() {
	}

	@Test
	void findById() {
	}

	@Test
	void updateLine() {
	}

	@Test
	void deleteLine() {
	}
}
