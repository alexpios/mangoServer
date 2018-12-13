package by.kuchinsky.alexandr.komilfoserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import by.kuchinsky.alexandr.komilfoserver.Interface.ItemClickListener;
import by.kuchinsky.alexandr.komilfoserver.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderDate;
        private ItemClickListener itemClickListener;

        public OrderViewHolder(View itemView) {
            super(itemView);

            txtOrderDate = (TextView)itemView.findViewById(R.id.order_adress);
            txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
            txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
            txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
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
        menu.add(0, 0, getAdapterPosition(), "Обновить");
        menu.add(0, 1, getAdapterPosition(), "Удалить");


    }
}