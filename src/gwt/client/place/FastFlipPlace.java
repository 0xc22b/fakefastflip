package gwt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class FastFlipPlace extends Place {

    public static class Tokenizer implements PlaceTokenizer<FastFlipPlace> {

        @Override
        public String getToken(FastFlipPlace place) {
            return place.getToken();
        }

        @Override
        public FastFlipPlace getPlace(String token) {
            return new FastFlipPlace(token);
        }
    }
    
    private String token;

    public FastFlipPlace(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

}
