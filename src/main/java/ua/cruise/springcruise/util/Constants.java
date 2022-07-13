package ua.cruise.springcruise.util;

import lombok.Getter;

/**
 * Class than contains constants to prevent hard-code of variables
 * @author Vladyslav Kucher
 * @version 1.1
 */

@Getter
public final class Constants {
    private Constants() {}

    public static final long USER_DEFAULT_ROLE_ID = 2L;
    public static final long CRUISE_DEFAULT_STATUS_ID = 1L;
    public static final long TICKET_DEFAULT_STATUS_ID = 1L;
    public static final long TICKET_PAYED_STATUS = 3L;
    public static final long TICKET_CANCELED_STATUS = 5L;
    public static final long CRUISE_ACTUAL_STATUS_MORE_THAN = 1L;
    public static final long CRUISE_ACTUAL_STATUS = 2L;
    public static final long CRUISE_FULL_STATUS = 3L;
    public static final long CRUISE_OUTDATED_STATUS_ID = 7;
    public static final long TICKET_ACTUAL_STATUS_LESS_THAN = 4L;
    public static final long CRUISE_STARTED_STATUS_ID = 4L;
    public static final long CRUISE_ENDED_STATUS_ID = 5L;
    public static final long TICKET_OUTDATED_STATUS_ID = 5L;

    public static final String CRUISE_AUTOUPDATE_DELAY = "0 0 * * * *";
    public static final String TICKET_AUTOUPDATE_DELAY = "0 30 * * * *";

    public static final String DATA_PATH = "D:/data/spring/";

    public enum equalitySign {LT,GT, E}

    public enum durationType {DAYS, WEEKS, MONTHS}
}
