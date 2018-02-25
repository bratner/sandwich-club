package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    @BindView(R.id.also_known_tv) TextView mKnownAsTextView;
    @BindView(R.id.description_tv) TextView mDescriptionTextView;
    @BindView(R.id.origin_tv) TextView mOriginTextView;
    @BindView(R.id.ingredients_tv) TextView mIngridientsTextView;
    @BindView(R.id.image_iv) ImageView mSandwichImage;
    private Sandwich mSandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* ButterKnife gets rid of findViewByIds, but the fields must be package private (no qualifier) */
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
//        mKnownAsTextView = findViewById(R.id.also_known_tv);
//        mSandwichImage = findViewById(R.id.image_iv);
//        mDescriptionTextView = findViewById(R.id.description_tv);
//        mOriginTextView = findViewById(R.id.origin_tv);
//        mIngridientsTextView = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        mSandwich = JsonUtils.parseSandwichJson(json);
        if (mSandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(mSandwich.getImage())
                .into(mSandwichImage);

        setTitle(mSandwich.getMainName());

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        TextView aka_label = findViewById(R.id.also_known_label);
        TextView origin_label = findViewById(R.id.origin_label);

        /* Mandatory fields */
        mDescriptionTextView.setText(mSandwich.getDescription());
        String ingredients_text = TextUtils.join(", ", mSandwich.getIngredients());
        mIngridientsTextView.setText(ingredients_text);

        /* Optional fields */
        if (mSandwich.getAlsoKnownAs().isEmpty()) {
            aka_label.setVisibility(View.GONE);
            mKnownAsTextView.setVisibility(View.GONE);
        } else {
            aka_label.setVisibility(View.VISIBLE);
            mKnownAsTextView.setVisibility(View.VISIBLE);
            String aka_text = TextUtils.join(", ", mSandwich.getAlsoKnownAs());
            mKnownAsTextView.setText(aka_text);
        }

        if (mSandwich.getPlaceOfOrigin().isEmpty())
        {
            origin_label.setVisibility(View.GONE);
            mOriginTextView.setVisibility(View.GONE);
        } else {
            origin_label.setVisibility(View.VISIBLE);
            mOriginTextView.setVisibility(View.VISIBLE);
            mOriginTextView.setText(mSandwich.getPlaceOfOrigin());
        }

    }
}
