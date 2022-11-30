package edu.uga.cs.finalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListItemRecyclerAdapter extends RecyclerView.Adapter<ListItemRecyclerAdapter.ListItemHolder> {

    public static final String DEBUG_TAG = "JobLeadRecyclerAdapter";

    private List<ListItem> productList;
    private Context context;

    public ListItemRecyclerAdapter( List<ListItem> productList, Context context ) {
        this.productList = productList;
        this.context = context;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class ListItemHolder extends RecyclerView.ViewHolder {

        TextView productName;

        public ListItemHolder(View itemView ) {
            super(itemView);
            productName = itemView.findViewById( R.id.productName );
        }
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.list_item, parent, false );
        return new ListItemHolder( view );
    }

    // This method fills in the values of the Views to show a JobLead
    @Override
    public void onBindViewHolder( ListItemHolder holder, int position ) {
        ListItem jobLead = productList.get( position );

        Log.d( DEBUG_TAG, "onBindViewHolder: " + jobLead );

        String key = jobLead.getKey();
        String productName = jobLead.getProductName();

        holder.productName.setText( jobLead.getProductName());

        //add onClickListener to list item
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                EditProductDialogFragment editProductDialogFragment =
                        EditProductDialogFragment.newInstance( holder.getAdapterPosition(), key, productName);
                editProductDialogFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
}
