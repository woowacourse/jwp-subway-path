package subway.domain.policy.discount;

public enum AgeGroup {

    BABY {
        @Override
        int calculate(int fare) {
            return 0;
        }
    },
    CHILD {
        @Override
        int calculate(int fare) {
            return (int) Math.ceil((fare - 350) * 0.5);
        }
    },
    TEEN {
        @Override
        int calculate(int fare) {
            return (int) Math.ceil((fare - 350) * 0.8);
        }
    },
    ADULT {
        @Override
        int calculate(int fare) {
            return fare;
        }
    };

    public static AgeGroup of(int age) {
        validateRange(age);
        if (age < 6) {
            return BABY;
        }
        if (6 <= age && age < 13) {
            return CHILD;
        }
        if (13 <= age && age < 19) {
            return TEEN;
        }
        return ADULT;
    }

    private static void validateRange(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("[ERROR] 나이는 음수가 될 수 없습니다.");
        }
    }

    abstract int calculate(int fare);
}
