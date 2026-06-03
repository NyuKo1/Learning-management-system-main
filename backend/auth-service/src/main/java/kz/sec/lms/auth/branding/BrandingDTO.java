package kz.sec.lms.auth.branding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandingDTO {
    private String name;
    private String productName;
    private String primaryColor;
    private String logoUrl;
    private String faviconUrl;
    private String supportEmail;
}
