package com.alibaba.csp.sentinel.dashboard.rule.nacos.degraderules;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
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
 * @desc 将sentinel 控制台的降级规则配置推到nacos
 */
@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DegradeRuleNacosPublisher.class);

  @Autowired
  private ConfigService configService;

  @Autowired
  private Converter<List<DegradeRuleEntity>, String> converter;

  @Override
  public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {
    AssertUtil.notEmpty(app, "app name cannot be empty");
    if (rules == null) {
      return;
    }
    String convertedRule = converter.convert(rules);
    LOGGER.info("sentinel dashboard publish degrade rules: {}", convertedRule);
    configService.publishConfig(app + NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX,
        NacosConfigUtil.GROUP_ID, convertedRule);
  }
}
