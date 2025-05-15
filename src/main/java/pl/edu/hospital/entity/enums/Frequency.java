package pl.edu.hospital.entity.enums;

public enum Frequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    NEEDED("When needed");
    private final String label;

    Frequency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
