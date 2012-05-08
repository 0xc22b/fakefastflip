package gwt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class HomePlace extends Place {
    
    public static class Tokenizer implements PlaceTokenizer<HomePlace> {

        @Override
        public String getToken(HomePlace place) {
            return place.getToken();
        }

        @Override
        public HomePlace getPlace(String token) {
            return new HomePlace();
        }
    }
    
    private String token;
    
    public HomePlace(){
        this.token = "";
    }
    
    
    public String getToken() {
        return this.token;
    }
}
