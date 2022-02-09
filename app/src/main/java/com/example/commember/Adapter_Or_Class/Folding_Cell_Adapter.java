package com.example.commember.Adapter_Or_Class;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.commember.HomeActivity;
import com.example.commember.Modal.Cus_Detail_Model;
import com.example.commember.Prevalant.Prevalant;
import com.example.commember.R;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import io.paperdb.Paper;

public class Folding_Cell_Adapter extends ArrayAdapter<Cus_Detail_Model> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    public View b;
    Context cc;
    ArrayList<Cus_Detail_Model> mod;

    public Folding_Cell_Adapter(Context context, ArrayList<Cus_Detail_Model> objects) {
        super(context, 0, objects);

        cc = context;
        mod = objects;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Cus_Detail_Model item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

        b = convertView;

//        cell.fold(true);

        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.shu_cus_layout, parent, false);

            viewHolder.title_name = cell.findViewById(R.id.title_name);
            viewHolder.title_mon = cell.findViewById(R.id.title_mon);
            viewHolder.title_date = cell.findViewById(R.id.title_date);
            viewHolder.title_plan = cell.findViewById(R.id.title_plan);
            viewHolder.title_no = cell.findViewById(R.id.title_no);
            viewHolder.title_pic = cell.findViewById(R.id.title_pic);

            viewHolder.body_name = cell.findViewById(R.id.body_cus_name);
            viewHolder.body_mon_pla = cell.findViewById(R.id.body_cus_mon);
            viewHolder.body_date = cell.findViewById(R.id.body_cus_date);
            viewHolder.body_plan = cell.findViewById(R.id.body_cus_plan);
            viewHolder.body_no = cell.findViewById(R.id.body_cus_num);
            viewHolder.body_time = cell.findViewById(R.id.body_cus_time);
            viewHolder.body_address = cell.findViewById(R.id.body_cus_address);
            viewHolder.body_pin = cell.findViewById(R.id.body_cus_pin);
            viewHolder.body_pic = cell.findViewById(R.id.body_cus_pic);

            viewHolder.edit_Cust = cell.findViewById(R.id.edit_cus);

            cell.setTag(viewHolder);

            Picasso.get().load(mod.get(position).getCus_Image()).centerCrop().fit().into(viewHolder.title_pic);
            Picasso.get().load(mod.get(position).getCus_Image()).centerCrop().fit().into(viewHolder.body_pic);

            viewHolder.title_name.setText(mod.get(position).getCus_Name());
            viewHolder.title_mon.setText(mod.get(position).getCus_Month() + " Months");
            viewHolder.title_date.setText(mod.get(position).getDate());
            viewHolder.title_plan.setText("Rs." + mod.get(position).getCus_Plan() + "/month");
            viewHolder.title_no.setText("Phone No. " + mod.get(position).getCus_Number());

            viewHolder.body_name.setText(mod.get(position).getCus_Name());
            viewHolder.body_mon_pla.setText(mod.get(position).getCus_Month() + " Months");
            viewHolder.body_date.setText(mod.get(position).getDate());
            viewHolder.body_plan.setText("Rs." + mod.get(position).getCus_Plan() + "/month");
            viewHolder.body_no.setText(mod.get(position).getCus_Number());
            viewHolder.body_time.setText(mod.get(position).getTime());
            viewHolder.body_address.setText(mod.get(position).getCus_Address());
            viewHolder.body_pin.setText(mod.get(position).getCus_Pin());

            viewHolder.edit_Cust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Paper.book().write(Prevalant.Cus_PID, mod.get(position).getPID());
                    ((HomeActivity)cc).getEditCus();

                }
            });

            cell.fold(true);

        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView title_name, title_mon, title_date, title_plan, title_no, edit_Cust;
        ImageView title_pic;
        TextView body_name, body_mon_pla, body_date, body_plan, body_no, body_time, body_address, body_pin;
        ImageView body_pic;
    }

}
