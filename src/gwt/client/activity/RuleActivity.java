package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.place.HomePlace;
import gwt.client.place.RulePlace;
import gwt.client.view.RuleView;
import gwt.shared.model.SFFF;
import gwt.shared.model.SRule;
import gwt.shared.model.SRule.Comm;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class RuleActivity extends AbstractActivity implements RuleView.Presenter {

    private ClientFactory clientFactory;

    public RuleActivity(RulePlace rulePlace, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        Window.scrollTo(0, 0);
        
        clientFactory.getRuleView().setPresenter(this);

        panel.setWidget(clientFactory.getRuleView().asWidget());
        
        getSRuleList();
    }

    @Override
    public String mayStop() {
        return null;
    }
    
    @Override
    public void goBack() {
        clientFactory.getPlaceController().goTo(new HomePlace());
    }
    
    @Override
    public void addRule(Comm comm, String arg) {
        
        if(comm.equals(Comm.NoAllowedInIFrame)){
            arg = getWebsiteName(arg);
        }
        
        final SRule sRule = new SRule(null, comm, arg);
        
        clientFactory.getRpcService().addRule(sRule, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            
            @Override
            public void onSuccess(String result) {
                clientFactory.getRuleView().addRule(result, sRule.getComm(), sRule.getArg());
            }
        });
    }

    @Override
    public void deleteRule(final String keyString) {
        clientFactory.getRpcService().deleteRule(keyString, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            
            @Override
            public void onSuccess(String result) {
                clientFactory.getRuleView().deleteRule(keyString);
            }
        });
    }

    private void getSRuleList(){
        clientFactory.getRpcService().getSRuleList(new AsyncCallback<SFFF>() {
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage() + caught.getCause());
                clientFactory.getPlaceController().goTo(new HomePlace());
            }
            
            @Override
            public void onSuccess(SFFF result) {
                clientFactory.getRuleView().setRules(result);
            }
        });
    }
    
    private String getWebsiteName(String url){
        String name = url;
        if(name.contains("https")){
            name = name.substring(8);
        }
        if(name.contains("http")){
            name = name.substring(7);
        }
        if(name.contains("www")){
            name = name.substring(4);
        }
        int index = name.indexOf("/");
        if(index!=-1){
            name = name.substring(0, index);
        }
        return name;
    }
}
