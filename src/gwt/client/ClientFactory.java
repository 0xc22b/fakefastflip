package gwt.client;

import gwt.client.view.FastFlipView;
import gwt.client.view.HomeView;
import gwt.client.view.RuleView;
import gwt.shared.model.SFFF;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {
    App getApp();
	
    EventBus getEventBus();
	PlaceController getPlaceController();
	RpcServiceAsync getRpcService();
	
	HomeView getHomeView();
	FastFlipView getFastFlipView();
	RuleView<SFFF> getRuleView();
}
