package com.example.mobileshopping.ui.dashboard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobileshopping.R;
import com.example.mobileshopping.ui.dashboard.bean.ShoppingCarDataBean;
import com.example.mobileshopping.ui.dashboard.util.ToastUtil;
import com.example.mobileshopping.ui.dashboard.util.LogUtil;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车的adapter
 * 因为使用的是ExpandableListView，所以继承BaseExpandableListAdapter
 */
public class ShoppingCarAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final LinearLayout llSelectAll;
    private final ImageView ivSelectAll;
    private final Button btnOrder;
    private final Button btnDelete;
    private final RelativeLayout rlTotalPrice;
    private final TextView tvTotalPrice;
    private List<ShoppingCarDataBean.DatasBean> data;
    private boolean isSelectAll = false;
    private double total_price;

    public ShoppingCarAdapter(Context context, LinearLayout llSelectAll,
                              ImageView ivSelectAll, Button btnOrder, Button btnDelete,
                              RelativeLayout rlTotalPrice, TextView tvTotalPrice) {
        this.context = context;
        this.llSelectAll = llSelectAll;
        this.ivSelectAll = ivSelectAll;
        this.btnOrder = btnOrder;
        this.btnDelete = btnDelete;
        this.rlTotalPrice = rlTotalPrice;
        this.tvTotalPrice = tvTotalPrice;
    }

    /**
     * 自定义设置数据方法；
     * 通过notifyDataSetChanged()刷新数据，可保持当前位置
     *
     * @param data 需要刷新的数据
     */
    public void setData(List<ShoppingCarDataBean.DatasBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_group, null);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final ShoppingCarDataBean.DatasBean datasBean = data.get(groupPosition);

        String store_id = datasBean.getStore_id();

        String store_name = datasBean.getStore_name();

        if (store_name != null) {
            groupViewHolder.tvStoreName.setText(store_name);
        } else {
            groupViewHolder.tvStoreName.setText("");
        }

        //When select all item, select the store too
        for (int i = 0; i < datasBean.getGoods().size(); i++) {
            ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = datasBean.getGoods().get(i);
            boolean isSelect = goodsBean.getIsSelect();
            if (isSelect) {
                datasBean.setIsSelect_shop(true);
            } else {
                datasBean.setIsSelect_shop(false);
                break;
            }
        }

        //Need (get) again after (set)
        //To check store is selected or not
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        if (isSelect_shop) {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.select);
        } else {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.unselect);
        }

        //Click the store in white spaces
        groupViewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasBean.setIsSelect_shop(!isSelect_shop);

                List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = datasBean.getGoods();
                for (int i = 0; i < goods.size(); i++) {
                    ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(i);
                    goodsBean.setIsSelect(!isSelect_shop);
                }
                notifyDataSetChanged();
            }
        });

        //When all items are selected, "select all" need to be selected as well
        w:
        for (int i = 0; i < data.size(); i++) {
            List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = data.get(i).getGoods();
            for (int y = 0; y < goods.size(); y++) {
                ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.getIsSelect();
                if (isSelect) {
                    isSelectAll = true;
                } else {
                    isSelectAll = false;
                    break w;
                }
            }
        }
        if (isSelectAll) {
            ivSelectAll.setBackgroundResource(R.mipmap.select);
        } else {
            ivSelectAll.setBackgroundResource(R.mipmap.unselect);
        }

        //Select All function
        llSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectAll = !isSelectAll;

                if (isSelectAll) {
                    for (int i = 0; i < data.size(); i++) {
                        List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = data.get(i).getGoods();
                        for (int y = 0; y < goods.size(); y++) {
                            ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(y);
                            goodsBean.setIsSelect(true);
                        }
                    }
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = data.get(i).getGoods();
                        for (int y = 0; y < goods.size(); y++) {
                            ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(y);
                            goodsBean.setIsSelect(false);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        //Count total
        total_price = 0.0;
        tvTotalPrice.setText("HKD$0.00");
        for (int i = 0; i < data.size(); i++) {
            List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = data.get(i).getGoods();
            for (int y = 0; y < goods.size(); y++) {
                ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.getIsSelect();
                if (isSelect) {
                    String num = goodsBean.getGoods_num();
                    String price = goodsBean.getGoods_price();

                    double v = Double.parseDouble(num);
                    double v1 = Double.parseDouble(price);

                    total_price = total_price + v * v1;


                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    tvTotalPrice.setText("HKD$" + decimalFormat.format(total_price));
                }
            }
        }

        //Pay function
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Temp list to store selected store that its items has been selected
                List<ShoppingCarDataBean.DatasBean> tempStores = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    //Check any item is selected in a store
                    boolean hasGoodsSelect = false;
                    //Temp list to store selected items
                    List<ShoppingCarDataBean.DatasBean.GoodsBean> tempGoods = new ArrayList<>();

                    ShoppingCarDataBean.DatasBean storesBean = data.get(i);
                    List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = storesBean.getGoods();
                    for (int y = 0; y < goods.size(); y++) {
                        ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(y);
                        boolean isSelect = goodsBean.getIsSelect();
                        if (isSelect) {
                            hasGoodsSelect = true;
                            tempGoods.add(goodsBean);
                        }
                    }

                    if (hasGoodsSelect) {
                        ShoppingCarDataBean.DatasBean storeBean = new ShoppingCarDataBean.DatasBean();
                        storeBean.setStore_id(storesBean.getStore_id());
                        storeBean.setStore_name(storesBean.getStore_name());
                        storeBean.setGoods(tempGoods);

                        tempStores.add(storeBean);
                    }
                }

                if (tempStores != null && tempStores.size() > 0) {//If any item is selected than can go to pay
                    /**
                     * 实际开发中，如果有被选中的商品，
                     * 则跳转到确认订单页面，完成后续订单流程。
                     */
                    ToastUtil.makeText(context, "Go To Confirm Order Page");
                } else {
                    ToastUtil.makeText(context, "Please Select Item To Buy");
                }
            }
        });

        //Delete button function
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 实际开发中，通过回调请求后台接口实现删除操作
                 */
                if (mDeleteListener != null) {
                    mDeleteListener.onDelete();
                }
            }
        });

        return convertView;
    }

    static class GroupViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.tv_store_name)
        TextView tvStoreName;
        @BindView(R.id.ll)
        LinearLayout ll;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //------------------------------------------------------------------------------------------------
    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getGoods() != null && data.get(groupPosition).getGoods().size() > 0) {
            return data.get(groupPosition).getGoods().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getGoods().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_child, null);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ShoppingCarDataBean.DatasBean datasBean = data.get(groupPosition);
        //Store id
        String store_id = datasBean.getStore_id();
        //Store name
        String store_name = datasBean.getStore_name();
        //Store is selected or not
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        final ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = datasBean.getGoods().get(childPosition);
        //Item Image
        String goods_image = goodsBean.getGoods_image();
        //Item ID
        final String goods_id = goodsBean.getGoods_id();
        //Item Name
        String goods_name = goodsBean.getGoods_name();
        //Item Price
        String goods_price = goodsBean.getGoods_price();
        //Item quantity
        String goods_num = goodsBean.getGoods_num();
        //Item is selected or not
        final boolean isSelect = goodsBean.getIsSelect();

        Glide.with(context)
                .load(goods_image)
                .into(childViewHolder.ivPhoto);
        if (goods_name != null) {
            childViewHolder.tvName.setText(goods_name);
        } else {
            childViewHolder.tvName.setText("");
        }
        if (goods_price != null) {
            childViewHolder.tvPriceValue.setText(goods_price);
        } else {
            childViewHolder.tvPriceValue.setText("");
        }
        if (goods_num != null) {
            childViewHolder.tvEditBuyNumber.setText(goods_num);
        } else {
            childViewHolder.tvEditBuyNumber.setText("");
        }

        //Item is select or not
        if (isSelect) {
            childViewHolder.ivSelect.setImageResource(R.mipmap.select);
        } else {
            childViewHolder.ivSelect.setImageResource(R.mipmap.unselect);
        }

        //When Store has been clicked
        childViewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsBean.setIsSelect(!isSelect);
                if (!isSelect == false) {
                    datasBean.setIsSelect_shop(false);
                }
                notifyDataSetChanged();
            }
        });

        //add quantity of item
        childViewHolder.ivEditAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String num = goodsBean.getGoods_num();
                Integer integer = Integer.valueOf(num);
                integer++;
                goodsBean.setGoods_num(integer + "");
                notifyDataSetChanged();

                /**
                 * 实际开发中，通过回调请求后台接口实现数量的加减
                 */
                if (mChangeCountListener != null) {
                    mChangeCountListener.onChangeCount(goods_id);
                }
            }
        });
        //reduce quantity of item
        childViewHolder.ivEditSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String num = goodsBean.getGoods_num();
                Integer integer = Integer.valueOf(num);
                if (integer > 1) {
                    integer--;
                    goodsBean.setGoods_num(integer + "");

                    /**
                     * 实际开发中，通过回调请求后台接口实现数量的加减
                     */
                    if (mChangeCountListener != null) {
                        mChangeCountListener.onChangeCount(goods_id);
                    }
                } else {
                    ToastUtil.makeText(context, "Minimum Amount Is 1");
                }
                notifyDataSetChanged();
            }
        });

        if (childPosition == data.get(groupPosition).getGoods().size() - 1) {
            childViewHolder.view.setVisibility(View.GONE);
            childViewHolder.viewLast.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.view.setVisibility(View.VISIBLE);
            childViewHolder.viewLast.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price_key)
        TextView tvPriceKey;
        @BindView(R.id.tv_price_value)
        TextView tvPriceValue;
        @BindView(R.id.iv_edit_subtract)
        ImageView ivEditSubtract;
        @BindView(R.id.tv_edit_buy_number)
        TextView tvEditBuyNumber;
        @BindView(R.id.iv_edit_add)
        ImageView ivEditAdd;
        @BindView(R.id.view)
        View view;
        @BindView(R.id.view_last)
        View viewLast;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //-----------------------------------------------------------------------------------------------

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //Delete function
    public interface OnDeleteListener {
        void onDelete();
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mDeleteListener = listener;
    }

    private OnDeleteListener mDeleteListener;

    //Change item quantity
    public interface OnChangeCountListener {
        void onChangeCount(String goods_id);
    }

    public void setOnChangeCountListener(OnChangeCountListener listener) {
        mChangeCountListener = listener;
    }

    private OnChangeCountListener mChangeCountListener;
}
