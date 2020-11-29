package com.zappfoundry.aermenu_v20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuItemRecyclerViewHandlerv2 extends RecyclerView.Adapter<MenuItemRecyclerViewHandlerv2.RecyclerViewHolder>  {
    private final List<MenuItem> Menuitemlist;
    private final Context context;
    SetDataInterface setDataInterface;

    public MenuItemRecyclerViewHandlerv2(List<MenuItem> ctrgyListReceived, Context receivedContext ,SetDataInterface setDataInterface1 )
    {
        this.Menuitemlist=ctrgyListReceived;
        this.context=receivedContext;
        this.setDataInterface=setDataInterface1;


    }
    @NonNull
    @Override
    public MenuItemRecyclerViewHandlerv2.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.menu_item_view,null);
        final MenuItemRecyclerViewHandlerv2.RecyclerViewHolder rvHolder=new MenuItemRecyclerViewHandlerv2.RecyclerViewHolder(view);
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuItemRecyclerViewHandlerv2.RecyclerViewHolder holder, int position) {
        MenuItem ctgy =Menuitemlist.get(position);
        holder.itemName.setText(ctgy.getItemName());
        holder.itemPrice.setText("₹ "+ctgy.getItemPrice());
        holder.itemDescription.setText(ctgy.itemDescription);
        if(ctgy.isVeg()==false){
            holder.imageViewVeg.setImageResource(R.drawable.nvg);}
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.imgLike.setImageResource(R.drawable.heart);
            }
        });
      //  holder.itemDescription.setText(ctgy.getItemDescription());
        holder.cardViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.collapsible.getVisibility()==View.VISIBLE)
                {  holder.cardviewCount.setVisibility(View.GONE);
                    holder.cardviewAdd.setVisibility(View.GONE);
                   holder.collapsible.setVisibility(View.GONE);

                    final RotateAnimation rotateAnim = new RotateAnimation(0.0f, 0,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnim.setDuration(100);
                    rotateAnim.setFillAfter(true);
                    holder.imageViewRotate.startAnimation(rotateAnim);
                }
                else{
                    holder.cardviewCount.setVisibility(View.VISIBLE);
                    holder.cardviewAdd.setVisibility(View.VISIBLE);
                    holder.collapsible.setVisibility(View.VISIBLE);
                    final RotateAnimation rotateAnim = new RotateAnimation(0.0f, 90,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnim.setDuration(100);
                    rotateAnim.setFillAfter(true);
                    holder.imageViewRotate.startAnimation(rotateAnim);
                }

            }
        });
        final int[] countVal = {1};

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(countVal[0]>1)
               {countVal[0]-=1;}
                holder. tvItemCount.setText(Integer.toString(countVal[0]));
            }
        });
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {countVal[0]+=1;}
                holder. tvItemCount.setText(Integer.toString(countVal[0]));
            }
        });
        holder.cardviewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // myDialog.dismiss();

                Order order=new Order();
                order.setItemName(Menuitemlist.get(holder.getAdapterPosition()).getItemName());
                order.setItemCount(countVal[0]);
                order.setItemPrice(Integer.parseInt(Menuitemlist.get(holder.getAdapterPosition()).getItemPrice()));
                holder.addItemToCart(order,1);
                countVal[0]=1;
                holder. tvItemCount.setText(Integer.toString(countVal[0]));

             //   ((ViewGroup)viewPopUp.getParent()).removeView(viewPopUp);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Menuitemlist.size();
    }
    class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        Button btnPlus,btnMinus;

        TextView itemName,itemPrice,itemDescription,tvItemCount;
      ImageView imageViewRotate,imgLike,imageViewVeg;
        CardView cardViewCategory,cardviewAdd,cardviewCount;
        LinearLayout collapsible; List<Order> cart1;

        public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
            imageViewVeg=itemView.findViewById(R.id.imageView4);
            btnMinus=itemView.findViewById(R.id.button4);
            btnPlus=itemView.findViewById(R.id.button3);
            tvItemCount=itemView.findViewById(R.id.textView29);
            itemName=itemView.findViewById(R.id.textView8);itemDescription=itemView.findViewById(R.id.textView31);
            itemPrice=itemView.findViewById(R.id.textView28);
            imageViewRotate=itemView.findViewById(R.id.imageView32);
        cardViewCategory=itemView.findViewById(R.id.cardView3);cardviewAdd=itemView.findViewById(R.id.cardviewAdd);cardviewCount=itemView.findViewById(R.id.cardviewcount);
            collapsible=itemView.findViewById(R.id.collapsable);
imgLike=itemView.findViewById(R.id.imageView33);
        }
        public void addItemToCart(Order order,int flag2)
        { cart1=new ArrayList<>(10);
            cart1.add(order);
            order.setId(3);
            setDataInterface.addItemToCart(order,flag2,3);
            Toast toast = Toast.makeText(context.getApplicationContext(), "Item Added Successfully", Toast.LENGTH_SHORT);
          //  toast.show();
        }
    }}
