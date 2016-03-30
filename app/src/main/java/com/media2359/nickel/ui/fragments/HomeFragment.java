package com.media2359.nickel.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.event.OnRecipientEditClickEvent;
import com.media2359.nickel.event.OnSendMoneyClickEvent;
import com.media2359.nickel.model.DummyRecipient;
import com.media2359.nickel.ui.MainActivity;
import com.media2359.nickel.ui.adapter.RecipientAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xijun on 10/3/16.
 */
public class HomeFragment extends BaseFragment {

    private MainActivity mainActivity;
    private RecyclerView rv;
    private RecipientAdapter recipientAdapter;
    private TextView tvExchangeRate, tvFees, tvTotal, tvMyName, tvMyInfo, tvAddRecipient, tvGetAmount;
    private EditText etSendAmount;
    private ImageButton btnMyInfoEdit;
    private List<DummyRecipient> dataList = new ArrayList<>();
    private float exchangeRate = 9679.13f; // 1SGD = [exchangeRate] IDR
    private float getAmount = 0, fee = 7f, totalAmount = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (getActivity() instanceof MainActivity)
            mainActivity = (MainActivity) getActivity();
        initViews(view);
        getRecipients();
        return view;
    }

    private void initViews(View view) {
        rv = (RecyclerView) view.findViewById(R.id.rvRecipients);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        recipientAdapter = new RecipientAdapter(getActivity(), dataList);
        rv.setAdapter(recipientAdapter);

        tvExchangeRate = (TextView) view.findViewById(R.id.tvExchangeRate);
        //TODO: set the exchange rate
        tvFees = (TextView) view.findViewById(R.id.tvFeesAmount);
        tvTotal = (TextView) view.findViewById(R.id.tvTotalAmount);
        etSendAmount = (EditText) view.findViewById(R.id.etSendAmount);
        tvGetAmount = (TextView) view.findViewById(R.id.tvGetAmount);
        btnMyInfoEdit = (ImageButton) view.findViewById(R.id.btnMyInfoEdit);
        tvMyInfo = (TextView) view.findViewById(R.id.tvMyInformation);
        tvAddRecipient = (TextView) view.findViewById(R.id.tvAddRecipient);
        tvMyInfo.setClickable(true);
        tvAddRecipient.setClickable(true);

        btnMyInfoEdit.setOnClickListener(onMyInfoClick);
        etSendAmount.addTextChangedListener(onAmountChangedWatcher);
    }

    private View.OnClickListener onMyInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainActivity.overridePendingTransition(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left);
            mainActivity.switchFragment(new ProfileFragment(), true);
        }
    };

    private TextWatcher onAmountChangedWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            etSendAmount.removeTextChangedListener(this);

            //update the get amount
            if (!TextUtils.isEmpty(s.toString())) {
                long sendAmount = Long.parseLong(s.toString().replaceAll(",", ""));
                getAmount = sendAmount * exchangeRate;
                tvGetAmount.setText("" + getAmount);
                totalAmount = sendAmount + fee;
                tvTotal.setText("" + totalAmount);
            } else {
                tvGetAmount.setText("");
                tvTotal.setText("" + fee);
            }

            // add thousand separators
            try {
                String givenstring = s.toString();
                Long longval;
                if (givenstring.contains(",")) {
                    givenstring = givenstring.replaceAll(",", "");
                }
                longval = Long.parseLong(givenstring);
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String formattedString = formatter.format(longval);
                etSendAmount.setText(formattedString);
                etSendAmount.setSelection(etSendAmount.getText().length());
                // to place the cursor at the end of text
            } catch (Exception e) {
                e.printStackTrace();
            }

            etSendAmount.addTextChangedListener(this);

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(OnRecipientEditClickEvent onRecipientEditClickEvent) {
        //TODO: change to actual ID
        mainActivity.switchFragment(RecipientFragment.newInstance(onRecipientEditClickEvent.getPosition()), true);
    }

    @Subscribe
    public void onEvent(OnSendMoneyClickEvent onSendMoneyClickEvent) {
        //TODO: change to actual value
        mainActivity.showPaymentConfirmationDialog("", 0, 0);

    }

    private void getRecipients() {
        DummyRecipient a = new DummyRecipient("Husband", "BRI 281973021894");
        DummyRecipient b = new DummyRecipient("Mother", "BRI 0123874123");
        DummyRecipient c = new DummyRecipient("Sister", "MYI 9012830912");
        DummyRecipient d = new DummyRecipient("Han", "SGW 0911298301");
        a.setExpanded(false);
        b.setExpanded(false);
        c.setExpanded(false);
        d.setExpanded(false);
        dataList.clear();
        dataList.add(a);
        dataList.add(b);
        dataList.add(c);
        dataList.add(d);
        recipientAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.home);
    }


}
