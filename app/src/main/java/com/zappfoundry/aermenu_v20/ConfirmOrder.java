package com.zappfoundry.aermenu_v20;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConfirmOrder  extends AppCompatActivity implements Serializable {
    private int shortAnimationDuration;
    RecyclerView recyclerViewOrderSummary;
    List<Order> cart,cart2;
    AlertDialog.Builder builder;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    private static final String TAG ="TagTest";
    int Total;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch(SplashScreenHandlerActivity.x) {
            case "bbr" :  setTheme(R.style.AppThemeBBR);      break;
            case "cnc" :  setTheme(R.style.AppThemeCNC);   break;
            case "mgp" : setTheme(R.style.AppThemeMGP);   break;
            default:setTheme(R.style.AppTheme);  break;
        }
        setContentView(R.layout.confirm_order);

        recyclerViewOrderSummary = findViewById(R.id.recyclerviewOrderSummary);
        cart = new ArrayList<>();   cart2 = new ArrayList<>();
        ImageView backButton = findViewById(R.id.imageView27);
        backButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent intent = new Intent(ConfirmOrder.this, MenuDisplay.class);
                                              finish();
                                              startActivity(intent);

                                          }
                                      }
        );
TextView OrderIDText=findViewById(R.id.textView20);
        TextView OrderNumberText=findViewById(R.id.textView35);
     //   OrderNumberText.setText(MenuDisplay.orderIDString);

if(MenuDisplay.isTakeAway)
{
  /*  TakeAwayObject takeAwayObject=new TakeAwayObject();
    takeAwayObject=
   // HashMap<String,ArrayList>map= (HashMap<String, ArrayList>) getIntent().getSerializableExtra("Orders");
    ;*/
    ArrayList<Order> arrayList = new ArrayList<>();
    arrayList=(ArrayList<Order>) getIntent().getSerializableExtra("Orders");

    for(Order o:arrayList)
    {
        cart.add(o);
        Log.e("cart1",o.getItemName());
    }
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            // This method will be executed once the timer is over
            refreshSummary();
        }
    }, 2500);


    /*ArrayList<HashMap> array=map.get("OrderID :"+String.valueOf(MenuDisplay.orderIDString));
    for (int i = 0; i < array.size(); i++) {
        HashMap<String, Object> m = new HashMap<>();
        m = array.get(i);
        for (int j = 1; j <= m.size(); j++) {
            Order order = new Order();
            order = (Order) m.get("Item" + j);
            Log.e("tag2", order.getItemName());
            cart.add(order);
        }

    }*/
 //   refreshSummary();
}
//firestore code for orders
        else {
    DocumentReference docRefOrders;
    docRefOrders = db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("orders").collection("tables").document("table" + SplashScreenHandlerActivity.y.trim());


    docRefOrders.addSnapshotListener(new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot snapshot,
                            @Nullable FirebaseFirestoreException e) {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                {
                    List<Map<String, Object>> veg = (List<Map<String, Object>>) snapshot.get("orders");
                    if (veg == null) {
                        Log.d("Order Data", "Order " + snapshot.getData());
                    } else {
                        ArrayList<Map> arrayList = new ArrayList<>();

                        for (int i = 0; i < veg.size(); i++) {
                            Map<String, Object> m = veg.get(i);
                            Log.e("VegGet", String.valueOf(veg.get(i)));
                            final Order item = new Order();
                            item.setItemName(m.get("itemName").toString());
                            item.setItemCount(Integer.parseInt(m.get("itemCount").toString()));
                            item.setFromDB(true);
                            item.setItemTotal(Integer.parseInt(m.get("itemTotal").toString()));
                            item.setItemPrice(Integer.parseInt(m.get("itemPrice").toString()));
                            cart.add(item);
                        }

                        refreshSummary();
                    }
                    Random random=new Random();
                  MenuDisplay.orderIDString= +random.nextInt(99999);
                    TextView textviewOrderID = findViewById(R.id.textView35);
                    textviewOrderID.setText(String.valueOf(MenuDisplay.orderIDString) );
                }
            } else {
                Log.d(TAG, "Current data: null");
            }
        }
    });
}
    //firestore code for orders ends


    recyclerViewOrderSummary.setLayoutManager(new LinearLayoutManager(this));
    recyclerViewOrderSummary.setHasFixedSize(true);
    OrderSummaryHandler orderSummaryHandler = new OrderSummaryHandler(this, cart);
    recyclerViewOrderSummary.setAdapter(orderSummaryHandler);
    orderSummaryHandler.notifyDataSetChanged();
    builder = new AlertDialog.Builder(this);
    Button buttonConfirm = findViewById(R.id.buttonConfirm);

    buttonConfirm.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if (cart.isEmpty()) {

                                                 Toast toast = Toast.makeText(getApplicationContext(), "No Items Ordered!", Toast.LENGTH_SHORT);
                                                 toast.show();
                                             } else {
                                                 builder.setMessage("Do you want to Request Bill ?")
                                                         .setCancelable(false)
                                                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                             public void onClick(DialogInterface dialog, int id) {
                                                                 finish();
                                                                 orderComplete();
                                                                 Toast toast = Toast.makeText(getApplicationContext(), "Burp! Payment time!--Closing App", Toast.LENGTH_SHORT);
                                                               //  toast.show();
                                                                 Intent intent = new Intent(ConfirmOrder.this, MenuDisplay.class);
                                                                 finish();
                                                                 startActivity(intent);

                                                             }
                                                         })
                                                         .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                             public void onClick(DialogInterface dialog, int id) {
                                                                 //  Action for 'NO' Button
                                                                 dialog.cancel();

                                                             }
                                                         });
                                                 //Creating dialog box
                                                 AlertDialog alert = builder.create();
                                                 //Setting the title manually
                                                 alert.setTitle("Gone too Soon?");
                                                 alert.show();
                                             }
                                         }
                                     }
    );


}






    void refreshSummary(){
        OrderSummaryHandler orderSummaryHandler=new OrderSummaryHandler(this,cart);
        recyclerViewOrderSummary.setAdapter(orderSummaryHandler);
        orderSummaryHandler.notifyDataSetChanged();
        for (Order o : cart)
        {
            Log.e("Oio",o.getItemName());
            if (o.getItemTotal() != 0)
            {
                Total += (o.getItemTotal());
            }
        }
        TextView textviewTotal = findViewById(R.id.textView18);
        textviewTotal.setText("₹" + Integer.toString(Total));
        TextView textviewOrderID = findViewById(R.id.textView35);
        textviewOrderID.setText(String.valueOf(MenuDisplay.orderIDString) );
    }


    void orderComplet1e( ){
        RequestQueue requestQueue= Volley.newRequestQueue(   this );
        //http://restapi-env.eba-depnvpsr.ap-south-1.elasticbeanstalk.com/clearTable1
      //  String url1="http://restapi-env.eba-depnvpsr.ap-south-1.elasticbeanstalk.com/orders/table1/status";
      //  String url1="http://restapi-env.eba-depnvpsr.ap-south-1.elasticbeanstalk.com/clearTable"+MainActivity.tableNumber;
        String url1="https://aermenu.herokuapp.com/orders/"+"table"+ MenuDisplay.tableNumber+"/status";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url1,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //  Log.d("Error.Response", response);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("isComplete", "true");


                return params;
            }
        };
        requestQueue.add(postRequest);

    }

    void orderComplete(){
        Random rand = new Random();
        EditText phoneNumber=findViewById(R.id.editTextPhone2);
        EditText emailAddress=findViewById(R.id.editTextTextEmailAddress);

        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(1000);

        DateFormat dateFormatNotif = new SimpleDateFormat("hh:mm a");
        String dateFormatNotif2 = dateFormatNotif.format(new Date()).toString();
        Date currentTime = Calendar.getInstance().getTime();DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        DateFormat dateFormat2 = new SimpleDateFormat("hhmm");

        String dateString = dateFormat.format(new Date()).toString();     String dateString2 = dateFormat2.format(new Date()).toString();
        String orderID=dateString2.toString()+rand_int1;

       // DocumentReference cf = db.collection("orders").document("table"+ MenuDisplay.tableNumber) ;
     //   DocumentReference cf = db.collection(SplashScreenHandlerActivity.x).document("orders").collection("takeAway").document("takeAway") ;
        DocumentReference cf =  db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("orders").collection("tables").document("table" + String.valueOf(SplashScreenHandlerActivity.y).trim() );



        cf.update("isComplete", true);
        cf.update("orderID",Integer.parseInt(orderID) );
        //cf.update("userPhone",phoneNumber.getText().toString());
        cf.update("userEmail",SplashScreenHandlerActivity.userEmail);
     //   cf.update("totalAmount",Total);
        cf.update("timeStamp",currentTime.toString());
        Notification notification=new Notification();
        notification.setMessage("Table Number "+MenuDisplay.tableNumber+" has requested for bill");
        notification.setTableNumber(MenuDisplay.tableNumber);

        notification.setTimeStamp(dateFormatNotif2);
        notification.setNotificationType("Request Bill");
        DocumentReference notif =  db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("notifications");

        notif.update("orders", FieldValue.arrayUnion(notification));

    }


}
