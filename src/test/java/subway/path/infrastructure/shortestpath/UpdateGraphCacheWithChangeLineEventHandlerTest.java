package subway.path.infrastructure.shortestpath;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import subway.line.domain.event.ChangeLineEvent;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("UpdateGraphCacheWithChangeLineEventHandler 은(는)")
@SpringBootTest
class UpdateGraphCacheWithChangeLineEventHandlerTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private DataSourceTransactionManager tx;

    @MockBean
    private UpdateGraphCacheWithChangeLineEventHandler handler;

    @Test
    void 트랜잭션_종료_시_ChangeLineEvent_를_받아_캐시를_업데이트한다() {
        // given
        final TransactionStatus transaction = tx.getTransaction(new DefaultTransactionDefinition());
        publisher.publishEvent(new ChangeLineEvent());
        tx.commit(transaction);

        // when & then
        verify(handler, times(1)).updateCache();
    }
}
