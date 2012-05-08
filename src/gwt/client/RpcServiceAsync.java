package gwt.client;

import gwt.shared.model.SFFF;
import gwt.shared.model.SRule;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface RpcServiceAsync {
    
    void feedRss(String rssUrl, AsyncCallback<SFFF> callback );

    void getSRuleList(AsyncCallback<SFFF> callback);
    void addRule(SRule sRule, AsyncCallback<String> callback);
    void deleteRule(String keyString, AsyncCallback<String> callback);
}
