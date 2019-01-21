package loop.view.historylistview;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.ListView;
import loop.controller.ResultHistoryItem;

public class RefreshSkin<ResultHistoryItem> extends ListViewSkin<ResultHistoryItem> {
    public RefreshSkin(ListView<ResultHistoryItem> listView) {
        super(listView);
    }

    public void refresh() {
        super.flow.recreateCells();
    }
}
