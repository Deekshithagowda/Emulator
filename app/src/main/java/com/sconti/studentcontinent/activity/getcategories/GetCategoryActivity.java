package com.sconti.studentcontinent.activity.getcategories;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.getcategories.fragments.CategoryFragment;
import com.sconti.studentcontinent.activity.getcategories.fragments.LevelOneFragment;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.CategoryModel;
import com.sconti.studentcontinent.model.LevelOneModel;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_GAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_OUTDOOR;

public class GetCategoryActivity extends BaseActivity implements
        CategoryFragment.OnFragmentInteractionListener, LevelOneFragment.OnFragmentInteractionListener {
    private static final String TAG = "GetCategoryActivity";

    private String Category;


    @Override
    protected int getContentView() {
        return R.layout.activity_get_category;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Category = getIntent().getStringExtra("CATEGORY");
        Fragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CATEGORY", Category);
        fragment.setArguments(bundle);
        addFragment(fragment, false);
    }

    private void addFragment(Fragment fragment, boolean isReplace) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isReplace)
        {
            fragmentTransaction.replace(R.id.fragment_container, fragment, "Category");
            fragmentTransaction.addToBackStack(null);
        }
        else
        {
            fragmentTransaction.add(R.id.fragment_container, fragment, "Category");
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onCategorySelected(CategoryModel uri) {
        Fragment hello = new LevelOneFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CAT_ID", uri.getC_sl_no());
        hello.setArguments(bundle);
       addFragment(hello, true);
       // fragmentTransaction.commit();
    }

    @Override
    public void onLevelOneSelected(LevelOneModel uri) {
        Log.d(TAG, "onLevelOneSelected: "+uri.getName());
        if(Category.equalsIgnoreCase("1"))
        {
            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_KNOWLEDGE, uri.getCategory()+"_"+uri.getL1_sl_no());
        }
        else if(Category.equalsIgnoreCase("2"))
        {
            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_OUTDOOR, uri.getCategory()+"_"+uri.getL1_sl_no());
        }
        else if(Category.equalsIgnoreCase("3")){
            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_GAME, uri.getCategory()+"_"+uri.getL1_sl_no());
        }
        else if(Category.equalsIgnoreCase("4"))
        {
            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION, uri.getCategory()+"_"+uri.getL1_sl_no());
        }
        finish();

    }
}
