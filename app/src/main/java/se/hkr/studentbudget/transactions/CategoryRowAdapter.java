package se.hkr.studentbudget.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategoryRowAdapter extends ArrayAdapter<CategoryRowItem> {

    public CategoryRowAdapter(Context context, ArrayList<CategoryRowItem> spendRowItem) {
        super(context, 0, spendRowItem);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View converView, ViewGroup parent) {
        if (converView == null) {
            converView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spend_categories_spinner_row, parent, false);
        }

        ImageView imageViewSpendIcon = converView.findViewById(R.id.image_spinner_spend_icon);
        TextView textViewSPendName = converView.findViewById(R.id.image_spinner_spend_text);

        CategoryRowItem currentItem = getItem(position);
        if (currentItem != null) {
            imageViewSpendIcon.setImageResource(currentItem.getmCategoryIcon());
            textViewSPendName.setText(currentItem.getmCategoryName());
        }
        return converView;
    }
}
