package com.example.commember.Fragment;

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

import com.example.commember.Adapter_Or_Class.Folding_Cell_Adapter;
import com.example.commember.HomeActivity;
import com.example.commember.Modal.Cus_Detail_Model;
import com.example.commember.Prevalant.Prevalant;
import com.example.commember.R;
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
    ListView recyclerView;
    Folding_Cell_Adapter adapter;
    ArrayList<Cus_Detail_Model> List, FL;
    ImageView see_Cl;
    ListView theListView;
    DatabaseReference SearchRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_see_cus, container, false);

        S_PhoneS = root.findViewById(R.id.search_view_See);

        see_Cl = root.findViewById(R.id.see_clos);

        theListView = root.findViewById(R.id.sho_Cus_Li);

//        makeAdapter();

        see_Cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((HomeActivity)getActivity()).getBack();

            }
        });

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
                        FL = new ArrayList<>();
                        for (DataSnapshot ss : dataSnapshot.getChildren()) {
                            List.add(ss.getValue(Cus_Detail_Model.class));
                        }

                        for (int y = 0; y < List.size(); y++){

                            if(List.get(y).getWho().equals(Paper.book().read(Prevalant.UserNumberA))){
                                FL.add(List.get(y));
                            }

                        }

                        if(getActivity() != null){

                            adapter = new Folding_Cell_Adapter(getActivity(), FL);
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

        for(Cus_Detail_Model product : List){
            if(product.getKeyward().toLowerCase().contains(newText.toLowerCase()) && product.getWho().equals(Paper.book().read(Prevalant.UserNumberA))){
                Newlist.add(product);
            }
        }
        adapter = new Folding_Cell_Adapter(getActivity(), Newlist);

        theListView.setAdapter(adapter);
    }

//    private void makeAdapter() {
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.exists()){
//
//                    List = new ArrayList<>();
//
//                    for (DataSnapshot ss : snapshot.getChildren()) {
//                        List.add(ss.getValue(Cus_Detail_Model.class));
//                    }
//
//                    adapter = new Folding_Cell_Adapter(getActivity(), List);
//
//                    theListView.setAdapter(adapter);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

}