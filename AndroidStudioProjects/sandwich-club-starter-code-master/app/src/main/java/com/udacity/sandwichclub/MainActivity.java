package com.udacity.sandwichclub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Sandwich m;
    public SandwichAdapter sandwichAdapter;
    public ArrayList<Sandwich> sampel = new ArrayList<Sandwich>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sandwiches);

        // Simplification: Using a ListView instead of a RecyclerView
        ListView listView = findViewById(R.id.sandwiches_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle intent = new Bundle();
                m = (Sandwich)adapterView.getAdapter().getItem(i);

                String Name = m.getMainName();
                List<String> also = m.getAlsoKnownAs();
                String origin = m.getPlaceOfOrigin();
                String desc = m.getDescription();
                List<String> ingredients = m.getIngredients();


                Log.e("Name","::::::"+Name);

                intent.putString("mainName",Name);
                intent.putString("alsoKnownAs", String.valueOf(also));
                intent.putString("placeOfOrigin",origin);
                intent.putString("Description",desc);
                intent.putString("ingredients", String.valueOf(ingredients));


                Intent main = new Intent(getApplicationContext(), DetailActivity.class);
                main.putExtra(DetailActivity.EXTRA_POSITION, i);
                startActivity(main);
            }
        });
    }
}
