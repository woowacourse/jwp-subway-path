package subway.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;

import subway.exception.BlankNameException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class NameTest {

	@ParameterizedTest
	@EmptySource
	@DisplayName("이름이 공백이면 예외를 발생한다.")
	void exception_whenBlankName(final String input) {
		// then
		assertThatThrownBy(() -> new Name(input))
			.isInstanceOf(BlankNameException.class);
	}

	@Test
	@DisplayName("이름을 생성한다.")
	void createName() {
		// given
		String givenName = "2호선";

		// when
		Name name = new Name(givenName);

		// then
		assertThat(name.getName()).isEqualTo(givenName);
	}
}
