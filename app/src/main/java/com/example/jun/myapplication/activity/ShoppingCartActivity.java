package com.example.jun.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jun.myapplication.R;
import com.example.jun.myapplication.adapter.ShoppingCartAdapter;
import com.example.jun.myapplication.bean.GoodsBean;
import com.example.jun.myapplication.bean.StoreBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JUN
 */
public class ShoppingCartActivity extends BaseActivity {

    //定义父列表项List数据集合
    List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>();
    //定义子列表项List数据集合
    List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();

    ShoppingCartAdapter myBaseExpandableListAdapter;
    CheckBox id_cb_select_all;
    LinearLayout id_ll_normal_all_state;
    LinearLayout id_ll_editing_all_state;
    ExpandableListView expandableListView;
    RelativeLayout id_rl_cart_is_empty;
    RelativeLayout id_rl_foot;

    TextView id_tv_edit_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_shopping_cart);

        expandableListView = (ExpandableListView) findViewById(R.id.id_elv_listview);

        myBaseExpandableListAdapter = new ShoppingCartAdapter(this, parentMapList, childMapList_list);
        expandableListView.setAdapter(myBaseExpandableListAdapter);
        //展开全部
        for (int i = 0; i < parentMapList.size(); i++) {
            expandableListView.expandGroup(i);
        }

        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ShoppingCartActivity.this, "click：" + position, Toast.LENGTH_SHORT).show();
            }
        });

        //底部默认布局
        id_ll_normal_all_state = (LinearLayout) findViewById(R.id.id_ll_normal_all_state);
        //底部编辑状态下布局
        id_ll_editing_all_state = (LinearLayout) findViewById(R.id.id_ll_editing_all_state);
        //空布局
        id_rl_cart_is_empty = (RelativeLayout) findViewById(R.id.id_rl_cart_is_empty);
        //底部总布局
        id_rl_foot = (RelativeLayout) findViewById(R.id.id_rl_foot);
        //底部收藏按钮
        TextView id_tv_save_star_all = (TextView) findViewById(R.id.id_tv_save_star_all);
        id_tv_save_star_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShoppingCartActivity.this, "收藏多选商品", Toast.LENGTH_SHORT).show();
            }
        });

        //底部删除按钮
        TextView id_tv_delete_all = (TextView) findViewById(R.id.id_tv_delete_all);
        id_tv_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBaseExpandableListAdapter.removeGoods();
                Toast.makeText(ShoppingCartActivity.this, "删除多选商品", Toast.LENGTH_SHORT).show();
            }
        });

        //头部编辑按钮
        id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
        id_tv_edit_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof TextView) {
                    TextView tv = (TextView) v;
                    if (ShoppingCartAdapter.EDITING.equals(tv.getText())) {
                        myBaseExpandableListAdapter.setupEditingAll(true);
                        tv.setText(ShoppingCartAdapter.FINISH_EDITING);
                        changeFootShowDeleteView(true);//这边类似的功能 后期待使用观察者模式
                    } else {
                        myBaseExpandableListAdapter.setupEditingAll(false);
                        tv.setText(ShoppingCartAdapter.EDITING);
                        changeFootShowDeleteView(false);//这边类似的功能 后期待使用观察者模式
                    }

                }
            }
        });

        //底部全选按钮
        id_cb_select_all = (CheckBox) findViewById(R.id.id_cb_select_all);
        id_cb_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) v;
                    myBaseExpandableListAdapter.setupAllChecked(checkBox.isChecked());
                }
            }
        });
        //底部合计价格
        final TextView id_tv_totalPrice = (TextView) findViewById(R.id.id_tv_totalPrice);
        //底部结算
        final TextView id_tv_totalCount_jiesuan = (TextView) findViewById(R.id.id_tv_totalCount_jiesuan);
        id_tv_totalCount_jiesuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShoppingCartActivity.this, "click：结算", Toast.LENGTH_SHORT).show();
            }
        });

        //选中项计算费用注册
        myBaseExpandableListAdapter.setOnGoodsCheckedChangeListener(new ShoppingCartAdapter.OnGoodsCheckedChangeListener() {
            @Override
            public void onGoodsCheckedChange(int totalCount, double totalPrice) {
                id_tv_totalPrice.setText(String.format(getString(R.string.total), totalPrice));
                id_tv_totalCount_jiesuan.setText(String.format(getString(R.string.jiesuan), totalCount));
            }
        });

        //全选按钮回调注册
        myBaseExpandableListAdapter.setOnAllCheckedBoxNeedChangeListener(new ShoppingCartAdapter.OnAllCheckedBoxNeedChangeListener() {
            @Override
            public void onCheckedBoxNeedChange(boolean allParentIsChecked) {
                id_cb_select_all.setChecked(allParentIsChecked);
            }
        });

        //顶部编辑按钮回调注册
        myBaseExpandableListAdapter.setOnEditingTvChangeListener(new ShoppingCartAdapter.OnEditingTvChangeListener() {
            @Override
            public void onEditingTvChange(boolean allIsEditing) {

                changeFootShowDeleteView(allIsEditing);//这边类似的功能 后期待使用观察者模式

            }
        });

        //是否有商品回调注册
        myBaseExpandableListAdapter.setOnCheckHasGoodsListener(new ShoppingCartAdapter.OnCheckHasGoodsListener() {
            @Override
            public void onCheckHasGoods(boolean isHasGoods) {
                setupViewsShow(isHasGoods);
            }
        });
    }

    @Override
    public void initData() {
        for (int i = 0; i < 20; i++) {
            String store = "旗舰店";
            if (i % 2 == 0) {
                store = "专营店";
            }
            //提供父列表的数据
            Map<String, Object> parentMap = new HashMap<String, Object>();
            parentMap.put("parentName", new StoreBean("" + i, store + i, false, false));
            parentMapList.add(parentMap);

            //提供当前父列的子列数据
            List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < 3; j++) {
                Map<String, Object> childMap = new HashMap<String, Object>();
                GoodsBean goodsBean = new GoodsBean(i + "_" + j, store + i + "下的商品" + j, "url", "均码，红色", 150, 120, 1, GoodsBean.STATUS_VALID, false, false);
                childMap.put("childName", goodsBean);
                childMapList.add(childMap);
            }
            childMapList_list.add(childMapList);
        }
    }

    private void setupViewsShow(boolean isHasGoods) {
        if (isHasGoods) {
            expandableListView.setVisibility(View.VISIBLE);
            id_rl_cart_is_empty.setVisibility(View.GONE);
            id_rl_foot.setVisibility(View.VISIBLE);
            id_tv_edit_all.setVisibility(View.VISIBLE);
        } else {
            expandableListView.setVisibility(View.GONE);
            id_rl_cart_is_empty.setVisibility(View.VISIBLE);
            id_rl_foot.setVisibility(View.GONE);
            id_tv_edit_all.setVisibility(View.GONE);
        }
    }

    public void changeFootShowDeleteView(boolean showDeleteView) {

        if (showDeleteView) {
            id_tv_edit_all.setText(ShoppingCartAdapter.FINISH_EDITING);

            id_ll_normal_all_state.setVisibility(View.INVISIBLE);
            id_ll_editing_all_state.setVisibility(View.VISIBLE);
        } else {
            id_tv_edit_all.setText(ShoppingCartAdapter.EDITING);

            id_ll_normal_all_state.setVisibility(View.VISIBLE);
            id_ll_editing_all_state.setVisibility(View.INVISIBLE);
        }
    }


}
