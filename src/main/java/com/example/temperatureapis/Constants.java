package com.example.temperatureapis;

public class Constants {
    private Constants() {}

    // profiles
    public static final String H2_PROFILE = "h2Repo";
    public static final String IN_MEMORY_PROFILE = "inMemoryRepo";
    public static final String DEV = "dev";

    // endpoints
    public static final String TEMPERATURE_CONTROLLER = "/temperature";
    public static final String TEMPERATURE_HOURLY_ENDPOINT = "/hourly/{fromEpoch}/{tillEpoch}";
    public static final String TEMPERATURE_DAILY_ENDPOINT = "/daily/{fromEpoch}/{tillEpoch}";
}
