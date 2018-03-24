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
import com.example.shelterconnect.model.Requestnew;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by parth on 20-03-2018.
 */

public class EditRequestAdapter extends RecyclerView.Adapter<EditRequestAdapter.RequestViewHolder> {

    private ArrayList<Requestnew> requests;
    private RequestViewHolder holder;
    private int position;
    private Requestnew currItem;
    // private Context adapterContext;

    public EditRequestAdapter(ArrayList<Requestnew> myDataset) {
        this.requests = myDataset;
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        EditText needed;
        EditText raised;
        TextView quantity;
        TextView workerID;
        TextView itemID;
        TextView active;

        RequestViewHolder(View v) {
            super(v);
            cv =  v.findViewById(R.id.cv);
            name =  v.findViewById(R.id.name);
            needed =  v.findViewById(R.id.required);
            quantity =  v.findViewById(R.id.quantity);
            raised =  v.findViewById(R.id.raised);
       //     workerID = v.findViewById(R.id.workerID);
         //   itemID =  v.findViewById(R.id.itemID);
           // active = v.findViewById(R.id.active);
        }
    }

    public void add(int position, Requestnew request) {
        this.requests.add(position, request);
        this.notifyItemInserted(position);
    }

    public void remove(int position) {
        this.requests.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_list_edit, viewGroup, false);
        RequestViewHolder iVH = new RequestViewHolder(v);
        return iVH;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {

        currItem = requests.get(position);
        holder.name.setText(currItem.getRequestID());
        holder.needed.setText(Double.toString(currItem.getAmountNeeded()));
        holder.raised.setText(Double.toString(currItem.getAmountRaised()));

        holder.raised.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
          public void afterTextChanged(Editable editable) {
              currItem.setEdited(true);

               if(!holder.raised.getText().toString().isEmpty()) {
                currItem.setAmountRaised(Integer.parseInt(holder.raised.getText().toString()));
                System.out.println(currItem.getAmountRaised());
              } else if (TextUtils.isEmpty(holder.raised.getText().toString().trim())) {
                holder.raised.setError("Amount Raised must not be empty");
              }
            }


        });
        holder.needed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                currItem.setEdited(true);

                if(!holder.needed.getText().toString().isEmpty()) {
                    currItem.setAmountNeeded(Integer.parseInt(holder.needed.getText().toString()));
                    System.out.println(currItem.getAmountNeeded());
                } else if (TextUtils.isEmpty(holder.needed.getText().toString().trim())) {
                    holder.needed.setError("Amount Required must not be empty");
                }
            }


        });

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}

