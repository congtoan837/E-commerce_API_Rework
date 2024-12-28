package com.poly.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduledService {
    OrderService orderService;

//    @Scheduled(fixedRate = 3600000) // Chạy mỗi giờ
    public void cancelUnpaidOrdersTask() {
        orderService.cancelUnpaidOrders();
    }
}
