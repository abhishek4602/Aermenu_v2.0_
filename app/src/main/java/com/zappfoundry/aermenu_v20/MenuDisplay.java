package com.zappfoundry.aermenu_v20;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MenuDisplay extends AppCompatActivity implements  IAttachMenu,SetDataInterface, Serializable {
    Context context;
     List<Order> cart;
     public static Boolean isTakeAway=false;
public static int orderIDString;
    AlertDialog myDialog,takeAwayDineInDialog;
   // androidx.appcompat.app.AlertDialog ;

     //
    //Location
     private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;

    //
    List<CategoryItem> categoryItemList;  TextView totalPrice,orderCount;    SlidingUpPanelLayout slidingUpPanelLayout;
    RecyclerView recyclerviewcart;  SetDataInterface setDataInterfaceMH;
    CardView cardViewSummary;   static int totalPriceInt;
    ViewPager2 viewPager2;int OrderCounter=0,total=0;
    static int tableNumber=1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    IAttachMenu iAttachMenu;
    RecyclerView recyclerViewCategory;
    List<MenuItem> menuItemListBurgers,menuItemListPanini,menuItemListSW,menuItemListSiders,menuItemListFrankie,menuItemListFries;
    List<MenuItem> NmenuItemListBurgers,NmenuItemListPanini,NmenuItemListSW,NmenuItemListSiders,NmenuItemListFrankie,NmenuItemListFries;
    Button orderCompleteButton,QuickActionsButton,CallCaptainButton;
    RecyclerView recyclerViewMenuVeg,recyclerViewMenuNonVeg;
    List<MenuItem> menuItemListVeg,menuItemListNonVeg;
    @SuppressLint("ResourceAsColor")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  Bundle extras = getIntent().getExtras();
        String value="dumE";
        /*if (extras != null) {
             value = extras.getString("x");
             Log.e("val",value);
        }*/


        switch(SplashScreenHandlerActivity.x) {
            case "bbr" :  setTheme(R.style.AppThemeBBR);     Toast.makeText(MenuDisplay.this,value,Toast.LENGTH_LONG);break;
            case "cnc" :  setTheme(R.style.AppThemeCNC);   Toast.makeText(MenuDisplay.this,value,Toast.LENGTH_LONG);break;
            case "mgp" : setTheme(R.style.AppThemeMGP);   Toast.makeText(MenuDisplay.this,value,Toast.LENGTH_LONG);break;
            default:setTheme(R.style.AppTheme);   Toast.makeText(MenuDisplay.this,value,Toast.LENGTH_LONG);break;
        }
       // setTheme(R.style.AppThemeCNC);

        setContentView(R.layout.menu_display); context=this;
      /*
        sample.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));*/
        categoryItemList=new ArrayList<>();
        recyclerViewCategory=findViewById(R.id.categoriesRecyclerView);
/*

        Log.e("color", String.valueOf((R.color.colorAccent)));*/
TextView tvUserName=findViewById(R.id.textView5);
tvUserName.setText(SplashScreenHandlerActivity.userName+" !");
        TextView RestaurantName=findViewById(R.id.textView311);
        RestaurantName.setText(SplashScreenHandlerActivity.restName.toUpperCase());
        ImageView logo=findViewById(R.id.imageView11111);
        Picasso.get().load(SplashScreenHandlerActivity.imageURL).into(logo);
        iAttachMenu=this;      menuItemListVeg=new ArrayList<>(); menuItemListNonVeg=new ArrayList<>();
      //  iAttachMenu=this;
        totalPrice = findViewById(R.id.textView15);
        final AppBarLayout appBarLayout=findViewById(R.id.AppBarLayout);   final ConstraintLayout OfflineLayout=findViewById(R.id.OfflineLayout);
        orderCount = findViewById(R.id.textView14);
        cardViewSummary = findViewById(R.id.cartsummaryCard);
        slidingUpPanelLayout = findViewById(R.id.SlidingUpPanel);
        cart = new ArrayList<>();
       if(isOnline()){appBarLayout.setVisibility(View.VISIBLE);OfflineLayout.setVisibility(View.GONE);}else{appBarLayout.setVisibility(View.GONE);OfflineLayout.setVisibility(View.VISIBLE);}
        Button tryAgainButton=findViewById(R.id.buttonTryAgain);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){appBarLayout.setVisibility(View.VISIBLE);OfflineLayout.setVisibility(View.GONE);recreate();}else{appBarLayout.setVisibility(View.GONE);OfflineLayout.setVisibility(View.VISIBLE);}

            }
        });
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View takeAwayPopup= layoutInflater.inflate(R.layout.istakeaway,null);
      //  final androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(this);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(takeAwayPopup);
        /*takeAwayDineInDialog= builder.create();
        takeAwayDineInDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_box);
        takeAwayDineInDialog.show();takeAwayDineInDialog.getWindow().setLayout(900,1300);takeAwayDineInDialog.setCanceledOnTouchOutside(true);
        takeAwayDineInDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ((ViewGroup)takeAwayPopup.getParent()).removeView(takeAwayPopup);
                        //the dialog gets canceled and this method executes.
                    }
                }
        );*/
        final ImageView imgTakeAwayDineInSelect=findViewById(R.id.imageView251);
       // Log.e("",SplashScreenHandlerActivity.y);
        if(SplashScreenHandlerActivity.y.equals("takeaway"))
        {

            isTakeAway=true;
            imgTakeAwayDineInSelect.setImageResource(R.drawable.takeaway);
        }
        else
        {
            isTakeAway=false;
            imgTakeAwayDineInSelect.setImageResource(R.drawable.eat);
        }
      /*  final ImageView imgTakeAwayDineInSelect=findViewById(R.id.imageView251);
        ImageButton imagetakeAway=takeAwayPopup.findViewById(R.id.i1111);
        imagetakeAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTakeAway=true;
                imgTakeAwayDineInSelect.setImageResource(R.drawable.takeaway);
                ((ViewGroup)takeAwayPopup.getParent()).removeView(takeAwayPopup);
                takeAwayDineInDialog. dismiss();
            }
        });
        ImageButton imageDineIn=takeAwayPopup.findViewById(R.id.imageButtonTable2);
        imageDineIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTakeAway=false;
                imgTakeAwayDineInSelect.setImageResource(R.drawable.eat);
                ((ViewGroup)takeAwayPopup.getParent()).removeView(takeAwayPopup);
                takeAwayDineInDialog. dismiss();
            }
        });*/


        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recyclerViewMenuNonVeg.setVisibility(View.GONE);
                } else {
                    recyclerViewMenuNonVeg.setVisibility(View.VISIBLE);
                }
            }
        });
        imgTakeAwayDineInSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   recyclerViewCategory.findViewHolderForAdapterPosition(0).itemView.performClick();
            }
        });
                String restname= SplashScreenHandlerActivity.x ;
                if(restname!=null)
                { Log.e("RestName",restname);
                }
                else{restname="BBR";}
               context=this;

        LoadDBData(restname.toUpperCase());
        setDataInterfaceMH = this;
        recyclerViewCategory.setHasFixedSize(true);

        //Code to get categories

        for(int j=1;j<=SplashScreenHandlerActivity.categoryCount;j++) {
            final DocumentReference docRefDesserts = db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("menu").collection("categories" ).document("category"+j);
            final int finalJ = j;
            final int finalJ1 = j;
            final int finalJ2 = j;
            docRefDesserts.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot snapshot,
                                    @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        {

Log.e("j", String.valueOf(finalJ1));

                        if(finalJ1 == SplashScreenHandlerActivity.categoryCount-1)
                        {

                            final CategoryRecyclerViewHandler  ctgryRecyclerViewHandlerobj = new CategoryRecyclerViewHandler( categoryItemList ,context,iAttachMenu);
                            recyclerViewCategory.setAdapter(ctgryRecyclerViewHandlerobj);
                            ReloadMenu(categoryItemList.get(0).getCategoryItemName());
                            Log.e("xategoryItemList",categoryItemList.toString());
                            ctgryRecyclerViewHandlerobj.notifyDataSetChanged();
                        }

                        List<Map<String, Object>> veg = (List<Map<String, Object>>)  snapshot.get("veg");
                        for(int i=0;i<veg.size();i++)
                        {
                            Map<String, Object> m=veg.get(i);
                           // Log.e("menuItem",m.toString());
                            MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            item.setItemID(m.get("ItemId").toString());
                            item.setItemPrice(m.get("ItemPrice").toString());
                            item.setVeg(true);
                            item.setItemCategory((String.valueOf(snapshot.get("categoryName"))));
                            menuItemListVeg.add(item);

                        }


try{
                            List<Map<String, Object>> nonVeg = (List<Map<String, Object>>)  snapshot.get("nonVeg");
                            for(int i=0;i<nonVeg.size();i++){
                                Map<String, Object> m=nonVeg.get(i);
                                Log.e("menuItem",m.toString());
                                MenuItem item = new MenuItem();
                                item.setItemName(m.get("ItemName").toString());
                                item.setItemDescription(m.get("ItemDescription").toString());
                                item.setItemID(m.get("ItemId").toString());
                                item.setItemPrice(m.get("ItemPrice").toString());
                                item.setVeg(false);
                                item.setItemCategory((String.valueOf(snapshot.get("categoryName"))));
                                menuItemListNonVeg.add(item);
                            }}
catch (Exception le){}
                            CategoryItem categoryItem=new CategoryItem();
                            categoryItem.setItemCount(  (veg.size()));
                            Log.e("categoryitemList",(String.valueOf(snapshot.get("categoryName"))));
                            categoryItem.setImageURL(String.valueOf(snapshot.get("categoryImage")));
                            categoryItem.setCategoryItemName(String.valueOf(snapshot.get("categoryName")));
                            categoryItemList.add(categoryItem);
                        }
                    } else {
                        Log.d("TAG", "Current data: null");
                    }
                }
            });
        }

        //



     recyclerViewCategory.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        final CategoryRecyclerViewHandler  ctgryRecyclerViewHandlerobj = new CategoryRecyclerViewHandler( categoryItemList ,context,iAttachMenu);
        recyclerViewCategory.setAdapter(ctgryRecyclerViewHandlerobj);


        recyclerviewcart = (RecyclerView) findViewById(R.id.shoppingcartsummaryrecyclerviewUI);
        recyclerviewcart.setHasFixedSize(true);

        recyclerviewcart.setLayoutManager(new LinearLayoutManager(this));
       String s= getCompleteAddressString(18.501051,73.811653);
       Log.e("Add",s);

        Button buttonCovidPanel=findViewById(R.id.buttonHideCovidPanel);
        final CardView cardViewCovidPanel=findViewById(R.id.CardViewCovidNotice);
        buttonCovidPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardViewCovidPanel.setVisibility(View.GONE);
            }
        });
        menuItemListBurgers=new ArrayList<>();menuItemListPanini=new ArrayList<>();menuItemListFries=new ArrayList<>();menuItemListFrankie=new ArrayList<>();menuItemListSW=new ArrayList<>();menuItemListSiders=new ArrayList<>();
        NmenuItemListBurgers=new ArrayList<>();NmenuItemListPanini=new ArrayList<>();NmenuItemListFries=new ArrayList<>();NmenuItemListFrankie=new ArrayList<>();NmenuItemListSW=new ArrayList<>();NmenuItemListSiders=new ArrayList<>();

        orderCompleteButton=findViewById(R.id.orderCompleteButton2);
        QuickActionsButton=findViewById(R.id.orderCompleteButton1);
        CallCaptainButton=findViewById(R.id.orderCompleteButton3);

        recyclerViewMenuVeg=findViewById(R.id.menuVeg);
        recyclerViewMenuVeg.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        final MenuItemRecyclerViewHandlerv2  ctgryRecyclerViewHandlerobj2 = new MenuItemRecyclerViewHandlerv2( menuItemListBurgers ,context,setDataInterfaceMH);
        recyclerViewMenuVeg.setAdapter(ctgryRecyclerViewHandlerobj2);



        recyclerViewMenuNonVeg=findViewById(R.id.menuNonVeg);
        recyclerViewMenuNonVeg.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        final MenuItemRecyclerViewHandlerv2  ctgryRecyclerViewHandlerobj3 = new MenuItemRecyclerViewHandlerv2( NmenuItemListBurgers ,context,setDataInterfaceMH);
        recyclerViewMenuNonVeg.setAdapter(ctgryRecyclerViewHandlerobj3);

        QuickActionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if(CallCaptainButton.getVisibility()==View.GONE){ orderCompleteButton.setVisibility(View.VISIBLE);
                    CallCaptainButton.setVisibility(View.VISIBLE);}else{ orderCompleteButton.setVisibility(View.GONE);
                    CallCaptainButton.setVisibility(View.GONE);}*/
             slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

            }
        });


        orderCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if   (cart.isEmpty())
                {     Toast toast=Toast.makeText(getApplicationContext(),"Cart is Empty!",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(isTakeAway)
                {

                    Intent intent = new Intent(MenuDisplay.this,ConfirmOrder.class);

                    startActivity(intent);}
            }
        });

        CallCaptainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(MenuDisplay.this, MapsActivity.class);
//                startActivity(i);
//                finish();




                Notification notification=new Notification();
                notification.setMessage("Table Number "+tableNumber+" has called for Captain");
                notification.setTableNumber(tableNumber);  DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String dateString = dateFormat.format(new Date()).toString();
                notification.setTimeStamp(dateString);
                notification.setNotificationType("Captain Call");   DocumentReference notif =  db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("notifications");

                notif.update("orders", FieldValue.arrayUnion(notification));
                Toast toast=Toast.makeText(getApplicationContext(),"Captain incoming!",Toast.LENGTH_SHORT);
               // toast.show();toast.show();
            }
        });
        Button btn = findViewById(R.id.FireOrderButtonUI);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendOrderToDB();
            }
        });

      //  recyclerViewCategory.findViewHolderForAdapterPosition(0).itemView.performClick();
       Button trigger=findViewById(R.id.buttonConfirm2) ;
       trigger.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(MenuDisplay.this, ConfirmOrder.class);

               startActivity(i);
           }
       });

        final OrderHandler recycleViewHandlercart = new OrderHandler(this, cart, setDataInterfaceMH);
       if(!isTakeAway) {
           DocumentReference docRefOrders;
           docRefOrders = db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("orders").collection("tables").document("table"+SplashScreenHandlerActivity.y.trim());


           docRefOrders.addSnapshotListener(new EventListener<DocumentSnapshot>() {
               @Override
               public void onEvent(@Nullable DocumentSnapshot snapshot,
                                   @Nullable FirebaseFirestoreException e) {
                   if (e != null) {
                       Log.w("TAG", "Listen failed.", e);
                       return;
                   }

                   if (snapshot != null && snapshot.exists()) {
                       {
                           List<Map<String, Object>> veg = (List<Map<String, Object>>) snapshot.get("orders");
                           if (veg == null) {
                               Log.d("Order Data", "Order " + snapshot.getData());
                           } else {
                               ArrayList<Map> arrayList = new ArrayList<>();
                int total=0;
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
                                   total+=item.getItemTotal();
                               }
                               try {
                                   totalPrice.setText(String.valueOf(total) );
                                   orderCount.setText(cart.size() + " items");
                               }catch (Exception o){}
                               if(!isTakeAway&&!cart.isEmpty())
                               {
                                   LinearLayout l=findViewById(R.id.l);
                                   l.setVisibility(View.VISIBLE);
                                   TextView Or=findViewById(R.id.textView41); Or.setVisibility(View.VISIBLE);
                               }

                               recyclerviewcart.setAdapter(recycleViewHandlercart);
                               recycleViewHandlercart.notifyDataSetChanged();
                           }

                       }
                   } else {
                       Log.d("TAG", "Current data: null");
                   }
               }
           });

           //firestore code for orders ends
if(SplashScreenHandlerActivity.x.equals("cnc")){
LinearLayout cncTokenLayout =findViewById(R.id.cncTokenLL);
           cncTokenLayout.setVisibility(View.VISIBLE);
           TextView token=findViewById(R.id.textView42);
           token.setText("Your token Number is : "+SplashScreenHandlerActivity.tokenID);
       }}
       }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(slidingUpPanelLayout.getPanelState()==SlidingUpPanelLayout.PanelState.EXPANDED)
        {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        }
    }

    @Override
    public void ReloadMenu(String categoryItemName) {
        TextView CategoryName=findViewById(R.id.textView27);
        /*switch(categoryItemName){
            case "Burgers": RefreshMenuData(menuItemListBurgers,NmenuItemListBurgers);CategoryName.setText("Burgers' Menu"); break;
            case  "Panini": RefreshMenuData(menuItemListPanini,NmenuItemListPanini);CategoryName.setText("Panini Bread Sandwich' Menu"); break;
            case "Sandwiches": RefreshMenuData(menuItemListSW,NmenuItemListSW);CategoryName.setText("Grilled Sandwiches' Menu"); break;
            case "Frankies": RefreshMenuData(menuItemListFrankie,NmenuItemListFrankie);CategoryName.setText("Frankies' Menu"); break;
            case "Fries": RefreshMenuData(menuItemListFries,NmenuItemListFries);CategoryName.setText("Fries' Menu"); break;
            case "Siders": RefreshMenuData(menuItemListSiders,NmenuItemListSiders);CategoryName.setText("Siders' Menu"); break;
        }*/

        ArrayList<MenuItem> tempList=new ArrayList<>();  ArrayList<MenuItem> tempList2=new ArrayList<>();
        for (MenuItem m : menuItemListVeg)
        {
            if(m.getItemCategory()==categoryItemName)
            {
                tempList.add(m);

            }
        }
        for (MenuItem m : menuItemListNonVeg)
        {
            if(m.getItemCategory()==categoryItemName)
            {
                tempList2.add(m);

            }
        }
        RefreshMenuData(tempList,tempList2);CategoryName.setText(categoryItemName+"' Menu");


    }
    void RefreshMenuData(List<MenuItem> vlist,List<MenuItem> nvList)
    {
        final MenuItemRecyclerViewHandlerv2 MenuVeg = new MenuItemRecyclerViewHandlerv2(vlist,context,setDataInterfaceMH );
        recyclerViewMenuVeg.setAdapter(MenuVeg);
        MenuVeg.notifyDataSetChanged();

        final MenuItemRecyclerViewHandlerv2 MenuNonVeg = new MenuItemRecyclerViewHandlerv2(nvList,context,setDataInterfaceMH );
        recyclerViewMenuNonVeg.setAdapter(MenuNonVeg);
        MenuNonVeg.notifyDataSetChanged();
    }

    void LoadDBData(String restaurantName)
    {
        //BURGERS///////////////////////////////////////////////////////
        final DocumentReference docRefDesserts = db.collection(restaurantName).document("menu").collection("category1").document("menuItems");
        docRefDesserts.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    {
                        final Boolean[] isLoadingMenuFirstTime = {true};
                        menuItemListBurgers.clear();
                        List<Map<String, Object>> veg = (List<Map<String, Object>>)  snapshot.get("veg");
                        for(int i=0;i<veg.size();i++){
                            Map<String, Object> m=veg.get(i);
                            Log.e("menuItem",m.toString());
                            MenuItem item = new MenuItem();
                           item.setItemName(m.get("itemName").toString());
                           item.setItemDescription(m.get("ItemDescription").toString());
                           item.setItemID(m.get("ItemId").toString());
                           item.setItemPrice(m.get("itemPrice").toString());
                           Log.e("itemName",m.get("itemName").toString());
                            menuItemListBurgers.add(item);

                        }
                        NmenuItemListBurgers.clear();
                     /*   List<Map<String, Object>> nonVeg = (List<Map<String, Object>>)  snapshot.get("nonVeg");
                        for(int i=0;i<nonVeg.size();i++)
                        {
                            Map<String, Object> m=nonVeg.get(i);
                            final MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            String itemID=m.get("ItemId").toString();
                            item.setItemID(itemID);
                            item.setItemPrice(m.get("ItemPrice").toString());
                       //     item.setImageURL(m.get("imageURL").toString());


                            //String url = "https://aermenu.herokuapp.com/itemImage/"+itemID+".png";
*//*
                            ImageRequest request = new ImageRequest(url,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {

                                            item.setItemImage(bitmap);
                                            //           refreshDataForMenu();
                                        }
                                    }, 0, 0, null,
                                    new Response.ErrorListener() {
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Image NV Loading failed",error.toString());
                                        }
                                    });*//*


                            //  requestQueue.add(request);
                            NmenuItemListBurgers.add(item); Collections.sort(NmenuItemListBurgers);if(isLoadingMenuFirstTime[0] ==false){RefreshMenuData(menuItemListBurgers,NmenuItemListBurgers);}
                        }*/
                        {RefreshMenuData(menuItemListBurgers,menuItemListBurgers);}
                    }
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });


    //Panini///////////////////////////////////
        final DocumentReference docRefPanini = db.collection("menu").document("PaniniSandwich");
        docRefPanini.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    {
                        final Boolean[] isLoadingMenuFirstTime = {true};
                        menuItemListPanini.clear();
                        List<Map<String, Object>> veg = (List<Map<String, Object>>)  snapshot.get("veg");
                        for(int i=0;i<veg.size();i++){
                            Map<String, Object> m=veg.get(i);
                            MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            item.setItemID(m.get("ItemId").toString());
                            item.setItemPrice(m.get("ItemPrice").toString());
                            menuItemListPanini.add(item);

                        }
                        NmenuItemListPanini.clear();
                        List<Map<String, Object>> nonVeg = (List<Map<String, Object>>)  snapshot.get("nonVeg");
                        for(int i=0;i<nonVeg.size();i++)
                        {
                            Map<String, Object> m=nonVeg.get(i);
                            final MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            String itemID=m.get("ItemId").toString();
                            item.setItemID(itemID);
                            item.setItemPrice(m.get("ItemPrice").toString());
                   //         item.setImageURL(m.get("imageURL").toString());


                            //String url = "https://aermenu.herokuapp.com/itemImage/"+itemID+".png";
/*
                            ImageRequest request = new ImageRequest(url,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {

                                            item.setItemImage(bitmap);
                                            //           refreshDataForMenu();
                                        }
                                    }, 0, 0, null,
                                    new Response.ErrorListener() {
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Image NV Loading failed",error.toString());
                                        }
                                    });*/


                            //  requestQueue.add(request);
                            NmenuItemListPanini.add(item); Collections.sort(NmenuItemListPanini); Collections.sort(menuItemListPanini);if(isLoadingMenuFirstTime[0] ==false){RefreshMenuData(menuItemListPanini,NmenuItemListPanini);}
                        }
                    }
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });


//Sandwitch////////////////////////////////////////////////////////////

        final DocumentReference docRefSandwich = db.collection("menu").document(
                "GrillSandwich");
        docRefSandwich.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    {
                        final Boolean[] isLoadingMenuFirstTime = {true};
                        menuItemListSW.clear();
                        List<Map<String, Object>> veg = (List<Map<String, Object>>)  snapshot.get("veg");
                        for(int i=0;i<veg.size();i++){
                            Map<String, Object> m=veg.get(i);
                            MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            item.setItemID(m.get("ItemId").toString());
                            item.setItemPrice(m.get("ItemPrice").toString());
                            menuItemListSW.add(item);

                        }
                        NmenuItemListSW.clear();
                        List<Map<String, Object>> nonVeg = (List<Map<String, Object>>)  snapshot.get("nonVeg");
                        for(int i=0;i<nonVeg.size();i++)
                        {
                            Map<String, Object> m=nonVeg.get(i);
                            final MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            String itemID=m.get("ItemId").toString();
                            item.setItemID(itemID);
                            item.setItemPrice(m.get("ItemPrice").toString());
                  //          item.setImageURL(m.get("imageURL").toString());


                            //String url = "https://aermenu.herokuapp.com/itemImage/"+itemID+".png";
/*
                            ImageRequest request = new ImageRequest(url,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {

                                            item.setItemImage(bitmap);
                                            //           refreshDataForMenu();
                                        }
                                    }, 0, 0, null,
                                    new Response.ErrorListener() {
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Image NV Loading failed",error.toString());
                                        }
                                    });*/


                            //  requestQueue.add(request);
                            NmenuItemListSW.add(item); Collections.sort(NmenuItemListSW); Collections.sort(menuItemListSW);if(isLoadingMenuFirstTime[0] ==false){RefreshMenuData(menuItemListSW,NmenuItemListSW);}
                        }
                    }
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });
//Frankies//////////////////////////////

        final DocumentReference docRefFrankie = db.collection("menu").document("frankie");
        docRefFrankie.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    {
                        final Boolean[] isLoadingMenuFirstTime = {true};
                        menuItemListFrankie.clear();
                        List<Map<String, Object>> veg = (List<Map<String, Object>>)  snapshot.get("veg");
                        for(int i=0;i<veg.size();i++){
                            Map<String, Object> m=veg.get(i);
                            MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            item.setItemID(m.get("ItemId").toString());
                            item.setItemPrice(m.get("ItemPrice").toString());
                            menuItemListFrankie.add(item);

                        }
                        NmenuItemListFrankie.clear();
                        List<Map<String, Object>> nonVeg = (List<Map<String, Object>>)  snapshot.get("nonVeg");
                        for(int i=0;i<nonVeg.size();i++)
                        {
                            Map<String, Object> m=nonVeg.get(i);
                            final MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            String itemID=m.get("ItemId").toString();
                            item.setItemID(itemID);
                            item.setItemPrice(m.get("ItemPrice").toString());
                     //       item.setImageURL(m.get("imageURL").toString());


                            //String url = "https://aermenu.herokuapp.com/itemImage/"+itemID+".png";
/*
                            ImageRequest request = new ImageRequest(url,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {

                                            item.setItemImage(bitmap);
                                            //           refreshDataForMenu();
                                        }
                                    }, 0, 0, null,
                                    new Response.ErrorListener() {
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Image NV Loading failed",error.toString());
                                        }
                                    });*/


                            //  requestQueue.add(request);
                            NmenuItemListFrankie.add(item); Collections.sort(NmenuItemListFrankie); Collections.sort(menuItemListFrankie);if(isLoadingMenuFirstTime[0] ==false){RefreshMenuData(menuItemListFrankie,NmenuItemListFrankie);}
                        }
                    }
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });

        //Fries////////////////////////////////////////////////////////////////

        final DocumentReference docRefFries = db.collection("menu").document("fries");
        docRefFries.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    {
                        final Boolean[] isLoadingMenuFirstTime = {true};
                        menuItemListFries.clear();


                        List<Map<String, Object>> nonVeg = (List<Map<String, Object>>)  snapshot.get("veg");
                        for(int i=0;i<nonVeg.size();i++)
                        {
                            Map<String, Object> m=nonVeg.get(i);
                            final MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            String itemID=m.get("ItemId").toString();
                            item.setItemID(itemID);
                            item.setItemPrice(m.get("ItemPrice").toString());
                      //      item.setImageURL(m.get("imageURL").toString());


                            //String url = "https://aermenu.herokuapp.com/itemImage/"+itemID+".png";
/*
                            ImageRequest request = new ImageRequest(url,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {

                                            item.setItemImage(bitmap);
                                            //           refreshDataForMenu();
                                        }
                                    }, 0, 0, null,
                                    new Response.ErrorListener() {
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Image NV Loading failed",error.toString());
                                        }
                                    });*/


                            //  requestQueue.add(request);
                            menuItemListFries.add(item); Collections.sort(menuItemListFries); if(isLoadingMenuFirstTime[0] ==false){RefreshMenuData(menuItemListFries,menuItemListFries);}
                        }
                    }
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });

        //Siders//////////////////////////////////

        final DocumentReference docRefSiders = db.collection("menu").document("siders");
        docRefSiders.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    {
                        final Boolean[] isLoadingMenuFirstTime = {true};
                        menuItemListSiders.clear();
                        List<Map<String, Object>> veg = (List<Map<String, Object>>)  snapshot.get("veg");
                        for(int i=0;i<veg.size();i++){
                            Map<String, Object> m=veg.get(i);
                            MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            item.setItemID(m.get("ItemId").toString());
                            item.setItemPrice(m.get("ItemPrice").toString());
                            menuItemListSiders.add(item);

                        }
                        NmenuItemListSiders.clear();
                        List<Map<String, Object>> nonVeg = (List<Map<String, Object>>)  snapshot.get("nonVeg");
                        for(int i=0;i<nonVeg.size();i++)
                        {
                            Map<String, Object> m=nonVeg.get(i);
                            final MenuItem item = new MenuItem();
                            item.setItemName(m.get("ItemName").toString());
                            item.setItemDescription(m.get("ItemDescription").toString());
                            String itemID=m.get("ItemId").toString();
                            item.setItemID(itemID);
                            item.setItemPrice(m.get("ItemPrice").toString());
                     //       item.setImageURL(m.get("imageURL").toString());


                            //String url = "https://aermenu.herokuapp.com/itemImage/"+itemID+".png";
/*
                            ImageRequest request = new ImageRequest(url,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {

                                            item.setItemImage(bitmap);
                                            //           refreshDataForMenu();
                                        }
                                    }, 0, 0, null,
                                    new Response.ErrorListener() {
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Image NV Loading failed",error.toString());
                                        }
                                    });*/


                            //  requestQueue.add(request);
                            NmenuItemListSiders.add(item); Collections.sort(NmenuItemListSiders); Collections.sort(menuItemListSiders);if(isLoadingMenuFirstTime[0] ==false){RefreshMenuData(menuItemListSiders,NmenuItemListSiders);}
                        }
                    }
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });

    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            Toast toast=Toast.makeText(getApplicationContext(),"Online",Toast.LENGTH_LONG);
         //   toast.show();
            return true;
        } else {
            Toast toast=Toast.makeText(getApplicationContext(),"App not connected to the internet",Toast.LENGTH_LONG);
            toast.show();

            return false;
        }
    }
    //public void pushToMongo(final String itemName, final int itemCount, final int itemPrice)
    public void pushToMongo(ArrayList<Order> array)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.e("db",db.toString());
        Date currentTime = Calendar.getInstance().getTime();
        Order order=new Order();
        order.setTimeStamp(currentTime.toString() );
        order.setTableNumber(SplashScreenHandlerActivity.y);

        /*order.setItemCount(itemCount);
        order.setItemName(itemName);
        order.setItemTotal((itemCount*itemPrice));
        order.setItemPrice(itemPrice);*/
       // DocumentReference cf = db.collection("orders").document("table1") ;
        String doc="";
       /* if(SplashScreenHandlerActivity.y.equals("takeaway"))
        {
             doc="takeAway";
            Log.e("Doc is",doc);
        }
        else
        {
            doc="table"+SplashScreenHandlerActivity.y;
            Log.e("Doc is",doc);
        }*/
        if(isTakeAway)
        {
            DocumentReference cf = db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("orders").collection("tables").document("takeAway") ;

            HashMap<String,String>map=new HashMap<>();
            HashMap<String,String>map3=new HashMap<>();
            HashMap<String,ArrayList>map2=new HashMap<>();
            Random random=new Random();
            orderIDString= +random.nextInt(99999);;
           // orderIDString=121;
            int tot = 0;
for (int i=0;i<array.size();i++)
{
    Order p =new Order();p= array.get(i);tot+=p.getItemTotal();
}
            TakeAwayObject takeAwayObject=new TakeAwayObject();
            takeAwayObject.Orders=array;
            takeAwayObject.setOrderID(String.valueOf(orderIDString) );
            takeAwayObject.setTimeStamp(currentTime.toString());
            takeAwayObject.setUserEmail(SplashScreenHandlerActivity.userEmail);
            takeAwayObject.setTotalAmount(tot);


            map.put("Order ID2", String.valueOf(orderIDString));
            map3.put("userEmail2",SplashScreenHandlerActivity.userEmail);
            ArrayList<HashMap> a1 = new ArrayList<>(); a1.add(map);
            ArrayList<HashMap> a2 = new ArrayList<>();a2.add(map3);
          //  array.add(map);array.add(map3);
            map2.put("Orders",array);
            map2.put("OrderID",a1);
            map2.put("uSerEmail",a2);

            cf.update("orders", FieldValue.arrayUnion(takeAwayObject));
          //  cf.update("orderID",FieldValue.arrayUnion(map));
            //cf.update("userEmail",FieldValue.arrayUnion(SplashScreenHandlerActivity.userEmail));
            //   Toast toast=Toast.makeText(context,"Order Placed",Toast.LENGTH_SHORT);
            //   toast.show();
        //    cf.update("totalAmount", totalPriceInt);

            Notification notification=new Notification();
            notification.setMessage("New Take away order");
            notification.setTableNumber(tableNumber);
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String dateString = dateFormat.format(new Date()).toString();
            notification.setTimeStamp(dateString);
            notification.setNotificationType("New Order");
            DocumentReference notif =  db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("notifications");
            notif.update("orders", FieldValue.arrayUnion(notification));
            Intent i = new Intent(MenuDisplay.this, ConfirmOrder.class);
            i.putExtra("Orders", array);
            startActivity(i);
        }

        else//dine in
            {
                Log.e("Y=","table"+String.valueOf(SplashScreenHandlerActivity.y).trim());
                DocumentReference cf = db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("orders").collection("tables").document("table"+SplashScreenHandlerActivity.y.trim()) ;
                //String.valueOf(SplashScreenHandlerActivity.y).trim()
                HashMap<String,ArrayList>map=new HashMap<>();
                HashMap<String, Object> m = new HashMap<>();
               // m=array.get(0);
                for(int l=0;l<array.size();l++){   cf.update("orders", FieldValue.arrayUnion(array.get(l)));      }
                DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String dateString = dateFormat.format(new Date()).toString();
                Random random=new Random();
                orderIDString= +random.nextInt(99999);
            //    Log.e("TakeAwayRandom", String.valueOf(orderIDString));
               // orderIDString=121;
               // map.put("OrderID :"+String.valueOf(orderIDString),array);
           //     cf.update("orders", FieldValue.arrayUnion(m.get("Item1")));
                //   Toast toast=Toast.makeText(context,"Order Placed",Toast.LENGTH_SHORT);
                //   toast.show();
            //    cf.update("userEmail", SplashScreenHandlerActivity.userEmail);
                cf.update("totalAmount", totalPriceInt);
                Log.e("Totalprice", String.valueOf(totalPriceInt));
                cf.update("timeStamp", dateString);
             //   cf.update("orderID", orderIDString);
                Notification notification=new Notification();
                notification.setMessage("Table number "+tableNumber+"has ordered new items");
                notification.setTableNumber(tableNumber);
                notification.setTimeStamp(dateString);
                notification.setNotificationType("New Order");
                DocumentReference notif =  db.collection(SplashScreenHandlerActivity.x.toUpperCase()).document("notifications");

                notif.update("orders", FieldValue.arrayUnion(notification));
            }




    }
    public void addItemToCart(Order order,int flag2,int id) {
        int flag=0;
        ImageView imageViewCartNotEmpty =findViewById(R.id.imageView26);
        imageViewCartNotEmpty.setVisibility(View.VISIBLE);



        cart.add(0,order);
        if(order.isFromDB()||order.getItemPrice()==0){}
        else{

            order.setItemTotal((order.getItemCount() * order.getItemPrice()));
        }
        totalPriceInt=0;
        for (Order o : cart){
            if(o.getItemTotal()==0){}else
            if(o.isFromDB()!=true){   Button btn = findViewById(R.id.FireOrderButtonUI); btn.setVisibility(View.VISIBLE);}
            {            totalPriceInt+= o.getItemTotal();
            }
        }
        totalPrice.setText(Integer.toString(totalPriceInt) );
        //order.setItemTotal(order.getItemPrice());
        flag=0;
        // totalPrice.setText(Integer.toString(totalPriceInt) );
        if(flag!=1)
        {
         /*   cart.add(order);
            order.setItemTotal(order.getItemPrice());
            flag=0;*/
        }
        if(cart.size()>0)
        {
            orderCount.setText(Integer.toString(cart.size())+" Items"); slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED); cardViewSummary.setVisibility(View.VISIBLE);
        }
        else
        {
            orderCount.setText(Integer.toString(cart.size())+" Items");
            cardViewSummary.setVisibility(View.VISIBLE);slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);totalPriceInt=0;
        }




        orderCount.setText(Integer.toString(cart.size())+" Items");
        QuickActionsButton.setText ("CART : "+(Integer.toString(cart.size()))+" Items");

        orderCount.setText(Integer.toString(cart.size())+" Items");
        final OrderHandler recycleViewHandlercart = new OrderHandler(this, cart,setDataInterfaceMH);
        recyclerviewcart.setAdapter(recycleViewHandlercart);
        recycleViewHandlercart.notifyDataSetChanged();

    }


    @Override
    public void removeFromCart(Order order, int id) {
        //totalPriceInt-=Integer.parseInt(order.getItemPrice());
        cart.remove(order);

        if(cart.isEmpty()==true){totalPriceInt=0;}
        int amt=order.getItemPrice();
        if(totalPriceInt-amt>=0){totalPriceInt-=amt;}
        totalPrice.setText(Integer.toString(totalPriceInt) );
        final OrderHandler recycleViewHandlercart = new OrderHandler(this, cart,setDataInterfaceMH);
        recyclerviewcart.setAdapter(recycleViewHandlercart);
        recycleViewHandlercart.notifyDataSetChanged();

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void DeletefromCart(Order order, int id){
        cart.remove(order);
        int amt=order.getItemPrice();
        // totalPriceInt-=amt;
        totalPriceInt=0;
        for (Order o : cart){

            totalPriceInt+= o.getItemTotal();
        }
        totalPrice.setText(Integer.toString(totalPriceInt) );

        final OrderHandler recycleViewHandlercart = new OrderHandler(this, cart,setDataInterfaceMH);
        recyclerviewcart.setAdapter(recycleViewHandlercart);
        recycleViewHandlercart.notifyDataSetChanged();

        if(cart.size()>0){orderCount.setText(Integer.toString(cart.size())+" Item(s)"); cardViewSummary.setVisibility(View.VISIBLE);}else{ slidingUpPanelLayout.setPanelHeight(50);cardViewSummary.setVisibility(View.VISIBLE);  ImageView imageViewCartNotEmpty =findViewById(R.id.imageView26);
            imageViewCartNotEmpty.setVisibility(View.INVISIBLE);}

     /*   switch (id) {
            case 3:
                resetCountInterface1.deleteData(order);break;
            case 2:
                resetCountInterface2.deleteData(order);break;
            case 1:
                resetCountInterface3.deleteData(order);break;
        }*/
    }
    public void SendOrderToDB()   {
        StringBuilder itemString =new StringBuilder();
        //  orderIDString="OrderID "+random.nextInt(99999);;
        final OrderHandler recycleViewHandlercart = new OrderHandler(this, cart,setDataInterfaceMH);
        recyclerviewcart.setAdapter(recycleViewHandlercart);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        HashMap<String, Object> m = new HashMap<>();
        //     OrderString="Order Number "+orderIDInt;
        //cart= recycleViewHandlercart.GetCartValues();
        //ArrayList<HashMap> arrayList = new ArrayList<>();
        ArrayList<Order> arrayList = new ArrayList<>();

        List itemsOrdered=new ArrayList(); int p=1;
        for(Order order1:cart)
        {


            //order1.setTimeStamp(format);
            if(!order1.isFromDB())
            {
                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                String dateString = dateFormat.format(new Date()).toString();
                order1.setTimeStamp(dateString);
                order1.setDelivered(false);
                order1.setTableNumber(SplashScreenHandlerActivity.y);
                  //  m.put("Item"+p,order1);
                arrayList.add(order1);

                    Log.e("Order1",order1.getItemName());
                   // pushToMongo(order1.getItemName(),order1.getItemCount(),order1.getItemPrice());

                itemString.append (order1.getItemCount()+"x"+order1.getItemName()+" "+",");
                    itemsOrdered.add(order1.getItemCount()+" x "+order1.getItemName()+" ");
p++;
            }



            //myRef.child(orderIDString).push().setValue(order1.getItemCount()+order1.getItemName());
        }
       // arrayList.add(m);
        pushToMongo(arrayList);
        Order orderTime=new Order();
        String singleString = itemString.toString();
        orderTime.setTimeStamp(format);
        orderTime.setItemName(singleString);
        orderTime.setItemPrice(totalPriceInt);
        if (cart.isEmpty())
        {
            Toast toast=Toast.makeText(getApplicationContext(),"Cart is empty, Order Not Placed!",Toast.LENGTH_SHORT);
            toast.show();}
        else
        {
            //myRef.child(orderIDString).setValue(orderTime);cart.removeAll(cart);
            slidingUpPanelLayout.setPanelHeight(0);cardViewSummary.setVisibility(View.VISIBLE);
           recyclerviewcart.setVisibility(View.INVISIBLE);
Log.e("istakwarer", String.valueOf(isTakeAway));
            if(isTakeAway){
                OpenOrderReport(itemsOrdered,arrayList);
            }
            else
                {

recreate();
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }
        //myRef.child(orderIDString).setValue(orderTime);
        OrderCounter++;


    }
    void OpenOrderReport(List item , final ArrayList<Order> arrayList)
    {
        slidingUpPanelLayout.setPanelHeight(50
        );
        cardViewSummary.setVisibility(View.VISIBLE);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View viewPopUp= getLayoutInflater().inflate(R.layout.order_confirm, null);

        builder.setView(viewPopUp);
        myDialog= builder.create();
        myDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_box);
        myDialog.show();myDialog.getWindow().setLayout(900,1600);myDialog.setCanceledOnTouchOutside(true);
        myDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // ((ViewGroup)viewPopUp.getParent()).removeView(viewPopUp);
                        //the dialog gets canceled and this method executes.
                    }
                }
        );
        ListView itemOrderderedList=viewPopUp.findViewById(R.id.ListviewItemsOrdered);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, item);
        itemOrderderedList.setAdapter(adapter);
        Button okButton=viewPopUp.findViewById(R.id.button2);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //slidingUpPanelLayout.setPanelHeight(0);cardViewSummary.setVisibility(View.GONE);
                recyclerviewcart.setVisibility(View.VISIBLE);
                Log.e("istakeaway", String.valueOf(isTakeAway));
                if(isTakeAway)
                {
                    Intent i = new Intent(MenuDisplay.this, ConfirmOrder.class);
                   // i.putExtra("mylist", arrayList);
                    startActivity(i);}
                else
                    {recreate();}
                //recreate();//to refresh activity


                myDialog.cancel(); // ((ViewGroup)viewPopUp.getParent()).removeView(viewPopUp);
            }
        });

       /* Intent intent = new Intent(this, FinalPageHandler.class);

        startActivity(intent);
*/

    }

    @SuppressLint("LongLogTag")
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
}
