package affily.id.mysharedpreference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormUserPreferenceActivity extends AppCompatActivity {
    EditText edtName, edtEmail, edtAge, edtPhone;
    RadioGroup rgLoveMu;
    RadioButton rbYes, rbNo;
    Button btnSave;

    public static String EXTRA_TYPE_FORM = "extra_form";
    public static String EXTRA_RESULT = "extra_result";
    public static int RESULT_CODE = 101;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;
    int formType;

    final String FIELD_REQUIRED = "Field tidak boleh kosong";
    final String FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik";
    final String FIELD_IS_NOT_VALID = "Email tidak valid";

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user_preference);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtAge = findViewById(R.id.edt_age);
        edtPhone = findViewById(R.id.edt_phone);
        rgLoveMu = findViewById(R.id.rg_love_mu);
        rbYes = findViewById(R.id.rb_yes);
        rbNo = findViewById(R.id.rb_no);
        btnSave = findViewById(R.id.btn_save);
        Intent intent = getIntent();
        userModel = intent.getParcelableExtra("USER");

        formType = getIntent().getIntExtra(EXTRA_TYPE_FORM, 0);

        String actionBarTitle ="";
        String btnTitle ="";

        switch (formType) {
            case TYPE_ADD:
                actionBarTitle = "Tambah baru";
                btnTitle = "Simpan";
                break;
            case TYPE_EDIT:
                actionBarTitle = "Ubah";
                btnTitle = "Update";
                showPreferenceInForm();
        }

        btnSave.setText(btnTitle);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String age = edtAge.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                boolean isLoveMU = rgLoveMu.getCheckedRadioButtonId() == R.id.rb_yes;

                if (TextUtils.isEmpty(name)) {
                    edtName.setError(FIELD_REQUIRED);
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError(FIELD_REQUIRED);
                    return;
                }

                if (!isValidEmail(email)) {
                    edtEmail.setError(FIELD_IS_NOT_VALID);
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    edtPhone.setError(FIELD_REQUIRED);
                    return;
                }

                if (!TextUtils.isDigitsOnly(phone)) {
                    edtPhone.setError(FIELD_DIGIT_ONLY);
                    return;
                }

                if (TextUtils.isEmpty(age)) {
                    edtAge.setError(FIELD_REQUIRED);
                    return;
                }

                saveUser(name, email, phone, age, isLoveMU);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_RESULT, userModel);
                setResult(RESULT_CODE, resultIntent);

                finish();
            }
        });
    }

    void saveUser(String name, String email, String phone, String age, boolean isLoveMu) {
        UserPreference userPreference = new UserPreference(this);

        userModel.setName(name);
        userModel.setEmail(email);
        userModel.setPhoneNumber(phone);
        userModel.setAge(Integer.valueOf(age));
        userModel.setLove(isLoveMu);

        userPreference.setUser(userModel);
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_LONG).show();
    }

    private void showPreferenceInForm() {
        edtName.setText(userModel.getName());
        edtEmail.setText(userModel.getEmail());
        edtAge.setText(String.valueOf(userModel.getAge()));
        edtPhone.setText(userModel.getPhoneNumber());
        if (userModel.isLove()) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
