package subway.domain.policy.discount;

public enum AgeGroup {

    BABY {
        @Override
        int calculate(int fare) {
            return 0;
        }

        @Override
        AgeGroup checkAgeGroup(int age) {
            if (age < 6) {
                return BABY;
            }
            return CHILD.checkAgeGroup(age);
        }
    },
    CHILD {
        @Override
        int calculate(int fare) {
            return (int) Math.ceil((fare - 350) * 0.5);
        }

        @Override
        AgeGroup checkAgeGroup(int age) {
            if (6 <= age && age < 13) {
                return CHILD;
            }
            return TEEN.checkAgeGroup(age);
        }
    },
    TEEN {
        @Override
        int calculate(int fare) {
            return (int) Math.ceil((fare - 350) * 0.8);
        }

        @Override
        AgeGroup checkAgeGroup(int age) {
            if (13 <= age && age < 19) {
                return TEEN;
            }
            return ADULT.checkAgeGroup(age);
        }
    },
    ADULT {
        @Override
        int calculate(int fare) {
            return fare;
        }

        @Override
        AgeGroup checkAgeGroup(int age) {
            return ADULT;
        }
    };

    public static AgeGroup of(int age) {
        validateRange(age);
        return BABY.checkAgeGroup(age);
    }

    private static void validateRange(int age) {
        if (age < 0) {
            throw new IllegalArgumentException(
                    String.format("[ERROR] 나이는 음수가 될 수 없습니다. (입력값 : %d)", age)
            );
        }
    }

    abstract int calculate(int fare);

    abstract AgeGroup checkAgeGroup(int age);
}
