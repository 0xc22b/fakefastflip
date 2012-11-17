package gwt.server.model;

import com.google.appengine.api.datastore.Entity;

public class RuleGrp {
    
    private static final String RULE_GRP_KEY_NAME = "ruleGrpKeyName";
    
    private Entity entity;
    
    public RuleGrp() {
        entity = new Entity(RuleGrp.class.getSimpleName(), RULE_GRP_KEY_NAME);
    }
    
    public Entity getEntity() {
        return entity;
    }
}
