package androidsupersquad.rocketfrenzy.Fragments;
import androidsupersquad.rocketfrenzy.Fragments.Adapters.ShopExpandableAdapter;
import androidsupersquad.rocketfrenzy.Fragments.Models.ShopItems;
import androidsupersquad.rocketfrenzy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.List;

/**
 *  This is the Shop Menu where it will display to the user
 *  different shop items they can buy
 *  Created by Lazer
 */
public class ShopFragment extends Fragment {
    //Data Structure to hold all shop items
    private List<ShopItems> Items;
    /**
     * Fragment Constructor that creates all the items in the shop
     */
    public ShopFragment() {}
    public void CreateItemList()
    {
        //Create new ArrayList to hold existing items or add items
        Items = new ArrayList<ShopItems>();
        Items.add(new ShopItems("Launch Pad",R.drawable.bleach,getActivity().getString(R.string.launchpad),10000));
        Items.add(new ShopItems("Launch Pad Bundle",R.drawable.bleach,getActivity().getString(R.string.launchpad_bundle),10000));
        Items.add(new ShopItems("Horizon Icon",R.drawable.bleach,getActivity().getString(R.string.Horizon_Icon),10000));
        Items.add(new ShopItems("Fire Icon",R.drawable.bleach,getActivity().getString(R.string.Flame_Icon),10000));
        Items.add(new ShopItems("Skull Icon",R.drawable.bleach,getActivity().getString(R.string.Skull_Icon),10000));
        Items.add(new ShopItems("Bleach",R.drawable.bleach,getActivity().getString(R.string.Bleach),10000));
    }
    /**
     * Sets teh Expandable List View Adapter with specific
     * Group Layout
     * Child Layout
     * @param savedInstanceState
     */
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        CreateItemList();
        //get Expandable List View
        ExpandableListView listView = (ExpandableListView) getActivity().findViewById(R.id.ShopList);
        //Set it's adapter to the custom Adapter
        listView.setAdapter(new ShopExpandableAdapter(getActivity().getBaseContext(),Items));
    }

    /**
     * Inflates the fragment with the specific layout in the resource file
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }
}
