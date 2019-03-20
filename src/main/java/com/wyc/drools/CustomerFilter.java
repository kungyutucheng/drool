package com.wyc.drools;

import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

/**
 * @author: wyc
 * @date: 2019/3/20
 */
public class CustomerFilter implements AgendaFilter {
    @Override
    public boolean accept(Match match) {
        Rule rule = match.getRule();
        return rule.getName().startsWith("customer");
    }
}
