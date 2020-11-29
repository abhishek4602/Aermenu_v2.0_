package com.zappfoundry.aermenu_v20;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerViewHandler extends RecyclerView.Adapter<CategoryRecyclerViewHandler.RecyclerViewHolder>  {
  List<CategoryItem> categoryItemList;
    List<CardView>cardViewList = new ArrayList<>();
  Context context;
  Boolean isFirstTime=true;
    int selectedPosition = 0;
  IAttachMenu iAttachMenu;
    public CategoryRecyclerViewHandler(List<CategoryItem> ctrgyListReceived, Context receivedContext ,IAttachMenu iAttachMenu1)
    {
        this.categoryItemList=ctrgyListReceived;
        this.context=receivedContext;
        this.iAttachMenu=iAttachMenu1;

    }
    @NonNull
    @Override
    public CategoryRecyclerViewHandler.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.categories_item,null);
        final RecyclerViewHolder rvHolder=new RecyclerViewHolder(view);
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryRecyclerViewHandler.RecyclerViewHolder holder, final int position) {
        final int[] row_index = new int[1];
        final CategoryItem ctgy =categoryItemList.get(position);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorHighlight, typedValue, true);
        @ColorInt final int color = typedValue.data;
        if(position == selectedPosition){
            holder.cardViewCategory.callOnClick();
            Log.e("Holder","onClickCalled");
        }
        Log.e("cgy", ctgy.CategoryItemName);
        Log.e("pos", String.valueOf(position));
        Log.e("pos",ctgy.imageURL);
        if(isFirstTime)
        {
            if(position==0)
            {

                holder.cardViewCategory.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                ViewGroup viewGroup = ((ViewGroup)holder.cardViewCategory.getChildAt(0));
                Log.e("0",viewGroup.toString());
                ((RelativeLayout)viewGroup.getChildAt(1)).setVisibility(View.VISIBLE);
                ViewGroup RLImage=((ViewGroup)viewGroup.getChildAt(0));
             //   ((RelativeLayout)RLImage ).setBackgroundColor(Color.parseColor("#C0392B"));
                ((RelativeLayout)RLImage ).setBackgroundColor(color);
                //  ((RelativeLayout)RLImage ).setAlpha((float) 0.8);
                ViewGroup RLText=((ViewGroup)viewGroup.getChildAt(1));
              //  ((RelativeLayout)RLText ).setBackgroundColor(Color.parseColor("#C0392B"));
                ((RelativeLayout)RLText ).setBackgroundColor(color);
                // ((RelativeLayout)RLText ).setAlpha((float) 0.8);
                ((TextView)RLText.getChildAt(0)).setTextColor(Color.parseColor("#FFFFFF"));
                ((TextView)RLText.getChildAt(1)).setTextColor(Color.parseColor("#FFFFFF"));
            }
            isFirstTime=false;
        }

        row_index[0]=-1;
        holder.categoryName.setText(String.valueOf( ctgy.getCategoryItemName()));
        holder.itemCount.setText(String.valueOf(ctgy.getItemCount())+ " Items");
        if (!cardViewList.contains(holder.cardViewCategory)) {
            cardViewList.add(holder.cardViewCategory);
            Picasso.get( ).cancelRequest(holder.imageViewBackground);

            Picasso.get().load( ctgy.getImageURL()).into(holder.imageViewBackground);
            Log.e("category",ctgy.getCategoryItemName());
            Log.e("IMGURL",ctgy.getImageURL());
        }
        /*switch (ctgy.getCategoryItemName()){
            case "Burgers":  holder.imageViewBackground.setImageResource(R.drawable.burgcircle);break;
            case "Panini" :holder.imageViewBackground.setImageResource(R.drawable.panini);break;
            case "Sandwiches":  holder.imageViewBackground.setImageResource(R.drawable.sw);break;
            case "Frankies":  holder.imageViewBackground.setImageResource(R.drawable.frankie);break;
            case "Fries":  holder.imageViewBackground.setImageResource(R.drawable.fries);break;
            case "Siders":  holder.imageViewBackground.setImageResource(R.drawable.bread);break;
    }*/
        holder.cardViewCategory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view)
            {
                iAttachMenu.ReloadMenu(ctgy.getCategoryItemName());
             /*   row_index[0]=position;
                notifyDataSetChanged();*/
                Toast toast=Toast.makeText(context,"Selected is"+ctgy.getCategoryItemName(),Toast.LENGTH_SHORT);
              //  toast.show();


                for(CardView cardView : cardViewList)
                {
                    ViewGroup viewGroup = ((ViewGroup)cardView.getChildAt(0));
                    Log.e("0",viewGroup.toString());
                   // ((RelativeLayout)viewGroup.getChildAt(1)).setVisibility(View.GONE);
                    ViewGroup RLImage=((ViewGroup)viewGroup.getChildAt(0));
                    ((RelativeLayout)RLImage ).setBackgroundColor(Color.parseColor("#FFFFFF"));
                    ViewGroup RLText=((ViewGroup)viewGroup.getChildAt(1));
                    ((RelativeLayout)RLText ).setBackgroundColor(Color.parseColor("#FFFFFF"));
                    ((TextView)RLText.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                    ((TextView)RLText.getChildAt(1)).setTextColor(Color.parseColor("#000000"));


                   /* ((LinearLayout)viewGroup ).setBackgroundColor(Color.parseColor("#FFFFFF"));*//*
                    ((TextView)viewGroup.getChildAt(2)).setTextColor(Color.parseColor("#000000"));
                    ((TextView)viewGroup.getChildAt(3)).setTextColor(Color.parseColor("#000000"));*//*
                    ((RelativeLayout)viewGroup.getChildAt(3)).setVisibility(View.INVISIBLE);
                    *//*holder.rl.setBackgroundColor(Color.parseColor("#ffffff"));
                    holder.categoryName.setTextColor(Color.parseColor("#1c2833"));
                    holder.itemCount.setTextColor(Color.parseColor("#1c2833"));*/
                }


                holder.cardViewCategory.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                ViewGroup viewGroup = ((ViewGroup)holder.cardViewCategory.getChildAt(0));
                Log.e("0",viewGroup.toString());
                ((RelativeLayout)viewGroup.getChildAt(1)).setVisibility(View.VISIBLE);
                ViewGroup RLImage=((ViewGroup)viewGroup.getChildAt(0));
               // ((RelativeLayout)RLImage ).setBackgroundColor(Color.parseColor("#C0392B"));
                ((RelativeLayout)RLImage ).setBackgroundColor(color);
              //  ((RelativeLayout)RLImage ).setAlpha((float) 0.8);
                ViewGroup RLText=((ViewGroup)viewGroup.getChildAt(1));
               // ((RelativeLayout)RLText ).setBackgroundColor(Color.parseColor("#C0392B"));
                ((RelativeLayout)RLText ).setBackgroundColor(color);

                // ((RelativeLayout)RLText ).setAlpha((float) 0.8);
                ((TextView)RLText.getChildAt(0)).setTextColor(Color.parseColor("#FFFFFF"));
                ((TextView)RLText.getChildAt(1)).setTextColor(Color.parseColor("#FFFFFF"));

             /*   ViewGroup viewGroup = ((ViewGroup)holder.cardViewCategory.getChildAt(1));
                Log.e("0",viewGroup.toString());
                ((RelativeLayout)viewGroup ).setBackgroundColor(Color.parseColor("#000000"));*//*
                ((TextView)viewGroup.getChildAt(1)).setTextColor(Color.parseColor("#ffffff"));
                ((TextView)viewGroup.getChildAt(2)).setTextColor(Color.parseColor("#ffffff"));*//*
                ((RelativeLayout)viewGroup.getChildAt(3)).setVisibility(View.VISIBLE);*/


            }


        });
/*
        if(row_index[0]==position){
            holder.rl.setBackgroundColor(Color.parseColor("#1c2833"));
            holder.categoryName.setTextColor(Color.parseColor("#ffffff"));
            holder.itemCount.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.rl.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.categoryName.setTextColor(Color.parseColor("#1c2833"));
            holder.itemCount.setTextColor(Color.parseColor("#1c2833"));
        }*/

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }
    class RecyclerViewHolder extends RecyclerView.ViewHolder
    {

        TextView categoryName,itemCount;
        ImageView imageViewBackground;
        CardView cardViewCategory;
        RelativeLayout rl;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName=itemView.findViewById(R.id.tvCategoryName);
            itemCount=itemView.findViewById(R.id.tvItemCount);
            imageViewBackground=itemView.findViewById(R.id.imageViewCategory_item);
            cardViewCategory=itemView.findViewById(R.id.Cardview_Category_Item);
            rl= itemView.findViewById(R.id.rl);
    }

}}
