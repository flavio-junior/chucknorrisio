package br.com.js.chucknorrisio.model;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import br.com.js.chucknorrisio.R;

public class CategoryItem extends Item<ViewHolder> {

    private final String categoryName;
    private final int bgColor;

    public CategoryItem(String categoryName, int bgColor) {
        this.categoryName = categoryName;
        this.bgColor = bgColor;
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView txtCategory = viewHolder.itemView.findViewById(R.id.txt_category);
        txtCategory.setText(categoryName);
        viewHolder.itemView.setBackgroundColor(bgColor);
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public int getLayout() {
        return R.layout.card_category;
    }
}
