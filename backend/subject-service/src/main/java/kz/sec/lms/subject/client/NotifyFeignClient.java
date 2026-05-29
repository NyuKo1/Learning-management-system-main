package kz.sec.lms.subject.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notify-service")
public interface NotifyFeignClient {
    @PostMapping("/notify/send")
    Map<String, Object> sendNotification(@RequestBody Map<String, Object> request);
}
