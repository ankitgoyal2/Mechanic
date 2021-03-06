package com.example.mechanic.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mechanic.R;
import com.example.mechanic.adapters.RequestPendingAdapter;
import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestPendingFragment extends Fragment {


    RecyclerView s_recyclerView_pending_request;
    RequestPendingAdapter requestPendingAdapter;
    LinearLayout nothing;
    FirebaseDatabase firebaseDatabase;

    FirebaseAuth auth;
    FirebaseUser user;

    String name;


    public RequestPendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.request_pending, container, false);


        s_recyclerView_pending_request = (RecyclerView)rootView.findViewById(R.id.s_recyclerView_pending_request);
        nothing = rootView.findViewById(R.id.EmptyList1);
        s_recyclerView_pending_request.setLayoutManager(new LinearLayoutManager(getActivity()));

//        dialogBox = new CustomDialogBox(getActivity());
//        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        dialogBox.show();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        Query baseQuery = firebaseDatabase.getReference("Users").child("Mechanic").child(user.getUid()).child("pendingRequests");

        DatabaseReference reference1 = firebaseDatabase.getReference().child("Users").child("Mechanic").child(user.getUid()).child("pendingRequests");

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    nothing.setVisibility(View.VISIBLE);
                    s_recyclerView_pending_request.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Request> options = new DatabasePagingOptions.Builder<Request>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Request.class)
                .build();

        requestPendingAdapter = new RequestPendingAdapter(options,getContext());
        s_recyclerView_pending_request.setAdapter(requestPendingAdapter);
        requestPendingAdapter.startListening();


        return rootView;
    }


}

