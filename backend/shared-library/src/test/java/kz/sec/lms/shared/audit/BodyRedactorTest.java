package kz.sec.lms.shared.audit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BodyRedactorTest {

    @Test
    void redactsPasswordField() {
        String input  = "{\"username\":\"alice\",\"password\":\"hunter2\"}";
        String result = BodyRedactor.redact(input);
        assertTrue(result.contains("\"password\":\"***\""));
        assertTrue(result.contains("\"username\":\"alice\""));
    }

    @Test
    void redactsNestedSecretField() {
        String input  = "{\"oauth\":{\"client_secret\":\"abc\",\"client_id\":\"x\"}}";
        String result = BodyRedactor.redact(input);
        assertTrue(result.contains("\"client_secret\":\"***\""));
        assertTrue(result.contains("\"client_id\":\"x\""));
    }

    @Test
    void redactsRefreshTokenField() {
        String input  = "{\"refreshToken\":\"eyJhbGc...\",\"foo\":\"bar\"}";
        String result = BodyRedactor.redact(input);
        assertTrue(result.contains("\"refreshToken\":\"***\""));
        assertTrue(result.contains("\"foo\":\"bar\""));
    }

    @Test
    void redactsCardNumberField() {
        String input  = "{\"cardNumber\":\"4242424242424242\"}";
        String result = BodyRedactor.redact(input);
        assertTrue(result.contains("\"cardNumber\":\"***\""));
    }

    @Test
    void passesThroughNonSensitive() {
        String input  = "{\"name\":\"alice\",\"age\":30}";
        String result = BodyRedactor.redact(input);
        assertEquals(input, result);
    }

    @Test
    void handlesNull() {
        assertNull(BodyRedactor.redact(null));
    }

    @Test
    void handlesNonJson() {
        assertEquals("just a string", BodyRedactor.redact("just a string"));
    }
}
