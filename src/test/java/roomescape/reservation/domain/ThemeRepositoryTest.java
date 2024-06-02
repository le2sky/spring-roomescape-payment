package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import roomescape.common.RepositoryTest;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.MIA_RESERVATION_TIME;
import static roomescape.TestFixture.USER_MIA;
import static roomescape.TestFixture.WOOTECO_THEME;
import static roomescape.reservation.domain.ReservationStatus.BOOKING;

class ThemeRepositoryTest extends RepositoryTest {
    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("테마를 저장한다.")
    void save() {
        // given
        Theme theme = WOOTECO_THEME();

        // when
        Theme savedTheme = themeRepository.save(theme);

        // then
        assertThat(savedTheme.getId()).isNotNull();
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void findAll() {
        // given
        themeRepository.save(WOOTECO_THEME());

        // when
        List<Theme> themes = themeRepository.findAll();

        // then
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("Id로 테마를 조회한다.")
    void findById() {
        // given
        Long themeId = themeRepository.save(WOOTECO_THEME()).getId();

        // when
        Optional<Theme> foundTheme = themeRepository.findById(themeId);

        // then
        assertThat(foundTheme).isNotEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 Id로 테마를 조회하면 빈 Optional을 반환한다.")
    void findByNotExistingId() {
        // given
        Long notExistingId = 1L;

        // when
        Optional<Theme> foundTheme = themeRepository.findById(notExistingId);

        // then
        assertThat(foundTheme).isEmpty();
    }

    @Test
    @DisplayName("Id로 테마를 삭제한다.")
    void deleteById() {
        // given
        Long themeId = themeRepository.save(WOOTECO_THEME()).getId();

        // when
        themeRepository.deleteById(themeId);

        // then
        Optional<Theme> theme = themeRepository.findById(themeId);
        assertThat(theme).isEmpty();
    }

    @Test
    @DisplayName("최근 일주일을 기준으로 예약이 많은 순으로 테마 10개를 조회한다.")
    void findAllOrderByReservationCountInLastWeek() {
        // given
        Theme secondRankTheme = themeRepository.save(WOOTECO_THEME());
        Theme firstRankTheme = themeRepository.save(WOOTECO_THEME());
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(MIA_RESERVATION_TIME));
        Member member = memberRepository.save(USER_MIA());

        LocalDate today = LocalDate.now();
        reservationRepository.save(
                new Reservation(member, today.minusDays(7), reservationTime, firstRankTheme, BOOKING));
        reservationRepository.save(
                new Reservation(member, today.minusDays(6), reservationTime, firstRankTheme, BOOKING));
        reservationRepository.save(
                new Reservation(member, today.minusDays(1), reservationTime, secondRankTheme, BOOKING));

        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.minusDays(1);

        // when
        List<Theme> allOrderByReservationCountInLastWeek = themeRepository.findAllByDateBetweenOrderByReservationCount(
                startDate, endDate, PageRequest.of(0, 10));

        // then
        assertThat(allOrderByReservationCountInLastWeek).extracting(Theme::getId)
                .containsExactly(firstRankTheme.getId(), secondRankTheme.getId());
    }
}
