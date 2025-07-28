package dev.mkhub.knowlegde.repository;

import dev.mkhub.knowledge.domain.Member;
import dev.mkhub.knowledge.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //@Test
    void testInsertDummyMembers() {
        Member m1 = new Member("alice", "alice@test.com", passwordEncoder.encode("1234"), "USER");
        Member m2 = new Member("bob", "bob@test.com", passwordEncoder.encode("1234"), "USER");
        Member admin = new Member("admin", "admin@admintest.com", passwordEncoder.encode("1234"), "ADMIN");

        memberRepository.saveAll(List.of(m1, m2, admin));

        System.out.println("✅ Dummy members inserted");
    }
}