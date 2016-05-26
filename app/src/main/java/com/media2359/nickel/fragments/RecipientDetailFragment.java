package com.media2359.nickel.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.activities.MainActivity;
import com.media2359.nickel.managers.CentralDataManager;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.ui.customview.ProfileField;

/**
 * Created by Xijun on 17/3/16.
 */
public class RecipientDetailFragment extends BaseFragment {

    public static final int NO_RECIPIENT = -1;
    private static final String BUNDLE_RECIPIENT = "recipient";
    private Recipient recipient;
    private ProfileField pfFullName, pfDisplayName, pfPhone, pfStreet, pfCity, pfPostalCode, pfBankName, pfBankAccount, pfBankAgain;
    private Button btnSaveChanges;

    public static RecipientDetailFragment newInstance(int recipientPosition) {
        RecipientDetailFragment instance = new RecipientDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_RECIPIENT, recipientPosition);
        instance.setArguments(bundle);

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipient_detail, container, false);
        Bundle bundle = this.getArguments();
        recipient = null;
        if (bundle != null) {
            int position = bundle.getInt(BUNDLE_RECIPIENT);
            if (position >= 0) {
                recipient = CentralDataManager.getInstance().getRecipientAtPosition(bundle.getInt(BUNDLE_RECIPIENT));
            }
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

        pfBankAgain.removeDefaultTextWatchers();
        pfBankAgain.addTextWatcher(bankAgainWatcher);
    }

    private TextWatcher bankAgainWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals(pfBankAccount.getInput())){
                pfBankAgain.showCompletedStatus(true);
            }else {
                pfBankAgain.showCompletedStatus(false);
                pfBankAgain.showErrorMessage(true,"Bank account entered does not match");
            }
        }
    };

    /**
     * On button save changes click
     * This should save the changes/ new recipient profile to both server and local disk
     */
    private void saveChanges() {

        if (validateRecipient()) {
            int ID;
            if (recipient !=null){
                ID = recipient.getID();
            }else {
                ID = CentralDataManager.getInstance().getNextValidRecipientID();
            }
            
            recipient = new Recipient(ID,pfDisplayName.getInput(), pfFullName.getInput(), pfPhone.getInput(), pfStreet.getInput(), pfCity.getInput(), pfPostalCode.getInput(), pfBankName.getInput(), pfBankAccount.getInput());

            //TODO save to server
            CentralDataManager.getInstance().addNewOrUpdateRecipient(recipient);
            Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }

    }

    private boolean validateRecipient() {

        if (!pfDisplayName.validateInput()) {
            pfDisplayName.requestFocus();
            return false;
        }

        if (!pfFullName.validateInput()) {
            pfFullName.requestFocus();
            return false;
        }

        if (!pfPhone.validateInput()) {
            pfPhone.requestFocus();
            return false;
        }

        if (!pfStreet.validateInput()) {
            pfStreet.requestFocus();
            return false;
        }

        if (!pfCity.validateInput()) {
            pfCity.requestFocus();
            return false;
        }

        if (!pfPostalCode.validateInput()) {
            pfPostalCode.requestFocus();
            return false;
        }

        if (!pfBankName.validateInput()) {
            pfBankName.requestFocus();
            return false;
        }

        if (!pfBankAccount.validateInput()) {
            pfBankAccount.requestFocus();
            return false;
        }

        if (!pfBankAgain.validateInput() || !pfBankAccount.getInput().equals(pfBankAgain.getInput())) {
            pfBankAgain.requestFocus();
            return false;
        }

        return true;
    }

    private void fillInTheData() {
        if (recipient == null) {
            addNewRecipient();
        } else {
            retrieveFromServer();
        }
    }

    /**
     * This will show the screen which allow user to add new recipient
     */
    private void addNewRecipient() {

    }

    private void retrieveFromServer() {
        // TODO load data from server

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
