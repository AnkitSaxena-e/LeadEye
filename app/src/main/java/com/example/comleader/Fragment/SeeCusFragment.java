package com.example.comleader.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comleader.Prevalant.Prevalant;
import com.example.comleader.Adapter_Or_Class.Folding_Cell_Cus_Adapter;
import com.example.comleader.Modal.Cus_Detail_Model;
import com.example.comleader.Modal.MemModel;
import com.example.comleader.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import io.paperdb.Paper;

public class SeeCusFragment extends Fragment {

    private SearchView S_PhoneS;
    ListView theListView;
    Folding_Cell_Cus_Adapter adapter;
    ArrayList<Cus_Detail_Model> List, FList;
    DatabaseReference SearchRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_see_cus, container, false);

        S_PhoneS = root.findViewById(R.id.search_view_Cus);

        theListView = root.findViewById(R.id.see_cus_recy);

//        makeAdapter();

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
//                view.findViewById(R.id.unko).setVisibility(View.VISIBLE);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });

        SearchRef = FirebaseDatabase.getInstance().getReference().child("Cus");

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!SearchRef.equals(null)) {

            SearchRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        List = new ArrayList<>();
                        FList = new ArrayList<>();
                        for (DataSnapshot ss : dataSnapshot.getChildren()) {

                            List.add(ss.getValue(Cus_Detail_Model.class));

                        }

                        for (int y = 0; y < List.size(); y++){

                            if(List.get(y).getWho().equals(Paper.book().read(Prevalant.Mem_No))){

                                FList.add(List.get(y));

                            }

                        }

                        if(getActivity() != null){

                            adapter = new Folding_Cell_Cus_Adapter(getActivity(), FList);
                            theListView.setAdapter(adapter);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if(!S_PhoneS.equals(null)){

            S_PhoneS.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchIt(newText);
                    return false;
                }
            });
        }
    }

    private void searchIt(String newText) {

        ArrayList<Cus_Detail_Model> Newlist = new ArrayList<>();

        for(Cus_Detail_Model product : FList){
            if(product.getKeyward().toLowerCase().contains(newText.toLowerCase()) && product.getWho().equals(Paper.book().read(Prevalant.Mem_No))){
                Newlist.add(product);
            }
        }
        adapter = new Folding_Cell_Cus_Adapter(getActivity(), Newlist);

        theListView.setAdapter(adapter);
    }


}