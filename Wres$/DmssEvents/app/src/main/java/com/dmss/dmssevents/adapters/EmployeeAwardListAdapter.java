package com.dmss.dmssevents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.common.CircleTransform;
import com.dmss.dmssevents.interfaces.SubAdapterCallBackInterface;
import com.dmss.dmssevents.models.AwardsModel;
import com.dmss.dmssevents.models.EmployeeAwardModel;
import com.dmss.dmssevents.models.ParticipantsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class EmployeeAwardListAdapter extends BaseAdapter {
    ArrayList<EmployeeAwardModel> employeeAwardModel;
    Context context;
    SubAdapterCallBackInterface subAdapterCallBackInterface;

    public EmployeeAwardListAdapter(ArrayList<EmployeeAwardModel> employeeAwardModel, Context context) {
        this.employeeAwardModel = employeeAwardModel;
        this.context = context;
        subAdapterCallBackInterface = (SubAdapterCallBackInterface) context;
    }

    @Override
    public int getCount() {
        return employeeAwardModel.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return employeeAwardModel.hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.ratinglist_item, null);
        } else {
            rowView = convertView;
        }

        if (position % 2 != 0) {
            rowView.setBackgroundResource(R.color.greyLight);
        }
        holder.employeeNmae = (TextView) rowView.findViewById(R.id.employeeNmae);
        holder.employeeDepartment = (TextView) rowView.findViewById(R.id.employeeDepartment);
        holder.poleProfileImageView = (ImageView) rowView.findViewById(R.id.poleProfileImageView);
        holder.poleProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subAdapterCallBackInterface.SubAdapterCallBack(employeeAwardModel.get(position).getProfilePic());
            }
        });
        if (employeeAwardModel.size() > 0) {
            if (employeeAwardModel.get(position).getProfilePic().length() > 0) {
                Picasso.with(context).load(employeeAwardModel.get(position).getProfilePic()).error(R.drawable.profilepic).transform(new CircleTransform()).into(holder.poleProfileImageView);
            }
            String employName = employeeAwardModel.get(position).getDisplayName() + " - " + employeeAwardModel.get(position).getEmpIdCard();
            String departaName = employeeAwardModel.get(position).getDeptName();
            holder.employeeNmae.setText(employName);
            holder.employeeDepartment.setText(departaName);

        }
        return rowView;
    }

    public class Holder {
        TextView employeeNmae, employeeDepartment;
        ImageView poleProfileImageView;


    }
}
