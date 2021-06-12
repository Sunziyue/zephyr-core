package xyz.szy.zephyr.core.admin.controller;

import xyz.szy.zephyr.core.api.service.ProcessOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SunZiYue
 * @date 2020/09/14 17:45
 * @apiNote 定时任务测试控制器
 */
@Slf4j
@RestController("test")
@Api(tags = "定时任务测试控制器")
public class TestScheduleController {
    private final ProcessOrderService processOrderService;

    @Autowired
    public TestScheduleController(ProcessOrderService processOrderService) {
        this.processOrderService = processOrderService;
    }

    /**
     * @author: SunZiYue
     * @param subnetId 数据库节点
     * @return 状态
     */
    @ApiOperation("处理订单缓存")
    @GetMapping("/process")
    public void process(String subnetId){
        this.processOrderService.processOrderBySubnetId(subnetId);
    }
}
