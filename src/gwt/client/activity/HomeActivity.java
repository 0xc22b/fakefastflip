package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.place.FastFlipPlace;
import gwt.client.place.HomePlace;
import gwt.client.place.RulePlace;
import gwt.client.view.HomeView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class HomeActivity extends AbstractActivity implements HomeView.Presenter {

    private ClientFactory clientFactory;

    public HomeActivity(HomePlace homePlace, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        Window.scrollTo(0, 0);
        
        clientFactory.getHomeView().setPresenter(this);

        panel.setWidget(clientFactory.getHomeView().asWidget());
    }

    @Override
    public String mayStop() {
        return null;
    }

    @Override
    public void rssSubmit(String rss) {
        clientFactory.getPlaceController().goTo(new FastFlipPlace(rss));
    }

    @Override
    public void goToRule() {
        clientFactory.getPlaceController().goTo(new RulePlace());
    }
}
