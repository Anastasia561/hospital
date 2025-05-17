package pl.edu.hospital.entity.enums;

public enum WorkingDay {
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6);
    private final int mysqlDay;

    WorkingDay(int mysqlDay) {
        this.mysqlDay = mysqlDay;
    }

    public int getMysqlDay() {
        return mysqlDay;
    }
}
