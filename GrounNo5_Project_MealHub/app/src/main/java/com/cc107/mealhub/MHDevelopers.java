package com.cc107.mealhub;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MHDevelopers extends AppCompatActivity {
    TextView dev01, dev02, dev03, dev04, dev05;
    String email01, email02, email03, email04, email05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_developers);

        dev01 = findViewById(R.id.textviewdev02);
        dev02 = findViewById(R.id.textviewdev04);
        dev03 = findViewById(R.id.textviewdev06);
        dev04 = findViewById(R.id.textviewdev08);
        dev05 = findViewById(R.id.textviewdev10);

        email01 = dev01.getText().toString();
        email02 = dev02.getText().toString();
        email03 = dev03.getText().toString();
        email04 = dev04.getText().toString();
        email05 = dev05.getText().toString();

        dev01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email01};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"MealHub Application concern");
                intent.putExtra(Intent.EXTRA_TEXT,"Developers.. ");
                intent.putExtra(Intent.EXTRA_CC,email01);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        dev02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email02};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"MealHub Application concern");
                intent.putExtra(Intent.EXTRA_TEXT,"Developers.. ");
                intent.putExtra(Intent.EXTRA_CC,email02);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        dev03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email03};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"MealHub Application concern");
                intent.putExtra(Intent.EXTRA_TEXT,"Developers.. ");
                intent.putExtra(Intent.EXTRA_CC,email03);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        dev04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email04};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"MealHub Application concern");
                intent.putExtra(Intent.EXTRA_TEXT,"Developers.. ");
                intent.putExtra(Intent.EXTRA_CC,email04);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        dev05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email05};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"MealHub Application concern");
                intent.putExtra(Intent.EXTRA_TEXT,"Developers.. ");
                intent.putExtra(Intent.EXTRA_CC,email05);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
    }

    public void onBackPressed(){
        Intent b = new Intent(MHDevelopers.this, MHDashboard.class );
                startActivity(b);
                finish();
    }
}
