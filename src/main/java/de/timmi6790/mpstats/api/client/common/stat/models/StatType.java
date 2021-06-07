package de.timmi6790.mpstats.api.client.common.stat.models;

public enum StatType {
    NUMBER,
    TIME_IN_SECONDS;

    public static StatType getDefault() {
        return NUMBER;
    }
}
