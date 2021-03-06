package limitless.android.androiddevelopment.BottomSheet;

import android.Manifest;
import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import limitless.android.androiddevelopment.Adapter.PhotoAdapter;
import limitless.android.androiddevelopment.Adapter.SongsAdapter;
import limitless.android.androiddevelopment.Adapter.VideoAdapter;
import limitless.android.androiddevelopment.Interface.Listener;
import limitless.android.androiddevelopment.Model.Photo;
import limitless.android.androiddevelopment.Model.Song;
import limitless.android.androiddevelopment.Model.Video;
import limitless.android.androiddevelopment.Data.Data;
import limitless.android.androiddevelopment.Other.Tools;
import limitless.android.androiddevelopment.R;

public class SelectBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private Listener<Uri> uriListener;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    private AppCompatTextView tvTitle;
    private View vNoFile;
    private BottomSheetBehavior bottomSheetBehavior;
    private int appBarHeight = 0;
    private int type;

    public SelectBottomSheet(Listener<Uri> uriListener, int type){
        this.uriListener = uriListener;
        this.type = type;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_select, null);
        dialog.setContentView(view);
        init(view);
        return dialog;
    }

    private void init(View view) {
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        recyclerView = view.findViewById(R.id.recyclerView);
        appBarLayout = view.findViewById(R.id.appbar);
        tvTitle = view.findViewById(R.id.textView_title);
        vNoFile = view.findViewById(R.id.linearLayout_noFile);

        appBarHeight = appBarLayout.getLayoutParams().height;
        appBarLayout.setVisibility(View.INVISIBLE);
        appBarLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, 0));
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback);
        tvTitle.setText(title());
        recyclerView.setLayoutManager(layoutManager());
        view.findViewById(R.id.imageButton_close).setOnClickListener(this);

        if (Tools.permissionGranted(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            new GetData().execute();
            return;
        }
        dismiss();
    }

    private RecyclerView.LayoutManager layoutManager() {
        if (type == 0)
            return new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        if (type == 1)
            return new LinearLayoutManager(getContext());
        if (type == 2)
            return new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        if (type == 3)
            return new LinearLayoutManager(getContext());
        return null;
    }

    private String title() {
        if (type == 0)
            return "Select photo";
        if (type == 1)
            return "Select Audio";
        if (type == 2)
            return "Select Video";
        if (type == 3)
            return "Select file";
        return null;
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int i) {
            if (i == BottomSheetBehavior.STATE_HIDDEN){
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {
            if (v <= 0){
                appBarLayout.setVisibility(View.INVISIBLE);
            }else {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) appBarLayout.getLayoutParams();
                int h = (int) Tools.convertDipToPx(
                        getContext().getResources().getDisplayMetrics(), (int) (appBarHeight * v * 0.5));
                layoutParams.height = Math.min(h, appBarHeight);
                appBarLayout.setLayoutParams(layoutParams);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        }
    };
    private Listener<Uri> uriListenerAdapter = new Listener<Uri>() {
        @Override
        public void data(Uri uri) {
            if (uriListener != null)
                uriListener.data(uri);
            dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton_close){
            dismiss();
        }
    }

    private class GetData extends AsyncTask<Void, Void, List<?>>{

        @Override
        protected List<?> doInBackground(Void... voids) {
            if (type == 0)
                return Data.getAllPhotos(getContext(), MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");
            if (type == 1)
                return Data.getAllSongs(getContext(), MediaStore.Audio.AudioColumns.DATE_ADDED + " DESC");
            if (type == 2)
                return Data.getAllVideos(getContext().getContentResolver(), MediaStore.Video.VideoColumns.DATE_ADDED + " DESC");
            if (type == 3)
                return null;
            return null;
        }

        @Override
        protected void onPostExecute(List<?> objects) {
            super.onPostExecute(objects);
            if (objects == null || objects.size() <= 0){
                vNoFile.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                return;
            }
            vNoFile.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (type == 0)
                recyclerView.setAdapter(new PhotoAdapter(getContext(), (List<Photo>) objects, uriListenerAdapter));
            if (type == 1)
                recyclerView.setAdapter(new SongsAdapter(getContext(), (List<Song>) objects, uriListenerAdapter));
            if (type == 2)
                recyclerView.setAdapter(new VideoAdapter(getContext(), (List<Video>) objects, uriListenerAdapter));
            if (type == 3)
                return;
        }
    }

}
