package com.example.shelterconnect.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.model.Item;

import java.util.ArrayList;

/**
 * Edit Item Adapter
 * Created by daniel on 3/4/18.
 */
public class ItemEditAdapter extends RecyclerView.Adapter<ItemEditAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    private ItemViewHolder holder;
    private int position;
    private Item currItem;
    // private Context adapterContext;

    public ItemEditAdapter(ArrayList<Item> myDataset) {
        this.items = myDataset;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView price;
        EditText quantity;

        ItemViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.cv);
            name = (TextView) v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
            quantity = (EditText) v.findViewById(R.id.quantity);
        }
    }

    public void add(int position, Item item) {
        this.items.add(position, item);
        this.notifyItemInserted(position);
    }

    public void remove(int position) {
        this.items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_edit, viewGroup, false);
        ItemViewHolder iVH = new ItemViewHolder(v);
        return iVH;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        currItem = items.get(position);
        holder.name.setText(currItem.getName());
        holder.price.setText(Double.toString(currItem.getPrice()));
        holder.quantity.setText(Integer.toString(currItem.getQuantity()));

        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                currItem.setEdited(true);

                if(!holder.quantity.getText().toString().isEmpty()) {
                    currItem.setQuantity(Integer.parseInt(holder.quantity.getText().toString()));
                    System.out.println(currItem.getQuantity());
                } else if (TextUtils.isEmpty(holder.quantity.getText().toString().trim())) {
                    holder.quantity.setError("Quantity must not be empty");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
