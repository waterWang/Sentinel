package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway.api;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author water
 * @desc 从nacos拉取流控规则
 */
@Component("gatewayApiNacosProvider")
public class GatewayApiNacosProvider implements DynamicRuleProvider<List<ApiDefinitionEntity>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApiNacosProvider.class);

  @Autowired
  private ConfigService configService;
  @Autowired
  private Converter<String, List<ApiDefinitionEntity>> converter;

  @Override
  public List<ApiDefinitionEntity> getRules(String appName) throws Exception {
    String rules = configService.getConfig(appName + NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX,
        NacosConfigUtil.GROUP_ID, 3000);
    LOGGER.info("get gateway api rules from nacos, rules: {}", rules);
    if (StringUtil.isEmpty(rules)) {
      return new ArrayList<>();
    }
    return converter.convert(rules);
  }
}
