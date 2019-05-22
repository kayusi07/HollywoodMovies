package hollywood.kayushi07.com.hollywoodmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ayushi on 19-12-2017.
 */

public class CustomGridAdapter extends BaseAdapter {
    Context context;
    int logos[];
    LayoutInflater inflter;
    String year[] = {"2016 - 2020", "2011 - 2015",  "2006 - 2010", "2001 - 2005", "1996 - 2000", "1990 - 1995"};

    public CustomGridAdapter(Context applicationContext, int[] logos) {
        this.context = applicationContext;
        this.logos = logos;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return logos.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.grid_item, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.img_grid_item); // get the reference of ImageView
        icon.setImageResource(logos[i]); // set logo images
        TextView txt = (TextView) view.findViewById(R.id.txt_year);
        txt.setText(year[i]);

        return view;
    }
}
