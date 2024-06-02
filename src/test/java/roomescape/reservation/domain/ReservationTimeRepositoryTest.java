package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.RepositoryTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.MIA_RESERVATION_TIME;

class ReservationTimeRepositoryTest extends RepositoryTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 시간을 저장한다.")
    void save() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        // when
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        // when
        assertThat(savedReservationTime.getId()).isNotNull();
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void findAll() {
        // given
        reservationTimeRepository.save(new ReservationTime(MIA_RESERVATION_TIME));

        // when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        // then
        assertThat(reservationTimes).hasSize(1);
    }

    @Test
    @DisplayName("Id로 예약 시간을 조회한다.")
    void findById() {
        // given
        Long id = reservationTimeRepository.save(new ReservationTime(MIA_RESERVATION_TIME)).getId();

        // when
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(id);

        // then
        assertThat(reservationTime).isPresent();
    }

    @Test
    @DisplayName("Id에 해당하는 예약 시간이 없다면 빈 Optional을 반환한다.")
    void findByNotExistingId() {
        // given
        Long id = 1L;

        // when
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(id);

        // then
        assertThat(reservationTime).isEmpty();
    }

    @Test
    @DisplayName("Id로 예약 시간을 삭제한다.")
    void deleteById() {
        // given
        Long id = reservationTimeRepository.save(new ReservationTime(MIA_RESERVATION_TIME)).getId();

        // when
        reservationTimeRepository.deleteById(id);

        // then
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(id);
        assertThat(reservationTime).isEmpty();
    }
}
