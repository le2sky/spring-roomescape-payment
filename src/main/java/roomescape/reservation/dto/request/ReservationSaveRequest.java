package roomescape.reservation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationSaveRequest(
        @NotNull(message = "예약 날짜는 비어있을 수 없습니다.")
        LocalDate date,
        @NotNull(message = "예약 시간 Id는 비어있을 수 없습니다.")
        Long timeId,
        @NotNull(message = "테마 Id는 비어있을 수 없습니다.")
        Long themeId) {
}
