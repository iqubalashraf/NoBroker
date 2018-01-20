package app.nobroker.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.nobroker.GeneralUtil;
import app.nobroker.R;
import app.nobroker.dataModels.Data;

/**
 * Created by ashrafiqubal on 20/01/18.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    List<Data> data = new ArrayList<>();

    public MainAdapter(Activity activity) {

        this.activity = activity;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderDisplayCards(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_for_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Data data1 = data.get(position);
        if (holder instanceof ViewHolderDisplayCards) {
            ((ViewHolderDisplayCards) holder).propertyTitle.setText(data1.getPropertyTitle());
            String loaclity = "at " + data1.getSociety() + ", " + data1.getStreet();
            ((ViewHolderDisplayCards) holder).locality.setText(loaclity);
            ((ViewHolderDisplayCards) holder).price.setText(GeneralUtil.getPriceToDisplay(data1.getRent()));
            ((ViewHolderDisplayCards) holder).furnishing_stage.setText(GeneralUtil.getFurnishingTextToDisplay(data1.getFurnishing())
            + "\n"
            + GeneralUtil.getLeaseTypeToDisplay(data1.getLeaseType()));
            ((ViewHolderDisplayCards) holder).area.setText(data1.getPropertySize() + " Sq.ft");
            if (data1.isPhotoAvailable()) {
                for (int i = 0; i < data1.getPhotos().size(); i++) {
                    if (data1.getPhotos().get(i).isDisplayPic()) {
                        Picasso.with(activity)
                                .load(GeneralUtil.IMAGE_BASE_URL + data1.getId() + "/" + data1.getPhotos().get(i).getImagesMap().getMedium())
                                .placeholder(R.drawable.property_placeholder_grande)
                                .error(R.drawable.property_placeholder_grande)
                                .resize((int) (GeneralUtil.getScreenWidth() - GeneralUtil.pxFromDp(16)), (int) ((GeneralUtil.getScreenWidth() - GeneralUtil.pxFromDp(16)) * (0.5625)))
                                .centerCrop()
                                .into(((ViewHolderDisplayCards) holder).photo);
                    }
                }

            }

        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class ViewHolderDisplayCards extends RecyclerView.ViewHolder {
        View itemView;
        TextView propertyTitle, price, furnishing_stage, area;
        TextView locality;
        ImageView photo;

        private ViewHolderDisplayCards(View itemView) {
            super(itemView);
            this.itemView = itemView;
            propertyTitle = itemView.findViewById(R.id.propertyTitle);
            price = itemView.findViewById(R.id.price);
            furnishing_stage = itemView.findViewById(R.id.furnishing_stage);
            area = itemView.findViewById(R.id.area);
            locality = itemView.findViewById(R.id.locality);
            photo = itemView.findViewById(R.id.photo);

        }
    }
}
