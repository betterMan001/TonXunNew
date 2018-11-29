package tx.a316.com.tx_teacher.Utils;

import java.util.List;

public class MyEMHelper {
    private static MyEMHelper instance = null;
    private List<DataSyncListener> syncContactsListeners;
    private boolean isContactsSyncedWithServer = false;
    private boolean isSyncingContactsWithServer = false;
    //获得该类的单例对象
    public synchronized static MyEMHelper getInstance() {
        if (instance == null) {
            instance = new MyEMHelper();
        }
        return instance;
    }
    //一个监听器的接口
    public interface DataSyncListener {
        /**
         * sync complete
         * @param success true：data sync successful，false: failed to sync data
         */
        void onSyncComplete(boolean success);
    }
    public void addSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactsListeners.contains(listener)) {
            syncContactsListeners.add(listener);
        }
    }
    public boolean isContactsSyncedWithServer() {
        return isContactsSyncedWithServer;
    }
    public boolean isSyncingContactsWithServer() {
        return isSyncingContactsWithServer;
    }
}
