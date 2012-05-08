package gwt.client.view.desktop;

import gwt.client.view.HomeView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HomeViewImpl extends Composite implements HomeView {

    @UiTemplate("HomeViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, HomeViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    public interface Style extends CssResource {
        
    }

    @UiField
    Style style;
    @UiField
    TextBox rssTB;
    @UiField
    Label errRssLb;

    private Presenter presenter;
    
    
    public HomeViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        
        rssTB.getElement().setAttribute("placeholder", "Enter RSS URL here.");
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("rssBtn")
    void onRssBtnClicked(ClickEvent event){
        if(!validateInputs()){
            return;
        }
        
        presenter.rssSubmit(rssTB.getValue());
    }
    
    @UiHandler("ruleBtn")
    void onRuleBtnClicked(ClickEvent event){
        presenter.goToRule();
    }
    
    private boolean validateInputs(){
        boolean isValid = true;
        
        if(rssTB.getValue().isEmpty()){
            errRssLb.setText("Required value is missing!");
            isValid = false;
        }else{
            errRssLb.setText("");
        }
        
        return isValid;
    }
}
