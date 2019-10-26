package ch.appquest.nico.memory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTextView;
        public ImageView itemImageView;

        public MyViewHolder(View v) {
            super(v);
            itemTextView = (TextView) v.findViewById(R.id.textViewItem);
            itemImageView = (ImageView) v.findViewById(R.id.imageViewItem);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memory_layout, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - replace the contents of the view with new elements
        holder.itemTextView.setText(MainActivity.textStrings.get(position));
        holder.itemImageView.setImageBitmap(bitmapAusgeben(MainActivity.imagePaths.get(position)));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return MainActivity.textStrings.size();
    }

    private Bitmap bitmapAusgeben(String path){
        return BitmapFactory.decodeFile(path);
    }
}
