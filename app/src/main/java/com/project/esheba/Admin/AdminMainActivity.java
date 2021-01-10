package com.project.esheba.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.esheba.Admin.Profile.CustomerListActivity;
import com.project.esheba.Admin.Profile.ProfileAdminActivity;
import com.project.esheba.Admin.Profile.SpListActivity;
import com.project.esheba.Constant;
import com.project.esheba.LoginActivity;
import com.project.esheba.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

public class AdminMainActivity extends AppCompatActivity {

    CardView cardProfileAdmin, cardServiceProviderAdmin, cardAllUsersAdmin, cardDashboadAdmin, cardLogoutAdmin;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    ShimmerTextView txtWelcomeNameAdmin, txtHelloAdmin;
    Shimmer shimmerAdmin;

    String UserCell;

    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        getSupportActionBar().setTitle("Admin Panel");
        //Fetching mobile from shared preferences
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String mobile = sp.getString(Constant.CELL_SHARED_PREF, "Not Available");
        UserCell = mobile;

        cardProfileAdmin = findViewById(R.id.card_adminProfile);
        cardServiceProviderAdmin = findViewById(R.id.card_viewSpAdmn);
        cardAllUsersAdmin = findViewById(R.id.card_allUserAdmn);
        cardDashboadAdmin = findViewById(R.id.card_dashboadAdmn);
        cardDashboadAdmin.setVisibility(View.INVISIBLE);
        cardLogoutAdmin = findViewById(R.id.card_logoutAdmn);

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        cardProfileAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, ProfileAdminActivity.class);
                startActivity(intent);

            }
        });

        cardServiceProviderAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, SpListActivity.class);
                startActivity(intent);

            }
        });


        cardAllUsersAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, CustomerListActivity.class);
                startActivity(intent);

            }
        });


        cardDashboadAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(AdminMainActivity.this, OrderListAdminActivity.class);
                startActivity(intent);*/
                Toasty.info(AdminMainActivity.this, "Dashboard Panel Clocked!", Toast.LENGTH_SHORT).show();

            }
        });

        cardLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // editor.clear();
                // editor.apply();
                // finishAffinity();
                Toasty.info(AdminMainActivity.this, "Log out from admin panel!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                startActivity(intent);
                editor.clear();
                editor.apply();

            }
        });


    }

    //double backpress to exit
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //finish();
            finishAffinity();

        } else {
            Toasty.info(this, "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}