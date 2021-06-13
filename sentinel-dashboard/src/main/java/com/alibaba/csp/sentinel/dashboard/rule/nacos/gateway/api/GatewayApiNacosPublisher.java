package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway.api;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
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
 * @desc 将sentinel 控制台的配置推到nacos
 */
@Component("gatewayApiNacosPublisher")
public class GatewayApiNacosPublisher implements DynamicRulePublisher<List<ApiDefinitionEntity>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApiNacosPublisher.class);

  @Autowired
  private ConfigService configService;

  @Autowired
  private Converter<List<ApiDefinitionEntity>, String> converter;

  @Override
  public void publish(String app, List<ApiDefinitionEntity> rules) throws Exception {
    AssertUtil.notEmpty(app, "app name cannot be empty");
    if (rules == null) {
      return;
    }
    String convertedRule = converter.convert(rules);
    LOGGER.info("sentinel dashboard publish gateway api rules: {}", convertedRule);
    configService.publishConfig(app + NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX,
        NacosConfigUtil.GROUP_ID, convertedRule);
  }
}
