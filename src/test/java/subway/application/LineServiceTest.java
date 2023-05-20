package subway.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.request.LineCreateRequest;
import subway.dto.response.LineResponse;

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
		assertThat(lines).hasSize(1);
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

	@DisplayName("노선의 이름을 공백으로 입력했을 떄 예외가 발생한다")
	@ParameterizedTest()
	@ValueSource(strings = {"", " "})
	void nameNotBlank(String name) {
		// given
		final LineCreateRequest newRequest = new LineCreateRequest(name);

		// then
		assertThatThrownBy(() -> lineService.createLine(newRequest)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("노선의 이름은 공백일 수 없습니다");
	}

	@DisplayName("노선의 이름이 2글자 미만 10글자 초과로 입력됐을 때 예외가 발생한다")
	@ParameterizedTest()
	@ValueSource(strings = {"호", "가나다라마바사아자차카"})
	void nameNotBetweenTwo_Ten(String name) {
		// given
		final LineCreateRequest newRequest = new LineCreateRequest(name);

		// then
		assertThatThrownBy(() -> lineService.createLine(newRequest)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("노선의 이름은 최소 2글자 이상 최대 10글자 이하로 등록해주세요");
	}

	@DisplayName("존재하지 않는 노선 삭제를 요청할 경우 예외가 발생한다")
	@Test
	void unableToDeleteNone() {
		// given
		final long expectedId = 5L;

		// when
		assertThatThrownBy(() -> lineService.deleteLine(expectedId)).isInstanceOf(NullPointerException.class)
			.hasMessage("존재하지 않는 노선입니다");
	}
}
