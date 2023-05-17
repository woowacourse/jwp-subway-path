package subway.fixture;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import subway.dao.LineEntity;
import subway.dao.SectionEntity;
import subway.dao.StationEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class EntityFixture {

    public static final StationEntity 후추_Entity = new StationEntity(1L, "후추");
    public static final StationEntity 디노_Entity = new StationEntity(2L, "디노");

    public static final LineEntity 일호선_남색_Entity = new LineEntity(1L, "일호선", "남색");
    public static final LineEntity 이호선_초록색_Entity = new LineEntity(2L, "이호선", "초록색");

    public static final SectionEntity 후추_디노_Entity = new SectionEntity(1L, 후추_Entity.getId(), 디노_Entity.getId(), 7, 일호선_남색_Entity.getId());
}
