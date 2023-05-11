package subway.application.section;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.persistence.dao.SectionDao;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class SectionServiceTest {

    @Autowired
    SectionService sectionService;

    @Autowired
    SectionDao sectionDao;

    @Test
    void 새로운_섹션을_저장한다() {

    }

}
