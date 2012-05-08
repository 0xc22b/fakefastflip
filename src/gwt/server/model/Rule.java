package gwt.server.model;

import gwt.shared.model.SRule.Comm;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Rule {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Comm comm;
    
    @Persistent
    private String arg;

    public Rule(Comm comm, String arg) {
        this.comm = comm;
        this.arg = arg;
    }

    public Key getKey() {
        return key;
    }
    
    public String getKeyString(){
        return KeyFactory.keyToString(key);
    }

    public Comm getComm() {
        return comm;
    }

    public String getArg() {
        return arg;
    }

}
