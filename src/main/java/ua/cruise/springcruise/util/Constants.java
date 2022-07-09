package ua.cruise.springcruise.util;

import lombok.Getter;

@Getter
public final class Constants {
    private Constants() {}

    public static final long USER_DEFAULT_ROLE_ID = 2L;
    public static final long CRUISE_DEFAULT_STATUS_ID = 1L;
    public static final long TICKET_DEFAULT_STATUS_ID = 1L;

    public static final String DATA_PATH = "D:/data/spring/";

    public enum equalitySign {LT,GT, E}

    public enum durationType {DAYS, WEEKS, MONTHS}
}
