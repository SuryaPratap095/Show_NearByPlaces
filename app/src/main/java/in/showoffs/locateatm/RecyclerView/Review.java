package in.showoffs.locateatm.RecyclerView;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Administrator on 12/1/2015.
 */
public class Review {

    Bitmap profile_image = null;
    String user_name = "";
    Float rating = null;
    String user_comment = "";

    public Review() {
    }

    public Review(Bitmap profile_image, String user_name, Float rating, String user_comment) {
        this.profile_image = profile_image;
        this.user_name = user_name;
        this.rating = rating;
        this.user_comment = user_comment;
    }

    private List<Review> list;

    public void setReview(List<Review> list){
        this.list = list;
    }
    public List<Review> getReview(){
        return list;
    }


}
