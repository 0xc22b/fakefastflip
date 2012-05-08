package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.place.FastFlipPlace;
import gwt.client.place.HomePlace;
import gwt.client.place.RulePlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	/**
	 * AppActivityMapper associates each Place with its corresponding
	 * {@link Activity}
	 * 
	 * @param clientFactory Factory to be passed to activities
	 */
	public AppActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	/**
	 * Map each Place to its corresponding Activity. This would be a great use
	 * for GIN.
	 */
	@Override
	public Activity getActivity(final Place place) {
		
	    if (place instanceof HomePlace){
	        return new HomeActivity((HomePlace) place, clientFactory);
	    }else if (place instanceof FastFlipPlace){
            return new FastFlipActivity((FastFlipPlace) place, clientFactory);
	    }else if (place instanceof RulePlace){
            return new RuleActivity((RulePlace) place, clientFactory);
        } 
		return null;
	}

}
