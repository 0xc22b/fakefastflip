package gwt.client.view;

import gwt.shared.model.SRule.Comm;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface RuleView<T> extends IsWidget {

    public interface Presenter {
        void goBack();
        void addRule(Comm comm, String arg);
        void deleteRule(String keyString);
    }

    Widget asWidget();
    void setPresenter(Presenter presenter);
    void setRules(T t);
    void addRule(String keyString, Comm comm, String arg);
    void deleteRule(String keyString);
}
