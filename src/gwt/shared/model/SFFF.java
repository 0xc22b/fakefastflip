package gwt.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SFFF implements Serializable{

	private static final long serialVersionUID = 1L;

	String rss;
	ArrayList<SRule> sRuleList = new ArrayList<SRule>();
	
	public SFFF(){
		
	}

    public SFFF(String rss) {
        this.rss = rss;
    }
    
    public String getRss(){
        return this.rss;
    }

    public void addSRule(SRule sRule){
        sRuleList.add(sRule);
    }
    
    public List<SRule> getSRuleList(){
        return Collections.unmodifiableList(sRuleList);
    }
	
}
