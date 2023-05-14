package subway.application;


import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.persistence.StationRepositoryImpl;
import subway.ui.dto.StationCreateRequest;
import subway.ui.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

	@Mock
	StationRepositoryImpl repository;

	@InjectMocks
	StationService service;

	@DisplayName("역 생성 서비스 테스트")
	@Test
	void createStation() {
		// given
		given(repository.createStation(any())).willReturn(1L);

		// when
		final StationCreateRequest request = new StationCreateRequest("잠실");
		final StationResponse response = service.createStation(request);

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
	void updateStation() {
	}

	@Test
	void deleteById() {
	}
}
