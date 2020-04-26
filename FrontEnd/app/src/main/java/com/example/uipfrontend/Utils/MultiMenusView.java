package com.example.uipfrontend.Utils;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.qlh.dropdownmenu.adapter.TextAdapter;
import com.qlh.dropdownmenu.view.ViewBaseAction;
import com.qlh.dropdownmenu.R.dimen;
import com.qlh.dropdownmenu.R.drawable;
import com.qlh.dropdownmenu.R.id;
import com.qlh.dropdownmenu.R.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class MultiMenusView extends LinearLayout implements ViewBaseAction {
    private ListView levelOneMenuListView;
    private ListView levelTwoMenuListView;
    private String[] levelOneMenu;
    private ArrayList<String> levelOneMenusList = new ArrayList();
    private String[][] levelTwoMenu;
    private LinkedList<String> levelTwoMenusList = new LinkedList();
    private SparseArray<LinkedList<String>> children = new SparseArray();
    private TextAdapter levelTwoMenuListViewAdapter;
    private TextAdapter levelOneMenuListViewAdapter;
    private MultiMenusView.OnSelectListener mOnSelectListener;
    private int levelOneMenuPosition = 0;
    private int levelTwoMenuPosition = 0;
    private String menuTwo = "不限";
    private String menuOne = "不限";

    public MultiMenusView(Context context, String[] levelOneMenu, String[][] levelTwoMenu) {
        super(context);
        this.levelOneMenu = levelOneMenu;
        this.levelTwoMenu = levelTwoMenu;
        this.init(context);
    }

    public MultiMenusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public void updateShowText(String showArea, String showBlock) {
        if (showArea != null && showBlock != null) {
            int j;
            for(j = 0; j < this.levelOneMenusList.size(); ++j) {
                if (((String)this.levelOneMenusList.get(j)).equals(showArea)) {
                    this.levelOneMenuListViewAdapter.setSelectedPosition(j);
                    this.levelTwoMenusList.clear();
                    if (j < this.children.size()) {
                        this.levelTwoMenusList.addAll((Collection)this.children.get(j));
                    }

                    this.levelOneMenuPosition = j;
                    break;
                }
            }

            for(j = 0; j < this.levelTwoMenusList.size(); ++j) {
                if (((String)this.levelTwoMenusList.get(j)).replace("不限", "").equals(showBlock.trim())) {
                    this.levelTwoMenuListViewAdapter.setSelectedPosition(j);
                    this.levelTwoMenuPosition = j;
                    break;
                }
            }

            this.setDefaultSelect();
        }
    }

    public void setMenuContent() {
        for(int i = 0; i < this.levelOneMenu.length; ++i) {
            this.levelOneMenusList.add(this.levelOneMenu[i]);
            LinkedList<String> tItem = new LinkedList();

            for(int j = 0; j < this.levelTwoMenu[i].length; ++j) {
                tItem.add(this.levelTwoMenu[i][j]);
            }

            this.children.put(i, tItem);
        }

    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layout.view_multi_menu, this, true);
        this.levelOneMenuListView = (ListView)this.findViewById(id.level_one_menu_ls);
        this.levelTwoMenuListView = (ListView)this.findViewById(id.level_two_menu_ls);
        this.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.setMenuContent();
        this.levelOneMenuListViewAdapter = new TextAdapter(context, this.levelOneMenusList, drawable.choose_item_selected, drawable.choose_level_one_menu_item_selector, Color.parseColor("#0084ff"), -16777216);
        this.levelOneMenuListViewAdapter.setTextSize(this.getResources().getDimension(dimen.x43));
        this.levelOneMenuListViewAdapter.setSelectedPositionNoNotify(this.levelOneMenuPosition);
        this.levelOneMenuListView.setAdapter(this.levelOneMenuListViewAdapter);
        this.levelOneMenuListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {
            public void onItemClick(View view, int position) {
                MultiMenusView.this.menuOne = (String) MultiMenusView.this.levelOneMenusList.get(position);
                if (position < MultiMenusView.this.children.size()) {
                    MultiMenusView.this.levelTwoMenusList.clear();
                    MultiMenusView.this.levelTwoMenusList.addAll((Collection) MultiMenusView.this.children.get(position));
                    MultiMenusView.this.levelTwoMenuListViewAdapter.notifyDataSetChanged();

                    if (MultiMenusView.this.mOnSelectListener != null) {
                        MultiMenusView.this.mOnSelectListener.getMenuOne(MultiMenusView.this.menuOne,position);
                    }

                }

            }
        });
        if (this.levelOneMenuPosition < this.children.size()) {
            this.levelTwoMenusList.addAll((Collection)this.children.get(this.levelOneMenuPosition));
        }

        this.levelTwoMenuListViewAdapter = new TextAdapter(context, this.levelTwoMenusList, drawable.choose_item_selected, drawable.choose_level_two_menu_item_selector, Color.parseColor("#0084ff"), -16777216);
        this.levelTwoMenuListViewAdapter.setTextSize(this.getResources().getDimension(dimen.x43));
        this.levelTwoMenuListView.setAdapter(this.levelTwoMenuListViewAdapter);
        this.levelTwoMenuListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {
            public void onItemClick(View view, int position) {
                MultiMenusView.this.menuTwo = (String) MultiMenusView.this.levelTwoMenusList.get(position);
                if (MultiMenusView.this.mOnSelectListener != null) {
                    MultiMenusView.this.mOnSelectListener.getMenuTwo(MultiMenusView.this.menuTwo,position);
                }

            }
        });
        if (this.levelTwoMenuPosition < this.levelTwoMenusList.size()) {
            this.menuTwo = (String)this.levelTwoMenusList.get(this.levelTwoMenuPosition);
        }

        if (this.menuTwo.contains("不限")) {
            this.menuTwo = this.menuTwo.replace("不限", "");
        }

        this.setDefaultSelect();
    }

    public void setDefaultSelect() {
        this.levelOneMenuListView.setSelection(this.levelOneMenuPosition);
        this.levelTwoMenuListView.setSelection(this.levelTwoMenuPosition);
    }

    public String getShowTextOne() {
        return this.menuOne;
    }

    public String getShowTextTwo() {
        return this.menuTwo;
    }

    public void setOnSelectListener(MultiMenusView.OnSelectListener onSelectListener) {
        this.mOnSelectListener = onSelectListener;
    }

    public void hide() {
    }

    public void show() {
    }

    public interface OnSelectListener {
        void getMenuOne(String var1,int position);
        void getMenuTwo(String var1,int position);

    }
}

