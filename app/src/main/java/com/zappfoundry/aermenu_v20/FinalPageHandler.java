package com.zappfoundry.aermenu_v20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class FinalPageHandler extends AppCompatActivity {
TextView tvOrder,tvAmount,tvtimestamp;
Context context;
String itemNumber,Amout;
Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


      //  final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);

        setContentView(R.layout.fragment_report);

        switch(SplashScreenHandlerActivity.x) {
            case "bbr" :  setTheme(R.style.AppThemeBBR);      break;
            case "cnc" :  setTheme(R.style.AppThemeCNC);   break;
            case "mgp" : setTheme(R.style.AppThemeMGP);   break;
            default:setTheme(R.style.AppTheme);  break;
        }
        ImageView imageView ;
        imageView    = findViewById(R.id.imageView3);

       // tvOrder=findViewById(R.id.orderNum);
       // tvAmount=findViewById(R.id.amt);
       // tvtimestamp=findViewById(R.id.time);
    //    final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);

        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
       // ScaleAnimation scaler = new ScaleAnimation((float) 0.7, (float) 1.0, (float) 0.7, (float) 1.0);
       // imageView.startAnimation(anim);
        //scaler.setDuration(40);
      //  imageView.startAnimation(scaler);
// textView is the TextView view that should display it
      //  tvtimestamp.setText(currentDateTimeString);
      //  tvtimestamp=findViewById(R.id.textView9);
     //   webDriver.navigate().to("https://api.whatsapp.com/send?phone=\" + 9325932166 + \"&text=\" + Uri.EscapeDataString(message)");

     //   tvAmount.setText(String.valueOf(MainActivity.totalPriceInt) );
        //tvOrder.setText(String.valueOf(MainActivity.orderIDString) );
        //tvtimestamp.setText(Amout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderMore();
            }
        });
context=this;
//new SendToWhatsapp().execute("ABC");
    }

    void OrderMore()
    {
        Intent intent = new Intent(this, MenuDisplay.class);
        startActivity(intent);

    }
    void sendData(int amout,String orderID){
      //  tvOrder=findViewById(R.id.orderNum);
        tvAmount=findViewById(R.id.amt);
     //   tvtimestamp=findViewById(R.id.textView9);

        tvAmount.setText(amout);
        tvOrder.setText(orderID);
        //tvtimestamp.setText(time);
    }

}
