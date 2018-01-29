package com.example.jun.myapplication.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jun.myapplication.R;
import com.example.jun.myapplication.bean.GoodsBean;
import com.example.jun.myapplication.bean.StoreBean;

import java.util.List;
import java.util.Map;

/**
 * Created by JUN on 2018/1/2.
 */

public class ShoppingCartAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "MyBaseEtAdapter";
    List<Map<String, Object>> parentMapList;
    List<List<Map<String, Object>>> childMapList_list;
    Context context;
    int totalCount = 0;
    double totalPrice = 0.00;
    public static final String EDITING = "编辑";
    public static final String FINISH_EDITING = "完成";

    OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener;
    OnGoodsCheckedChangeListener onGoodsCheckedChangeListener;
    OnCheckHasGoodsListener onCheckHasGoodsListener;
    OnEditingTvChangeListener onEditingTvChangeListener;

    public ShoppingCartAdapter(Context context, List<Map<String, Object>> parentMapList, List<List<Map<String, Object>>> childMapList_list) {
        this.parentMapList = parentMapList;
        this.childMapList_list = childMapList_list;
        this.context = context;
    }

    /**
     * 是否有商品回调
     *
     * @param onCheckHasGoodsListener
     */
    public void setOnCheckHasGoodsListener(OnCheckHasGoodsListener onCheckHasGoodsListener) {
        this.onCheckHasGoodsListener = onCheckHasGoodsListener;
    }

    /**
     * 顶部编辑按钮回调
     *
     * @param onEditingTvChangeListener
     */
    public void setOnEditingTvChangeListener(OnEditingTvChangeListener onEditingTvChangeListener) {
        this.onEditingTvChangeListener = onEditingTvChangeListener;
    }

    /**
     * 选中项计算费用
     *
     * @param onGoodsCheckedChangeListener
     */
    public void setOnGoodsCheckedChangeListener(OnGoodsCheckedChangeListener onGoodsCheckedChangeListener) {
        this.onGoodsCheckedChangeListener = onGoodsCheckedChangeListener;
    }

    /**
     * 全选按钮回调
     *
     * @param onAllCheckedBoxNeedChangeListener
     */
    public void setOnAllCheckedBoxNeedChangeListener(OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener) {
        this.onAllCheckedBoxNeedChangeListener = onAllCheckedBoxNeedChangeListener;
    }

    @Override
    public int getGroupCount() {
        return parentMapList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childMapList_list.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return parentMapList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childMapList_list.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.parent_layout, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_title_parent = (TextView) view
                    .findViewById(R.id.tv_title_parent);
            groupViewHolder.id_tv_edit = (TextView) view
                    .findViewById(R.id.id_tv_edit);
            groupViewHolder.id_cb_select_parent = (CheckBox) view
                    .findViewById(R.id.id_cb_select_parent);

            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
        final String parentName = storeBean.getName();
        groupViewHolder.tv_title_parent.setText(parentName);

        if (storeBean.isEditing()) {
            groupViewHolder.id_tv_edit.setText(FINISH_EDITING);
        } else {
            groupViewHolder.id_tv_edit.setText(EDITING);
        }
        //parent编辑/完成按钮监听
        groupViewHolder.id_tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "";
                TextView textView = null;
                if (view instanceof TextView) {
                    textView = (TextView) view;
                }
                textView.setText(text);
                //更新该parent下所有child的编辑状态
                setupEditing(i);
                //设置顶部编辑和底部状态监听回调
                onEditingTvChangeListener.onEditingTvChange(dealAllEditingIsEditing());
            }
        });
        //覆盖原有收起展开事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "店铺：" + parentName, Toast.LENGTH_SHORT).show();
            }
        });
        //parent全选按钮监听
        groupViewHolder.id_cb_select_parent.setChecked(storeBean.isChecked());
        final boolean nowBeanChecked = storeBean.isChecked();
        groupViewHolder.id_cb_select_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新该parent下所有child的选中状态，并计算总价
                setupOneParentAllChildChecked(!nowBeanChecked, i);
                //控制总checkedbox 接口
                onAllCheckedBoxNeedChangeListener.onCheckedBoxNeedChange(dealAllParentIsChecked());
            }
        });


        return view;
    }

    private boolean dealAllParentIsChecked() {
        for (int i = 0; i < parentMapList.size(); i++) {
            StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
            if (!storeBean.isChecked()) {
                //如果有一个没选择,就false
                return false;
            }
        }
        return true;
    }

    private void setupOneParentAllChildChecked(boolean b, int i) {
        StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
        storeBean.setIsChecked(b);

        List<Map<String, Object>> childMapList = childMapList_list.get(i);
        for (int j = 0; j < childMapList.size(); j++) {
            GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get("childName");
            goodsBean.setIsChecked(b);
        }
        notifyDataSetChanged();
        dealPrice();
    }

    private void dealPrice() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < parentMapList.size(); i++) {

            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get("childName");
                int count = goodsBean.getCount();
                double discountPrice = goodsBean.getDiscountPrice();
                if (goodsBean.isChecked()) {
                    totalCount++;//单品多数量只记1
                    totalPrice += discountPrice * count;
                }

            }
        }
        //计算回调
        onGoodsCheckedChangeListener.onGoodsCheckedChange(totalCount, totalPrice);
    }

    private boolean dealAllEditingIsEditing() {
        for (int i = 0; i < parentMapList.size(); i++) {
            StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
            if (storeBean.isEditing()) {
                //如果有一个是编辑状态  就true
                return true;
            }
        }
        return false;
    }

    private void setupEditing(int i) {
        StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
        boolean isEditing = !storeBean.isEditing();
        storeBean.setIsEditing(isEditing);
        List<Map<String, Object>> childMapList = childMapList_list.get(i);
        for (int j = 0; j < childMapList.size(); j++) {
            GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get("childName");
            goodsBean.setIsEditing(isEditing);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.child_layout, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv_items_child = (TextView) view
                    .findViewById(R.id.tv_items_child);
            childViewHolder.id_cb_select_child = (CheckBox) view
                    .findViewById(R.id.id_cb_select_child);
            childViewHolder.id_ll_normal = (LinearLayout) view
                    .findViewById(R.id.id_ll_normal);
            childViewHolder.id_ll_edtoring = (LinearLayout) view
                    .findViewById(R.id.id_ll_edtoring);
            //常规下：
            childViewHolder.tv_items_child_desc = (TextView) view
                    .findViewById(R.id.tv_items_child_desc);
            childViewHolder.id_tv_price = (TextView) view
                    .findViewById(R.id.id_tv_price);
            childViewHolder.id_tv_discount_price = (TextView) view
                    .findViewById(R.id.id_tv_discount_price);
            childViewHolder.id_tv_count = (TextView) view
                    .findViewById(R.id.id_tv_count);
            //编辑下：
            childViewHolder.id_iv_reduce = (ImageView) view
                    .findViewById(R.id.id_iv_reduce);
            childViewHolder.id_iv_add = (ImageView) view
                    .findViewById(R.id.id_iv_add);
            childViewHolder.id_tv_count_now = (TextView) view
                    .findViewById(R.id.id_tv_count_now);
            childViewHolder.id_tv_price_now = (TextView) view
                    .findViewById(R.id.id_tv_price_now);
            childViewHolder.id_tv_des_now = (TextView) view
                    .findViewById(R.id.id_tv_des_now);
            childViewHolder.id_tv_goods_star = (TextView) view
                    .findViewById(R.id.id_tv_goods_star);
            childViewHolder.id_tv_goods_delete = (TextView) view
                    .findViewById(R.id.id_tv_goods_delete);

            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }

        final GoodsBean goodsBean = (GoodsBean) childMapList_list.get(i).get(i1).get("childName");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "商品：" + goodsBean.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //设置商品名
        childViewHolder.tv_items_child.setText(goodsBean.getName());
        //设置商品原价
        childViewHolder.id_tv_price.setText(String.format(context.getString(R.string.price), goodsBean.getPrice()));
        //设置原价划线并抗锯齿
        childViewHolder.id_tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        //设置商品折扣价
        childViewHolder.id_tv_discount_price.setText(String.format(context.getString(R.string.price), goodsBean.getDiscountPrice()));
        //设置商品颜色规格描述
        childViewHolder.tv_items_child_desc.setText(String.valueOf(goodsBean.getDesc()));
        //设置最终商品数量
        childViewHolder.id_tv_count.setText(String.format(context.getString(R.string.good_count), goodsBean.getCount()));


        //设置商品数量（调整中）
        childViewHolder.id_tv_count_now.setText(String.valueOf(goodsBean.getCount()));
        double priceNow = goodsBean.getCount() * goodsBean.getDiscountPrice();
        //设置商品总价（单价*数量）
        childViewHolder.id_tv_price_now.setText(String.format(context.getString(R.string.price), priceNow));
        //设置商品颜色规格描述
        childViewHolder.id_tv_des_now.setText(goodsBean.getDesc());


        //child选择按钮是否被选中
        childViewHolder.id_cb_select_child.setChecked(goodsBean.isChecked());
        childViewHolder.id_cb_select_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean nowBeanChecked = goodsBean.isChecked();
                //更新数据
                goodsBean.setIsChecked(!nowBeanChecked);

                boolean isOneParentAllChildIsChecked = dealOneParentAllChildIsChecked(i);
                StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
                storeBean.setIsChecked(isOneParentAllChildIsChecked);

                notifyDataSetChanged();
                //控制总checkedbox 接口
                onAllCheckedBoxNeedChangeListener.onCheckedBoxNeedChange(dealAllParentIsChecked());
                dealPrice();
            }
        });
        if (goodsBean.isEditing()) {
            childViewHolder.id_ll_normal.setVisibility(View.GONE);
            childViewHolder.id_ll_edtoring.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.id_ll_normal.setVisibility(View.VISIBLE);
            childViewHolder.id_ll_edtoring.setVisibility(View.GONE);
        }
        childViewHolder.id_iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealAdd(goodsBean);
            }
        });
        childViewHolder.id_iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealReduce(goodsBean);
            }
        });
        childViewHolder.id_tv_goods_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "收藏商品：" + goodsBean.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        childViewHolder.id_tv_goods_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOneGood(i, i1);
            }
        });

        return view;
    }

    private void removeOneGood(int i, int i1) {

        List<Map<String, Object>> childMapList = childMapList_list.get(i);
        childMapList.remove(i1);
        if (childMapList != null && childMapList.size() > 0) {

        } else {
            parentMapList.remove(i);
            childMapList_list.remove(i);
        }
        if (parentMapList != null && parentMapList.size() > 0) {
            onCheckHasGoodsListener.onCheckHasGoods(true);
        } else {
            onCheckHasGoodsListener.onCheckHasGoods(false);
        }
        notifyDataSetChanged();
        dealPrice();
    }

    private void dealReduce(GoodsBean goodsBean) {
        int count = goodsBean.getCount();
        if (count == 1) {
            return;
        }
        count--;
        goodsBean.setCount(count);
        notifyDataSetChanged();
        dealPrice();
    }

    private void dealAdd(GoodsBean goodsBean) {
        int count = goodsBean.getCount();
        count++;
        goodsBean.setCount(count);
        notifyDataSetChanged();
        dealPrice();
    }

    private boolean dealOneParentAllChildIsChecked(int i) {
        List<Map<String, Object>> childMapList = childMapList_list.get(i);
        for (int j = 0; j < childMapList.size(); j++) {
            GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get("childName");
            if (!goodsBean.isChecked()) {
                //如果有一个没选择,就false
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public void removeGoods() {
        for (int i = parentMapList.size() - 1; i >= 0; i--) {
            //倒过来遍历  remove
            StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
            if (storeBean.isChecked()) {
                parentMapList.remove(i);
                childMapList_list.remove(i);
            } else {
                List<Map<String, Object>> childMapList = childMapList_list.get(i);
                for (int j = childMapList.size() - 1; j >= 0; j--) {
                    //倒过来遍历  remove
                    GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get("childName");
                    if (goodsBean.isChecked()) {
                        childMapList.remove(j);
                    }
                }
            }

        }

        if (parentMapList != null && parentMapList.size() > 0) {
            onCheckHasGoodsListener.onCheckHasGoods(true);
        } else {
            onCheckHasGoodsListener.onCheckHasGoods(false);
        }

        notifyDataSetChanged();
        dealPrice();//重新计算
    }

    //供总编辑按钮调用
    public void setupEditingAll(boolean isEditingAll) {
        for (int i = 0; i < parentMapList.size(); i++) {
            StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
            storeBean.setIsEditing(isEditingAll);

            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get("childName");
                goodsBean.setIsEditing(isEditingAll);
            }
        }
        notifyDataSetChanged();
    }

    //供全选按钮调用
    public void setupAllChecked(boolean isChecked) {

        for (int i = 0; i < parentMapList.size(); i++) {
            StoreBean storeBean = (StoreBean) parentMapList.get(i).get("parentName");
            storeBean.setIsChecked(isChecked);

            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get("childName");
                goodsBean.setIsChecked(isChecked);
            }
        }
        notifyDataSetChanged();
        dealPrice();
    }

    class GroupViewHolder {
        TextView tv_title_parent;
        TextView id_tv_edit;
        CheckBox id_cb_select_parent;
    }

    class ChildViewHolder {
        TextView tv_items_child;
        CheckBox id_cb_select_child;
        LinearLayout id_ll_normal;
        LinearLayout id_ll_edtoring;

        TextView tv_items_child_desc;
        TextView id_tv_price;
        TextView id_tv_discount_price;
        TextView id_tv_count;

        ImageView id_iv_reduce;
        ImageView id_iv_add;
        TextView id_tv_des_now;
        TextView id_tv_count_now;
        TextView id_tv_price_now;
        TextView id_tv_goods_star;
        TextView id_tv_goods_delete;

    }

    public interface OnAllCheckedBoxNeedChangeListener {
        void onCheckedBoxNeedChange(boolean allParentIsChecked);
    }

    public interface OnEditingTvChangeListener {
        void onEditingTvChange(boolean allIsEditing);
    }

    public interface OnGoodsCheckedChangeListener {
        void onGoodsCheckedChange(int totalCount, double totalPrice);
    }

    public interface OnCheckHasGoodsListener {
        void onCheckHasGoods(boolean isHasGoods);
    }
}
