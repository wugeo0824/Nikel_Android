package com.media2359.nickel.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.activities.MainActivity;
import com.media2359.nickel.ui.customview.ProfileField;

import org.parceler.Parcels;

/**
 * Created by Xijun on 17/3/16.
 */
public class RecipientDetailFragment extends BaseFragment {

    private static final String BUNDLE_RECIPIENT = "recipient";
    private Recipient recipient;

    private ProfileField pfFullName, pfDisplayName, pfPhone, pfStreet, pfCity, pfPostalCode, pfBankName, pfBankAccount, pfBankAgain;
    private Button btnSaveChanges;

    public static RecipientDetailFragment newInstance(Recipient recipient){
        RecipientDetailFragment instance = new RecipientDetailFragment();
        if (recipient != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_RECIPIENT, Parcels.wrap(recipient));
            instance.setArguments(bundle);
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipient_detail, container, false);
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            recipient = Parcels.unwrap(bundle.getParcelable(BUNDLE_RECIPIENT));
        }else{
            recipient = null;
        }
        initViews(view);
        fillInTheData();
        ((MainActivity) getActivity()).setDrawerState(false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setDrawerState(true);
    }

    private void initViews(View view) {
        pfFullName = (ProfileField) view.findViewById(R.id.pfReciName);
        pfDisplayName = (ProfileField) view.findViewById(R.id.pfReciDisplayName);
        pfPhone = (ProfileField) view.findViewById(R.id.pfReciPhone);
        pfStreet = (ProfileField) view.findViewById(R.id.pfReciStreet);
        pfCity = (ProfileField) view.findViewById(R.id.pfReciCity);
        pfPostalCode = (ProfileField) view.findViewById(R.id.pfReciPostal);
        pfBankName = (ProfileField) view.findViewById(R.id.pfReciBankName);
        pfBankAccount = (ProfileField) view.findViewById(R.id.pfReciBankAccount);
        pfBankAgain = (ProfileField) view.findViewById(R.id.pfReciBankAccountConfirm);
        btnSaveChanges = (Button) view.findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

    }

    private void saveChanges() {
        //TODO
        Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    private void fillInTheData() {
        if (recipient == null){
            addNewRecipient();
        }else{
            retrieveFromServer();
        }
    }

    private void addNewRecipient() {
    }

    private void retrieveFromServer() {
        pfFullName.setInput(recipient.getName());
        pfDisplayName.setInput(recipient.getName());
        pfBankName.setInput(recipient.getBankName());
        pfBankAccount.setInput(recipient.getBankAccount());
        pfStreet.setInput(recipient.getStreet());
        pfCity.setInput(recipient.getCity());
        pfPhone.setInput(recipient.getPhoneNumber());
        pfPostalCode.setInput(recipient.getPostalCode());
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.my_recipients);
    }

}
