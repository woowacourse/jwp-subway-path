package subway.fixture;

import subway.domain.vo.Distance;
import subway.domain.vo.Section;
import subway.domain.vo.Station;


@SuppressWarnings("all")
public class Fixture {
    public static final Station SINLIM = new Station(1l, "신림");
    public static final Station BONGCHUN = new Station(2l, "봉천");
    public static final Station SEOUL = new Station(3l, "서울");
    public static final Station NAKSUNG = new Station(4l, "낙성");
    public static final Station SADANG = new Station(5l, "사당");
    public static final Station NODLE = new Station(6l, "노들");
    public static final Station EESU = new Station(7l, "이수");
    public static final Station KM_9 = new Station(8l, "9키로");
    public static final Station KM_10 = new Station(9l, "10키로");
    public static final Station KM_11 = new Station(10l, "11키로");
    public static final Station KM_16 = new Station(11l, "16키로");
    public static final Station KM_58 = new Station(12l, "58키로");

    public static final Section SILIM_BONCHUN = new Section(2l, SINLIM, BONGCHUN, new Distance(2));
    public static final Section BONCHUN_SEOUL = new Section(2l, BONGCHUN, SEOUL, new Distance(2));
    public static final Section SEOUL_NAKSUNG = new Section(2l, SEOUL, NAKSUNG, new Distance(2));
    public static final Section NAKSUNG_SADANG = new Section(2l, NAKSUNG, SADANG, new Distance(2));
    public static final Section SILIM_NODLE = new Section(1l, SINLIM, NODLE, new Distance(5));
    public static final Section NODLE_EESU = new Section(1l, NODLE, EESU, new Distance(5));
    public static final Section EESU_SADANG = new Section(1l, EESU, SADANG, new Distance(1));
    public static final Section SILIM_KM_9 = new Section(4l, SINLIM, KM_9, new Distance(9));
    public static final Section SILIM_KM_10 = new Section(5l, SINLIM, KM_10, new Distance(10));
    public static final Section SILIM_KM_11 = new Section(6l, SINLIM, KM_11, new Distance(11));
    public static final Section SILIM_KM_16 = new Section(7l, SINLIM, KM_16, new Distance(16));
    public static final Section SILIM_KM_58 = new Section(8l, SINLIM, KM_58, new Distance(58));

}
