package com.zappfoundry.aermenu_v20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderSummaryHandler extends RecyclerView.Adapter<OrderSummaryHandler.RecyclerViewHolder>  {
    TextView textView;
    SetDataInterface setDataInterface;
    private Context orderContext;
    private List<Order> cart;
    public List<Order> setList (List<Order> cart1){ this.cart=cart1; return cart;}

    public OrderSummaryHandler(Context orderContext, List<Order> cart )


    {
        this.orderContext=orderContext;
        this.cart=cart;


    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(orderContext);
        View view= layoutInflater.inflate(R.layout.order_summary_item,null);


        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Order order = cart.get(position);
        holder.orderTextViewName.setText(order.getItemName());
        holder.orderTextViewPrice.setText("₹"+" "+order.getItemPrice());
        holder.orderTextViewCount.setText("x"+" "+order.getItemCount());
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }


    class  RecyclerViewHolder extends RecyclerView.ViewHolder   {
        TextView orderTextViewName,orderTextViewPrice,orderTextViewCount;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            orderTextViewName=itemView.findViewById(R.id.itemnameSummary);
            orderTextViewPrice=itemView.findViewById(R.id.itemPriceSummary);
            orderTextViewCount=itemView.findViewById(R.id.itemCountSummary);

        }






    }




}



