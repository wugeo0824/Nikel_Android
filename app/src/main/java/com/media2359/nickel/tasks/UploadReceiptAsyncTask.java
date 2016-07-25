package com.media2359.nickel.tasks;

import android.os.AsyncTask;

import com.media2359.nickel.event.OnReceiptUploadedEvent;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.network.NikelService;
import com.media2359.nickel.network.RequestHandler;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xijun on 20/7/16.
 */
public class UploadReceiptAsyncTask extends AsyncTask<NickelTransfer, Void, Void> {

    @Override
    protected Void doInBackground(NickelTransfer... params) {
        final NickelTransfer transfer = params[0];

        final File receipt = new File(transfer.getReceiptFilePath());

        // create RequestBody instance from file
        RequestBody requestbody = RequestBody.create(MediaType.parse("image/jpeg"), receipt);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part receiptPart =
                MultipartBody.Part.createFormData("receipt", receipt.getName(), requestbody);

        NikelService.getApiManager().uploadReceipt(transfer.getTransactionID(), receiptPart).enqueue(new Callback<NickelTransfer>() {
            @Override
            public void onResponse(Call<NickelTransfer> call, Response<NickelTransfer> response) {
                OnReceiptUploadedEvent event = new OnReceiptUploadedEvent();
                if (response.isSuccessful()){
                    event.setNickelTransfer(response.body());
                    event.setSuccess(true);
                    event.setMessage(response.message());
                }else {
                    event.setSuccess(false);
                    event.setMessage(RequestHandler.convert400Response(response));
                }
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure(Call<NickelTransfer> call, Throwable t) {
                OnReceiptUploadedEvent event = new OnReceiptUploadedEvent();
                event.setSuccess(false);
                event.setMessage(t.getLocalizedMessage());
                EventBus.getDefault().post(event);
            }
        });

        return null;
    }
}
