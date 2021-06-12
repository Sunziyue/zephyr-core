package xyz.szy.zephyr.core.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.szy.zephyr.core.api.service.ProcessOrderService;

/**
 * @author Sunziyue
 * @date 2020/09/14 18:03
 * @apiNote 处理订单总入口
 */
@Slf4j
@Service
public class ProcessOrderServiceImpl implements ProcessOrderService {
    @Override
    public void processOrderBySubnetId(String subnetId) {
        System.out.println(subnetId);
    }
}
