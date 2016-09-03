package in.showoffs.locateatm.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.showoffs.locateatm.R;

/**
 * Created by Administrator on 12/1/2015.
 */
public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.ReviewHolder>  {

    public RecycleviewAdapter(List<Review> list) {
        this.list = list;
    }

    List<Review> list;
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
        ReviewHolder viewHolder = new ReviewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.profile_image.setImageBitmap(list.get(position).profile_image);
        holder.user_name.setText(list.get(position).user_name);
        holder.ratingBar.setRating(list.get(position).rating);
        holder.user_comment.setText(list.get(position).user_comment);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        CircleImageView profile_image;
        TextView user_name;
        RatingBar ratingBar;
        TextView user_comment;

        public ReviewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardView);
            profile_image = (CircleImageView)itemView.findViewById(R.id.user_profile_image);
            user_name = (TextView)itemView.findViewById(R.id.userName);
            ratingBar = (RatingBar)itemView.findViewById(R.id.userRatingBar);
            user_comment = (TextView)itemView.findViewById(R.id.userComment);

        }

    }

}
