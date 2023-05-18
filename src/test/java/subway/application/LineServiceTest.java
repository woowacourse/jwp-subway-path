package subway.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import subway.persistence.LineJdbcRepository;
import subway.ui.dto.request.LineCreateRequest;
import subway.ui.dto.response.LineResponse;

@SpringBootTest
@Transactional
class LineServiceTest {

	@Autowired
	LineService lineService;

	String lineName;
	LineCreateRequest request;
	LineResponse response;

	@BeforeEach
	void setUp() {
		lineName = "2호선";
		request = new LineCreateRequest(lineName);
		response = lineService.createLine(request);
	}

	@DisplayName("노선 생성 서비스 테스트")
	@Test
	void createLine() {
		// then
		assertThat(response.getName()).isEqualTo("2호선");
	}

	@DisplayName("전체 노선 조회 서비스 테스트")
	@Test
	void findAll() {
		// when
		final List<LineResponse> lines = lineService.findAll();

		// then
		assertThat(lines)
			.usingRecursiveComparison()
			.isEqualTo(List.of(new LineResponse(1L, lineName)));
	}

	@DisplayName("ID를 사용한 노선 조회 서비스 테스트")
	@Test
	void findById() {
		// when
		final LineResponse foundLine = lineService.findById(response.getId());

		// then
		assertThat(foundLine.getName()).isEqualTo(lineName);
	}

	@DisplayName("노선 갱신 서비스 테스트")
	@Test
	void updateLine() {
		// when
		final long lineId = response.getId();
		final String newLineName = "3호선";
		final LineCreateRequest newRequest = new LineCreateRequest(newLineName);
		final LineResponse updatedLine = lineService.updateLine(lineId, newRequest);

		// then
		assertThat(updatedLine.getName()).isEqualTo(newLineName);
	}

	@DisplayName("노선 삭제 서비스 테스트")
	@Test
	void deleteLine() {
		// given
		final long lineId = response.getId();

		// when
		final long deletedId = lineService.deleteLine(lineId);

		// then
		assertThat(deletedId).isEqualTo(lineId);
	}

	@DisplayName("같은 이름의 노선을 생성하면 예외가 발생한다")
	@Test
	void exceptionWhenDuplicateName() {
		// given
		final String newLineName = "2호선";
		final LineCreateRequest newRequest = new LineCreateRequest(newLineName);

		// then
		assertThatThrownBy(() -> lineService.createLine(newRequest)).isInstanceOf(
			IllegalArgumentException.class).hasMessage("이미 존재하는 노선입니다");
	}
}
