package gwt.client;

import gwt.client.activity.AppActivityMapper;
import gwt.client.def.FFFDefImpl;
import gwt.client.place.AppPlaceHistoryMapper;
import gwt.client.view.FastFlipView;
import gwt.client.view.HomeView;
import gwt.client.view.RuleView;
import gwt.client.view.desktop.FastFlipViewImpl;
import gwt.client.view.desktop.HomeViewImpl;
import gwt.client.view.desktop.RuleViewImpl;
import gwt.shared.model.SFFF;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ClientFactoryImpl implements ClientFactory {

    private final EventBus eventBus = new SimpleEventBus();
    
    private final PlaceController placeController = new PlaceController(eventBus);
    private final AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
    private final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
    
    private ActivityMapper activityMapper = new AppActivityMapper(this);
    private ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
    
    private final RpcServiceAsync rpcService = GWT.create(RpcService.class);

    private HomeView homeView;
    private FastFlipView fastFlipView;
    private RuleView<SFFF> ruleView;
    
    @Override
    public App getApp() {
        return new App(this,
                       getEventBus(), 
                       getPlaceController(), 
                       activityManager, 
                       historyMapper, 
                       historyHandler);
    }

    @Override
    public PlaceController getPlaceController() {
        return placeController;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public RpcServiceAsync getRpcService() {
        return rpcService;
    }

    @Override
    public HomeView getHomeView() {
        if (homeView == null) {
            homeView = new HomeViewImpl();
        }
        return homeView;
    }

    @Override
    public FastFlipView getFastFlipView() {
        if (fastFlipView == null) {
            fastFlipView = new FastFlipViewImpl();
        }
        return fastFlipView;
    }

    @Override
    public RuleView<SFFF> getRuleView() {
        if (ruleView == null) {
            ruleView = new RuleViewImpl<SFFF>(FFFDefImpl.getInstance());
        }
        return ruleView;
    }

}
