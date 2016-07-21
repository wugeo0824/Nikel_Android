package com.media2359.nickel.network.responses;

import com.media2359.nickel.model.Recipient;

import java.util.List;

/**
 * Created by Xijun on 20/7/16.
 */
public class RecipientListResponse extends BaseResponse{

    List<Recipient> recipientList;

    public List<Recipient> getRecipientList() {
        return recipientList;
    }
}
