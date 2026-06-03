package kz.sec.lms.shared.audit;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Redacts sensitive field values from JSON payloads before they are persisted to the audit log.
 * Operates on raw strings (no JSON parser dependency) to keep this allocation-light.
 */
public final class BodyRedactor {

    private static final Set<String> SENSITIVE_FIELDS = Set.of(
            "password",
            "secret",
            "client_secret",
            "clientSecret",
            "token",
            "refreshToken",
            "refresh_token",
            "accessToken",
            "access_token",
            "cardNumber",
            "card_number"
    );

    private static final Pattern FIELD_PATTERN = Pattern.compile(
            "\"([A-Za-z0-9_]+)\"\\s*:\\s*\"([^\"]*)\""
    );

    private BodyRedactor() {}

    public static String redact(String input) {
        if (input == null || input.isBlank()) return input;
        Matcher matcher = FIELD_PATTERN.matcher(input);
        StringBuffer out = new StringBuffer(input.length());
        while (matcher.find()) {
            String key = matcher.group(1);
            String replacement = isSensitive(key)
                    ? Matcher.quoteReplacement("\"" + key + "\":\"***\"")
                    : Matcher.quoteReplacement(matcher.group());
            matcher.appendReplacement(out, replacement);
        }
        matcher.appendTail(out);
        return out.toString();
    }

    private static boolean isSensitive(String key) {
        for (String s : SENSITIVE_FIELDS) {
            if (s.equalsIgnoreCase(key)) return true;
        }
        return false;
    }
}
