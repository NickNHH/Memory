package ch.appquest.nico.memory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTextView;
        ImageView itemImageView;

        MyViewHolder(View v) {
            super(v);
            itemTextView = v.findViewById(R.id.itemTextView);
            itemImageView = v.findViewById(R.id.itemImageView);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rvItem_main, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - replace the contents of the view with new elements
        holder.itemTextView.setText(MainActivity.textStrings.get(position));
        holder.itemImageView.setImageBitmap(outputBitmap(MainActivity.imagePaths.get(position)));
    }

    // Return the size of your dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return MainActivity.textStrings.size();
    }

    private Bitmap outputBitmap(String path){
        return BitmapFactory.decodeFile(path);
    }
}
