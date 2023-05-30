package subway.fixtures.domain;

import subway.domain.section.Section;

import static subway.fixtures.domain.DistanceFixture.FIVE_DISTANCE;
import static subway.fixtures.domain.DistanceFixture.TEN_DISTANCE;
import static subway.fixtures.domain.StationFixture.DMC;
import static subway.fixtures.domain.StationFixture.HONDDAE_DOWN;
import static subway.fixtures.domain.StationFixture.HONGDAE;
import static subway.fixtures.domain.StationFixture.JAMSIL;
import static subway.fixtures.domain.StationFixture.JAMSIL_UP;
import static subway.fixtures.domain.StationFixture.SADANG;
import static subway.fixtures.domain.StationFixture.SADANG_DOWN;
import static subway.fixtures.domain.StationFixture.SADANG_MID;
import static subway.fixtures.domain.StationFixture.SEOLLEUNG;
import static subway.fixtures.domain.StationFixture.SEOLLEUNG_DOWN;
import static subway.fixtures.domain.StationFixture.SEOLLEUNG_MID;

public class SectionFixture {

    // 위치가 없는 역을 가지는 구간
    public static final Section JAMSIL_SEOLLEUNG_10 = Section.of(JAMSIL, SEOLLEUNG, TEN_DISTANCE);
    public static final Section SEOLLEUNG_SADANG_10 = Section.of(SEOLLEUNG, SADANG, TEN_DISTANCE);
    public static final Section SADANG_HONGDAE_10 = Section.of(SADANG, HONGDAE, TEN_DISTANCE);
    public static final Section HONGDAE_DMC_10 = Section.of(HONGDAE, DMC, TEN_DISTANCE);
    public static final Section JAMSIL_SADANG_5 = Section.of(JAMSIL, SADANG, FIVE_DISTANCE);

    // 위치가 있는 역을 가지는 구간
    public static final Section JAMSIL_UP_SEOLLEUNG_MID_10 = Section.of(JAMSIL_UP, SEOLLEUNG_MID, TEN_DISTANCE);
    public static final Section JAMSIL_UP_SEOLLEUNG_DOWN_10 = Section.of(JAMSIL_UP, SEOLLEUNG_DOWN, TEN_DISTANCE);
    public static final Section SEOLLEUNG_MID_SADANG_MID_10 = Section.of(SEOLLEUNG_MID, SADANG_MID, TEN_DISTANCE);
    public static final Section SEOLLEUNG_MID_SADANG_DOWN_10 = Section.of(SEOLLEUNG_MID, SADANG_DOWN, TEN_DISTANCE);
    public static final Section SADANG_MID_HONGDAE_DOWN_10 = Section.of(SADANG_MID, HONDDAE_DOWN, TEN_DISTANCE);
    public static final Section JAMSIL_UP_SADANG_MID_5 = Section.of(JAMSIL_UP, SADANG_MID, FIVE_DISTANCE);
    public static final Section SADAND_MID_SEOLLEUNG_DOWN_5 = Section.of(SADANG_MID, SEOLLEUNG_DOWN, FIVE_DISTANCE);
    public static final Section JAMSIL_UP_SADANG_MID_10 = Section.of(JAMSIL_UP, SADANG_MID, TEN_DISTANCE);
    public static final Section SADANG_MID_SEOLLEUNG_DOWN_10 = Section.of(SADANG_MID, SEOLLEUNG_DOWN, TEN_DISTANCE);
    public static final Section SADANG_MID_SEOLLEUNG_DOWN_5 = Section.of(SADANG_MID, SEOLLEUNG_DOWN, FIVE_DISTANCE);

}
