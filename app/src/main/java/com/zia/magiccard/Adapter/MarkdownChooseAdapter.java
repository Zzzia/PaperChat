package com.zia.magiccard.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zia.magiccard.Bean.MarkdownData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MarkdownUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zia.magiccard.View.MainActivity.userData;

/**
 * Created by zia on 17-8-25.
 */

public class MarkdownChooseAdapter extends RecyclerView.Adapter<MarkdownChooseAdapter.MyViewHolder> {

    private Context context;
    private List<MarkdownData> markdownDatas;
    private List<MarkdownData> selecteds;
    private boolean canPreview = false;
    private View rootView;

    public MarkdownChooseAdapter(Context context,List<MarkdownData> markdownDatas){
        this.context = context;
        this.markdownDatas = markdownDatas;
        selecteds = new ArrayList<>();
    }

    public void setCanPreview(boolean b,View rootView){
        canPreview = b;
        this.rootView = rootView;
    }

    public List<MarkdownData> getSelecteds(){
        return selecteds;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choose_markdown,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MarkdownData markdownData = markdownDatas.get(position);
        holder.title.setText(markdownData.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //没有选中
                if(holder.selected.getVisibility() == View.INVISIBLE){
                    holder.selected.setVisibility(View.VISIBLE);
                    if(!selecteds.contains(markdownData)){
                        selecteds.add(markdownData);
                    }
                }else{
                    holder.selected.setVisibility(View.INVISIBLE);
                    selecteds.remove(markdownData);
                }
            }
        });
        if(canPreview){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MarkdownUtil.previewMarkDown(context,rootView,markdownData.getContent());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(markdownDatas == null) return 0;
        return markdownDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView selected;
        private TextView title;
        MyViewHolder(View itemView) {
            super(itemView);
            selected = itemView.findViewById(R.id.item_choose_markdown_selected);
            title = itemView.findViewById(R.id.item_choose_markdown_title);
        }
    }
}
