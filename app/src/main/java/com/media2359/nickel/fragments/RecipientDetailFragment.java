package com.media2359.nickel.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.activities.MainActivity;
import com.media2359.nickel.adapter.BankAdapter;
import com.media2359.nickel.event.OnRecipientsChangedEvent;
import com.media2359.nickel.managers.CentralDataManager;
import com.media2359.nickel.model.Bank;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.network.NikelService;
import com.media2359.nickel.network.responses.BanksResponse;
import com.media2359.nickel.ui.customview.ProfileField;
import com.media2359.nickel.utils.DisplayUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xijun on 17/3/16.
 */
public class RecipientDetailFragment extends BaseFragment {

    public static final int NO_RECIPIENT = -1;
    private static final String BUNDLE_RECIPIENT = "recipient";
    private Recipient recipient;
    private ProfileField pfFullName, pfDisplayName, pfPhone, pfStreet, pfCity, pfPostalCode, pfBankAccount, pfBankAgain;
    private Button btnSaveChanges;
    private List<Bank> bankList;
    private Spinner spinnerBank;
    private BankAdapter bankAdapter;
    private Bank selectedBank;
    private TextView tvStatus;

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
        bankList = new ArrayList<>();
        prepareBanks();
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
        //pfBankName = (ProfileField) view.findViewById(R.id.pfReciBankName);
        pfBankAccount = (ProfileField) view.findViewById(R.id.pfReciBankAccount);
        pfBankAgain = (ProfileField) view.findViewById(R.id.pfReciBankAccountConfirm);
        btnSaveChanges = (Button) view.findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        tvStatus = (TextView) view.findViewById(R.id.tvProfileStatus);
        tvStatus.setVisibility(View.GONE);

        spinnerBank = (Spinner) view.findViewById(R.id.spinnerBankName);
        bankAdapter = new BankAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, bankList);
        spinnerBank.setAdapter(bankAdapter);
        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBank = bankList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        pfBankAgain.removeDefaultTextWatchers();
        pfBankAgain.addTextWatcher(bankAgainWatcher);
    }

    private void prepareBanks() {

        bankList.clear();

        NikelService.getApiManager().getBanks().enqueue(new Callback<BanksResponse>() {
            @Override
            public void onResponse(Call<BanksResponse> call, Response<BanksResponse> response) {
                if (response.isSuccessful()) {
                    bankList.addAll(response.body().getBankList());
                    bankAdapter.notifyDataSetChanged();
                } else {
                    onBanksFailed();
                }

            }

            @Override
            public void onFailure(Call<BanksResponse> call, Throwable t) {
                onBanksFailed();
            }
        });
    }

    private void onBanksFailed() {
        String error = "Fetching banks failed, please click yes to try again.";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(error);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prepareBanks();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().onBackPressed();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
            if (s.toString().equals(pfBankAccount.getInput())) {
                pfBankAgain.showCompletedStatus(true);
            } else {
                pfBankAgain.showCompletedStatus(false);
                pfBankAgain.showErrorMessage(true, "Bank account entered does not match");
            }
        }
    };

    ProgressDialog progressDialog;

    /**
     * On button save changes click
     * This should save the changes/ new recipient profile to server
     */
    private void saveChanges() {

        DisplayUtils.hideKeyboard(btnSaveChanges);

        if (validateRecipient()) {
            recipient = new Recipient(pfFullName.getInput(), pfDisplayName.getInput(), pfPhone.getInput(), pfStreet.getInput(), pfCity.getInput(), pfPostalCode.getInput(), selectedBank.getName(), selectedBank.getId(), pfBankAccount.getInput());
            progressDialog = ProgressDialog.show(getContext(), "", "Please wait...", true);
            CentralDataManager.getInstance().addNewOrUpdateRecipient(recipient);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(OnRecipientsChangedEvent onRecipientsChangedEvent) {
        progressDialog.dismiss();

        if (onRecipientsChangedEvent.isSuccess()) {
            Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } else {
            Toast.makeText(getActivity(), onRecipientsChangedEvent.getMessage(), Toast.LENGTH_SHORT).show();
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

        if (selectedBank == null) {
            spinnerBank.requestFocus();
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
        if (recipient != null) {
            bindDataToViews();
        }
    }

    private void bindDataToViews() {

        if (recipient.getStatus().equals("Verified")){
            pfFullName.setInputAndLock(recipient.getFullName());
            pfDisplayName.setInputAndLock(recipient.getDisplayName());
            //pfBankName.setInput(recipient.getBankName());

            for (int i = 0; i < bankList.size(); i++) {
                Bank bank = bankList.get(i);
                if (bank.getName().equals(recipient.getBankName())) {
                    spinnerBank.setSelection(i);
                    break;
                }
            }

            spinnerBank.setEnabled(false);

            tvStatus.setText("Recipient status: " + recipient.getStatus());
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_ok, 0, 0, 0);
            tvStatus.setVisibility(View.VISIBLE);

            pfBankAccount.setInputAndLock(recipient.getBankAccount());
            pfStreet.setInputAndLock(recipient.getStreet());
            pfCity.setInputAndLock(recipient.getCity());
            pfPhone.setInputAndLock(recipient.getPhoneNumber());
            pfPostalCode.setInputAndLock(recipient.getPostalCode());
            btnSaveChanges.setVisibility(View.GONE);
            pfBankAgain.setVisibility(View.GONE);

        }else {
            pfFullName.setInput(recipient.getFullName());
            pfDisplayName.setInput(recipient.getDisplayName());
            //pfBankName.setInput(recipient.getBankName());

            for (int i = 0; i < bankList.size(); i++) {
                Bank bank = bankList.get(i);
                if (bank.getName().equals(recipient.getBankName())) {
                    spinnerBank.setSelection(i);
                    break;
                }
            }

            tvStatus.setText("Recipient status: " + recipient.getStatus());
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_alert, 0, 0, 0);
            tvStatus.setVisibility(View.VISIBLE);

            pfBankAccount.setInput(recipient.getBankAccount());
            pfStreet.setInput(recipient.getStreet());
            pfCity.setInput(recipient.getCity());
            pfPhone.setInput(recipient.getPhoneNumber());
            pfPostalCode.setInput(recipient.getPostalCode());
            btnSaveChanges.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.recipient_details);
    }

}
