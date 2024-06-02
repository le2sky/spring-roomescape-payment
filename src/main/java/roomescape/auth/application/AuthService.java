package roomescape.auth.application;

import org.springframework.stereotype.Service;
import roomescape.auth.exception.AuthorizationException;
import roomescape.member.domain.Email;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(String email, String password) {
        Member member = findMemberByEmail(new Email(email));
        if (!member.hasSamePassword(password)) {
            throw new AuthorizationException("사용자의 비밀번호와 입력 비밀번호가 일치하지 않습니다.");
        }
        String payload = member.getEmail().getValue();
        return jwtTokenProvider.createToken(payload);
    }

    public Member extractMember(String token) {
        jwtTokenProvider.validateToken(token);
        String email = jwtTokenProvider.getPayload(token);
        return findMemberByEmail(new Email(email));
    }

    private Member findMemberByEmail(Email email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("해당 이메일의 사용자가 없습니다."));
    }
}
