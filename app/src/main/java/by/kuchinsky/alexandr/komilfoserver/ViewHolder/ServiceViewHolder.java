package by.kuchinsky.alexandr.komilfoserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.kuchinsky.alexandr.komilfoserver.Common.Common;
import by.kuchinsky.alexandr.komilfoserver.Interface.ItemClickListener;
import by.kuchinsky.alexandr.komilfoserver.R;

public class ServiceViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnCreateContextMenuListener {
    public TextView service_name;
    public ImageView service_image;
    private ItemClickListener itemClickListener;
    public ServiceViewHolder(View itemView) {
        super(itemView);

        service_name = (TextView)itemView.findViewById(R.id.service_name);
        service_image=(ImageView)itemView.findViewById(R.id.service_image);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Выберите действие");
        menu.add(0,0,getAdapterPosition(),Common.UPDATE);
        menu.add(0,1,getAdapterPosition(),Common.DELETE);
    }
}