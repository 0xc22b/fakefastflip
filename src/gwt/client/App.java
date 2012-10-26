package gwt.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.UmbrellaException;

import gwt.client.place.AppPlaceHistoryMapper;
import gwt.client.place.HomePlace;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The heart of the applicaiton, mainly concerned with bootstrapping.
 */
public class App {
    
    private static final Logger log = Logger.getLogger(App.class.getName());

    private final ClientFactory clientFactory;
    private final EventBus eventBus;
    private final PlaceController placeController;
    private final ActivityManager activityManager;
    private final AppPlaceHistoryMapper historyMapper;
    private final PlaceHistoryHandler historyHandler;
    
    private final SimplePanel shell = new SimplePanel();
    private final Place defaultPlace = new HomePlace();

    public App(ClientFactory clientFactory, EventBus eventBus, PlaceController placeController, ActivityManager activityManager,
            AppPlaceHistoryMapper historyMapper, PlaceHistoryHandler historyHandler) {
        this.clientFactory = clientFactory;
        this.eventBus = eventBus;
        this.placeController = placeController;
        this.activityManager = activityManager;
        this.historyMapper = historyMapper;
        this.historyHandler = historyHandler;
    }

    public void run(HasWidgets.ForIsWidget parentView) {
        
        // Check for authentication failures or mismatches
        //reloadOnAuthenticationFailure.register(eventBus);
        
        // Define style in Fakefastflip.css
        shell.getElement().setId("shell");
        
        activityManager.setDisplay(shell);
        parentView.add(shell);

        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                while (e instanceof UmbrellaException) {
                    e = ((UmbrellaException) e).getCauses().iterator().next();
                }

                String message = e.getMessage();
                if (message == null) {
                    message = e.toString();
                }
                log.log(Level.SEVERE, "Uncaught exception", e);
                Window.alert("An unexpected error occurred: " + message);
            }
        });

        Place savedPlace = null;
        /*if (storage != null) {
            try {
                // wrap in try-catch in case stored value is invalid
                savedPlace = historyMapper.getPlace(storage.getItem(HISTORY_SAVE_KEY));
            } catch (Throwable t) {
                // ignore error and use the default-default
            }
        }*/
        if (savedPlace == null) {
            savedPlace = defaultPlace;
        }
        historyHandler.register(placeController, eventBus, savedPlace);
        historyHandler.handleCurrentHistory();
        
        /*
         * Monitor the eventbus for place changes and note them in LocalStorage
         * for the next launch.
         */
        /*if (storage != null) {
            eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
                public void onPlaceChange(PlaceChangeEvent event) {
                    storage.setItem(HISTORY_SAVE_KEY, historyMapper.getToken(event.getNewPlace()));
                }
            });
        }*/

        //preventFrameBusting();
    }
    
    /*
     * Not used!
     */
    private native static void preventFrameBusting()/*-{
        (function(){
            var preventBust = 0;
            var ourTop = 'http://' + $wnd.top.location.hostname;
            if($wnd.top.location.port){
                ourTop = ourTop + ':' + $wnd.top.location.port;
            }
            ourTop = ourTop + '/dono';
            
            setInterval( function() {  
                $wnd.onbeforeunload = function() { 
                    preventBust++;
                };
                
                if (preventBust > 0) {
                    preventBust -= 2;  
                    $wnd.top.location = ourTop;  
                }  
            }, 1)
        }());
    }-*/;
}
