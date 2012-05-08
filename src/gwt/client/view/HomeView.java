package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface HomeView extends IsWidget {

    public interface Presenter {
        void rssSubmit(String rss);
        void goToRule();
    }

    Widget asWidget();
    void setPresenter(Presenter presenter);
    
}
