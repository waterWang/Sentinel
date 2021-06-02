package com.alibaba.csp.sentinel.dashboard.rule.nacos.authrules;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
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
 * @desc 将sentinel 控制台的授权规则推到nacos
 */
@Component("authRuleNacosPublisher")
public class AuthRuleNacosPublisher implements DynamicRulePublisher<List<AuthorityRuleEntity>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthRuleNacosPublisher.class);

  @Autowired
  private ConfigService configService;

  @Autowired
  private Converter<List<AuthorityRuleEntity>, String> converter;

  @Override
  public void publish(String app, List<AuthorityRuleEntity> rules) throws Exception {
    AssertUtil.notEmpty(app, "app name cannot be empty");
    if (rules == null) {
      return;
    }
    String convertedRule = converter.convert(rules);
    LOGGER.info("sentinel dashboard publisher auth rules : {}", convertedRule);
    configService.publishConfig(app + NacosConfigUtil.AUTH_DATA_ID_POSTFIX,
        NacosConfigUtil.GROUP_ID, convertedRule);
  }
}
