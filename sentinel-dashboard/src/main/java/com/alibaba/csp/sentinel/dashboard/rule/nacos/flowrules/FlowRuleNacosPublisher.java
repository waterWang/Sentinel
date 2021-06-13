package com.alibaba.csp.sentinel.dashboard.rule.nacos.flowrules;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author water
 * @desc 将sentinel 控制台的流控配置推到nacos
 */
@Component("flowRuleNacosPublisher")
public class FlowRuleNacosPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FlowRuleNacosPublisher.class);
  @Autowired
  private ConfigService configService;
  @Autowired
  private Converter<List<FlowRuleEntity>, String> converter;

  @Override
  public void publish(String app, List<FlowRuleEntity> rules) throws Exception {

    LOGGER.info("input app is {}  rules: {}", app, rules);
    AssertUtil.notEmpty(app, "app name cannot be empty");
    if (rules == null) {
      return;
    }
    String dataId = app + NacosConfigUtil.FLOW_DATA_ID_POSTFIX;
    String group = NacosConfigUtil.GROUP_ID;
    String content = converter.convert(rules);
    LOGGER.info("set  rules: {},dataId is {}, groupId is {}, content is {}", rules, dataId,
        group, content);
    configService.publishConfig(dataId, group, content);
  }
}
