package gwt.server.model;

import gwt.shared.model.SRule.Comm;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Rule {

    public static final String COMM = "comm";
    public static final String ARG = "arg";
    
    private Entity entity;

    public Rule(Key ruleGrpKey, Comm comm, String arg) {
        entity = new Entity(Rule.class.getSimpleName(), ruleGrpKey);
        entity.setProperty(COMM, comm.name());
        entity.setProperty(ARG, arg);
    }
    
    public Rule(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }

    public Key getKey() {
        return entity.getKey();
    }
    
    public String getKeyString(){
        return KeyFactory.keyToString(entity.getKey());
    }

    public Comm getComm() {
        return Comm.valueOf((String)entity.getProperty(COMM));
    }

    public String getArg() {
        return (String)entity.getProperty(ARG);
    }
}
