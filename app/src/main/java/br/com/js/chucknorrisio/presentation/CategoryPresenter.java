package br.com.js.chucknorrisio.presentation;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import br.com.js.chucknorrisio.Colors;
import br.com.js.chucknorrisio.MainActivity;
import br.com.js.chucknorrisio.datasource.CategoryRemoteDatasource;
import br.com.js.chucknorrisio.model.CategoryItem;

public class CategoryPresenter implements CategoryRemoteDatasource.ListCategoriesCallback {

    private final CategoryRemoteDatasource datasource;
    private final MainActivity view;

    public CategoryPresenter(MainActivity mainActivity, CategoryRemoteDatasource datasource) {
        this.view = mainActivity;
        this.datasource = datasource;
    }

    public void requestAll() {
        this.view.showProgressBar();
        this.datasource.findAll(this);
    }

    @Override
    public void onSuccess(List<String> response) {
        List<CategoryItem> categoryItems = new ArrayList<>();
        for (String val : response) {
            categoryItems.add(new CategoryItem(val, Colors.randomColor()));
        }
        view.showCategories(categoryItems);
    }

    public void onError(String message) {
        this.view.showFailure(message);
    }

    @Override
    public void onComplete() {
        view.hideProgressBar();
    }
}