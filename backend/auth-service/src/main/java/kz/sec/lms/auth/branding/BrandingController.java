package kz.sec.lms.auth.branding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/branding")
public class BrandingController {

    @Value("${brand.name:SEC}")
    private String name;

    @Value("${brand.product-name:SmartEduControl}")
    private String productName;

    @Value("${brand.primary-color:#4f46e5}")
    private String primaryColor;

    @Value("${brand.logo-url:/assets/logo.png}")
    private String logoUrl;

    @Value("${brand.favicon-url:/assets/favicon.ico}")
    private String faviconUrl;

    @Value("${brand.support-email:support@sec.kz}")
    private String supportEmail;

    @GetMapping
    public ResponseEntity<BrandingDTO> get() {
        BrandingDTO body = BrandingDTO.builder()
                .name(name)
                .productName(productName)
                .primaryColor(primaryColor)
                .logoUrl(logoUrl)
                .faviconUrl(faviconUrl)
                .supportEmail(supportEmail)
                .build();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(body);
    }
}
