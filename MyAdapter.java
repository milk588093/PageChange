package com.asus.zenshot.ui.gimbal;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asus.zenshot.R;
import com.sightour.gimbal.GimbalDevice;
import com.sightour.gimbal.GimbalManager;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<GimbalDevice> list=new ArrayList<GimbalDevice>();
    GimbalManager manager;
    Context context;
    public void setData(List<GimbalDevice> Gimballist) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(list, Gimballist));
        list.clear();
        list.addAll(Gimballist);
        result.dispatchUpdatesTo(this);
    }
    private class DiffCallback extends DiffUtil.Callback {
        private List<GimbalDevice> oldData, newData;
        DiffCallback(List<GimbalDevice> oldData, List<GimbalDevice> newData) {
            this.oldData = oldData;
            this.newData = newData;
        }
        @Override
        public int getOldListSize() {
            return oldData.size();
        }
        @Override
        public int getNewListSize() {
            return newData.size();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).getId()== newData.get(newItemPosition).getId();
        }
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }
        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            //you can return particular field for changed item.
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_gimbal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GimbalDevice data =list.get(position);
        holder.setData(data);
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected abstract class GimbalListBaseViewHolder extends RecyclerView.ViewHolder {
        public GimbalListBaseViewHolder(View itemView)
        {
            super(itemView);
        }

        abstract void setData(GimbalDevice data);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }
    class ViewHolder extends GimbalListBaseViewHolder {
        TextView mSNtextview;
        TextView mGimbalName;
        TextView  textView3;
        ImageView img;
        Button button;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSNtextview = itemView.findViewById(R.id.SNtextview);
            mGimbalName = itemView.findViewById(R.id.GimbalName);
            img=itemView.findViewById(R.id.Gimbal_icon);
            button=itemView.findViewById(R.id.connect);
            textView=itemView.findViewById(R.id.textView22);
            textView3=itemView.findViewById(R.id.connect_text);
        }

        @Override
        void setData(GimbalDevice data) {
            final GimbalDevice gimbalDevice=(GimbalDevice)data;
            mSNtextview.setText(gimbalDevice.getId());
            mGimbalName.setText(gimbalDevice.getName());
           button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setVisibility(view.INVISIBLE);
                    textView3.setText("Connecting...");
                     manager = GimbalManager.instance(context);
                        manager.connect(gimbalDevice);
                        Log.d("aa","aaa");
                }
            });
        }
    }
}
