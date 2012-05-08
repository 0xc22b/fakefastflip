package gwt.client;

import gwt.shared.FetchUrlException;
import gwt.shared.model.SFFF;
import gwt.shared.model.SRule;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("rpc")
public interface RpcService extends RemoteService {
    SFFF feedRss(String rssUrl) throws FetchUrlException;
    
    SFFF getSRuleList();
    String addRule(SRule sRule);
    String deleteRule(String keyString);
}
