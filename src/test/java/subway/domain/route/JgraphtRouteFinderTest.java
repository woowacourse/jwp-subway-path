package subway.domain.route;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import subway.event.StationChange;

@SpringBootTest
class JgraphtRouteFinderTest {

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @MockBean
  private JgraphtRouteFinder jgraphtRouteFinder;

  @Test
  @DisplayName("init() : 애플리케이션 컨테이너가 뜰 때와 역이 변경될 경우 이벤트가 실행될 수 있다.")
  void test_init_eventListener() throws Exception {
    //given
    applicationEventPublisher.publishEvent(new StationChange());

    //when & then
    verify(jgraphtRouteFinder, times(2)).updateDijkstraGraph();
  }
}
