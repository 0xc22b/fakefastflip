package gwt.server;

import static com.google.appengine.api.urlfetch.FetchOptions.Builder.allowTruncate;
import gwt.client.RpcService;
import gwt.server.model.Rule;
import gwt.server.model.RuleGrp;
import gwt.shared.FetchUrlException;
import gwt.shared.FieldVerifier;
import gwt.shared.model.SFFF;
import gwt.shared.model.SRule;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
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
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Rule rule = new Rule(getRuleGrpKey(), sRule.getComm(), sRule.getArg());
        ds.put(rule.getEntity());
        return rule.getKeyString();
    }

    @Override
    public String deleteRule(String keyString) {
        Key key = KeyFactory.stringToKey(keyString);
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.delete(key);
        return keyString;
    }

    private List<Rule> getRuleList(){
        List<Rule> ruleList = new ArrayList<Rule>();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(Rule.class.getSimpleName());
        // Using ancestor query to get strongly consistent results.
        // https://developers.google.com/appengine/docs/java/datastore/overview#Queries_and_Indexes
        query.setAncestor(getRuleGrpKey());
        Iterator<Entity> iterator = ds.prepare(query).asIterator();
        while(iterator.hasNext()){
            Entity entity = iterator.next();
            Rule rule = new Rule(entity);
            ruleList.add(rule);
        }
        return ruleList;
    }
    
    private static Key ruleGrpKey;
    
    private static Key getRuleGrpKey() {
        if (ruleGrpKey == null) {
            DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
            RuleGrp userGrp = new RuleGrp();
            ds.put(userGrp.getEntity());
            ruleGrpKey = userGrp.getEntity().getKey();
        }
        return ruleGrpKey;
    }
}
