package com.bas.android.muralmaps;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtListFragment extends Fragment {

    private ListView artList;
    private MainActivity mainActivity;

    public ArtListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_art_list, container, false);

        artList = (ListView) view.findViewById(R.id.art_list);

        mainActivity = (MainActivity) this.getActivity();

        ArtArrayAdapter adapter = new ArtArrayAdapter(this.getActivity(), this.getAvailableArt());
        artList.setAdapter(adapter);

        artList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                final Art art = (Art) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), ArtActivity.class);
                intent.putExtra("art",art.getId());
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        //ArtArrayAdapter adapter = new ArtArrayAdapter(this.getActivity(), this.getAvailableArt());
        //artList.setAdapter(adapter);
    }

    public ArrayList<Art> getAvailableArt(){
        ArrayList<Art> art2 = new ArrayList<Art>();
        RealmResults<Art> arts = mainActivity.realm.where(Art.class).findAll();
        Log.d("check", "before art loop");
        String choice = mainActivity.filterSpinner.getSelectedItem().toString();

        if (choice == "Most Popular") {
            for (Art art: arts)
            {
                if(art2.isEmpty())
                {
                    art2.add(art);
                }
                for(Art artX: art2)
                {
                    if (art.getPopularity() > artX.getPopularity())
                    {
                        int posi = art2.indexOf(artX);
                        art2.add(posi, art);
                    }
                    else{
                        art2.add(art);
                    }

                }

            }
        }
        if (choice == "Favorites")
        {
            for (Art art : arts)
            {
                if(art.getLike())
                {
                    art2.add(art);
                }
            }
        }
        else {
            for (Art art : arts) {
                System.out.println(art);
                art2.add(art);
                // Boolean isPresent = false;
                // try {
                //     for (Vote vote : art.getVotes()) {
                //        Log.d("Warning: OWNER USERNAME", vote.getOwner().getUsername().toString());
                //      Log.d("MYUSERNAMEINPUT", mainActivity.user.getUsername().toString());
//
                //                  if (vote.getOwner().getUsername().equals(mainActivity.user.getUsername())) {
                //                    isPresent = true;
                //              }
                //        }
                //      if (isPresent == false) {
                //        art2.add(art);
                //  }
                //}catch (Exception e){ Log.d("Exception", e.getMessage()+e.getStackTrace());}
            }

        }
        return art2;
    }
}
