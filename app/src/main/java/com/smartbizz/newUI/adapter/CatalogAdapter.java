package com.smartbizz.newUI.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.smartbizz.BuildConfig;
import com.smartbizz.R;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.newViews.CatalogActivity;
import com.smartbizz.newUI.pojo.Requests;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<Requests> mBeatsList;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    /*
     * isLoading - to set the remote loading and complete status to fix back to back load more call
     * isMoreDataAvailable - to set whether more data from server available or not.
     * It will prevent useless load more request even after all the server data loaded
     * */

    public CatalogAdapter(Context context, List<Requests> mBeatsList) {
        this.context = context;
        this.mBeatsList = mBeatsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MOVIE) {
            return new MovieHolder(inflater.inflate(R.layout.view_catalog_adapter, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_MOVIE) {
            ((MovieHolder) holder).bindData(mBeatsList.get(position), position);
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {

        if (mBeatsList.get(position).name1.equals("load")) {
            return TYPE_LOAD;
        } else {
            return TYPE_MOVIE;
        }
    }

    @Override
    public int getItemCount() {
        return mBeatsList.size();
    }

    static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView photoView;

        private ImageView imgLike, imgWhatsApp, imgShare, imgEdit;

        private TextView txtMessage, txtLikeCount, txtShareCount;
        private CheckBox cbAddEmail, cbAddMobile;

        @Nullable
        @VisibleForTesting
        Uri mSaveImageUri;

        public MovieHolder(View itemView) {
            super(itemView);

            photoView = itemView.findViewById(R.id.photoView);
            imgLike = itemView.findViewById(R.id.imgLike);
//            imgLike.setOnClickListener(this);

            imgWhatsApp = itemView.findViewById(R.id.imgWhatsApp);
//            imgWhatsApp.setOnClickListener(this);

            imgShare = itemView.findViewById(R.id.imgShare);
//            imgShare.setOnClickListener(this);

            imgEdit = itemView.findViewById(R.id.imgEdit);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            cbAddEmail = itemView.findViewById(R.id.cbAddEmail);
            cbAddMobile = itemView.findViewById(R.id.cbAddMobile);
            txtLikeCount = itemView.findViewById(R.id.txtLikeCount);
            txtShareCount = itemView.findViewById(R.id.txtShareCount);
//            imgShare.setOnClickListener(this);
        }

        void bindData(Requests mBeats, int position) {
            Uri uri = Uri.parse(ApiConstants.BASE_URL + "/" + mBeats.getImgPath());
            photoView.setTag(mBeats.getHexCode());
            new AsyncTaskLoadImage(photoView).execute(ApiConstants.BASE_URL + "/" + mBeats.getImgPath());
            imgEdit.setTag(mBeats);

            txtLikeCount.setText(String.valueOf(mBeats.getLike()));

            if (mBeats.getIslike() == 1) {
                imgLike.setImageResource(R.drawable.ic_heart_filled);
                imgLike.setTag(1);
            } else {
                imgLike.setImageResource(R.drawable.ic_heart);
                imgLike.setTag(0);
            }

            txtShareCount.setText(String.valueOf(mBeats.getShare()));

            txtMessage.setText(mBeats.getComments());

            imgLike.setOnClickListener(v -> {

                Boolean emailChecked = false;
                if (cbAddEmail.isChecked()) {
                    emailChecked = true;
                }
                Boolean checked = false;
                if (cbAddMobile.isChecked()) {
                    checked = true;
                }

                if (Integer.parseInt(imgLike.getTag().toString()) == 0) {
                    int count = Integer.parseInt(txtLikeCount.getText().toString());
                    count = count + 1;
                    Handler handler = new Handler(); // write in onCreate function
                    int finalCount = count;
                    handler.post(() -> {
                        txtLikeCount.setText(String.valueOf(finalCount));
                        imgLike.setImageResource(R.drawable.ic_heart_filled);
                        imgLike.setTag(1);
                        NetworkManager.getInstance(context).likeShareCount(context, String.valueOf(imgLike.getTag()), "", mBeats.getCategoryType(), mBeats.getId(), response -> {
                            JSONObject jsonObject = response.getResponse();
                        });
                    });

                }

                if (Integer.parseInt(imgLike.getTag().toString()) == 1) {
                    int count = Integer.parseInt(txtLikeCount.getText().toString());
                    count = count - 1;
                    Handler handler = new Handler(); // write in onCreate function
                    int finalCount = count;
                    handler.post(() -> {
                        txtLikeCount.setText(String.valueOf(finalCount));
                        imgLike.setImageResource(R.drawable.ic_heart);
                        imgLike.setTag(0);
                        NetworkManager.getInstance(context).likeShareCount(context, String.valueOf(imgLike.getTag()), "", mBeats.getCategoryType(), mBeats.getId(), response -> {
                            JSONObject jsonObject = response.getResponse();
                        });
                    });

                }

            });

            imgWhatsApp.setOnClickListener(v -> {

                Boolean emailChecked = false;
                if (cbAddEmail.isChecked()) {
                    emailChecked = true;
                }

                Boolean checked = false;
                if (cbAddMobile.isChecked()) {
                    checked = true;
                }

                int count = Integer.parseInt(txtShareCount.getText().toString());

                count = count + 1;

                Handler handler = new Handler(); // write in onCreate function
                int finalCount = count;
                handler.post(() -> txtShareCount.setText(String.valueOf(finalCount)));

                FileOutputStream fileOutputStream = null;
                File file = getdisc();
                if (!file.exists() && !file.mkdirs()) {
                    Toast.makeText(context, "sorry can not make dir", Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
                String date = simpleDateFormat.format(new Date());
                String name = "img" + date + ".jpeg";
                String file_name = file.getAbsolutePath() + "/" + name;
                File new_file = new File(file_name);
                try {
                    fileOutputStream = new FileOutputStream(new_file);
                    Bitmap bitmap = viewToBitmap(photoView, photoView.getWidth(), photoView.getHeight());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    Toast.makeText(context, "sucses", Toast.LENGTH_LONG).show();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch
                (FileNotFoundException e) {

                } catch (IOException e) {

                }
                refreshGallary(file);

                mSaveImageUri = Uri.fromFile(file);

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("image/*");
                whatsappIntent.setPackage("com.whatsapp");
                String message = "";
                if (emailChecked && checked) {
                    message = "Call: " + PreferenceManager.getString(context, Constants.PrefKeys.MOBILE) + "\n" + PreferenceManager.getString(context, Constants.PrefKeys.EMAIL) + "\n" + txtMessage.getText().toString().trim();
                } else if (emailChecked) {
                    message = "Email: " + PreferenceManager.getString(context, Constants.PrefKeys.EMAIL) + "\n" + txtMessage.getText().toString().trim();
                } else if (checked) {
                    message = "Call: " + PreferenceManager.getString(context, Constants.PrefKeys.MOBILE) + "\n" + txtMessage.getText().toString().trim();
                } else {
                    message = txtMessage.getText().toString().trim();
                }
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
//                whatsappIntent.putExtra(Intent.EXTRA_STREAM, ((CatalogActivity) context).buildFileProviderUri(Uri.parse("file://" + new_file)));//Uri.parse("file://" + sharefile))
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    context.startActivity(whatsappIntent);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }

                NetworkManager.getInstance(context).likeShareCount(context, "", "1", mBeats.getCategoryType(), mBeats.getId(), response -> {
                    JSONObject jsonObject = response.getResponse();
                });
            });

            imgShare.setOnClickListener(v -> {

                Boolean emailChecked = false;
                if (cbAddEmail.isChecked()) {
                    emailChecked = true;
                }

                Boolean checked = false;
                if (cbAddMobile.isChecked()) {
                    checked = true;
                }

                int count = Integer.parseInt(txtShareCount.getText().toString());

                count = count + 1;

                Handler handler = new Handler(); // write in onCreate function
                int finalCount = count;
                handler.post(() -> txtShareCount.setText(String.valueOf(finalCount)));

                FileOutputStream fileOutputStream = null;
                File file = getdisc();
                if (!file.exists() && !file.mkdirs()) {
                    Toast.makeText(context, "sorry can not make dir", Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
                String date = simpleDateFormat.format(new Date());
                String name = "img" + date + ".jpeg";
                String file_name = file.getAbsolutePath() + "/" + name;
                File new_file = new File(file_name);
                try {
                    fileOutputStream = new FileOutputStream(new_file);
                    Bitmap bitmap = viewToBitmap(photoView, photoView.getWidth(), photoView.getHeight());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    Toast.makeText(context, "sucses", Toast.LENGTH_LONG).show();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch
                (FileNotFoundException e) {

                } catch (IOException e) {

                }
                refreshGallary(file);

                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new_file);
                mSaveImageUri = Uri.fromFile(file);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                String message = "";
                if (emailChecked && checked) {
                    message = "Call: " + PreferenceManager.getString(context, Constants.PrefKeys.MOBILE) + "\n" + PreferenceManager.getString(context, Constants.PrefKeys.EMAIL) + "\n" + txtMessage.getText().toString().trim();
                } else if (emailChecked) {
                    message = "Email: " + PreferenceManager.getString(context, Constants.PrefKeys.EMAIL) + "\n" + txtMessage.getText().toString().trim();
                } else if (checked) {
                    message = "Call: " + PreferenceManager.getString(context, Constants.PrefKeys.MOBILE) + "\n" + txtMessage.getText().toString().trim();
                } else {
                    message = txtMessage.getText().toString().trim();
                }
                intent.putExtra(Intent.EXTRA_TEXT, message);
//                intent.putExtra(Intent.EXTRA_STREAM, ((CatalogActivity) context).buildFileProviderUri(Uri.parse("file://" + new_file)));//Uri.parse("file://" + sharefile))
                intent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.msg_share_image)));

                NetworkManager.getInstance(context).likeShareCount(context, "", "1", mBeats.getCategoryType(), mBeats.getId(), response -> {
                    JSONObject jsonObject = response.getResponse();
                });

            });

            imgEdit.setOnClickListener(v -> {

                int count = Integer.parseInt(txtShareCount.getText().toString());

                count = count + 1;

                Handler handler = new Handler(); // write in onCreate function
                int finalCount = count;
                handler.post(() -> txtShareCount.setText(String.valueOf(finalCount)));

                Boolean emailChecked = false;
                if (cbAddEmail.isChecked()) {
                    emailChecked = true;
                }

                Boolean checked = false;
                if (cbAddMobile.isChecked()) {
                    checked = true;
                }

                String message = "";
                if (emailChecked && checked) {
                    message = "Call: " + PreferenceManager.getString(context, Constants.PrefKeys.MOBILE) + "\n" + PreferenceManager.getString(context, Constants.PrefKeys.EMAIL) + "\n" + txtMessage.getText().toString().trim();
                } else if (emailChecked) {
                    message = "Email: " + PreferenceManager.getString(context, Constants.PrefKeys.EMAIL) + "\n" + txtMessage.getText().toString().trim();
                } else if (checked) {
                    message = "Call: " + PreferenceManager.getString(context, Constants.PrefKeys.MOBILE) + "\n" + txtMessage.getText().toString().trim();
                } else {
                    message = txtMessage.getText().toString().trim();
                }
//                Intent intent = new Intent(context, EditImageActivity.class);
//                intent.putExtra("imgMessage", message);
//                intent.putExtra("imgUri", uri.toString());
//                intent.putExtra("imgBrandName", (PreferenceManager.getString(context, Constants.PrefKeys.BRANDNAME)));
//                intent.putExtra("imgColor", (((Requests) imgEdit.getTag()).getHexCode()).toString());
//                ((CatalogActivity) context).startActivity(intent);

                NetworkManager.getInstance(context).likeShareCount(context, "", "1", mBeats.getCategoryType(), mBeats.getId(), response -> {
                    JSONObject jsonObject = response.getResponse();
                });
            });
        }

        private void refreshGallary(File file) {
            Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            i.setData(Uri.fromFile(file));
            context.sendBroadcast(i);
        }

        private File getdisc() {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            return new File(file, "My Image");
        }

        private static Bitmap viewToBitmap(View view, int widh, int hight) {
            Bitmap bitmap = Bitmap.createBitmap(widh, hight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }

        public class AsyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {
            private final static String TAG = "AsyncTaskLoadImage";
            private ImageView imageView;
            private String colorCode;

            public AsyncTaskLoadImage(ImageView imageView) {
                this.imageView = imageView;
                this.colorCode = String.valueOf(imageView.getTag());
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(params[0]);
                    bitmap = BitmapFactory.decodeStream((InputStream) url.getContent()).copy(Bitmap.Config.ARGB_8888, true);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                if (colorCode != null) {
                    paint.setColor(Color.parseColor(colorCode)); // Text Color
                } else {
                    paint.setColor(context.getResources().getColor(R.color.white)); // Text Color
                }
                paint.setTextSize(80); //Text Size
                paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));

                float aspectRatio = bitmap.getWidth() /
                        (float) bitmap.getHeight();

                int width = context.getResources().getDisplayMetrics().widthPixels;
                int height = Math.round(width / aspectRatio);

                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

                Bitmap bitmapLogo = null;
//                try {
////                    URL url = new URL(ApiConstants.BASE_URL + "/" + PreferenceManager.getString(context, Constants.PrefKeys.LOGO));
////                    URL url = new URL("https://digiepost.in/assets/uploads/gallery/20/20_1596904590.jpg");
//                    bitmapLogo = BitmapFactory.decodeStream((InputStream) url.getContent()).copy(Bitmap.Config.ARGB_8888, true);
//                } catch (IOException e) {
//                    Log.e(TAG, e.getMessage());
//                }

                    bitmapLogo = ((CatalogActivity) context).bitmapLogo;

//                float aspectRatio1 = bitmapLogo.getWidth() /
//                        (float) bitmapLogo.getHeight();
//
//                int width1 = 200;
//                int height1 = Math.round(width1 / aspectRatio1);
//
//                bitmapLogo = Bitmap.createScaledBitmap(bitmapLogo, width1, height1, false);

                Canvas canvas = new Canvas(bitmap);

                int xPos = (canvas.getWidth() / 2 - 270);
                int yPos = canvas.getHeight() - (canvas.getHeight() - 70);
                if(!(PreferenceManager.getString(context, Constants.PrefKeys.BRANDNAME).equalsIgnoreCase("null"))){
                    canvas.drawText((PreferenceManager.getString(context, Constants.PrefKeys.BRANDNAME)).toUpperCase(), xPos, yPos, paint);
                }


                if(bitmapLogo != null) {
                    canvas.drawBitmap(bitmapLogo, (bitmap.getWidth()) - 300, (bitmap.getHeight()) - 310, new Paint());
                }

                imageView.setImageBitmap(bitmap);//1080   720
            }

        }

        @Override
        public void onClick(View view) {

        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public interface ActivityAdapterInterface {
        public void prepareSelection(View v, int position);
    }

}
