package limitless.android.androiddevelopment.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import limitless.android.androiddevelopment.Interface.StringListener;
import limitless.android.androiddevelopment.Model.VideoModel;
import limitless.android.androiddevelopment.Data.Data;
import limitless.android.androiddevelopment.Other.Tools;
import limitless.android.androiddevelopment.R;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.VideoViewHolder> {

    private Context context;
    private List<VideoModel> list;
    private StringListener stringListener;

    public AdapterVideo(Context context, List<VideoModel> list, StringListener stringListener) {
        this.context = context;
        this.list = list;
        this.stringListener = stringListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_videos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        try {
            return list.size();
        }catch (Exception e){
            Tools.error(e);
            return 0;
        }
    }

    public void setData(List<VideoModel> models){
        if (models == null)
            return;
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        list.addAll(models);
        notifyDataSetChanged();
    }

    public void clearAll(){
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        notifyDataSetChanged();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatImageView ivCover;
        private AppCompatTextView tvTitle, tvDuration;
        private AppCompatImageButton btnMore;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivCover = itemView.findViewById(R.id.imageView_cover);
            tvTitle = itemView.findViewById(R.id.textView_title);
            tvDuration = itemView.findViewById(R.id.textView_duration);
            btnMore = itemView.findViewById(R.id.button_more);

            btnMore.setOnClickListener(this);
        }

        void bindView(VideoModel vm){
            tvTitle.setText(vm.title);
            tvDuration.setText(Tools.displayDuration(vm.duration));
            ivCover.setImageResource(R.drawable.ic_video_library_black_24dp);
            new GetCover(getAdapterPosition()).execute(vm.id);
        }

        private class GetCover extends AsyncTask<Integer, Void, Bitmap>{
            private int position;
            public GetCover(int position) {
                this.position = position;
            }

            @Override
            protected Bitmap doInBackground(Integer... integers) {
                return Data.getVideoCoverPhoto(context.getContentResolver(), integers[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (getAdapterPosition() == position){
                    if (bitmap == null)
                        ivCover.setImageResource(R.drawable.ic_video_library_black_24dp);
                    else
                        ivCover.setImageBitmap(bitmap);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_more){
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.menu_adapter_song);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Tools.toast(context, item.getTitle().toString() + " " + list.get(getAdapterPosition()).title);
                        return false;
                    }
                });
                MenuPopupHelper helper = new MenuPopupHelper(context, (MenuBuilder) popupMenu.getMenu(), v);
                helper.setForceShowIcon(true);
                helper.show();

            }else {
                if (stringListener != null)
                    stringListener.string(list.get(getAdapterPosition()).data);
            }
        }
    }

}