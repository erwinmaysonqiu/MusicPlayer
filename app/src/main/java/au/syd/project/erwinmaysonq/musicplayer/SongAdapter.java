package au.syd.project.erwinmaysonq.musicplayer;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.lang.Float;

//SongAdapter is a subclass of the RecyclerView.Adapter<SongAdapter.MyViewHolder> class
//... and implements the Filterable interface
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> implements Filterable {

    //Initialize attributes of SongAdapter, which should be the list of Songs and a ClickListener
    private List<Song> mSongs;
    private ClickListener mListener;
    //Second list is a filtered list of Songs that gets populated through our getFilter() Methods
    private List<Song> mSongsFiltered;

    public static View view;


    //Implement the ClickListener
    public interface ClickListener {
        void onSongClick(View view, int songId);
    }

    //SongAdapter constructor method
    SongAdapter(List<Song> songs, ClickListener clickListener) {
        this.mSongs = songs;
        //by default, let the filtered list be entire list of songs
        this.mSongsFiltered = songs;
        mListener = clickListener;
    }


    //Initialize the filter method
    //Searches arraylist of songs for a matching character string
    //If it contains the character, it is added to the filtered list mSongsFiltered
    //At the end, we assign our filtered list's values and return them result as a FilterResults
    //@Override is required because we are overriding the parent implementation of Filter
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    //If user hasn't entered any characters, return entire list
                    mSongsFiltered = mSongs;

                } else {
                    //If it is not empty, then we create a new ArrayList of Songs called filteredSongs
                    //Populate it with songs that contain the String
                    ArrayList<Song> filteredSongs = new ArrayList<>();
                    //loop through all Songs in original ArrayList
                    for (Song song : mSongs) {
                        //if song's name contains characters entered by the user, add that song to our filtered list of songs
                        if (song.getSong().toLowerCase().contains(charString.toLowerCase(Locale.ROOT))) {
                            filteredSongs.add(song);
                        }
                    }
                    //Point our list mSongsFiltered to the filteredSongs list
                    mSongsFiltered = filteredSongs;
                }

                //Assign the filterResults's values to be mSongsFiltered and then return them
                //Remember, this method returns FilterResults
                FilterResults filterResults = new FilterResults();
                filterResults.values = mSongsFiltered;
                return filterResults;
            }

            //Need to let Java know that our dataset has changed so it refreshes
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mSongsFiltered = (ArrayList<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public SongAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Upon creation of the adapter, we want to inflate the Layout item_row (which is put into our recycler view)
        //Inflating it allows the program to find the xml elements for tvSong, tvSinger etc.
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view, mListener);


    }

    //This method is used to bind the data to xml elements
    @Override
    public void onBindViewHolder(@NonNull SongAdapter.MyViewHolder holder, int position) {
        final Song song = mSongsFiltered.get(position);
        int songId = position;

        if (Setting.isIsDarkMode() == true ) {
            //Grey background, light purple white text
            view.setBackgroundColor(Color.parseColor("#292929"));
            holder.tvSongItemRow.setTextColor(Color.parseColor("#F4E3FF"));
            holder.tvSingerItemRow.setTextColor(Color.parseColor("#F4E3FF"));
            holder.tvGenreItemRow.setTextColor(Color.parseColor("#F4E3FF"));
            holder.tvRatingItemRow.setTextColor(Color.parseColor("#F4E3FF"));
            holder.imgViewPlaying.setColorFilter(Color.parseColor("#9585A1"));
            holder.tvPlaying.setTextColor(Color.parseColor("#9585A1"));

        } else {
            ;
//            //White background, purple text
//            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            holder.tvSongItemRow.setTextColor(Color.parseColor("#673AB7"));
//            holder.tvSingerItemRow.setTextColor(Color.parseColor("#673AB7"));
//            holder.tvGenreItemRow.setTextColor(Color.parseColor("#673AB7"));
//            holder.tvRatingItemRow.setTextColor(Color.parseColor("#673AB7"));

        }
        //Prevent recyling, as we need to identify which song is playing, and recycle causes it to display duplicates when song is recycled
        holder.setIsRecyclable(false);
        holder.tvSongItemRow.setText(song.getSong());
        holder.tvSingerItemRow.setText(song.getSinger());
        holder.tvGenreItemRow.setText(song.getGenre());
        holder.tvRatingItemRow.setText(String.valueOf(song.getRating()));
        holder.itemView.setTag(songId);
        holder.imgViewAlbum.setImageResource(song.getImage());
        //If a song is currently playing, and the playing song's id matches this song's id, we show the playing icon and textview
        if (Song.isPlaying() == true && Song.getPlayingSongId() == song.getId()) {
            holder.imgViewPlaying.setVisibility(View.VISIBLE);
            holder.tvPlaying.setVisibility(View.VISIBLE);
        } else {
            ;
        }
    }

    //Returns total number of items in ViewHolder
    @Override
    public int getItemCount() {
        return mSongsFiltered.size();
    }


    //Implement the MyViewHolder class, which is a subclass of RecyclerView.ViewHolder
    //Needs to implement the interface View.OnClickListener
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declare the attributes of our implementation of MyViewHolder
        //Specify the XML elements in our item row of our RecyclerView
        public TextView tvSongItemRow, tvSingerItemRow, tvGenreItemRow, tvRatingItemRow, tvPlaying;
        public ImageView imgViewAlbum;
        private ClickListener clickListener;
        private ImageView imgViewPlaying;

        //Declare constructor of our implementation of MyViewHolder
        public MyViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            itemView.setOnClickListener(MyViewHolder.this);

            //Initialize our TextViews and assign them to our XML elements
            tvSongItemRow = itemView.findViewById(R.id.tvSongItemRow);
            tvSingerItemRow = itemView.findViewById(R.id.tvSingerItemRow);
            tvGenreItemRow = itemView.findViewById(R.id.tvGenreItemRow);
            tvRatingItemRow = itemView.findViewById((R.id.tvRatingItemRow));
            imgViewAlbum = itemView.findViewById(R.id.imgViewAlbum);
            tvPlaying = itemView.findViewById(R.id.tvPlaying);
            imgViewPlaying = itemView.findViewById(R.id.imgViewPlaying);
        }

        //Specify onClick behaviour when user clicks on the ViewHolder
        @Override
        public void onClick(View view) {
            mListener.onSongClick(view, (Integer) view.getTag());
        }
    }


    //Method to sort songs by either song or singer
    //if pass in 1, we sort by song, if pass in 2, we sort by total cases
    public void sort(final int sortApproach) {
        if (mSongsFiltered.size() > 0) {
            Collections.sort(mSongsFiltered, new Comparator<Song>() {
                @Override
                public int compare(Song i1, Song i2) {
                    //Depending on what integer is passed in, compare song, singer, rating, genre or play count
                    if (sortApproach == 1) {
                        return i1.getSong().compareTo(i2.getSong());

                    } else if (sortApproach == 2) {
                        return i1.getSinger().compareTo(i2.getSinger());

                    } else if (sortApproach == 3) {
                        return i1.getGenre().compareTo(i2.getGenre());

                        //Note, for numeric data types, need to first convert into Float or Integer
                    } else if (sortApproach == 4) {
                        return Float.valueOf(i2.getRating()).compareTo(Float.valueOf(i1.getRating()));

                    } else if (sortApproach == 5) {
                        //Note, for numeric data types, need to first convert into Float or Integer
                        return Integer.valueOf(i2.getPlays()).compareTo(Integer.valueOf(i1.getPlays()));
                    }
                    // if unspecified, just sort by song
                    return i1.getSong().compareTo(i2.getSong());
                }
            });
        }
        //Once the sort has finished, notify app that the dataset has been changed so it refreshes
        notifyDataSetChanged();
    }

    public void toDarkMode() {
        view.setBackgroundColor(Color.parseColor("#292929"));
    }
}
