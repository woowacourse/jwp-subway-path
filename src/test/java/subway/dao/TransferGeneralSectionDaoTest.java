package subway.dao;

import static fixtures.path.PathTransferSectionFixtures.DUMMY_TRANSFER_SECTION_LINE_7_TO_3_E;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.section.transfer.TransferSection;

@JdbcTest
@Sql({"/test-schema.sql", "/test-path-data.sql"})
class TransferGeneralSectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TransferSectionDao transferSectionDao;

    @BeforeEach
    void setUp() {
        this.transferSectionDao = new TransferSectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("환승 구간을 저장한다.")
    void insert() {
        // given
        TransferSection dummyTransferSectionToInsert = DUMMY_TRANSFER_SECTION_LINE_7_TO_3_E.DUMMY;

        // when
        TransferSection insertedTransferSection = transferSectionDao.insert(dummyTransferSectionToInsert);

        // then
        assertThat(insertedTransferSection).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(dummyTransferSectionToInsert);
    }
}
