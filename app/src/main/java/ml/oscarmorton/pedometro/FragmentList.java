package ml.oscarmorton.pedometro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentList extends Fragment {
    private ArrayList<Walk> data;
    private RecyclerView rvList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        data = new ArrayList<>();
        data.add(new Walk("Test walk", "javea", 20));
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    // Return to this
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvList = getView().findViewById(R.id.rvList);
        rvList.setAdapter(new AdapterWalks(data));
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    public ArrayList<Walk> getData(){
        return data;
    }
}
