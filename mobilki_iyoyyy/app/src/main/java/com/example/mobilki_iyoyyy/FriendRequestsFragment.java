package com.example.mobilki_iyoyyy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private final List<Friend> requests = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_friend_requests,
                container,
                false
        );

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadRequests();

        return view;
    }

    private void loadRequests() {
        RetrofitClient.getApiService()
                .getFriendRequests()
                .enqueue(new Callback<List<Friend>>() {
                    @Override
                    public void onResponse(
                            Call<List<Friend>> call,
                            Response<List<Friend>> response
                    ) {
                        if (!isAdded() || response.body() == null) return;

                        requests.clear();
                        requests.addAll(response.body());

                        recyclerView.setAdapter(
                                new FriendRequestsAdapter(requests)
                        );
                    }

                    @Override
                    public void onFailure(Call<List<Friend>> call, Throwable t) {
                        // можно Toast или лог
                    }
                });
    }
}
