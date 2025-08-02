package dev.mkhub.knowledge.common;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

@SpringBootTest
public class MessageTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    @DisplayName("메시지 테스트")
    public void testMessage() {
        String msg = messageSource.getMessage("Duplicate.email", null, Locale.KOREA);
        System.out.println("✅ 메시지: " + msg);
    }
}
