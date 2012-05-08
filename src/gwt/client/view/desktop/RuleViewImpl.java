package gwt.client.view.desktop;

import gwt.client.def.FFFDef;
import gwt.client.view.RuleView;
import gwt.shared.model.SRule.Comm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RuleViewImpl<T> extends Composite implements RuleView<T> {

    private class Item extends Composite {
        
        private String keyString;
        private Button delBtn;
        
        public Item(String keyString, String commName, String arg){
            
            this.keyString = keyString;
            
            FlowPanel panel = new FlowPanel();
            panel.addStyleName(style.item());
            
            Label label = new Label(commName + ": " + arg);
            label.addStyleName(style.rule());
            panel.add(label);
            
            delBtn = new Button("delete");
            delBtn.addStyleName(style.delBtn());
            delBtn.addClickHandler(delBtnClickHandler);
            panel.add(delBtn);
            
            initWidget(panel);
        }
        
        public String getKeyString(){
            return this.keyString;
        }
        
        private ClickHandler delBtnClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean confirm = Window.confirm("Are you sure you want to delete this?");
                if(confirm){
                    delBtn.setEnabled(false);
                    presenter.deleteRule(keyString);
                }
            }
        };
    }
    
    @SuppressWarnings("rawtypes")
    @UiTemplate("RuleViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, RuleViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    public interface Style extends CssResource {
        String item();
        String rule();
        String delBtn();
    }

    @UiField
    Style style;
    @UiField
    ListBox commLB;
    @UiField
    TextBox argTB;
    @UiField
    Button addRuleBtn;
    @UiField
    FlowPanel rulePanel;
    @UiField
    Label errAddRuleLb;
    
    private Presenter presenter;
    private FFFDef<T> fFFDef;
    
    public RuleViewImpl(FFFDef<T> fFFDef) {
        this.fFFDef = fFFDef;
        initWidget(uiBinder.createAndBindUi(this));
        
        for(Comm comm : Comm.values()){
            commLB.addItem(comm.name(), comm.name());
        }
        
        argTB.getElement().setAttribute("placeholder", "Enter website's name.");
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setRules(T t) {
        
        // Initialize
        commLB.setSelectedIndex(0);
        argTB.setText("");
        rulePanel.clear();
        
        // Set rules
        for(int i = 0; i < fFFDef.getRListSize(t); i++){
            rulePanel.add(new Item(fFFDef.getRKeyString(t, i), fFFDef.getRComm(t, i).name(), fFFDef.getRArg(t, i)));
        }
    }
    
    @Override
    public void addRule(String keyString, Comm comm, String arg) {
        rulePanel.insert(new Item(keyString, comm.name(), arg), 0);
        
        setEnabledInputs(true);
        argTB.setText("");
    }

    @Override
    public void deleteRule(String keyString) {
        for(int i = 0; i < rulePanel.getWidgetCount(); i++){
            @SuppressWarnings("unchecked")
            Item item = (Item) rulePanel.getWidget(i);
            if(item.getKeyString().equals(keyString)){
                item.removeFromParent();
            }
        }
    }

    @UiHandler("addRuleBtn")
    void onAddRuleBtnClicked(ClickEvent event) {
        if(!validateInputs()){
            return;
        }
        
        setEnabledInputs(false);
        presenter.addRule(getCommLBValue(), argTB.getValue());
    }
    
    @UiHandler("backBtn")
    void onBackBtnClicked(ClickEvent event) {
        presenter.goBack();
    }
    
    private boolean validateInputs(){
        boolean isValid = true;
        
        if(getCommLBValue().equals(Comm.NoAllowedInIFrame) && argTB.getValue().isEmpty()){
            errAddRuleLb.setText("Required value is missing!");
            isValid = false;
        }
        
        if(isValid){
            errAddRuleLb.setText("");
        }
        
        return isValid;
    }
    
    private void setEnabledInputs(boolean enabled){
        commLB.setEnabled(enabled);
        argTB.setEnabled(enabled);
        addRuleBtn.setEnabled(enabled);
    }
    
    private Comm getCommLBValue(){
        return Comm.valueOf(commLB.getValue(commLB.getSelectedIndex()));
    }
}
