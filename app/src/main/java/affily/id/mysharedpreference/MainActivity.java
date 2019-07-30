package affily.id.mysharedpreference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvName, tvAge, tvPhoneNo, tvEmail, tvIsLoveMu;
    private Button btnSave;
    private UserPreference userPreference;

    private boolean isPreferenceEmpty = false;
    private UserModel userModel;

    private final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvName = findViewById(R.id.tv_name);
        tvIsLoveMu = findViewById(R.id.tv_is_love_mu);
        tvPhoneNo = findViewById(R.id.tv_number);
        tvAge = findViewById(R.id.tv_age);
        tvEmail = findViewById(R.id.tv_email);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormUserPreferenceActivity.class);
                if (isPreferenceEmpty) {
                    intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_ADD);
                } else {
                    intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_EDIT);
                }
                intent.putExtra("USER", userModel);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        userPreference = new UserPreference(this);
        showExistingPreference();

        getSupportActionBar().setTitle("My User Preference");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == FormUserPreferenceActivity.RESULT_CODE) {
                userModel = data.getParcelableExtra(FormUserPreferenceActivity.EXTRA_RESULT);
                populateView(userModel);
                checkForm(userModel);
            }
        }
    }

    private void showExistingPreference() {
        userModel = userPreference.getUser();
        populateView(userModel);
        checkForm(userModel);
    }

    private void checkForm(UserModel userModel) {
        if (!userModel.getName().isEmpty()) {
            btnSave.setText(getString(R.string.change));
            isPreferenceEmpty = false;
        } else {
            btnSave.setText(getString(R.string.save));
            isPreferenceEmpty = true;
        }
    }

    private void populateView(UserModel userModel) {
        tvName.setText(userModel.getName().isEmpty() ? "Tidak ada" : userModel.getName());
        tvEmail.setText(userModel.getEmail().isEmpty() ? "Tidak ada" : userModel.getEmail());
        tvPhoneNo.setText(userModel.getPhoneNumber().isEmpty() ? "Tidak ada" : userModel.getPhoneNumber());
        tvIsLoveMu.setText(userModel.isLove() ? "Ya" : "Tidak");
        tvAge.setText(String.valueOf(userModel.getAge()).isEmpty() ? "Tidak ada " : String.valueOf(userModel.getAge()));
    }
}