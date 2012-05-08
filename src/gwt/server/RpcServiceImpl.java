package gwt.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import gwt.client.RpcService;
import gwt.server.model.Rule;
import gwt.shared.FetchUrlException;
import gwt.shared.FieldVerifier;
import gwt.shared.model.SFFF;
import gwt.shared.model.SRule;

import static com.google.appengine.api.urlfetch.FetchOptions.Builder.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.ResponseTooLargeException;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {

    public SFFF feedRss(String rssUrl) throws FetchUrlException {
        
        // Verify that the input is valid. 
        if (!FieldVerifier.isValidUrl(rssUrl)) {
            throw new IllegalArgumentException("rssUrl is invalid");
        }

        HTTPResponse res = null;
        try {
            URL url = new URL(rssUrl);
            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, allowTruncate().followRedirects().setDeadline(60.0));
            res = URLFetchServiceFactory.getURLFetchService().fetch(req);
          
        } catch (MalformedURLException e) {
            throw new FetchUrlException("MalformedURLException: " + e.getMessage(), e.getCause());
        } catch (ResponseTooLargeException e) {
            throw new FetchUrlException("ResponseTooLargeException: " + e.getMessage(), e.getCause());
        } catch (SocketTimeoutException e) {
            throw new FetchUrlException("SocketTimeoutException: " + e.getMessage(), e.getCause());
        } catch (IOException e) {
            throw new FetchUrlException("IOException: " + e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new FetchUrlException(e.getClass().getName() + ": " + e.getMessage(), e.getCause());
        }
        
        SFFF sFFF = new SFFF(new String(res.getContent()));
        
        List<Rule> ruleList = getRuleList();
        for(Rule rule : ruleList){
            SRule sRule = new SRule(rule.getKeyString(), rule.getComm(), rule.getArg());
            sFFF.addSRule(sRule);
        }
        
        return sFFF;
    }

    @Override
    public SFFF getSRuleList() {
        SFFF sFFF = new SFFF(null);
        List<Rule> ruleList = getRuleList();
        for(Rule rule : ruleList){
            SRule sRule = new SRule(rule.getKeyString(), rule.getComm(), rule.getArg());
            sFFF.addSRule(sRule);
        }
        return sFFF;
    }

    @Override
    public String addRule(SRule sRule) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Rule rule = new Rule(sRule.getComm(), sRule.getArg());
        pm.makePersistent(rule);
        return rule.getKeyString();
    }

    @Override
    public String deleteRule(String keyString) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        //try{
        Key key = KeyFactory.stringToKey(keyString);
        Rule rule = pm.getObjectById(Rule.class, key);
        pm.deletePersistent(rule);
        return rule.getKeyString();
        /*} catch(IllegalArgumentException e){
            
        } catch(JDOObjectNotFoundException e) {
            
        }*/
    }

    private List<Rule> getRuleList(){
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Rule.class);
        try {
            @SuppressWarnings("unchecked")
            List<Rule> ruleList = (List<Rule>)query.execute();
            return ruleList;
        } finally {
            query.closeAll();
        }
    }
}
