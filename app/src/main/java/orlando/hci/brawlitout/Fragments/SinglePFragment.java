package orlando.hci.brawlitout.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.DecimalFormat;

import orlando.hci.brawlitout.Activities.SinglePlayerActivity;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;

// Add Another Activiy for Movement
public class SinglePFragment extends Fragment {

    private DataHandlerSingleton dataHandler;

    private Button ss_btn;
    private Button r_btn;
    private TextView result;
    private EditText usernameTextE;
    private TextInputLayout textInputLayout;
    //private String username;
    private Player player;
    InputMethodManager inputManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.dataHandler = DataHandlerSingleton.getInstance(getActivity().getApplicationContext());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.player = dataHandler.getCurrentPlayer();
        if (this.player != null && this.player.getTime() > 0) {
            updateView(this.player.getName(), this.player.getTime());
            try {
                saveScore(this.player);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single, container, false);


        ss_btn = root.findViewById(R.id.button_start);
        r_btn = root.findViewById(R.id.button_reset);
        result = root.findViewById(R.id.chronometer);
        textInputLayout = root.findViewById(R.id.username_text_input_layout);
        usernameTextE = textInputLayout.getEditText();
        RelativeLayout single_relative = root.findViewById(R.id.single_relative);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        validateData(usernameTextE.getText());

        usernameTextE.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                validateData(text);

            }

            @Override
            public void afterTextChanged(Editable s) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

        });


        ss_btn.setOnClickListener(v -> startGame());

        r_btn.setOnClickListener(v -> resetGame());

        single_relative.setOnClickListener(v -> {
            inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if (getActivity().getCurrentFocus() != null) getActivity().getCurrentFocus().clearFocus();
        });
        return root;


    }

    private void saveScore(Player player) throws IOException, ClassNotFoundException {
        if (this.player.isSaved() | (player.getName().equals(""))) return;
        dataHandler.add(player);
        this.player.setSaved(true);
    }

    public void startGame() {
        String username = usernameTextE.getText().toString().trim();
        dataHandler.setCurrentPlayer(new Player(username));
        Intent myIntent = new Intent(getActivity(), SinglePlayerActivity.class);
        startActivity(myIntent);
    }

    public void resetGame() {
        updateView("", -1);
        dataHandler.clearCurrentPlayer();
    }

    private void updateView(String username, double time) {
        usernameTextE.setText(username);
        if (time == -1) {
            result.setText("");
        } else {
            String displayTime = new DecimalFormat("#.###").format(time);
            result.setText(displayTime);
        }
    }

    private void validateData(CharSequence text) {
        if (text.toString().trim().isEmpty()) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getString(R.string.username_consigliated));
            textInputLayout.setErrorIconDrawable(R.drawable.baseline_warning_24);
        } else {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setErrorIconDrawable(null);
        }
    }
}
