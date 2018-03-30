package com.dotcms.osgi.characteristics;

import com.dotmarketing.portlets.rules.business.FiredRule;
import com.dotmarketing.portlets.rules.business.FiredRulesList;
import com.dotmarketing.util.WebKeys;

import java.util.stream.Collectors;

public class RulesEngineCharacter extends AbstractCharacter {



    public RulesEngineCharacter(AbstractCharacter incomingCharacter) {
        super(incomingCharacter);

        FiredRulesList firedRulesList = request.getAttribute(WebKeys.RULES_ENGINE_FIRE_LIST) == null ? new FiredRulesList()
                : (FiredRulesList) request.getAttribute(WebKeys.RULES_ENGINE_FIRE_LIST);
        String rulesRequest =
                String.join(" ", firedRulesList.values().stream().map(FiredRule::getRuleID).collect(Collectors.toList()));

        firedRulesList = request.getSession().getAttribute(WebKeys.RULES_ENGINE_FIRE_LIST) == null ? new FiredRulesList()
                : (FiredRulesList) request.getSession().getAttribute(WebKeys.RULES_ENGINE_FIRE_LIST);
        String rulesSession =
                String.join(" ", firedRulesList.values().stream().map(FiredRule::getRuleID).collect(Collectors.toList()));


        getMap().put("rulesRequest", rulesRequest);
        getMap().put("rulesSession", rulesSession);

    }

}
