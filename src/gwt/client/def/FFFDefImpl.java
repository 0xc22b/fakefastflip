package gwt.client.def;

import gwt.shared.model.SFFF;
import gwt.shared.model.SRule.Comm;

public class FFFDefImpl extends FFFDef<SFFF> {

    private static FFFDefImpl instance = null;

    public static FFFDefImpl getInstance() {
        if (instance == null) {
            instance = new FFFDefImpl();
        }
        return instance;
    }

    @Override
    public String getRss(SFFF t) {
        return t.getRss();
    }
    
    @Override
    public int getRListSize(SFFF t) {
        return t.getSRuleList().size();
    }

    @Override
    public String getRKeyString(SFFF t, int i) {
        return t.getSRuleList().get(i).getKeyString();
    }

    @Override
    public Comm getRComm(SFFF t, int i) {
        return t.getSRuleList().get(i).getComm();
    }

    @Override
    public String getRArg(SFFF t, int i) {
        return t.getSRuleList().get(i).getArg();
    }
}
