package com.media2359.nickel.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.media2359.nickel.event.OnRecipientsChangedEvent;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.model.Recipient;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Xijun on 23/5/16.
 */
public class CentralDataManager implements RealmChangeListener<RealmResults<Recipient>> {

    private static final String TAG = "CentralDataManager";
    private Realm realm;
    private static NickelTransfer currentTransaction = null;
    private static int recipientPosition = -1;

    private static CentralDataManager manager;

    //keep a list of recipients, since they are needed almost throughout the entire life of this app
    private RealmResults<Recipient> recipientList;

    public static CentralDataManager getInstance() {
        if (manager == null) {
            manager = new CentralDataManager();
        }
        return manager;
    }

    private CentralDataManager() {
        realm = Realm.getDefaultInstance();
        recipientList = realm.where(Recipient.class).findAllAsync();
        recipientList.addChangeListener(this);
    }

    public void close() {
        Log.d(TAG, "close: ");
        recipientList.removeChangeListeners();
        recipientList = null;
        currentTransaction = null;
        realm.close();
        manager = null;
    }


    public static void setCurrentTransaction(NickelTransfer currentTransaction, int recipientPosition) {
        CentralDataManager.currentTransaction = currentTransaction;
        CentralDataManager.recipientPosition = recipientPosition;
    }

    public static NickelTransfer getCurrentTransaction() {
        return currentTransaction;
    }

    public void saveCurrentTransactionToRecipient() {

        Recipient recipient = getRecipientAtPosition(recipientPosition);
        if (recipient == null)
            return;

        updateTransactionForRecipient(currentTransaction, recipient);
    }


    public List<Recipient> mockRecipients() {

        List<Recipient> mockList = new ArrayList<>();
        Recipient a = new Recipient(0, "Husband", "BRI 281973021894", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient b = new Recipient(1, "Mother", "BRI 0123874123", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient c = new Recipient(2, "Sister", "MYI 9012830912", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient d = new Recipient(3, "Han", "SGW 0911298301", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        a.setExpanded(false);
        b.setExpanded(false);
        c.setExpanded(false);
        d.setExpanded(false);

        NickelTransfer aadd = new NickelTransfer("1238u9ashjd", "March 2, 2016", "500.00", "Funds Ready for Collection", NickelTransfer.TRANS_NEW_BORN, "Husband", 1235, "QWE 2132113");
        a.setCurrentTransaction(aadd);
        mockList.add(a);
        mockList.add(b);
        mockList.add(c);
        mockList.add(d);

        return mockList;
    }

    public List<Recipient> getAllRecipients() {
        return recipientList;
    }

    /**
     * @return whether fetching succeeded
     */
    public boolean fetchRecipientsFromServer() {
        //TODO call api
        final List<Recipient> apiResult = mockRecipients();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Recipient recipient : apiResult) {
                    realm.copyToRealmOrUpdate(recipient);
                }
            }
        });
        return true;
    }

    public int getNextValidRecipientID() {
        return recipientList.size();
    }

    public Recipient getRecipientAtPosition(int position) {
        if (checkIfOutOfBound(position)) {
            return null;
        }

        return recipientList.get(position);
    }

    public void updateTransactionForRecipient(@Nullable final NickelTransfer transaction, @NonNull final Recipient recipient) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NickelTransfer object = realm.copyToRealmOrUpdate(transaction);
                recipient.setCurrentTransaction(object);
                realm.copyToRealmOrUpdate(recipient);
            }
        });
    }

    public boolean deleteRecipientAtPosition(final int position) {
        if (checkIfOutOfBound(position))
            return false;
        
        //TODO call api to delete recipient
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                recipientList.deleteFromRealm(position);
            }
        });

        return true;
    }

    public boolean addNewOrUpdateRecipient(final Recipient recipient) {
        if (recipient == null) {
            Log.d(TAG, "addNewRecipient: recipient is null");
            return false;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(recipient);
            }
        });

        return true;
    }

    private boolean checkIfOutOfBound(int position) {
        return (position < 0 || position >= recipientList.size());
    }

    @Override
    public void onChange(RealmResults<Recipient> element) {

        // TODO handle api callback
        EventBus.getDefault().post(new OnRecipientsChangedEvent(true, "Ok"));
    }
}
