package com.media2359.nickel.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.media2359.nickel.event.OnRecipientsChangedEvent;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.network.NikelService;
import com.media2359.nickel.network.RequestHandler;
import com.media2359.nickel.network.responses.BaseResponse;
import com.media2359.nickel.network.responses.RecipientListResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xijun on 23/5/16.
 */
public class CentralDataManager {

    private static final String TAG = "CentralDataManager";

    private static NickelTransfer currentTransaction = null;
    private static int recipientPosition = -1;

    private static CentralDataManager manager;

    //keep a list of recipients, since they are needed almost throughout the entire life of this app
    private List<Recipient> recipientList;

    public static CentralDataManager getInstance() {
        if (manager == null) {
            manager = new CentralDataManager();
        }
        return manager;
    }

    private CentralDataManager() {

        recipientList = new ArrayList<>();
    }

    public void close() {
        Log.d(TAG, "close: ");

        recipientList = null;
        currentTransaction = null;
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


    public List<Recipient> getAllRecipients() {
        return recipientList;
    }

    /**
     * @return whether fetching succeeded
     */
    public boolean fetchRecipientsFromServer() {
        //TODO call api
        Call<List<Recipient>> call = NikelService.getApiManager().getRecipients();
        call.enqueue(new Callback<List<Recipient>>() {
            @Override
            public void onResponse(Call<List<Recipient>> call, Response<List<Recipient>> response) {
                if (response.isSuccessful()) {
                    recipientList.clear();
                    recipientList.addAll(response.body());
                    EventBus.getDefault().post(new OnRecipientsChangedEvent(true, response.message()));
                } else {
                    EventBus.getDefault().post(new OnRecipientsChangedEvent(false, RequestHandler.convert400Response(response)));
                }
            }

            @Override
            public void onFailure(Call<List<Recipient>> call, Throwable t) {
                EventBus.getDefault().post(new OnRecipientsChangedEvent(false, t.getLocalizedMessage()));
            }
        });


        return true;
    }

    public Recipient getRecipientAtPosition(int position) {
        if (checkIfOutOfBound(position)) {
            return null;
        }

        return recipientList.get(position);
    }

    public void updateTransactionForRecipient(@Nullable final NickelTransfer transaction, @NonNull final Recipient recipient) {


    }

    public boolean deleteRecipientAtPosition(final int position) {
        if (checkIfOutOfBound(position))
            return false;

        Call<BaseResponse> call = NikelService.getApiManager().deleteRecipient(recipientList.get(position).getRecipientId());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()){
                    recipientList.remove(position);
                    EventBus.getDefault().post(new OnRecipientsChangedEvent(true, response.message()));
                }else {
                    EventBus.getDefault().post(new OnRecipientsChangedEvent(false, RequestHandler.convert400Response(response)));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                EventBus.getDefault().post(new OnRecipientsChangedEvent(false, t.getLocalizedMessage()));
            }
        });

        return true;
    }

    public boolean addNewOrUpdateRecipient(final Recipient recipient) {
        if (recipient == null) {
            Log.d(TAG, "addNewRecipient: recipient is null");
            return false;
        }

        Call<Recipient> call;

        if (recipient.getRecipientId() == null) {
            call = NikelService.getApiManager().addRecipient(recipient);
            call.enqueue(new Callback<Recipient>() {
                @Override
                public void onResponse(Call<Recipient> call, Response<Recipient> response) {
                    if (response.isSuccessful()) {
                        getAllRecipients().add(response.body());
                        EventBus.getDefault().post(new OnRecipientsChangedEvent(true, response.message()));
                    } else {
                        EventBus.getDefault().post(new OnRecipientsChangedEvent(false, RequestHandler.convert400Response(response)));
                    }
                }

                @Override
                public void onFailure(Call<Recipient> call, Throwable t) {
                    EventBus.getDefault().post(new OnRecipientsChangedEvent(false, t.getLocalizedMessage()));
                }
            });
        } else {
            call = NikelService.getApiManager().updateRecipient(recipient.getRecipientId(), recipient);
            call.enqueue(new Callback<Recipient>() {
                @Override
                public void onResponse(Call<Recipient> call, Response<Recipient> response) {
                    if (response.isSuccessful()) {
                        // delete old recipient
                        for (Recipient oldRecipient: getAllRecipients()){
                            if (oldRecipient.getRecipientId().equals(recipient.getRecipientId())){
                                getAllRecipients().remove(oldRecipient);
                                break;
                            }
                        }
                        // add new recipient
                        getAllRecipients().add(response.body());
                        EventBus.getDefault().post(new OnRecipientsChangedEvent(true, response.message()));
                    }else {
                        EventBus.getDefault().post(new OnRecipientsChangedEvent(false, RequestHandler.convert400Response(response)));
                    }
                }

                @Override
                public void onFailure(Call<Recipient> call, Throwable t) {
                    EventBus.getDefault().post(new OnRecipientsChangedEvent(false, t.getLocalizedMessage()));
                }
            });
        }

        return true;
    }


    private boolean checkIfOutOfBound(int position) {
        return (position < 0 || position >= recipientList.size());
    }

}
