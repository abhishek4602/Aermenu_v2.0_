package com.zappfoundry.aermenu_v20;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHandler extends RecyclerView.Adapter<OrderHandler.RecyclerViewHolder> {
TextView textView;
SetDataInterface setDataInterface;
    private Context orderContext;
    private List<Order> cart;
    public List<Order> setList (List<Order> cart1){ this.cart=cart1; return cart;}

   public OrderHandler(Context orderContext, List<Order> cart, SetDataInterface setDataInterface1)


   {
        this.orderContext=orderContext;
        this.cart=cart;

        this.setDataInterface=setDataInterface1;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(orderContext);
        View view= layoutInflater.inflate(R.layout.summaryitem,null);


        return new RecyclerViewHolder(view);
    }

    public  List<Order> GetCartValues()
    {
        return  cart;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int i) {
Order order = cart.get(i);
holder.orderTextViewName.setText(order.getItemName());
holder.orderTextViewPrice.setText("₹"+" "+order.getItemTotal());
holder.orderTextViewCount.setText("x"+" "+order.getItemCount());
holder.setListenerOrder(i,order);
        if(order.isFromDB()==true)
        {
            holder.orderTextViewName.setTextColor(R.color.Grey);
            holder.orderTextViewPrice.setTextColor(R.color.Grey);
            holder.orderTextViewCount.setTextColor(R.color.Grey);
            holder.btnDeleteItem.setTextColor(R.color.Grey);
            holder.btnDeleteItem.setText("ORDERED");
            holder.btnDeleteItem.setEnabled(false);
            holder.imageviewNew.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }





    class  RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView orderTextViewName,orderTextViewPrice,orderTextViewCount;
        int position;
        Button btnDeleteItem=itemView.findViewById(R.id.pushbutton);
        int i1;
        Order orderSample;
        ImageView imageviewNew=itemView.findViewById(R.id.imageViewNewLabel);
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            orderTextViewName=itemView.findViewById(R.id.itemNameTextSCUI);
            orderTextViewPrice=itemView.findViewById(R.id.itemPriceTextSCUI);
            orderTextViewCount=itemView.findViewById(R.id.itemCountTextSCUI);

        }



        public  void setPosition (int position){this.position=position;}
        public void refreshdata()
        {
           notifyItemInserted(position);
        }

        public void setListenerOrder(int i, Order order)
        {

            btnDeleteItem.setOnClickListener(RecyclerViewHolder.this);
            this.i1=i;
            this.orderSample=order;
        }

        @Override
        public void onClick(View view) {
          //  pushOrder.pushOrderToDB(cart);
            DeleteItemFromCart(i1,orderSample.getId());
        }
    }


        public void receiveData(Order itemName){
        Order order=new Order();
        //order.setItemName("ABCD");
        //order.setItemCount("CNOP");
       order=itemName;
        cart.add(order);
//notifyItemInserted(1);

        }
        public void DeleteItemFromCart(int i,int id){
       //cart.remove(i);
            Order o=cart.get(i);
            o.setItemCount(0);
            setDataInterface.DeletefromCart(o,id);
  //     notifyDataSetChanged();
        }

}
