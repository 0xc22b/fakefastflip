package gwt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class RulePlace extends Place {
    
    public static class Tokenizer implements PlaceTokenizer<RulePlace> {

        @Override
        public String getToken(RulePlace place) {
            return place.getToken();
        }

        @Override
        public RulePlace getPlace(String token) {
            return new RulePlace();
        }
    }
    
    private String token;
    
    public RulePlace(){
        this.token = "";
    }
    
    
    public String getToken() {
        return this.token;
    }
}
