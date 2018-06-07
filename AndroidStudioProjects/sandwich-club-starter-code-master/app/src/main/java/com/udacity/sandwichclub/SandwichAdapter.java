package com.udacity.sandwichclub;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by lenovo-pc on 6/5/2018.
 */

public class SandwichAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Sandwich> register;

    public SandwichAdapter(Activity activity, List<Sandwich> bookItems) {
        this.activity = activity;
        this.register = bookItems;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.activity_detail, null);

        ImageView ingredientsIv = view.findViewById(R.id.image_iv);
        TextView name = view.findViewById(R.id.main);
        TextView also = view.findViewById(R.id.also);
        TextView incredients = view.findViewById(R.id.ingredients);
        TextView description = view.findViewById(R.id.desc);
        TextView place = view.findViewById(R.id.origin);

        final Sandwich m = register.get(i);
        // title
        name.setText(m.getMainName());
        also.setText((CharSequence) m.getAlsoKnownAs());
        description.setText(m.getDescription());
        place.setText(m.getPlaceOfOrigin());
        incredients.setText((CharSequence) m.getIngredients());
        Glide.with(activity).load(m.getImage()).into(ingredientsIv);


        return view;
    }
}

